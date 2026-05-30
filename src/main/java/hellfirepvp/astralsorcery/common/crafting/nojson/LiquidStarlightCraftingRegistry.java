package hellfirepvp.astralsorcery.common.crafting.nojson;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import hellfirepvp.astralsorcery.common.crafting.nojson.starlight.MergeCrystalsRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.starlight.FormGemCrystalClusterRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.starlight.FormCelestialCrystalClusterRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.starlight.GrowCrystalSizeRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.starlight.InfusedWoodRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.starlight.LiquidStarlightRecipe;

public class LiquidStarlightCraftingRegistry extends CustomRecipeRegistry<LiquidStarlightRecipe>
{
    public static final LiquidStarlightCraftingRegistry INSTANCE;
    
    @Override
    public void init() {
        ((CustomRecipeRegistry<InfusedWoodRecipe>)this).register(new InfusedWoodRecipe());
        ((CustomRecipeRegistry<GrowCrystalSizeRecipe>)this).register(new GrowCrystalSizeRecipe());
        ((CustomRecipeRegistry<FormCelestialCrystalClusterRecipe>)this).register(new FormCelestialCrystalClusterRecipe());
        ((CustomRecipeRegistry<FormGemCrystalClusterRecipe>)this).register(new FormGemCrystalClusterRecipe());
        ((CustomRecipeRegistry<MergeCrystalsRecipe>)this).register(new MergeCrystalsRecipe());
    }
    
    @Nullable
    public LiquidStarlightRecipe getRecipeFor(final ItemEntity itemEntity, final Level world, final BlockPos at) {
        return this.getRecipes().stream().filter(recipe -> recipe.doesStartRecipe(itemEntity.func_92059_d())).filter(recipes -> recipes.matches(itemEntity, world, at)).findFirst().orElse(null);
    }
    
    public static void tryCraft(final ItemEntity itemEntity, final BlockPos at) {
        if (!itemEntity.isAlive()) {

        }
        final Level world = itemEntity.level();
        final LiquidStarlightRecipe recipe = LiquidStarlightCraftingRegistry.INSTANCE.getRecipeFor(itemEntity, world, at);
        if (recipe != null) {
            if (!world.level()) {
                recipe.doServerCraftTick(itemEntity, world, at);
            }
            else {
                recipe.doClientEffectTick(itemEntity, world, at);
            }
        }
    }
    
    static {
        INSTANCE = new LiquidStarlightCraftingRegistry();
    }
}
