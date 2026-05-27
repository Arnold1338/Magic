package hellfirepvp.astralsorcery.common.crafting.nojson.starlight;

import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.item.Item;
import java.util.Random;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import java.util.Optional;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCrystalBase;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import hellfirepvp.astralsorcery.common.item.dust.ItemIlluminationPowder;
import hellfirepvp.astralsorcery.common.data.config.entry.CraftingConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import java.util.Arrays;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredient;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import hellfirepvp.astralsorcery.AstralSorcery;

public class FormGemCrystalClusterRecipe extends LiquidStarlightRecipe
{
    public FormGemCrystalClusterRecipe() {
        super(AstralSorcery.key("form_gem_crystal_cluster"));
    }
    
    @Override
    public List<Ingredient> getInputForRender() {
        return Arrays.asList(Ingredient.func_193369_a(new ItemStack[] { new ItemStack((ItemLike)ItemsAS.ILLUMINATION_POWDER) }), new CrystalIngredient(false, false));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Ingredient> getOutputForRender() {
        return Collections.singletonList(Ingredient.func_193369_a(new ItemStack[] { new ItemStack((ItemLike)BlocksAS.GEM_CRYSTAL_CLUSTER) }));
    }
    
    @Override
    public boolean doesStartRecipe(final ItemStack item) {
        return (boolean)CraftingConfig.CONFIG.liquidStarlightFormGemCrystalCluster.get() && item.getItem() instanceof ItemIlluminationPowder;
    }
    
    @Override
    public boolean matches(final ItemEntity trigger, final Level world, final BlockPos at) {
        if (!world.getBlockState(at.renderItem()).func_215682_a((IBlockReader)world, at.renderItem(), (Entity)trigger, Direction.UP)) {
            return false;
        }
        final List<Entity> otherEntities = this.getEntitiesInBlock((IWorld)world, at);
        otherEntities.remove(trigger);
        final Optional<Entity> crystalEntity = otherEntities.stream().filter(e -> e instanceof ItemEntity).filter(e -> ((ItemEntity)e).func_92059_d().getItem() instanceof ItemCrystalBase).findFirst();
        return crystalEntity.isPresent() && otherEntities.size() == 1;
    }
    
    @Override
    public void doServerCraftTick(final ItemEntity trigger, final Level world, final BlockPos at) {
        final Random r = new Random(Mth.func_180186_a((Vec3i)at));
        if (this.getAndIncrementCraftingTick((Entity)trigger) > 50 + r.nextInt(20) && this.consumeItemEntityInBlock((IWorld)world, at, ItemsAS.ILLUMINATION_POWDER) != null && this.consumeItemEntityInBlock((IWorld)world, at, 1, stack -> stack.getItem() instanceof ItemCrystalBase) != null) {
            world.func_175656_a(at, BlocksAS.GEM_CRYSTAL_CLUSTER.defaultBlockState());
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void doClientEffectTick(final ItemEntity trigger, final Level world, final BlockPos at) {
        for (int i = 0; i < 4; ++i) {
            final Vector3 target = Vector3.atEntityCenter((Entity)trigger);
            final Vector3 pos = target.clone().add(Vector3.random().normalize().multiply(3.0f + FormGemCrystalClusterRecipe.rand.nextFloat()));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).color(VFXColorFunction.constant(ColorsAS.ILLUMINATION_POWDER_2)).alpha(VFXAlphaFunction.PYRAMID.andThen(VFXAlphaFunction.proximity(target::clone, 2.0f))).motion(VFXMotionController.target(target::clone, 0.09f)).setScaleMultiplier(0.25f + FormGemCrystalClusterRecipe.rand.nextFloat() * 0.2f).setMaxAge(20 + FormGemCrystalClusterRecipe.rand.nextInt(20));
        }
    }
}
