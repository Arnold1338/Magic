package hellfirepvp.astralsorcery.common.crafting.nojson.starlight;

import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.crystal.CrystalGenerator;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCelestialCrystals;
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
import hellfirepvp.astralsorcery.common.item.ItemStardust;
import hellfirepvp.astralsorcery.common.data.config.entry.CraftingConfig;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Arrays;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.CrystalIngredient;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import java.util.List;
import hellfirepvp.astralsorcery.AstralSorcery;

public class FormCelestialCrystalClusterRecipe extends LiquidStarlightRecipe
{
    public FormCelestialCrystalClusterRecipe() {
        super(AstralSorcery.key("form_celestial_crystal_cluster"));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Ingredient> getInputForRender() {
        return Arrays.asList(Ingredient.func_193369_a(new ItemStack[] { new ItemStack((ItemLike)ItemsAS.STARDUST) }), new CrystalIngredient(false, false));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public List<Ingredient> getOutputForRender() {
        return Collections.singletonList(Ingredient.func_193369_a(new ItemStack[] { new ItemStack((ItemLike)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER) }));
    }
    
    @Override
    public boolean doesStartRecipe(final ItemStack item) {
        return (boolean)CraftingConfig.CONFIG.liquidStarlightFormCelestialCrystalCluster.get() && item.getItem() instanceof ItemStardust;
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
        final Random r = new Random(Mth.func_180186_a((Vector3i)at));
        final ItemStack crystalFound;
        if (!world.level() && this.getAndIncrementCraftingTick((Entity)trigger) > 50 + r.nextInt(20) && this.consumeItemEntityInBlock((IWorld)world, at, ItemsAS.STARDUST) != null && (crystalFound = this.consumeItemEntityInBlock((IWorld)world, at, 1, stack -> stack.getItem() instanceof ItemCrystalBase)) != null && world.func_175656_a(at, BlocksAS.CELESTIAL_CRYSTAL_CLUSTER.defaultBlockState())) {
            final TileCelestialCrystals cluster = MiscUtils.getTileAt((IBlockReader)world, at, TileCelestialCrystals.class, true);
            if (cluster != null) {
                final CrystalAttributes attr = ((CrystalAttributeItem)crystalFound.getItem()).getAttributes(crystalFound);
                final ItemStack targetCrystal = new ItemStack((ItemLike)ItemsAS.CELESTIAL_CRYSTAL);
                ((CrystalAttributeItem)crystalFound.getItem()).setAttributes(targetCrystal, attr);
                cluster.setAttributes(CrystalGenerator.upgradeProperties(targetCrystal));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void doClientEffectTick(final ItemEntity trigger, final Level world, final BlockPos at) {
        for (int i = 0; i < 3; ++i) {
            final Vector3 pos = Vector3.atEntityCorner((Entity)trigger);
            MiscUtils.applyRandomOffset(pos, FormCelestialCrystalClusterRecipe.rand, 0.15f);
            final Vector3 motion = Vector3.RotAxis.Y_AXIS.clone();
            motion.rotate(Math.toRadians(10 + FormCelestialCrystalClusterRecipe.rand.nextInt(20)), Vector3.RotAxis.X_AXIS).rotate(FormCelestialCrystalClusterRecipe.rand.nextFloat() * 3.141592653589793 * 2.0, Vector3.RotAxis.Y_AXIS).normalize().multiply(0.07f + FormCelestialCrystalClusterRecipe.rand.nextFloat() * 0.04f);
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).alpha(VFXAlphaFunction.FADE_OUT).setMotion(motion).setScaleMultiplier(0.05f + FormCelestialCrystalClusterRecipe.rand.nextFloat() * 0.2f).setMaxAge(30 + FormCelestialCrystalClusterRecipe.rand.nextInt(20));
        }
        for (int i = 0; i < 4; ++i) {
            final Vector3 target = Vector3.atEntityCorner((Entity)trigger);
            final Vector3 pos2 = target.clone().add(Vector3.random().normalize().multiply(3.0f + FormCelestialCrystalClusterRecipe.rand.nextFloat()));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos2).alpha(VFXAlphaFunction.PYRAMID.andThen(VFXAlphaFunction.proximity(target::clone, 2.0f))).motion(VFXMotionController.target(target::clone, 0.1f)).setScaleMultiplier(0.15f + FormCelestialCrystalClusterRecipe.rand.nextFloat() * 0.1f).setMaxAge(20 + FormCelestialCrystalClusterRecipe.rand.nextInt(20));
        }
    }
}
