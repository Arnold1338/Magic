package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.common.crafting.nojson.CustomRecipeRegistry;
import hellfirepvp.astralsorcery.common.crafting.nojson.CustomRecipe;
import hellfirepvp.astralsorcery.common.util.data.BiDiPair;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import hellfirepvp.astralsorcery.common.constellation.star.StarConnection;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import java.util.HashSet;
import hellfirepvp.astralsorcery.common.constellation.world.ConstellationHandler;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.crafting.nojson.AttunementCraftingRegistry;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import javax.annotation.Nullable;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.ItemConstellationPaper;
import net.minecraft.world.phys.Vec3;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.constellation.world.WorldContext;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.base.MoonPhase;
import hellfirepvp.astralsorcery.common.constellation.SkyHandler;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.client.util.sound.PositionedLoopSound;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.sounds.SoundSource;
import java.util.Set;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingSprite;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import java.util.Iterator;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraftforge.fml.LogicalSide;
import java.util.HashMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.core.BlockPos;
import java.util.Map;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.AttunementRecipe;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileAttunementAltar extends TileEntityTick
{
    private IConstellation activeConstellation;
    private AttunementRecipe.Active<?> currentRecipe;
    private Map<BlockPos, Object> activeStarSprites;
    private Object attunementAltarIdleSound;
    public static final int MAX_START_ANIMATION_TICK = 60;
    public static final int MAX_START_ANIMATION_SPIN = 100;
    public int activationTick;
    public int prevActivationTick;
    public boolean animate;
    public boolean tesrLocked;
    
    public TileAttunementAltar() {
        super(TileEntityTypesAS.ATTUNEMENT_ALTAR);
        this.activeConstellation = null;
        this.currentRecipe = null;
        this.activeStarSprites = new HashMap<BlockPos, Object>();
        this.attunementAltarIdleSound = null;
        this.activationTick = 0;
        this.prevActivationTick = 0;
        this.animate = false;
        this.tesrLocked = true;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.getLevel().level()) {
            if (!this.doesSeeSky() || !this.hasMultiblock()) {
                if (this.activeConstellation != null) {
                    this.activeConstellation = null;
                    this.markForUpdate();
                }
                if (this.currentRecipe != null) {
                    this.currentRecipe = null;
                    this.markForUpdate();
                }
                return;
            }
            this.updateActiveConstellation();
            if (this.currentRecipe == null) {
                this.searchAndStartRecipe();
            }
            else {
                this.currentRecipe.tick(LogicalSide.SERVER, this);
                if (!this.currentRecipe.matches(this)) {
                    this.currentRecipe = null;
                    this.markForUpdate();
                }
                else if (this.currentRecipe.isFinished(this)) {
                    this.finishActiveRecipe();
                }
            }
        }
        else {
            this.tickEffects();
            if (this.currentRecipe != null) {
                this.currentRecipe.tick(LogicalSide.CLIENT, this);
            }
        }
    }
    
    private void updateActiveConstellation() {
        if (this.getTicksExisted() % 20 == 0) {
            final IConstellation found = this.searchActiveConstellation();
            if (this.activeConstellation == null) {
                if (found != null) {
                    this.activeConstellation = found;
                    this.markForUpdate();
                }
            }
            else if (found != null) {
                if (!this.activeConstellation.equals(found)) {
                    this.activeConstellation = found;
                    this.markForUpdate();
                }
            }
            else {
                this.activeConstellation = null;
                this.markForUpdate();
            }
            if (this.activeConstellation != null) {
                for (final BlockPos pos : this.getConstellationPositions(this.activeConstellation)) {
                    final TileSpectralRelay relay = MiscUtils.getTileAt((IBlockReader)this.getLevel(), pos, TileSpectralRelay.class, false);
                    if (relay != null && !relay.getInventory().getStackInSlot(0).isEmpty()) {
                        ItemUtils.dropInventory((IItemHandler)relay.getInventory(), this.getLevel(), pos.above());
                        relay.getInventory().clearInventory();
                    }
                }
            }
        }
    }
    
    public void finishActiveRecipe() {
        if (this.currentRecipe != null) {
            this.currentRecipe.finishRecipe(this);
            this.currentRecipe.stopCrafting(this);
            this.currentRecipe = null;
            this.markForUpdate();
            EntityFlare.spawnAmbientFlare(this.getLevel(), this.getBlockState().offset(-5 + TileAttunementAltar.rand.nextInt(11), 1 + TileAttunementAltar.rand.nextInt(3), -5 + TileAttunementAltar.rand.nextInt(11)));
            EntityFlare.spawnAmbientFlare(this.getLevel(), this.getBlockState().offset(-5 + TileAttunementAltar.rand.nextInt(11), 1 + TileAttunementAltar.rand.nextInt(3), -5 + TileAttunementAltar.rand.nextInt(11)));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickEffects() {
        if (!this.hasMultiblock() || !this.doesSeeSky()) {
            this.tickEffectNonActive();
            return;
        }
        this.spawnAmbientEffects();
        this.spawnHighlightedEffects();
        this.tickEffectsConstellation();
        if (!this.canPlayConstellationActiveEffects()) {
            this.tickEffectNonActive();
            return;
        }
        this.tickEffectActive();
        this.tickConstellationBeams();
        if (this.getActiveRecipe() == null) {
            this.tickSoundIdle();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickEffectsConstellation() {
        if (this.activeConstellation != null) {
            final Set<BlockPos> positions = this.getConstellationPositions(this.activeConstellation);
            for (final BlockPos key : this.activeStarSprites.keySet()) {
                if (!positions.contains(key)) {
                    final FXFacingSprite sprite = this.activeStarSprites.get(key);
                    if (sprite.isRemoved()) {
                        continue;
                    }
                    sprite.requestRemoval();
                }
            }
            final float night = DayTimeHelper.getCurrentDaytimeDistribution(this.getLevel());
            for (final BlockPos key2 : positions) {
                if (!this.activeStarSprites.containsKey(key2)) {
                    final FXFacingSprite sprite2 = EffectHelper.of(EffectTemplatesAS.FACING_SPRITE).spawn(new Vector3((Vec3i)key2).add(0.5, 0.5, 0.5)).setSprite(SpritesAS.SPR_RELAY_FLARE).setScaleMultiplier(1.4f).refresh(fx -> this.canPlayConstellationActiveEffects());
                    this.activeStarSprites.put(key2, sprite2);
                }
                else {
                    final FXFacingSprite spr = this.activeStarSprites.get(key2);
                    if (spr.isRemoved()) {
                        EffectHelper.refresh(spr, EffectTemplatesAS.FACING_SPRITE);
                    }
                }
                if (night >= 0.1f && this.getActiveRecipe() == null) {
                    this.playConstellationHighlightParticles(this.activeConstellation, key2, night);
                }
            }
            this.playAltarConstellationHighlightParticles(this.activeConstellation, night);
        }
        else if (!this.activeStarSprites.isEmpty()) {
            for (final BlockPos key3 : this.activeStarSprites.keySet()) {
                final FXFacingSprite sprite3 = this.activeStarSprites.get(key3);
                if (!sprite3.isRemoved()) {
                    sprite3.requestRemoval();
                }
            }
            this.activeStarSprites.clear();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickSoundIdle() {
        if (SoundHelper.getSoundVolume(SoundSource.BLOCKS) <= 0.0f) {
            this.attunementAltarIdleSound = null;
            return;
        }
        if (this.attunementAltarIdleSound == null || ((PositionedLoopSound)this.attunementAltarIdleSound).hasStoppedPlaying()) {
            this.attunementAltarIdleSound = SoundHelper.playSoundLoopFadeInClient(SoundsAS.ATTUNEMENT_ATLAR_IDLE, new Vector3(this).add(0.5, 1.0, 0.5), 0.4f, 1.0f, false, s -> !this.canPlayConstellationActiveEffects() || SoundHelper.getSoundVolume(SoundSource.BLOCKS) <= 0.0f || this.getActiveRecipe() != null).setFadeInTicks(20.0f).setFadeOutTicks(20.0f);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickConstellationBeams() {
        final VFXColorFunction<?> beamColor = VFXColorFunction.constant(ColorsAS.DEFAULT_GENERIC_PARTICLE);
        final float beamSize = 0.8f;
        for (final Tuple<BlockPos, BlockPos> conn : this.getConstellationConnectionPositions(this.activeConstellation)) {
            final Vector3 from = new Vector3((Vec3i)conn.getA()).add(0.5, 0.5, 0.5);
            final Vector3 to = new Vector3((Vec3i)conn.getB()).add(0.5, 0.5, 0.5);
            if (this.getTicksExisted() % 50 == 0) {
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(from).setup(to, beamSize, beamSize).color(beamColor);
                EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(to).setup(from, beamSize, beamSize).color(beamColor);
            }
            if (TileAttunementAltar.rand.nextBoolean()) {
                final Vector3 at = from.clone().subtract(to).multiply(TileAttunementAltar.rand.nextFloat()).add(to).add(Vector3.random().multiply(TileAttunementAltar.rand.nextFloat() * 0.25f));
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(this.activeConstellation.getConstellationColor())).setScaleMultiplier(0.2f + TileAttunementAltar.rand.nextFloat() * 0.1f).setMaxAge(20 + TileAttunementAltar.rand.nextInt(10));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickEffectNonActive() {
        this.animate = false;
        this.prevActivationTick = this.activationTick;
        if (this.activationTick > 0) {
            --this.activationTick;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickEffectActive() {
        this.animate = true;
        this.prevActivationTick = this.activationTick;
        if (this.activationTick < 60) {
            ++this.activationTick;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean canPlayConstellationActiveEffects() {
        final WorldContext ctx = SkyHandler.getContext(this.getLevel(), LogicalSide.CLIENT);
        return ctx != null && !this.func_145837_r() && this.hasMultiblock() && this.doesSeeSky() && this.getActiveConstellation() != null && DayTimeHelper.isNight(this.getLevel()) && ctx.getConstellationHandler().isActiveCurrently(this.getActiveConstellation(), MoonPhase.fromWorld((IWorld)this.getLevel()));
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnAmbientEffects() {
        if (TileAttunementAltar.rand.nextBoolean()) {
            final Vector3 pos = new Vector3(this).add(TileAttunementAltar.rand.nextFloat() * 15.0f - 7.0f, 0.01, TileAttunementAltar.rand.nextFloat() * 15.0f - 7.0f);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).color(VFXColorFunction.WHITE).setAlphaMultiplier(0.7f).setScaleMultiplier(0.3f + TileAttunementAltar.rand.nextFloat() * 0.1f);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnHighlightedEffects() {
        if (this.canPlayConstellationActiveEffects()) {
            return;
        }
        final WorldContext ctx = SkyHandler.getContext(this.getLevel(), LogicalSide.CLIENT);
        if (ctx == null) {
            return;
        }
        final Player player = (Player)Minecraft.getInstance().player;
        if (player == null || player.func_195048_a(Vec3.func_237489_a_((Vec3i)this.getBlockState())) >= 256.0) {
            return;
        }
        final Tuple<Hand, ItemStack> heldTpl = MiscUtils.getMainOrOffHand((LivingEntity)player, stack -> stack.getItem() instanceof ItemConstellationPaper);
        if (heldTpl != null) {
            final ItemStack cstPaper = (ItemStack)heldTpl.getB();
            final IConstellation cst = ((ItemConstellationPaper)cstPaper.getItem()).getConstellation(cstPaper);
            if (cst != null && ResearchHelper.getClientProgress().hasConstellationDiscovered(cst)) {
                final float night = DayTimeHelper.getCurrentDaytimeDistribution(this.getLevel());
                if (night >= 0.1f) {
                    for (final BlockPos pos : this.getConstellationPositions(cst)) {
                        this.playConstellationHighlightParticles(cst, pos, night);
                    }
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playConstellationHighlightParticles(final IConstellation cst, final BlockPos pos, final float nightPercent) {
        final Vector3 at = new Vector3((Vec3i)pos).add(0.5, 0.0, 0.5);
        final Vector3 offset = Vector3.random().multiply(0.5f).setY(0);
        if (TileAttunementAltar.rand.nextInt(3) == 0) {
            offset.multiply(0.5);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at.add(offset)).color(VFXColorFunction.constant(cst.getConstellationColor())).setGravityStrength(-0.002f).setMotion(Vector3.random().addY(3.0).normalize().multiply(0.03 + TileAttunementAltar.rand.nextFloat() * 0.01)).setAlphaMultiplier(0.6f * nightPercent).setScaleMultiplier(0.15f + TileAttunementAltar.rand.nextFloat() * 0.1f).alpha(VFXAlphaFunction.FADE_OUT);
        }
        else if (TileAttunementAltar.rand.nextInt(3) == 0) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at.add(offset)).setGravityStrength(-5.0E-4f).setMotion(Vector3.random().addY(3.0).normalize().multiply(0.005)).setAlphaMultiplier(0.6f * nightPercent).setScaleMultiplier(0.4f + TileAttunementAltar.rand.nextFloat() * 0.2f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(60 + TileAttunementAltar.rand.nextInt(40));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playAltarConstellationHighlightParticles(final IConstellation cst, final float nightPercent) {
        final Vector3 at = new Vector3((Vec3i)this.getBlockState()).add(0.5, 0.0, 0.5).add(Vector3.random().setY(0).multiply(0.65f));
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).color(VFXColorFunction.constant(cst.getConstellationColor().brighter())).setGravityStrength(-0.0015f).setMotion(Vector3.random().addY(3.0).normalize().multiply(0.03 + TileAttunementAltar.rand.nextFloat() * 0.015)).setAlphaMultiplier(0.85f * nightPercent).setScaleMultiplier(0.2f + TileAttunementAltar.rand.nextFloat() * 0.1f).alpha(VFXAlphaFunction.FADE_OUT);
    }
    
    @Nullable
    public IConstellation getActiveConstellation() {
        return this.activeConstellation;
    }
    
    @Nullable
    public AttunementRecipe.Active<?> getActiveRecipe() {
        return this.currentRecipe;
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        return StructureTypesAS.PTYPE_ATTUNEMENT_ALTAR;
    }
    
    private void searchAndStartRecipe() {
        if (this.currentRecipe != null) {
            return;
        }
        final AttunementRecipe<?> match = this.searchMatchingRecipe();
        if (match != null) {
            (this.currentRecipe = (AttunementRecipe.Active<?>)match.createRecipe(this)).startCrafting(this);
            this.markForUpdate();
        }
    }
    
    @Nullable
    private AttunementRecipe<?> searchMatchingRecipe() {
        for (final AttunementRecipe<?> recipe : AttunementCraftingRegistry.INSTANCE.getRecipes()) {
            if (recipe.canStartCrafting(this)) {
                return recipe;
            }
        }
        return null;
    }
    
    @Nullable
    private IConstellation searchActiveConstellation() {
        final WorldContext ctx = SkyHandler.getContext(this.getLevel());
        if (ctx == null) {
            return null;
        }
        final ConstellationHandler cstHandler = ctx.getConstellationHandler();
        IConstellation match = null;
        for (final IConstellation cst : RegistriesAS.REGISTRY_CONSTELLATIONS.getValues()) {
            boolean isValid = true;
            for (final BlockPos expectedRelayPos : this.getConstellationPositions(cst)) {
                if (expectedRelayPos.equals((Object)this.getBlockState())) {
                    continue;
                }
                final BlockEntity tile = MiscUtils.getTileAt((IBlockReader)this.getLevel(), expectedRelayPos, BlockEntity.class, true);
                if (!(tile instanceof TileSpectralRelay) && !(tile instanceof TileAttunementAltar)) {
                    isValid = false;
                    break;
                }
            }
            if (isValid) {
                match = cst;
                break;
            }
        }
        if (match != null && cstHandler.isActiveCurrently(match, MoonPhase.fromWorld((IWorld)this.getLevel()))) {
            return match;
        }
        return null;
    }
    
    public Set<BlockPos> getConstellationPositions(final IConstellation cst) {
        final Set<BlockPos> offsetPositions = new HashSet<BlockPos>();
        for (final StarLocation sl : cst.getStars()) {
            final int x = sl.x / 2;
            final int z = sl.y / 2;
            offsetPositions.add(new BlockPos(x - 7, 0, z - 7).func_177971_a((Vec3i)this.getBlockState()));
        }
        return offsetPositions;
    }
    
    private Set<Tuple<BlockPos, BlockPos>> getConstellationConnectionPositions(final IConstellation cst) {
        final Set<Tuple<BlockPos, BlockPos>> offsetPositions = new HashSet<Tuple<BlockPos, BlockPos>>();
        for (final StarConnection conn : cst.getStarConnections()) {
            final StarLocation from = ((BiDiPair<StarLocation, V>)conn).getLeft();
            final StarLocation to = ((BiDiPair<K, StarLocation>)conn).getRight();
            final int fX = from.x / 2;
            final int fZ = from.y / 2;
            final int tX = to.x / 2;
            final int tZ = to.y / 2;
            offsetPositions.add((Tuple<BlockPos, BlockPos>)new Tuple((Object)new BlockPos(fX - 7, 0, fZ - 7).func_177971_a((Vec3i)this.getBlockState()), (Object)new BlockPos(tX - 7, 0, tZ - 7).func_177971_a((Vec3i)this.getBlockState())));
        }
        return offsetPositions;
    }
    
    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().func_72321_a(3.5, 2.0, 3.5);
    }
    
    @Override
    public void writeNetNBT(final CompoundTag compound) {
        super.writeNetNBT(compound);
        if (this.activeConstellation != null) {
            compound.putString("activeConstellation", this.activeConstellation.getRegistryName().toString());
        }
        if (this.currentRecipe != null) {
            final CompoundTag nbt = new CompoundTag();
            nbt.putString("recipe", ((CustomRecipe)this.currentRecipe.getRecipe()).getKey().toString());
            this.currentRecipe.writeToNBT(nbt);
            compound.put("currentRecipe", (Tag)nbt);
        }
    }
    
    @Override
    public void readNetNBT(final CompoundTag compound) {
        super.readNetNBT(compound);
        if (compound.contains("activeConstellation")) {
            this.activeConstellation = ConstellationRegistry.getConstellation(new ResourceLocation(compound.getString("activeConstellation")));
        }
        else {
            this.activeConstellation = null;
        }
        if (compound.contains("currentRecipe")) {
            final CompoundTag nbt = compound.func_74775_l("currentRecipe");
            final AttunementRecipe recipe = ((CustomRecipeRegistry<AttunementRecipe>)AttunementCraftingRegistry.INSTANCE).getRecipe(new ResourceLocation(nbt.getString("recipe")));
            if (recipe != null) {
                this.currentRecipe = recipe.deserialize(this, nbt, this.currentRecipe);
            }
            else if (this.currentRecipe != null) {
                this.currentRecipe.stopEffects(this);
                this.currentRecipe = null;
            }
        }
        else if (this.currentRecipe != null) {
            this.currentRecipe.stopEffects(this);
            this.currentRecipe = null;
        }
    }
}
