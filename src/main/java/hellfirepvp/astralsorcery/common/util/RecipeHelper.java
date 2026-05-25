package hellfirepvp.astralsorcery.common.util;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.multiplayer.play.ClientPlayNetHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Container;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import org.apache.commons.lang3.ObjectUtils;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.Inventory;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.util.Tuple;
import java.util.Optional;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;

public class RecipeHelper
{
    @Nullable
    public static SimpleAltarRecipe findAltarRecipeResult(final Predicate<ItemStack> match) {
        for (final SimpleAltarRecipe recipe : RecipeTypesAS.TYPE_ALTAR.getAllRecipes()) {
            if (match.test(recipe.getOutputForRender((Iterable<ItemStack>)Collections.emptyList()))) {
                return recipe;
            }
        }
        return null;
    }
    
    @Nonnull
    public static Optional<Tuple<ItemStack, Float>> findSmeltingResult(final Level world, final BlockState input) {
        final ItemStack stack = ItemUtils.createBlockStack(input);
        if (stack.isEmpty()) {
            return Optional.empty();
        }
        return findSmeltingResult(world, stack);
    }
    
    @Nonnull
    public static Optional<Tuple<ItemStack, Float>> findSmeltingResult(final Level world, final ItemStack input) {
        final RecipeManager mgr = world.func_199532_z();
        final Container inv = (Container)new Inventory(new ItemStack[] { input });
        final Optional<Recipe<Container>> optRecipe = (Optional<Recipe<Container>>)ObjectUtils.firstNonNull((Object[])new Optional[] { mgr.func_215371_a(RecipeType.field_222150_b, inv, world), mgr.func_215371_a(RecipeType.field_222153_e, inv, world), mgr.func_215371_a(RecipeType.field_222152_d, inv, world), Optional.empty() });
        return optRecipe.map(recipe -> {
            final ItemStack smeltResult = recipe.func_77572_b(inv).copy();
            float exp = 0.0f;
            if (recipe instanceof AbstractCookingRecipe) {
                exp = ((AbstractCookingRecipe)recipe).func_222138_b();
            }
            return new Tuple((Object)smeltResult, (Object)exp);
        });
    }
    
    @Nullable
    public static RecipeManager getRecipeManager() {
        if (EffectiveSide.get() == LogicalSide.CLIENT) {
            return getClientManager();
        }
        final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        if (srv != null) {
            return srv.func_199529_aN();
        }
        return null;
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    private static RecipeManager getClientManager() {
        final ClientPlayNetHandler conn;
        if ((conn = Minecraft.getInstance().func_147114_u()) != null) {
            return conn.func_199526_e();
        }
        return null;
    }
}
