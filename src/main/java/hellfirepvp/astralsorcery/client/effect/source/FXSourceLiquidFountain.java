package hellfirepvp.astralsorcery.client.effect.source;

import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.EffectProperties;
import java.awt.Color;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import net.minecraft.client.Minecraft;
import java.util.function.Function;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import hellfirepvp.astralsorcery.client.effect.vfx.FXCube;

public class FXSourceLiquidFountain extends FXSource<FXCube, BatchRenderContext<FXCube>>
{
    private final TextureAtlasSprite sprite;
    private final FluidStack fluid;
    
    public FXSourceLiquidFountain(final Vector3 pos, final FluidStack fluid) {
        super(pos, EffectTemplatesAS.CUBE_TRANSLUCENT_ATLAS);
        this.sprite = RenderingUtils.getParticleTexture(fluid);
        this.fluid = fluid;
        this.setMaxAge(40 + FXSourceLiquidFountain.rand.nextInt(30));
    }
    
    @Override
    public void tickSpawnFX(final Function<Vector3, FXCube> effectRegistrar) {
        final Vector3 motion = Vector3.positiveYRandom();
        motion.setY(motion.getY() * 8.0).normalize().multiply(new Vector3(0.01f + FXSourceLiquidFountain.rand.nextFloat() * 0.015f, 0.1f + FXSourceLiquidFountain.rand.nextFloat() * 0.015f, 0.01f + FXSourceLiquidFountain.rand.nextFloat() * 0.015f));
        effectRegistrar.apply(this.getPosition()).setTextureAtlasSprite(this.sprite).setTextureSubSizePercentage(1.0f).tumble().setAlphaMultiplier(DayTimeHelper.getCurrentDaytimeDistribution((World)Minecraft.func_71410_x().field_71441_e)).setScaleMultiplier(0.1f + FXSourceLiquidFountain.rand.nextFloat() * 0.05f).setMotion(motion).color((fx, pTicks) -> new Color(this.fluid.getFluid().getAttributes().getColor(this.fluid))).setGravityStrength(0.003f).setMaxAge(40 + FXSourceLiquidFountain.rand.nextInt(40));
    }
    
    @Override
    public void populateProperties(final EffectProperties<FXCube> properties) {
    }
}
