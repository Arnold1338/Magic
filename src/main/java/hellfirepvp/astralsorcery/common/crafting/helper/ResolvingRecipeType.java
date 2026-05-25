package hellfirepvp.astralsorcery.common.crafting.helper;

import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import java.util.Iterator;
import net.minecraft.world.level.Container;
import java.util.Collection;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.Recipe;
import java.util.ArrayList;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import java.util.List;
import net.minecraft.core.Registry;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.item.crafting.RecipeType;
import java.util.function.BiPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.items.IItemHandler;

public class ResolvingRecipeType<C extends IItemHandler, T extends IHandlerRecipe<C>, R extends RecipeCraftingContext<T, C>>
{
    private final ResourceLocation id;
    private final Class<T> baseClass;
    private final BiPredicate<T, R> matchFct;
    private final RecipeType<T> type;
    
    public ResolvingRecipeType(final String name, final Class<T> baseClass, final BiPredicate<T, R> matchFct) {
        this(AstralSorcery.key(name), baseClass, matchFct);
    }
    
    public ResolvingRecipeType(final ResourceLocation id, final Class<T> baseClass, final BiPredicate<T, R> matchFct) {
        this.id = id;
        this.baseClass = baseClass;
        this.matchFct = matchFct;
        this.type = (RecipeType<T>)new RecipeType<T>() {
            @Override
            public String toString() {
                return ResolvingRecipeType.this.id.addTransientModifier();
            }
        };
        Registry.func_218322_a(Registry.field_218367_H, this.getRegistryName(), (Object)this.getType());
    }
    
    @Nonnull
    public List<T> getAllRecipes() {
        final RecipeManager mgr = RecipeHelper.getRecipeManager();
        if (mgr == null) {
            return Collections.emptyList();
        }
        final Collection<Recipe<Container>> recipeSet = mgr.getRecipeFor((RecipeType)this.type).values();
        final List<T> recipes = new ArrayList<T>(recipeSet.size());
        for (final Recipe<Container> rec : recipeSet) {
            recipes.add((T)(IHandlerRecipe)rec);
        }
        return recipes;
    }
    
    @Nonnull
    public List<T> getRecipes(final Predicate<T> test) {
        return this.getAllRecipes().stream().filter((Predicate<? super Object>)test).collect((Collector<? super Object, ?, List<T>>)Collectors.toList());
    }
    
    public final Class<T> getBaseClass() {
        return this.baseClass;
    }
    
    public RecipeType<T> getType() {
        return this.type;
    }
    
    public ResourceLocation getRegistryName() {
        return this.id;
    }
    
    @Nullable
    public T findRecipe(final R context) {
        return MiscUtils.iterativeSearch(this.getAllRecipes(), recipe -> this.matchFct.test((T)recipe, (R)context));
    }
    
    @Nonnull
    public List<T> findMatchingRecipes(final R context) {
        return this.getAllRecipes().stream().filter(recipe -> this.matchFct.test((T)recipe, (R)context)).collect((Collector<? super Object, ?, List<T>>)Collectors.toList());
    }
}
