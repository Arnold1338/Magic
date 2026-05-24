package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.constellation.DrawnConstellation;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightbeam;
import hellfirepvp.astralsorcery.client.effect.vfx.FXLightning;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.client.lib.SpritesAS;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXSpritePlane;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravedStarMap;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.item.ItemInfusedGlass;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.tile.NamedInventoryTile;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileRefractionTable extends TileEntityTick implements NamedInventoryTile
{
    private static final float RUN_TIME = 200.0f;
    private int runTick;
    private ItemStack glassStack;
    private int parchmentCount;
    private ItemStack inputStack;
    private Object effectHalo;
    
    public TileRefractionTable() {
        super(TileEntityTypesAS.REFRACTION_TABLE);
        this.runTick = 0;
        this.glassStack = ItemStack.EMPTY;
        this.parchmentCount = 0;
        this.inputStack = ItemStack.EMPTY;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (this.func_145831_w().func_201670_d()) {
            this.playEngravingEffects();
        }
        else if (DayTimeHelper.isNight(this.func_145831_w()) && this.doesSeeSky() && isValidGlassStack(this.getGlassStack())) {
            final EngravedStarMap starMap = ItemInfusedGlass.getEngraving(this.getGlassStack());
            if (starMap != null && !this.hasParchment() && !this.getInputStack().isEmpty() && starMap.canAffect(this.getInputStack())) {
                ++this.runTick;
                if (this.runTick > 200.0f) {
                    this.setInputStack(starMap.applyEffects(this.getInputStack()));
                    final ItemStack glassStack = this.getGlassStack();
                    if (glassStack.func_96631_a(1, TileRefractionTable.rand, (ServerPlayer)null)) {
                        glassStack.shrink(1);
                        this.setGlassStack(glassStack);
                        SoundHelper.playSoundAround(SoundEvents.field_187561_bM, SoundSource.BLOCKS, this.func_145831_w(), (Vector3i)this.func_174877_v(), TileRefractionTable.rand.nextFloat() * 0.5f + 1.0f, TileRefractionTable.rand.nextFloat() * 0.2f + 0.8f);
                    }
                    this.resetWorkTick();
                }
                this.markForUpdate();
            }
            else {
                this.resetWorkTick();
            }
        }
        else {
            this.resetWorkTick();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEngravingEffects() {
        if (this.runTick <= 0) {
            return;
        }
        if (this.effectHalo != null && ((FXSpritePlane)this.effectHalo).isRemoved()) {
            EffectHelper.refresh(this.effectHalo, EffectTemplatesAS.TEXTURE_SPRITE);
        }
        if (this.effectHalo == null) {
            this.effectHalo = EffectHelper.of(EffectTemplatesAS.TEXTURE_SPRITE).spawn(new Vector3(this).add(0.5, 0.8, 0.5)).setSprite(SpritesAS.SPR_HALO_INFUSION).setAxis(Vector3.RotAxis.Y_AXIS).setNoRotation(0.0f).setScaleMultiplier(0.8f).setAlphaMultiplier(0.8f).alpha((fx, alpha, pTicks) -> Mth.func_76131_a(alpha * this.getRunProgress(), 0.0f, 1.0f)).refresh(RefreshFunction.tileExistsAnd(this, (thisTile, fx) -> thisTile.getRunProgress() > 0.0f));
        }
        final Vector3 offset = new Vector3(-0.3125, 1.505, -0.1875);
        final int random = TileRefractionTable.rand.nextInt(ColorsAS.REFRACTION_TABLE_COLORS.length);
        if (random >= ColorsAS.REFRACTION_TABLE_COLORS.length / 2) {
            offset.addX(1.5);
        }
        offset.addZ(random % (ColorsAS.REFRACTION_TABLE_COLORS.length / 2) * 0.25);
        offset.add(TileRefractionTable.rand.nextFloat() * 0.1, 0.0, TileRefractionTable.rand.nextFloat() * 0.1).add((Vector3i)this.field_174879_c);
        final Color color = ColorsAS.REFRACTION_TABLE_COLORS[random];
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setGravityStrength(-0.002f).setScaleMultiplier(0.15f + TileRefractionTable.rand.nextFloat() * 0.1f).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(color)).setMaxAge(30 + TileRefractionTable.rand.nextInt(30));
        if (TileRefractionTable.rand.nextFloat() < this.getRunProgress() * 2.0f) {
            final Vector3 target = new Vector3(this).add(0.5, 0.9, 0.5);
            EffectHelper.of(EffectTemplatesAS.LIGHTNING).spawn(offset).makeDefault(target).color(VFXColorFunction.constant(color));
            final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setScaleMultiplier(0.15f + TileRefractionTable.rand.nextFloat() * 0.1f).alpha(VFXAlphaFunction.proximity(target::clone, 1.0f)).color(VFXColorFunction.constant(color)).setMaxAge(45);
            final Vector3 mov = target.clone().subtract(offset).normalize().multiply(0.05 * TileRefractionTable.rand.nextFloat());
            p.setMotion(mov);
        }
        if (TileRefractionTable.rand.nextInt(3) == 0) {
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(offset).setup(offset.clone().addY(0.56f + TileRefractionTable.rand.nextFloat() * 0.2f), 0.25, 0.25).color(VFXColorFunction.constant(color));
        }
        if (TileRefractionTable.rand.nextInt(4) == 0) {
            final Color beamColor = MiscUtils.eitherOf(TileRefractionTable.rand, new Color[] { ColorsAS.CONSTELLATION_TYPE_MAJOR, ColorsAS.CONSTELLATION_TYPE_WEAK, ColorsAS.CONSTELLATION_TYPE_MINOR });
            final Vector3 beamOffset = new Vector3(this).add(0.1 + TileRefractionTable.rand.nextFloat() * 0.8, 0.8, 0.1 + TileRefractionTable.rand.nextFloat() * 0.8f);
            EffectHelper.of(EffectTemplatesAS.LIGHTBEAM).spawn(beamOffset).setup(beamOffset.clone().addY(1.0 + TileRefractionTable.rand.nextFloat() * 0.5), 0.5, 0.5).color(VFXColorFunction.constant(beamColor)).setMaxAge(25 + TileRefractionTable.rand.nextInt(5));
        }
    }
    
    private void resetWorkTick() {
        if (this.runTick > 0) {
            this.runTick = 0;
            this.markForUpdate();
        }
    }
    
    public int addParchment(final int toAdd) {
        if (this.inputStack.isEmpty()) {
            final int overflow = Math.max(this.parchmentCount + toAdd - 64, 0);
            final int addable = toAdd - overflow;
            this.parchmentCount += addable;
            this.markForUpdate();
            return overflow;
        }
        return toAdd;
    }
    
    public int getParchmentCount() {
        return this.parchmentCount;
    }
    
    public boolean hasParchment() {
        return this.parchmentCount > 0;
    }
    
    public void engraveGlass(final List<DrawnConstellation> constellations) {
        if (this.hasParchment() && this.hasUnengravedGlass()) {
            --this.parchmentCount;
            ItemInfusedGlass.setEngraving(this.getGlassStack(), EngravedStarMap.buildStarMap(this.func_145831_w(), constellations));
            this.markForUpdate();
        }
    }
    
    @Nonnull
    public ItemStack setInputStack(@Nonnull final ItemStack inputStack) {
        ItemStack prevInput = this.inputStack.copy();
        if (this.parchmentCount > 0) {
            prevInput = new ItemStack((ItemLike)ItemsAS.PARCHMENT, this.parchmentCount);
            this.parchmentCount = 0;
        }
        this.inputStack = inputStack.copy();
        this.markForUpdate();
        return prevInput;
    }
    
    @Nonnull
    public ItemStack getInputStack() {
        return this.hasParchment() ? new ItemStack((ItemLike)ItemsAS.PARCHMENT, this.getParchmentCount()) : this.inputStack.copy();
    }
    
    public static boolean isValidGlassStack(@Nonnull final ItemStack glassStack) {
        return !glassStack.isEmpty() && glassStack.getItem() instanceof ItemInfusedGlass;
    }
    
    @Nonnull
    public ItemStack setGlassStack(@Nonnull final ItemStack glassStack) {
        if (!glassStack.isEmpty() && !isValidGlassStack(glassStack)) {
            return ItemStack.EMPTY;
        }
        final ItemStack prevStack = this.glassStack;
        this.glassStack = glassStack;
        this.markForUpdate();
        return prevStack;
    }
    
    @Nonnull
    public ItemStack getGlassStack() {
        return this.glassStack;
    }
    
    public boolean hasUnengravedGlass() {
        return isValidGlassStack(this.getGlassStack()) && ItemInfusedGlass.getEngraving(this.getGlassStack()) == null;
    }
    
    public float getRunProgress() {
        return Mth.func_76131_a(this.runTick / 200.0f, 0.0f, 1.0f);
    }
    
    public void dropContents() {
        final Vector3 at = new Vector3(this).add(0.5, 0.5, 0.5);
        if (!this.getGlassStack().isEmpty()) {
            ItemUtils.dropItemNaturally(this.func_145831_w(), at.getX(), at.getY(), at.getZ(), this.getGlassStack());
            this.setGlassStack(ItemStack.EMPTY);
        }
        if (!this.getInputStack().isEmpty()) {
            ItemUtils.dropItemNaturally(this.func_145831_w(), at.getX(), at.getY(), at.getZ(), this.getInputStack());
            this.setInputStack(ItemStack.EMPTY);
        }
        this.markForUpdate();
    }
    
    @Override
    public Component getDisplayName() {
        return (Component)new Component("screen.astralsorcery.refraction_table");
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.runTick = compound.getInt("runTick");
        this.parchmentCount = compound.getInt("parchmentCount");
        this.inputStack = NBTHelper.getStack(compound, "inputStack");
        this.glassStack = NBTHelper.getStack(compound, "glassStack");
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.putInt("runTick", this.runTick);
        compound.putInt("parchmentCount", this.parchmentCount);
        NBTHelper.setStack(compound, "inputStack", this.inputStack);
        NBTHelper.setStack(compound, "glassStack", this.glassStack);
    }
}
