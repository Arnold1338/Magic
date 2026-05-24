package hellfirepvp.astralsorcery.common.crafting.nojson.starlight;

import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.item.crystal.ItemAttunedRockCrystal;
import hellfirepvp.astralsorcery.common.item.crystal.ItemRockCrystal;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import net.minecraft.world.level.block.Blocks;
import java.util.Random;
import net.minecraft.core.Vec3i;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCrystalBase;
import hellfirepvp.astralsorcery.common.data.config.entry.CraftingConfig;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import hellfirepvp.astralsorcery.AstralSorcery;

public class GrowCrystalSizeRecipe extends LiquidStarlightRecipe
{
    public GrowCrystalSizeRecipe() {
        super(AstralSorcery.key("crystal_grow"));
    }
    
    @Override
    public List<Ingredient> getInputForRender() {
        return (List<Ingredient>)Collections.singletonList(new CrystalIngredient(false, false));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Ingredient> getOutputForRender() {
        return (List<Ingredient>)Collections.singletonList(new CrystalIngredient(false, false));
    }
    
    @Override
    public boolean doesStartRecipe(final ItemStack item) {
        return (boolean)CraftingConfig.CONFIG.liquidStarlightCrystalGrowth.get() && !item.isEmpty() && item.getItem() instanceof ItemCrystalBase;
    }
    
    @Override
    public boolean matches(final ItemEntity trigger, final World world, final BlockPos at) {
        final List<Entity> otherEntities = this.getEntitiesInBlock((IWorld)world, at);
        otherEntities.remove(trigger);
        return otherEntities.isEmpty();
    }
    
    @Override
    public void doServerCraftTick(final ItemEntity trigger, final World world, final BlockPos at) {
        final Random r = new Random(MathHelper.func_180186_a((Vector3i)at));
        if (!world.func_201670_d() && this.getAndIncrementCraftingTick((Entity)trigger) > 80 + r.nextInt(40)) {
            final ItemStack stack = trigger.func_92059_d();
            CrystalAttributes attr = ((ItemCrystalBase)stack.getItem()).getAttributes(stack);
            if (attr != null && world.func_175656_a(at, Blocks.field_150350_a.defaultBlockState())) {
                if (attr.getTotalTierLevel() >= ((ItemCrystalBase)stack.getItem()).getMaxPropertyTiers()) {
                    return;
                }
                float chance = 1.0f;
                if (attr.getTotalTierLevel() >= ((ItemCrystalBase)stack.getItem()).getGeneratedPropertyTiers()) {
                    chance = 0.5f;
                }
                if (GrowCrystalSizeRecipe.rand.nextFloat() < chance) {
                    attr = attr.modifyLevel(CrystalPropertiesAS.Properties.PROPERTY_SIZE, 1);
                    ((ItemCrystalBase)stack.getItem()).setAttributes(stack, attr);
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void doClientEffectTick(final ItemEntity trigger, final World world, final BlockPos at) {
        Color c = ColorsAS.DEFAULT_GENERIC_PARTICLE;
        if (trigger.func_92059_d().getItem() instanceof ItemRockCrystal || trigger.func_92059_d().getItem() instanceof ItemAttunedRockCrystal) {
            c = ColorsAS.ROCK_CRYSTAL;
        }
        for (int i = 0; i < 3; ++i) {
            final Vector3 pos = Vector3.atEntityCenter((Entity)trigger);
            MiscUtils.applyRandomOffset(pos, GrowCrystalSizeRecipe.rand, 0.15f);
            final Vector3 motion = Vector3.RotAxis.Y_AXIS.clone();
            motion.rotate(Math.toRadians(10 + GrowCrystalSizeRecipe.rand.nextInt(20)), Vector3.RotAxis.X_AXIS).rotate(GrowCrystalSizeRecipe.rand.nextFloat() * 3.141592653589793 * 2.0, Vector3.RotAxis.Y_AXIS).normalize().multiply(0.07f + GrowCrystalSizeRecipe.rand.nextFloat() * 0.04f);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.FADE_OUT).setMotion(motion).setScaleMultiplier(0.05f + GrowCrystalSizeRecipe.rand.nextFloat() * 0.2f).color(VFXColorFunction.constant(c)).setMaxAge(30 + GrowCrystalSizeRecipe.rand.nextInt(20));
        }
    }
}
