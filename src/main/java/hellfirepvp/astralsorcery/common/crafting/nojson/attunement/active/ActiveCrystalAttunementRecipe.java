package hellfirepvp.astralsorcery.common.crafting.nojson.attunement.active;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import hellfirepvp.astralsorcery.client.util.sound.PositionedLoopSound;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.Vec3i;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import hellfirepvp.astralsorcery.client.effect.source.orbital.FXOrbitalCrystalAttunement;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingSprite;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.client.util.sound.FadeLoopSound;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.lib.AdvancementsAS;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCrystalBase;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttuneCrystalRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttunementRecipe;

public class ActiveCrystalAttunementRecipe extends AttunementRecipe.Active<AttuneCrystalRecipe>
{
    private static final int DURATION_CRYSTAL_ATTUNEMENT = 500;
    private IConstellation constellation;
    private int entityId;
    private Object itemAttuneSound;
    private Object innerOrbital1;
    private Object attunementFlare;
    
    public ActiveCrystalAttunementRecipe(final AttuneCrystalRecipe recipe, final IConstellation constellation, final int crystalEntityId) {
        super(recipe);
        this.constellation = constellation;
        this.entityId = crystalEntityId;
    }
    
    public ActiveCrystalAttunementRecipe(final AttuneCrystalRecipe recipe, final CompoundTag nbt) {
        super(recipe);
        this.readFromNBT(nbt);
    }
    
    @Override
    public boolean matches(final TileAttunementAltar altar) {
        final Entity entity;
        return super.matches(altar) && (entity = altar.getLevel().getEntityById(this.entityId)) != null && entity.isAlive() && entity instanceof ItemEntity && this.constellation.equals(altar.getActiveConstellation()) && AttuneCrystalRecipe.isApplicableCrystal((ItemEntity)entity, altar.getActiveConstellation());
    }
    
    @Override
    public void startCrafting(final TileAttunementAltar altar) {
    }
    
    @Override
    public void stopCrafting(final TileAttunementAltar altar) {
    }
    
    @Override
    public void finishRecipe(final TileAttunementAltar altar) {
        final ItemEntity crystal = this.getEntity(altar.getLevel());
        if (crystal != null) {
            ItemStack stack = crystal.func_92059_d();
            if (!(stack.getItem() instanceof ConstellationItem) && stack.getItem() instanceof ItemCrystalBase) {
                final CompoundTag tag = stack.getTag();
                stack = new ItemStack((ItemLike)((ItemCrystalBase)stack.getItem()).getTunedItemVariant(), stack.getCount());
                stack.setTag(tag);
            }
            if (stack.getItem() instanceof ConstellationItem) {
                final IWeakConstellation attuned = ((ConstellationItem)stack.getItem()).getAttunedConstellation(stack);
                final IMinorConstellation trait = ((ConstellationItem)stack.getItem()).getTraitConstellation(stack);
                if (attuned == null) {
                    if (altar.getActiveConstellation() instanceof IWeakConstellation) {
                        ((ConstellationItem)stack.getItem()).setAttunedConstellation(stack, (IWeakConstellation)altar.getActiveConstellation());
                    }
                }
                else if (trait == null && altar.getActiveConstellation() instanceof IMinorConstellation) {
                    ((ConstellationItem)stack.getItem()).setTraitConstellation(stack, (IMinorConstellation)altar.getActiveConstellation());
                }
                crystal.func_92058_a(stack);
                final UUID throwerUUID = crystal.func_200214_m();
                if (throwerUUID != null) {
                    final Player thrower = altar.getLevel().getPlayerByUUID(throwerUUID);
                    if (thrower instanceof ServerPlayer) {
                        AdvancementsAS.ATTUNE_CRYSTAL.trigger((ServerPlayer)thrower, altar.getActiveConstellation());
                    }
                }
            }
        }
    }
    
    @Override
    public void doTick(final LogicalSide side, final TileAttunementAltar altar) {
        final ItemEntity crystal = this.getEntity(altar.getLevel());
        if (crystal == null) {
            return;
        }
        final Vector3 crystalHoverPos = new Vector3(altar).add(0.5, 1.4, 0.5);
        crystal.setPos(crystalHoverPos.getX(), crystalHoverPos.getY(), crystalHoverPos.getZ());
        crystal.field_70169_q = crystalHoverPos.getX();
        crystal.field_70167_r = crystalHoverPos.getY();
        crystal.field_70166_s = crystalHoverPos.getZ();
        crystal.func_213293_j(0.0, 0.0, 0.0);
        if (side.isClient()) {
            this.doClientTick(altar);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doClientTick(final TileAttunementAltar altar) {
        final Predicate<PositionedLoopSound> activeTest = s -> !altar.canPlayConstellationActiveEffects() || altar.getActiveRecipe() != this;
        if (this.itemAttuneSound == null || ((FadeLoopSound)this.itemAttuneSound).hasStoppedPlaying()) {
            this.itemAttuneSound = SoundHelper.playSoundLoopFadeInClient(SoundsAS.ATTUNEMENT_ATLAR_ITEM_LOOP, new Vector3(altar).add(0.5, 1.0, 0.5), 1.0f, 1.0f, false, activeTest).setFadeInTicks(20.0f).setFadeOutTicks(20.0f);
        }
        if (this.getTick() == 0) {
            SoundHelper.playSoundClientWorld(SoundsAS.ATTUNEMENT_ATLAR_ITEM_START, altar.getBlockState(), 1.0f, 1.0f);
        }
        if (this.getTick() >= 80 && (this.attunementFlare == null || ((EntityComplexFX)this.attunementFlare).isRemoved())) {
            this.attunementFlare = EffectHelper.of(EffectTemplatesAS.FACING_SPRITE).spawn(new Vector3(altar).add(0.5, 1.75, 0.5)).setSprite(SpritesAS.SPR_ATTUNEMENT_FLARE).setScaleMultiplier(2.5f).refresh(fx -> altar.canPlayConstellationActiveEffects() && altar.getActiveRecipe() == this);
        }
        final Vector3 altarPos = new Vector3(altar).add(0.5, 0.0, 0.5);
        if (this.innerOrbital1 == null) {
            this.innerOrbital1 = EffectHelper.spawnSource(new FXOrbitalCrystalAttunement(altarPos.clone(), altarPos.clone().addY(1.75), this.constellation)).setOrbitRadius(3.0).setBranches(4).setOrbitAxis(Vector3.RotAxis.Y_AXIS).setTicksPerRotation(200).refresh(RefreshFunction.tileExistsAnd(altar, (tile, effect) -> tile.canPlayConstellationActiveEffects() && tile.getActiveRecipe() == this));
        }
        final VFXColorFunction<?> beamColor = VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE);
        if (this.getTick() >= 80 && this.getTick() % 40 == 0) {
            for (final BlockPos pos : altar.getConstellationPositions(this.constellation)) {
                final Vector3 from = new Vector3((Vec3i)pos).add(0.5, 0.0, 0.5);
                MiscUtils.applyRandomOffset(from, this.rand, 0.1f);
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from).setup(from.clone().addY(6.0), 1.2, 1.2).setAlphaMultiplier(0.8f).color(beamColor).setMaxAge(60);
            }
        }
        final float total = 500.0f;
        final float percCycle = (float)(this.getTick() % total / total * 2.0f * 3.141592653589793);
        final int parts = (this.getTick() % 50 == 0) ? 180 : 6;
        final Vector3 center = new Vector3(altar).add(0.5, 0.1, 0.5);
        final float angleSwirl = 120.0f;
        final float dst = 4.5f;
        for (int i = 0; i < parts; ++i) {
            final Vector3 v = Vector3.RotAxis.X_AXIS.clone();
            final float originalAngle = i / (float)parts * 360.0f;
            final double angle = originalAngle + Mth.func_76126_a(percCycle) * angleSwirl;
            v.rotate(-Math.toRadians(angle), Vector3.RotAxis.Y_AXIS).normalize().multiply(dst);
            final Vector3 pos2 = center.clone();
            final Vector3 mot = center.clone().subtract(pos2.clone().add(v)).normalize().multiply(0.14);
            final int age = 20 + this.rand.nextInt(30);
            final float size = 0.2f + this.rand.nextFloat() * 0.7f;
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos2).setScaleMultiplier(size).setMotion(mot).color(VFXColorFunction.WHITE).setMaxAge(age);
            if (this.rand.nextInt(6) == 0) {
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos2).setScaleMultiplier(size * 1.4f).setMotion(mot).color(VFXColorFunction.constant(this.constellation.getConstellationColor())).setGravityStrength(-4.0E-4f + this.rand.nextFloat() * -1.5E-4f).setMaxAge(age + 30);
            }
        }
        final double scale = 7.0;
        final double edgeScale = scale * 2.0 + 1.0;
        for (int j = 0; j < 7; ++j) {
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
        if (this.getTick() >= 200) {
            for (int j = 0; j < 3; ++j) {
                final Vector3 at = new Vector3(altar).add(0.5, 0.0, 0.5);
                at.addX(this.rand.nextFloat() * 7.0f * (this.rand.nextBoolean() ? 1 : -1));
                at.addZ(this.rand.nextFloat() * 7.0f * (this.rand.nextBoolean() ? 1 : -1));
                final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).setAlphaMultiplier(0.75f).alpha(VFXAlphaFunction.FADE_OUT).setGravityStrength(-0.001f + this.rand.nextFloat() * -5.0E-4f).color(VFXColorFunction.WHITE).setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.1f).setMaxAge(20 + this.rand.nextInt(10));
                if (this.rand.nextBoolean()) {
                    p.color(VFXColorFunction.constant(this.constellation.getConstellationColor()));
                }
                if (this.getTick() >= 400) {
                    p.setScaleMultiplier(0.3f + this.rand.nextFloat() * 0.15f);
                }
            }
        }
        if (this.getTick() >= 460 && this.getTick() % 5 == 0) {
            final Vector3 from2 = new Vector3(altar).add(0.5, 0.0, 0.5);
            MiscUtils.applyRandomOffset(from2, this.rand, 0.25f);
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from2).setup(from2.clone().addY(8.0), 2.4, 2.0).setAlphaMultiplier(0.8f).setMaxAge(30 + this.rand.nextInt(15));
        }
        if (this.getTick() >= 490) {
            for (int j = 0; j < 25; ++j) {
                final Vector3 at = new Vector3(altar).add(0.5, 0.0, 0.5).addY(this.rand.nextFloat() * 0.5 + this.rand.nextFloat() * 0.5);
                final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).color(VFXColorFunction.WHITE).setMotion(Vector3.random().setY(0).normalize().multiply(0.025 + this.rand.nextFloat() * 0.075)).setAlphaMultiplier(0.75f).setScaleMultiplier(0.25f + this.rand.nextFloat() * 0.15f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(60 + this.rand.nextInt(40));
                if (this.rand.nextBoolean()) {
                    p.color(VFXColorFunction.constant(this.constellation.getConstellationColor()));
                }
            }
        }
    }
    
    @Override
    public boolean isFinished(final TileAttunementAltar altar) {
        return this.getTick() >= 500;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void stopEffects(final TileAttunementAltar altar) {
        if (this.isFinished(altar)) {
            SoundHelper.playSoundClientWorld(SoundsAS.ATTUNEMENT_ATLAR_ITEM_FINISH, altar.getBlockState().above(), 1.0f, 1.0f);
        }
        if (this.innerOrbital1 != null) {
            ((EntityComplexFX)this.innerOrbital1).requestRemoval();
        }
        if (this.attunementFlare != null) {
            ((EntityComplexFX)this.attunementFlare).requestRemoval();
        }
    }
    
    @Nullable
    private ItemEntity getEntity(final Level world) {
        final Entity entity = world.getEntityById(this.entityId);
        if (entity != null && entity.isAlive() && entity instanceof ItemEntity) {
            return (ItemEntity)entity;
        }
        return null;
    }
    
    @Override
    public void writeToNBT(final CompoundTag nbt) {
        super.writeToNBT(nbt);
        nbt.putString("constellation", this.constellation.getRegistryName().toString());
        nbt.putInt("entityId", this.entityId);
    }
    
    @Override
    protected void readFromNBT(final CompoundTag nbt) {
        super.readFromNBT(nbt);
        this.constellation = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(new ResourceLocation(nbt.getString("constellation")));
        this.entityId = nbt.getInt("entityId");
    }
}
