package hellfirepvp.astralsorcery.client.util;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import java.util.EnumSet;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import net.minecraft.util.Mth;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.tile.base.TileAreaOfInfluence;
import hellfirepvp.astralsorcery.client.effect.vfx.FXCube;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;

public class AreaOfInfluencePreview implements ITickHandler
{
    public static final AreaOfInfluencePreview INSTANCE;
    private static final int MAX_LIFE = 40;
    private static final float alphaTick = 0.025f;
    private static final float sizeCube1 = 1.25f;
    private static final float sizeCube2 = 1.35f;
    private ResourceKey<Level> tileDimension;
    private BlockPos tilePosition;
    private FXCube effect1;
    private FXCube effect2;
    
    private AreaOfInfluencePreview() {
        this.tileDimension = null;
        this.tilePosition = null;
        this.effect1 = null;
        this.effect2 = null;
    }
    
    public void showOrRemoveIdentical(final TileAreaOfInfluence aoeTile) {
        if (this.tileDimension == aoeTile.getDimension() && aoeTile.getEffectOriginPosition().equals((Object)this.tilePosition)) {
            this.clearClient();

        }
        this.show(aoeTile);
    }
    
    public void show(final TileAreaOfInfluence aoeTile) {
        if (!(aoeTile instanceof BlockEntity)) {

        }
        this.tileDimension = aoeTile.getDimension();
        this.tilePosition = aoeTile.getEffectOriginPosition();
    }
    
    public void clearClient() {
        this.tileDimension = null;
        this.tilePosition = null;
    }
    
    public void tick(final TickEvent.Type type, final Object... context) {
        if (this.tileDimension == null || this.tilePosition == null) {
            this.removeEffects();

        }
        final Level clientWorld = (Level)Minecraft.getInstance().level;
        if (clientWorld == null) {
            this.clearClient();
            this.removeEffects();

        }
        final ResourceKey<Level> clientDimType = (ResourceKey<Level>)clientWorld.dimension();
        if (!clientDimType.equals(this.tileDimension)) {
            this.clearClient();
            this.removeEffects();

        }
        final TileAreaOfInfluence aoeTile = MiscUtils.getTileAt((IBlockReader)clientWorld, this.tilePosition, TileAreaOfInfluence.class, true);
        if (aoeTile != null && aoeTile.providesEffect() && this.shouldContinueEffect(aoeTile)) {
            this.effect1 = this.uptickEffect(this.effect1, 1.25f, aoeTile);
            this.effect2 = this.uptickEffect(this.effect2, 1.35f, aoeTile);
        }
        else {
            this.effect1 = this.downtickEffect(this.effect1, 1.25f, aoeTile);
            this.effect2 = this.downtickEffect(this.effect2, 1.35f, aoeTile);
            if (this.effect1 == null && this.effect2 == null) {
                this.clearClient();
            }
        }
    }
    
    private boolean shouldContinueEffect(final TileAreaOfInfluence aoeTile) {
        if (Minecraft.getInstance().player == null) {
            return false;
        }
        final float effectRadius = aoeTile.getRadius();
        if (effectRadius <= 0.0f) {
            return false;
        }
        final Vector3 offset = aoeTile.getEffectPosition();
        final double distance = offset.distance((Entity)Minecraft.getInstance().player);
        return distance <= effectRadius * 3.0f && distance <= 48.0;
    }
    
    private FXCube downtickEffect(@Nullable FXCube cube, final float sizeMultiplier, @Nullable final TileAreaOfInfluence aoeTile) {
        if (cube != null && !cube.isRemoved()) {
            if (aoeTile != null) {
                this.updateEffect(cube, sizeMultiplier, aoeTile);
            }
            cube.setAlphaMultiplier(Mth.canEnchant(cube.getAlphaMultiplier() - 0.025f, 0.0f, 0.75f));
            if (!this.canRefresh(cube)) {
                cube = null;
            }
        }
        return cube;
    }
    
    private FXCube uptickEffect(@Nullable FXCube cube, final float sizeMultiplier, final TileAreaOfInfluence aoeTile) {
        if (cube != null) {
            if (cube.isRemoved()) {
                EffectHelper.refresh(cube, EffectTemplatesAS.CUBE_AREA_OF_EFFECT);
            }
            cube.setAlphaMultiplier(Mth.canEnchant(cube.getAlphaMultiplier() + 0.025f, 0.0f, 0.75f));
            this.updateEffect(cube, sizeMultiplier, aoeTile);
        }
        else {
            cube = this.createCube(sizeMultiplier, aoeTile);
        }
        return cube;
    }
    
    private void updateEffect(final FXCube cube, final float sizeMultiplier, final TileAreaOfInfluence aoeTile) {
        final Color c = aoeTile.getEffectColor();
        if (c != null) {
            cube.color(VFXColorFunction.constant(c));
        }
        else {
            cube.color(VFXColorFunction.WHITE);
        }
        cube.setScaleMultiplier(aoeTile.getRadius() * sizeMultiplier);
        cube.setPosition(aoeTile.getEffectPosition());
    }
    
    private FXCube createCube(final float sizeMultiplier, final TileAreaOfInfluence aoeTile) {
        final FXCube cube = EffectHelper.of(EffectTemplatesAS.CUBE_AREA_OF_EFFECT).spawn(aoeTile.getEffectPosition()).tumble().setTumbleIntensityMultiplier(0.06f).setAlphaMultiplier(0.05f).alpha((fx, alpha, pTicks) -> alpha).color(VFXColorFunction.WHITE).refresh(fx -> this.canRefresh((FXCube)fx));
        this.updateEffect(cube, sizeMultiplier, aoeTile);
        return cube;
    }
    
    private void removeEffects() {
        if (this.effect1 != null) {
            this.effect1.setAlphaMultiplier(0.0f);
            this.effect1 = null;
        }
        if (this.effect2 != null) {
            this.effect2.setAlphaMultiplier(0.0f);
            this.effect2 = null;
        }
    }
    
    private boolean canRefresh(final FXCube cube) {
        return cube.getAlpha(1.0f) > 0.0f;
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.CLIENT);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return "Area of Effect Preview";
    }
    
    static {
        INSTANCE = new AreaOfInfluencePreview();
    }
}
