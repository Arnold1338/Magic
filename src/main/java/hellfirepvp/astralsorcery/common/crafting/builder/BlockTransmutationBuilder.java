package hellfirepvp.astralsorcery.common.crafting.builder;

import hellfirepvp.astralsorcery.common.crafting.helper.CustomMatcherRecipe;
import hellfirepvp.astralsorcery.common.lib.RecipeSerializersAS;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeSerializer;
import javax.annotation.Nonnull;
import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraftforge.registries.ForgeRegistryEntry;
import java.util.ArrayList;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.util.block.BlockMatchInformation;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;
import hellfirepvp.astralsorcery.common.crafting.helper.CustomRecipeBuilder;

public class BlockTransmutationBuilder extends CustomRecipeBuilder<BlockTransmutation>
{
    private final ResourceLocation id;
    private BlockState outputState;
    private double starlight;
    private IWeakConstellation constellation;
    private ItemStack outputDisplay;
    private final List<BlockMatchInformation> stateCheck;
    
    private BlockTransmutationBuilder(final ResourceLocation id) {
        this.outputState = Blocks.AIR.defaultBlockState();
        this.starlight = 200.0;
        this.constellation = null;
        this.outputDisplay = ItemStack.EMPTY;
        this.stateCheck = new ArrayList<BlockMatchInformation>();
        this.id = id;
    }
    
    public static BlockTransmutationBuilder builder(final ForgeRegistryEntry<?> nameProvider) {
        return new BlockTransmutationBuilder(AstralSorcery.key(nameProvider.getRegistryName().addTransientModifier()));
    }
    
    public static BlockTransmutationBuilder builder(final ResourceLocation id) {
        return new BlockTransmutationBuilder(id);
    }
    
    public BlockTransmutationBuilder multiplyStarlightCost(final float multiply) {
        this.starlight *= multiply;
        return this;
    }
    
    public BlockTransmutationBuilder setStarlightCost(final double starlight) {
        this.starlight = starlight;
        return this;
    }
    
    public BlockTransmutationBuilder setConstellation(final IWeakConstellation constellation) {
        this.constellation = constellation;
        return this;
    }
    
    public BlockTransmutationBuilder addInputCheck(final Block matchBlock) {
        return this.addInputCheck(matchBlock.defaultBlockState());
    }
    
    public BlockTransmutationBuilder addInputCheck(final BlockState matchState) {
        return this.addInputCheck(matchState, false);
    }
    
    public BlockTransmutationBuilder addInputCheck(final ITag<Block> matchTag, final ItemStack display) {
        this.stateCheck.add(new BlockMatchInformation(matchTag, display));
        return this;
    }
    
    public BlockTransmutationBuilder addInputCheck(final BlockState matchState, final boolean matchExact) {
        return this.addInputCheck(matchState, new ItemStack((ItemLike)matchState.getBlock()), matchExact);
    }
    
    public BlockTransmutationBuilder addInputCheck(final BlockState matchState, final ItemLike display, final boolean matchExact) {
        return this.addInputCheck(matchState, new ItemStack(display), matchExact);
    }
    
    public BlockTransmutationBuilder addInputCheck(final BlockState matchState, final ItemStack display, final boolean matchExact) {
        this.stateCheck.add(new BlockMatchInformation(matchState, display, matchExact));
        return this;
    }
    
    public BlockTransmutationBuilder setOutput(final Block output) {
        return this.setOutput(output.defaultBlockState());
    }
    
    public BlockTransmutationBuilder setOutput(final BlockState outputState) {
        this.outputState = outputState;
        this.outputDisplay = new ItemStack((ItemLike)outputState.getBlock());
        return this;
    }
    
    public BlockTransmutationBuilder setOutputDisplay(final ItemLike item) {
        return this.setOutputDisplay(new ItemStack(item));
    }
    
    public BlockTransmutationBuilder setOutputDisplay(final ItemStack stack) {
        this.outputDisplay = stack.copy();
        return this;
    }
    
    @Nonnull
    @Override
    protected BlockTransmutation validateAndGet() {
        if (this.stateCheck.isEmpty()) {
            throw new IllegalArgumentException("No block input checks!");
        }
        for (final BlockMatchInformation inputMatch : this.stateCheck) {
            if (!inputMatch.isValid()) {
                throw new IllegalArgumentException("A block transmutation must not convert 'air' into something!");
            }
        }
        final BlockTransmutation tr = new BlockTransmutation(this.id, this.outputState, this.starlight, this.constellation);
        this.stateCheck.forEach(tr::addInputOption);
        tr.setOutputDisplay(this.outputDisplay);
        return tr;
    }
    
    @Override
    protected CustomRecipeSerializer<BlockTransmutation> getSerializer() {
        return RecipeSerializersAS.BLOCK_TRANSMUTATION_SERIALIZER;
    }
}
