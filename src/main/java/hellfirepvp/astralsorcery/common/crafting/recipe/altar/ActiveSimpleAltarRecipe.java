package hellfirepvp.astralsorcery.common.crafting.recipe.altar;

import javax.annotation.Nonnull;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import java.util.Optional;
import net.minecraft.world.item.crafting.RecipeManager;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import java.util.Set;
import net.minecraft.core.BlockPos;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import java.util.Iterator;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraft.world.item.crafting.Ingredient;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileSpectralRelay;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.templates.VoidFluidHandler;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.FluidIngredient;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.function.Function;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.LinkedList;
import java.util.HashMap;
import hellfirepvp.astralsorcery.common.crafting.helper.CraftingFocusStack;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import java.util.Map;
import java.util.Random;

public class ActiveSimpleAltarRecipe
{
    private static final Random rand;
    private Map<Integer, Object> clientEffectContainer;
    private final SimpleAltarRecipe recipeToCraft;
    private final UUID playerCraftingUUID;
    private int ticksCrafting;
    private int totalCraftingTime;
    private CraftingState state;
    private CompoundTag craftingData;
    private List<CraftingFocusStack> focusStacks;
    
    private ActiveSimpleAltarRecipe(final SimpleAltarRecipe recipeToCraft, final UUID playerCraftingUUID) {
        this(recipeToCraft, 1, playerCraftingUUID);
    }
    
    public ActiveSimpleAltarRecipe(final SimpleAltarRecipe recipeToCraft, final int durationDivisor, final UUID playerCraftingUUID) {
        this.clientEffectContainer = new HashMap<Integer, Object>();
        this.ticksCrafting = 0;
        this.craftingData = new CompoundTag();
        this.focusStacks = new LinkedList<CraftingFocusStack>();
        Objects.requireNonNull(recipeToCraft);
        this.recipeToCraft = recipeToCraft;
        this.playerCraftingUUID = playerCraftingUUID;
        this.state = CraftingState.ACTIVE;
        this.totalCraftingTime = recipeToCraft.getDuration() / durationDivisor;
    }
    
    private void recoverContainedEffects(@Nullable final ActiveSimpleAltarRecipe previous) {
        if (previous != null && previous.getRecipeToCraft().func_199560_c().equals((Object)this.recipeToCraft.func_199560_c())) {
            this.clientEffectContainer.putAll(previous.clientEffectContainer);
        }
    }
    
    public CompoundTag getCraftingData() {
        return this.craftingData;
    }
    
    public UUID getPlayerCraftingUUID() {
        return this.playerCraftingUUID;
    }
    
    public void setState(final CraftingState state) {
        this.state = state;
    }
    
    public CraftingState getState() {
        return this.state;
    }
    
    public List<CraftingFocusStack> getFocusStacks() {
        return this.focusStacks;
    }
    
    @Nullable
    public Player tryGetCraftingPlayerServer() {
        final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        return (Player)srv.getPlayerList().getPlayer(this.getPlayerCraftingUUID());
    }
    
    @OnlyIn(Dist.CLIENT)
    public <T> T getEffectContained(final int index, final Function<Integer, T> provider) {
        return (T)this.clientEffectContainer.computeIfAbsent(index, provider);
    }
    
    public void createItemOutputs(final TileAltar altar, final Consumer<ItemStack> output) {
        final Consumer<ItemStack> informer = stack -> ResearchManager.informCraftedAltar(altar, this, stack);
        final Consumer<ItemStack> handleCrafted = informer.andThen(output);
        this.getRecipeToCraft().getOutputs(altar).forEach(handleCrafted);
        this.getRecipeToCraft().onRecipeCompletion(altar, this);
    }
    
    public void consumeInputs(final TileAltar altar) {
        final TileInventory inv = altar.getInventory();
        final AltarRecipeGrid grid = this.recipeToCraft.getInputs();
        for (int slot = 0; slot < 25; ++slot) {
            final Ingredient input = grid.getIngredient(slot);
            if (input instanceof FluidIngredient) {
                final ItemStack stack = inv.getStackInSlot(slot);
                final FluidActionResult far = FluidUtil.tryEmptyContainer(stack, (IFluidHandler)VoidFluidHandler.INSTANCE, 1000, (Player)null, true);
                if (far.isSuccess()) {
                    inv.setStackInSlot(slot, far.getResult());
                }
            }
            else {
                ItemUtils.decrementItem(inv, slot, altar::dropItemOnTop);
            }
        }
        for (final CraftingFocusStack input2 : this.focusStacks) {
            final TileSpectralRelay tar = MiscUtils.getTileAt((IBlockReader)altar.getLevel(), input2.getRealPosition(), TileSpectralRelay.class, true);
            if (tar != null) {
                final TileInventory tarInventory = tar.getInventory();
                if (input2.getInput() != null && input2.getInput().getIngredient() instanceof FluidIngredient) {
                    final ItemStack stack2 = tarInventory.getStackInSlot(0);
                    final FluidActionResult far2 = FluidUtil.tryEmptyContainer(stack2, (IFluidHandler)VoidFluidHandler.INSTANCE, 1000, (Player)null, true);
                    if (!far2.isSuccess()) {

                    }
                    tarInventory.setStackInSlot(0, far2.getResult());
                }
                else {
                    ItemUtils.decrementItem(tarInventory, 0, altar::dropItemOnTop);
                }
            }
        }
    }
    
    public boolean matches(final TileAltar altar, final boolean ignoreStarlightRequirement, final boolean testNecessaryRelayInputs) {
        if (!this.getRecipeToCraft().matches(LogicalSide.SERVER, this.tryGetCraftingPlayerServer(), altar, ignoreStarlightRequirement)) {
            return false;
        }
        final List<WrappedIngredient> listIngredients = this.getRecipeToCraft().getRelayInputs();
        for (final CraftingFocusStack stack : this.focusStacks) {
            if (stack.getStackIndex() < 0 || stack.getStackIndex() >= listIngredients.size()) {
                return false;
            }
            final TileSpectralRelay relay = MiscUtils.getTileAt((IBlockReader)altar.getLevel(), stack.getRealPosition(), TileSpectralRelay.class, true);
            if (relay == null) {
                return false;
            }
            if (!testNecessaryRelayInputs) {

            }
            final WrappedIngredient ingredient = listIngredients.get(stack.getStackIndex());
            if (!ingredient.getIngredient().test(relay.getInventory().getStackInSlot(0))) {
                return false;
            }
        }
        return true;
    }
    
    public CraftingState tick(final TileAltar altar) {
        if (this.recipeToCraft instanceof AltarCraftingProgress && !((AltarCraftingProgress)this.recipeToCraft).tryProcess(altar, this, this.craftingData, this.ticksCrafting, this.totalCraftingTime)) {
            return CraftingState.WAITING;
        }
        final List<WrappedIngredient> iIngredients = this.getRecipeToCraft().getRelayInputs();
        if (!iIngredients.isEmpty()) {
            final boolean shouldWait = this.tickCraftingRelayInputs(altar, iIngredients);
            if (shouldWait) {
                return CraftingState.WAITING;
            }
        }
        ++this.ticksCrafting;
        return CraftingState.ACTIVE;
    }
    
    private boolean tickCraftingRelayInputs(final TileAltar altar, final List<WrappedIngredient> iIngredients) {
        final int part = this.totalCraftingTime / 3;
        final int cttPart = part / iIngredients.size() + 1;
        boolean waitMissingInputs = false;
        for (int i = 0; i < iIngredients.size(); ++i) {
            final WrappedIngredient input = iIngredients.get(i);
            final int index = i;
            final int offset = part + index * cttPart;
            if (this.ticksCrafting >= offset) {
                CraftingFocusStack found = MiscUtils.iterativeSearch(this.focusStacks, fStack -> fStack.getStackIndex() == index);
                if (found == null) {
                    final Set<BlockPos> relays = altar.nearbyRelays();
                    relays.removeIf(pos -> MiscUtils.contains(this.focusStacks, fs -> fs.getRealPosition().equals((Object)pos)));
                    if (relays.isEmpty()) {
                        waitMissingInputs = true;

                    }
                    final BlockPos at = MiscUtils.getRandomEntry(relays, ActiveSimpleAltarRecipe.rand);
                    final TileSpectralRelay tar = MiscUtils.getTileAt((IBlockReader)altar.getLevel(), at, TileSpectralRelay.class, true);
                    if (tar == null) {
                        waitMissingInputs = true;

                    }
                    found = new CraftingFocusStack(index, input, at);
                    this.focusStacks.add(found);
                }
                final TileSpectralRelay tar2 = MiscUtils.getTileAt((IBlockReader)altar.getLevel(), found.getRealPosition(), TileSpectralRelay.class, true);
                if (tar2 == null) {
                    waitMissingInputs = true;
                }
                else if (!input.getIngredient().test(tar2.getInventory().getStackInSlot(0))) {
                    waitMissingInputs = true;
                }
            }
        }
        return waitMissingInputs;
    }
    
    public int getTicksCrafting() {
        return this.ticksCrafting;
    }
    
    public int getTotalCraftingTime() {
        return this.totalCraftingTime;
    }
    
    public SimpleAltarRecipe getRecipeToCraft() {
        return this.recipeToCraft;
    }
    
    public boolean isFinished() {
        return this.ticksCrafting >= this.totalCraftingTime;
    }
    
    @Nullable
    public static ActiveSimpleAltarRecipe deserialize(final CompoundTag compound, @Nullable final ActiveSimpleAltarRecipe previous) {
        final RecipeManager mgr = RecipeHelper.getRecipeManager();
        if (mgr == null) {
            return null;
        }
        final ResourceLocation recipeKey = new ResourceLocation(compound.getString("recipeToCraft"));
        final Optional<?> recipe = mgr.func_215367_a(recipeKey);
        if (!recipe.isPresent() || !(recipe.get() instanceof SimpleAltarRecipe)) {
            AstralSorcery.log.info("Recipe with unknown/invalid name found: " + recipeKey);
            return null;
        }
        final SimpleAltarRecipe altarRecipe = (SimpleAltarRecipe)recipe.get();
        final UUID uuidCraft = compound.getUUID("playerCraftingUUID");
        final int tick = compound.getInt("ticksCrafting");
        final int total = compound.getInt("totalCraftingTime");
        final CraftingState state = CraftingState.values()[compound.getInt("state")];
        final List<CraftingFocusStack> stacks = new LinkedList<CraftingFocusStack>();
        final ListTag listStacks = compound.getList("focusStacks", 10);
        for (int i = 0; i < listStacks.size(); ++i) {
            stacks.add(new CraftingFocusStack(listStacks.getCompound(i)));
        }
        final ActiveSimpleAltarRecipe task = new ActiveSimpleAltarRecipe(altarRecipe, uuidCraft);
        task.ticksCrafting = tick;
        task.totalCraftingTime = total;
        task.setState(state);
        task.craftingData = compound.func_74775_l("craftingData");
        task.focusStacks = stacks;
        task.recoverContainedEffects(previous);
        return task;
    }
    
    @Nonnull
    public CompoundTag serialize() {
        final CompoundTag compound = new CompoundTag();
        compound.putString("recipeToCraft", this.getRecipeToCraft().func_199560_c().toString());
        compound.putUUID("playerCraftingUUID", this.getPlayerCraftingUUID());
        compound.putInt("ticksCrafting", this.getTicksCrafting());
        compound.putInt("totalCraftingTime", this.getTotalCraftingTime());
        compound.putInt("state", this.getState().ordinal());
        compound.put("craftingData", (Tag)this.craftingData);
        final ListTag list = new ListTag();
        for (final CraftingFocusStack stack : this.focusStacks) {
            list.add((Object)stack.serialize());
        }
        compound.put("focusStacks", (Tag)list);
        return compound;
    }
    
    static {
        rand = new Random();
    }
    
    public enum CraftingState
    {
        ACTIVE, 

    }
}
