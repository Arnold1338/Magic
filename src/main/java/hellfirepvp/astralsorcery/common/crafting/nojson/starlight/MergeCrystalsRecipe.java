package hellfirepvp.astralsorcery.common.crafting.nojson.starlight;

import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import net.minecraft.world.level.block.Blocks;
import java.util.Random;
import net.minecraft.core.Vec3i;
import net.minecraft.util.math.MathHelper;
import java.util.Optional;
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
import java.util.Arrays;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredient;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import hellfirepvp.astralsorcery.AstralSorcery;

public class MergeCrystalsRecipe extends LiquidStarlightRecipe
{
    public MergeCrystalsRecipe() {
        super(AstralSorcery.key("merge_crystals"));
    }
    
    @Override
    public List<Ingredient> getInputForRender() {
        return Arrays.asList(new CrystalIngredient(false, false), new CrystalIngredient(false, false));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Ingredient> getOutputForRender() {
        return (List<Ingredient>)Collections.singletonList(new CrystalIngredient(false, false));
    }
    
    @Override
    public boolean doesStartRecipe(final ItemStack item) {
        return (boolean)CraftingConfig.CONFIG.liquidStarlightDropInfusedWood.get() && !item.isEmpty() && item.getItem() instanceof ItemCrystalBase;
    }
    
    @Override
    public boolean matches(final ItemEntity trigger, final World world, final BlockPos at) {
        final List<Entity> otherEntities = this.getEntitiesInBlock((IWorld)world, at);
        otherEntities.remove(trigger);
        final Optional<Entity> crystalEntity = otherEntities.stream().filter(e -> e instanceof ItemEntity).filter(e -> ((ItemEntity)e).func_92059_d().getItem() instanceof ItemCrystalBase).findFirst();
        return crystalEntity.isPresent() && otherEntities.size() == 1;
    }
    
    @Override
    public void doServerCraftTick(final ItemEntity trigger, final World world, final BlockPos at) {
        final Random r = new Random(MathHelper.func_180186_a((Vector3i)at));
        final ItemStack crystalFoundOne;
        final ItemStack crystalFoundTwo;
        if (!world.func_201670_d() && this.getAndIncrementCraftingTick((Entity)trigger) > 40 + r.nextInt(20) && (crystalFoundOne = this.consumeItemEntityInBlock((IWorld)world, at, 1, stack -> stack.getItem() instanceof ItemCrystalBase)) != null && (crystalFoundTwo = this.consumeItemEntityInBlock((IWorld)world, at, 1, stack -> stack.getItem() instanceof ItemCrystalBase)) != null && world.func_180501_a(at, Blocks.field_150350_a.defaultBlockState(), 11)) {
            final ItemCrystalBase crystalOne = (ItemCrystalBase)crystalFoundOne.getItem();
            CrystalAttributes attrOne = crystalOne.getAttributes(crystalFoundOne);
            attrOne = ((attrOne != null) ? attrOne : CrystalAttributes.Builder.newBuilder(false).build());
            final ItemCrystalBase crystalTwo = (ItemCrystalBase)crystalFoundTwo.getItem();
            CrystalAttributes attrTwo = crystalTwo.getAttributes(crystalFoundTwo);
            attrTwo = ((attrTwo != null) ? attrTwo : CrystalAttributes.Builder.newBuilder(false).build());
            final CrystalAttributes mergeTo = (attrOne.getTotalTierLevel() >= attrTwo.getTotalTierLevel()) ? attrOne : attrTwo;
            CrystalAttributes mergeFrom = (attrOne.getTotalTierLevel() >= attrTwo.getTotalTierLevel()) ? attrTwo : attrOne;
            final ItemStack resultStack = (attrOne.getTotalTierLevel() >= attrTwo.getTotalTierLevel()) ? crystalFoundOne.copy() : crystalFoundTwo.copy();
            final ItemCrystalBase resultCrystal = (ItemCrystalBase)resultStack.getItem();
            final CrystalAttributes.Builder resultBuilder = CrystalAttributes.Builder.newBuilder(false).addAll(mergeTo);
            final int freeProperties = resultCrystal.getMaxPropertyTiers() - mergeTo.getTotalTierLevel();
            final int copyAmount = Math.min(freeProperties, mergeFrom.getTotalTierLevel());
            int mergeCount = 0;
            for (int i = 0; i < copyAmount; ++i) {
                final CrystalAttributes.Attribute attr = MiscUtils.getWeightedRandomEntry(mergeFrom.getCrystalAttributes(), MergeCrystalsRecipe.rand, CrystalAttributes.Attribute::getTier);
                if (attr != null) {
                    mergeFrom = mergeFrom.modifyLevel(attr.getProperty(), -1);
                    if (MergeCrystalsRecipe.rand.nextFloat() <= 1.0f - Math.min(mergeCount, 3) * 0.25f) {
                        resultBuilder.addProperty(attr.getProperty(), 1);
                    }
                    ++mergeCount;
                }
            }
            resultCrystal.setAttributes(resultStack, resultBuilder.build());
            ItemUtils.dropItemNaturally(world, trigger.func_226277_ct_(), trigger.func_226278_cu_(), trigger.func_226281_cx_(), resultStack);
        }
    }
    
    @Override
    public void doClientEffectTick(final ItemEntity trigger, final World world, final BlockPos at) {
        for (int i = 0; i < 3; ++i) {
            final Vector3 pos = Vector3.atEntityCenter((Entity)trigger);
            MiscUtils.applyRandomOffset(pos, MergeCrystalsRecipe.rand, 0.15f);
            final Vector3 motion = Vector3.RotAxis.Y_AXIS.clone();
            motion.rotate(Math.toRadians(10 + MergeCrystalsRecipe.rand.nextInt(20)), Vector3.RotAxis.X_AXIS).rotate(MergeCrystalsRecipe.rand.nextFloat() * 3.141592653589793 * 2.0, Vector3.RotAxis.Y_AXIS).normalize().multiply(0.07f + MergeCrystalsRecipe.rand.nextFloat() * 0.04f);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.FADE_OUT).setMotion(motion).setScaleMultiplier(0.1f + MergeCrystalsRecipe.rand.nextFloat() * 0.2f).color(VFXColorFunction.WHITE).setMaxAge(35 + MergeCrystalsRecipe.rand.nextInt(20));
        }
        for (int i = 0; i < 4; ++i) {
            final Vector3 target = Vector3.atEntityCenter((Entity)trigger);
            final Vector3 pos2 = target.clone().add(Vector3.random().normalize().multiply(3.0f + MergeCrystalsRecipe.rand.nextFloat()));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos2).alpha(VFXAlphaFunction.PYRAMID.andThen(VFXAlphaFunction.proximity(target::clone, 2.0f))).motion(VFXMotionController.target(target::clone, 0.1f)).setScaleMultiplier(0.15f + MergeCrystalsRecipe.rand.nextFloat() * 0.1f).color(VFXColorFunction.WHITE).setMaxAge(20 + MergeCrystalsRecipe.rand.nextInt(20));
        }
    }
}
