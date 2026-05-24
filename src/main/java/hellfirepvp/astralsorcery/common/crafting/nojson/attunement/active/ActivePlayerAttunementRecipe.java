package hellfirepvp.astralsorcery.common.crafting.nojson.attunement.active;

import hellfirepvp.astralsorcery.client.util.sound.FadeSound;
import hellfirepvp.astralsorcery.client.util.camera.EntityClientReplacement;
import hellfirepvp.astralsorcery.client.util.camera.EntityCameraRenderView;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.client.util.camera.ClientCameraManager;
import hellfirepvp.astralsorcery.client.util.camera.ICameraPersistencyFunction;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.network.play.client.PktAttunePlayerConstellation;
import hellfirepvp.astralsorcery.client.util.camera.ICameraTransformer;
import hellfirepvp.astralsorcery.client.util.camera.ICameraStopListener;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.util.camera.ICameraTickListener;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.client.util.camera.path.CameraPathBuilder;
import net.minecraft.client.Minecraft;
import java.util.Set;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import java.util.Collection;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.impl.RenderOffsetNoisePlane;
import hellfirepvp.astralsorcery.common.event.helper.EventHelperInvulnerability;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import net.minecraft.nbt.CompoundTag;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttunePlayerRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttunementRecipe;

public class ActivePlayerAttunementRecipe extends AttunementRecipe.Active<AttunePlayerRecipe>
{
    private static final int DURATION_PLAYER_ATTUNEMENT = 800;
    private IMajorConstellation constellation;
    private UUID playerUUID;
    public Object cameraHack;
    private boolean startedPlayerSound;
    private List<Object> playerNoisePlanes;
    
    public ActivePlayerAttunementRecipe(final AttunePlayerRecipe recipe, final IMajorConstellation constellation, final UUID playerUUID) {
        super(recipe);
        this.startedPlayerSound = false;
        this.playerNoisePlanes = new ArrayList<Object>();
        this.constellation = constellation;
        this.playerUUID = playerUUID;
    }
    
    public ActivePlayerAttunementRecipe(final AttunePlayerRecipe recipe, final CompoundTag nbt) {
        super(recipe);
        this.startedPlayerSound = false;
        this.playerNoisePlanes = new ArrayList<Object>();
        this.readFromNBT(nbt);
    }
    
    @Override
    public boolean matches(final TileAttunementAltar altar) {
        final Player player;
        return super.matches(altar) && (player = altar.func_145831_w().getPlayerByUUID(this.playerUUID)) != null && player.isAlive();
    }
    
    @Override
    public void startCrafting(final TileAttunementAltar altar) {
        final Player player = altar.func_145831_w().getPlayerByUUID(this.playerUUID);
        if (player != null && player.isAlive()) {
            final Vector3 offset = new Vector3(altar).add(0.5f, 1.2f, 0.5f);
            player.func_70080_a(offset.getX(), offset.getY(), offset.getZ(), 0.0f, 0.0f);
            player.func_70080_a(offset.getX(), offset.getY(), offset.getZ(), 0.0f, 0.0f);
        }
    }
    
    @Override
    public void stopCrafting(final TileAttunementAltar altar) {
    }
    
    @Override
    public void finishRecipe(final TileAttunementAltar altar) {
        final Player player = altar.func_145831_w().getPlayerByUUID(this.playerUUID);
        if (player != null) {
            ResearchManager.setAttunedConstellation(player, this.constellation);
        }
    }
    
    @Override
    public void doTick(final LogicalSide side, final TileAttunementAltar altar) {
        if (side.isServer()) {
            final Player player = altar.func_145831_w().getPlayerByUUID(this.playerUUID);
            if (player != null) {
                EventHelperInvulnerability.makeInvulnerable(player);
            }
        }
        else {
            this.setupPlanes();
            this.doClientSetup(altar);
            this.doEffectTick(altar);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void setupPlanes() {
        if (this.playerNoisePlanes.isEmpty()) {
            this.playerNoisePlanes.add(new RenderOffsetNoisePlane(1.0f));
            this.playerNoisePlanes.add(new RenderOffsetNoisePlane(1.4f));
            this.playerNoisePlanes.add(new RenderOffsetNoisePlane(1.8f));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doEffectTick(final TileAttunementAltar altar) {
        final IConstellation cst = altar.getActiveConstellation();
        if (cst == null) {
            return;
        }
        final Vector3 playerTarget = new Vector3(altar).add(0.5, 2.5, 0.5);
        final VFXColorFunction<?> beamColor = VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE);
        final int tick = this.getTick();
        if (tick % 40 == 0) {
            for (final BlockPos pos : altar.getConstellationPositions(cst)) {
                final Vector3 from = new Vector3((Vector3i)pos).add(0.5, 0.0, 0.5);
                MiscUtils.applyRandomOffset(from, this.rand, 0.1f);
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from).setup(from.clone().addY(6.0), 1.2, 1.2).setAlphaMultiplier(0.8f).color(beamColor).setMaxAge(60);
            }
        }
        final double scale = 7.0;
        final double edgeScale = scale * 2.0 + 1.0;
        for (int i = 0; i < 7; ++i) {
            final Vector3 offset = new Vector3(altar).add(-scale, 0.1, -scale);
            if (this.rand.nextBoolean()) {
                offset.add(edgeScale * (double)(this.rand.nextBoolean() ? 1 : 0), 0.0, this.rand.nextFloat() * edgeScale);
            }
            else {
                offset.add(this.rand.nextFloat() * edgeScale, 0.0, edgeScale * (double)(this.rand.nextBoolean() ? 1 : 0));
            }
            final FXFacingParticle particle = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).alpha(VFXAlphaFunction.FADE_OUT).setGravityStrength(-2.0E-4f + this.rand.nextFloat() * -1.0E-4f).setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.15f).color(VFXColorFunction.WHITE).setMaxAge(40 + this.rand.nextInt(10));
            if (this.rand.nextBoolean()) {
                particle.color(VFXColorFunction.constant(this.constellation.getConstellationColor()));
            }
        }
        for (int i = 0; i < 5; ++i) {
            final Set<BlockPos> offsets = altar.getConstellationPositions(cst);
            final BlockPos pos2 = MiscUtils.getRandomEntry(offsets, this.rand);
            if (tick <= 380) {
                final Vector3 offset2 = new Vector3((Vector3i)pos2).add(0.5, 0.0, 0.5).add(Vector3.random().setY(0).multiply(0.6));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset2).color(VFXColorFunction.WHITE).setGravityStrength(-6.0E-4f + this.rand.nextFloat() * -0.003f).setMotion(Vector3.random().addY(4.0).normalize().multiply(0.015 + this.rand.nextFloat() * 0.01)).setAlphaMultiplier(0.6f).setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.15f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(60 + this.rand.nextInt(20));
            }
            else {
                Vector3 offset2 = new Vector3((Vector3i)pos2).add(0.5, 0.0, 0.5).add(Vector3.random().setY(0).multiply(0.5));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset2).setAlphaMultiplier(0.6f).alpha(VFXAlphaFunction.proximity(playerTarget::clone, 3.0f)).motion(VFXMotionController.target(playerTarget::clone, 0.08f)).setScaleMultiplier(0.2f + this.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE).setMotion(new Vector3(0.0, 0.2 + this.rand.nextFloat() * 0.15f, 0.0)).setMaxAge(60 + this.rand.nextInt(20));
                offset2 = new Vector3(altar).add(0.5, 0.0, 0.5).add(Vector3.random().setY(0).multiply(0.6));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset2).color(VFXColorFunction.WHITE).setGravityStrength(-6.0E-4f + this.rand.nextFloat() * -0.004f).setMotion(Vector3.random().addY(4.0).normalize().multiply(0.02 + this.rand.nextFloat() * 0.01)).setAlphaMultiplier(0.75f).setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.1f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(40 + this.rand.nextInt(10));
            }
        }
        if (tick >= 220) {
            final Vector3 offset3 = new Vector3(altar).add(0.5, 0.0, 0.5).add(Vector3.random().setY(0));
            FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset3).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.proximity(playerTarget::clone, 3.0f)).motion(VFXMotionController.target(playerTarget::clone, 0.1f)).setScaleMultiplier(0.2f + this.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE).setMotion(Vector3.positiveYRandom().setY(1).normalize().multiply(0.5f + this.rand.nextFloat() * 0.1f)).setMaxAge(60 + this.rand.nextInt(20));
            if (this.rand.nextBoolean()) {
                p.color(VFXColorFunction.constant(this.constellation.getConstellationColor()));
            }
            for (int j = 0; j < 3; ++j) {
                final Vector3 at = new Vector3(altar).add(0.5, 0.0, 0.5);
                at.addX(this.rand.nextFloat() * 7.0f * (this.rand.nextBoolean() ? 1 : -1));
                at.addZ(this.rand.nextFloat() * 7.0f * (this.rand.nextBoolean() ? 1 : -1));
                p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setAlphaMultiplier(0.75f).alpha(VFXAlphaFunction.FADE_OUT).setGravityStrength(-0.001f + this.rand.nextFloat() * -5.0E-4f).color(VFXColorFunction.WHITE).setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.1f).setMaxAge(20 + this.rand.nextInt(10));
                if (this.rand.nextBoolean()) {
                    p.color(VFXColorFunction.constant(this.constellation.getConstellationColor()));
                }
                if (tick >= 500) {
                    p.setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.15f);
                }
            }
        }
        if (tick >= 400) {
            for (int amt = (tick >= 500) ? 4 : 1, k = 0; k < amt; ++k) {
                final RenderOffsetNoisePlane plane = MiscUtils.getRandomEntry((Collection<RenderOffsetNoisePlane>)this.playerNoisePlanes, this.rand);
                final FXFacingParticle p2 = plane.createParticle(playerTarget.clone()).setMotion(Vector3.random().setY(0).multiply(this.rand.nextFloat() * 0.015f)).setAlphaMultiplier(0.6f).setScaleMultiplier(0.2f + this.rand.nextFloat() * 0.05f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(60 + this.rand.nextInt(20));
                if (this.rand.nextBoolean()) {
                    p2.color(VFXColorFunction.constant(this.constellation.getConstellationColor()));
                }
            }
        }
        if (tick >= 600 && tick % 10 == 0) {
            final Vector3 from2 = new Vector3(altar).add(0.5, 0.0, 0.5);
            MiscUtils.applyRandomOffset(from2, this.rand, 0.25f);
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from2).setup(from2.clone().addY(8.0), 2.4, 2.0).setAlphaMultiplier(0.8f).setMaxAge(40 + this.rand.nextInt(20));
        }
        if (tick >= 796) {
            for (int i = 0; i < 60; ++i) {
                final Vector3 at2 = new Vector3(altar).add(0.5, 0.0, 0.5).addY(this.rand.nextFloat() * 15.0f);
                final FXFacingParticle p3 = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at2).color(VFXColorFunction.WHITE).setMotion(Vector3.random().setY(0).normalize().multiply(0.03 + this.rand.nextFloat() * 0.01)).setAlphaMultiplier(0.7f).setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.15f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(140 + this.rand.nextInt(60));
                if (this.rand.nextBoolean()) {
                    p3.color(VFXColorFunction.constant(this.constellation.getConstellationColor()));
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doClientSetup(final TileAttunementAltar altar) {
        if (this.cameraHack == null && Minecraft.getInstance().field_71439_g != null && Minecraft.getInstance().field_71439_g.getUUID().equals(this.getPlayerUUID())) {
            final Vector3 offset = new Vector3(altar).add(0.5, 6.0, 0.5);
            final CameraPathBuilder builder = CameraPathBuilder.builder(offset.clone().add(4.0f, 0.0f, 4.0f), new Vector3(altar).add(0.5, 0.5, 0.5));
            builder.addCircularPoints(offset, CameraPathBuilder.DynamicRadiusGetter.dyanmicIncrease(5.0, 0.025), 200, 2);
            builder.addCircularPoints(offset, CameraPathBuilder.DynamicRadiusGetter.dyanmicIncrease(10.0, -0.01), 200, 2);
            builder.setTickDelegate(this.createTickListener(new Vector3(altar).add(0.5f, 1.2f, 0.5f)));
            builder.setStopDelegate(this.createAttunementListener(altar));
            this.cameraHack = builder.finishAndStart();
        }
        if (!this.startedPlayerSound) {
            this.startedPlayerSound = true;
            SoundHelper.playSoundFadeInClient(SoundsAS.ATTUNEMENT_ATLAR_PLAYER_ATTUNE, new Vector3(altar).add(0.5, 1.0, 0.5), 0.7f, 1.0f, false, s -> !altar.canPlayConstellationActiveEffects() || altar.getActiveRecipe() != this).setFadeInTicks(10.0f).setFadeOutTicks(80.0f);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private ICameraTickListener createTickListener(final Vector3 offset) {
        return (renderView, focusedEntity) -> {
            if (focusedEntity != null) {
                final float floatTick = ClientScheduler.getClientTick() % 40L / 40.0f;
                final float sin = Mth.func_76126_a((float)(floatTick * 2.0f * 3.141592653589793)) / 2.0f + 0.5f;
                focusedEntity.func_174805_g(false);
                focusedEntity.func_70080_a(offset.getX(), offset.getY() + sin * 0.2, offset.getZ(), 0.0f, 0.0f);
                focusedEntity.func_70080_a(offset.getX(), offset.getY() + sin * 0.2, offset.getZ(), 0.0f, 0.0f);
                focusedEntity.field_70759_as = 0.0f;
                focusedEntity.field_70758_at = 0.0f;
                focusedEntity.field_70761_aq = 0.0f;
                focusedEntity.field_70760_ar = 0.0f;
                focusedEntity.func_70016_h(0.0, 0.0, 0.0);
            }
        };
    }
    
    @OnlyIn(Dist.CLIENT)
    private ICameraStopListener createAttunementListener(final TileAttunementAltar altar) {
        final BlockPos at = altar.func_174877_v();
        return () -> {
            if (this.cameraHack != null) {
                final ICameraTransformer transformer = (ICameraTransformer)this.cameraHack;
                final ICameraPersistencyFunction persistency = transformer.getPersistencyFunction();
                if (persistency.isExpired() && !persistency.wasForciblyStopped()) {
                    final PktAttunePlayerConstellation attuneRequest = new PktAttunePlayerConstellation(this.constellation, (RegistryKey<World>)altar.func_145831_w().dimension(), at);
                    PacketChannel.CHANNEL.sendToServer(attuneRequest);
                }
            }
        };
    }
    
    @Override
    public boolean isFinished(final TileAttunementAltar altar) {
        return this.getTick() >= 800;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void stopEffects(final TileAttunementAltar altar) {
        if (this.cameraHack != null) {
            ClientCameraManager.INSTANCE.removeTransformer((ICameraTransformer)this.cameraHack);
        }
    }
    
    public UUID getPlayerUUID() {
        return this.playerUUID;
    }
    
    @Override
    public void writeToNBT(final CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putUUID("playerUUID", this.playerUUID);
        nbt.putString("constellation", this.constellation.getRegistryName().toString());
    }
    
    @Override
    protected void readFromNBT(final CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.playerUUID = nbt.getUUID("playerUUID");
        this.constellation = (IMajorConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(new ResourceLocation(nbt.getString("constellation")));
    }
}
