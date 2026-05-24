package hellfirepvp.astralsorcery.common.crafting.recipe;

import net.minecraft.item.crafting.IRecipeSerializer;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.RecipeType;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.object.PredicateBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import net.minecraft.world.level.ItemLike;
import java.util.ArrayList;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Predicate;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.block.BlockMatchInformation;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;

public class BlockTransmutation extends CustomMatcherRecipe implements GatedRecipe.Progression
{
    private final BlockState outputState;
    private final double starlight;
    private final IWeakConstellation constellation;
    private final List<BlockMatchInformation> stateCheck;
    private ItemStack outputDisplay;
    private Predicate<BlockState> matcher;
    
    public BlockTransmutation(final ResourceLocation recipeId, final BlockState outState, final double starlight) {
        this(recipeId, outState, starlight, null);
    }
    
    public BlockTransmutation(final ResourceLocation recipeId, final BlockState outState, final double starlight, @Nullable final IWeakConstellation constellation) {
        super(recipeId);
        this.stateCheck = new ArrayList<BlockMatchInformation>();
        this.matcher = null;
        this.outputState = outState;
        this.starlight = starlight;
        this.constellation = constellation;
        this.outputDisplay = new ItemStack((ItemLike)outState.getBlock());
    }
    
    @Nonnull
    @Override
    public ResearchProgression getRequiredProgression() {
        return ResearchProgression.ATTUNEMENT;
    }
    
    public boolean matches(@Nonnull final IWorld world, @Nonnull final BlockPos pos, @Nonnull final BlockState state, @Nonnull final IWeakConstellation constellation) {
        if (this.matcher == null) {
            this.matcher = PredicateBuilder.joinOr((Collection<? extends Predicate<BlockState>>)this.stateCheck);
        }
        return this.matcher.test(state) && (this.constellation == null || this.constellation.equals(constellation));
    }
    
    public void addInputOption(final BlockMatchInformation test) {
        this.matcher = null;
        this.stateCheck.add(test);
    }
    
    public List<BlockMatchInformation> getInputOptions() {
        return this.stateCheck;
    }
    
    public List<ItemStack> getInputDisplay() {
        return this.getInputOptions().stream().map((Function<? super Object, ?>)BlockMatchInformation::getDisplayStack).collect((Collector<? super Object, ?, List<ItemStack>>)Collectors.toList());
    }
    
    @Nonnull
    public BlockState getOutput() {
        return this.outputState;
    }
    
    public void setOutputDisplay(@Nonnull final ItemStack outputDisplay) {
        this.outputDisplay = outputDisplay;
    }
    
    @Nonnull
    public ItemStack getOutputDisplay() {
        return this.outputDisplay.copy();
    }
    
    public double getStarlightRequired() {
        return this.starlight;
    }
    
    @Nullable
    public IWeakConstellation getRequiredConstellation() {
        return this.constellation;
    }
    
    @Override
    public CustomRecipeSerializer<?> getSerializer() {
        return RecipeSerializersAS.BLOCK_TRANSMUTATION_SERIALIZER;
    }
    
    public RecipeType<?> func_222127_g() {
        return RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION.getType();
    }
}
