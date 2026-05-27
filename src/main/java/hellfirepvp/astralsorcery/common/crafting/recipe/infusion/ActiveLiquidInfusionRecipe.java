package hellfirepvp.astralsorcery.common.crafting.recipe.infusion;

import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import javax.annotation.Nonnull;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.crafting.RecipeManager;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import javax.annotation.Nullable;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.capability.IFluidHandler;
import hellfirepvp.astralsorcery.common.tile.TileChalice;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.world.item.ItemStack;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.client.util.ColorizationHelper;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.client.effect.function.VFXMotionController;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingAtlasParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.source.orbital.FXOrbitalInfuserLiquid;
import net.minecraftforge.fluids.FluidStack;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;
import hellfirepvp.astralsorcery.common.auxiliary.ChaliceHelper;
import java.util.HashSet;
import net.minecraft.world.level.Level;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.BlockPos;
import java.util.Set;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import java.util.Random;

public class ActiveLiquidInfusionRecipe
{
    private static final Random rand;
    private static final int CHALICE_DISTANCE = 8;
    private final LiquidInfusion recipeToCraft;
    private final UUID playerCraftingUUID;
    private int ticksCrafting;
    private final Set<BlockPos> supportingChalices;
    private CompoundTag craftingData;
    private Object orbitalLiquid;
    
    public ActiveLiquidInfusionRecipe(final Level world, final BlockPos center, final LiquidInfusion recipeToCraft, final UUID playerCraftingUUID) {
        this(recipeToCraft, playerCraftingUUID);
        if (this.recipeToCraft.acceptsChaliceInput()) {
            this.findChalices(world, center);
        }
    }
    
    private ActiveLiquidInfusionRecipe(final LiquidInfusion recipeToCraft, final UUID playerCraftingUUID) {
        this.ticksCrafting = 0;
        this.supportingChalices = new HashSet<BlockPos>();
        this.craftingData = new CompoundTag();
        this.orbitalLiquid = null;
        this.recipeToCraft = recipeToCraft;
        this.playerCraftingUUID = playerCraftingUUID;
    }
    
    private void findChalices(final Level world, final BlockPos center) {
        ChaliceHelper.findNearbyChalicesCombined(world, center, this.getChaliceRequiredFluidInput(), 8).ifPresent(chalices -> chalices.forEach(chalice -> this.supportingChalices.add(chalice.getBlockState())));
    }
    
    public boolean matches(final TileInfuser infuser) {
        if (!this.getRecipeToCraft().matches(infuser, this.tryGetCraftingPlayerServer(), LogicalSide.SERVER)) {
            return false;
        }
        if (!this.supportingChalices.isEmpty() && !ChaliceHelper.doChalicesContainCombined(infuser.getLevel(), this.supportingChalices, this.getChaliceRequiredFluidInput())) {
            this.supportingChalices.clear();
        }
        return true;
    }
    
    public void tick() {
        ++this.ticksCrafting;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void tickClient(final TileInfuser infuser) {
        final FluidStack required = new FluidStack(this.getRecipeToCraft().getLiquidInput(), 1000);
        if (this.orbitalLiquid == null || ((FXOrbitalInfuserLiquid)this.orbitalLiquid).isRemoved()) {
            final ResourceLocation recipeName = this.getRecipeToCraft().func_199560_c();
            this.orbitalLiquid = EffectHelper.spawnSource((Object)new FXOrbitalInfuserLiquid(new Vector3(infuser).add(0.5f, 0.0f, 0.5f), required).setOrbitAxis(Vector3.RotAxis.Y_AXIS).setOrbitRadius(2.0).setBranches(4).setMaxAge(300).refresh(RefreshFunction.tileExistsAnd(infuser, (tInfuser, fx) -> tInfuser.getActiveRecipe() != null && recipeName.equals((Object)tInfuser.getActiveRecipe().getRecipeToCraft().func_199560_c()))));
            ((FXOrbitalInfuserLiquid)this.orbitalLiquid).setActive();
        }
        for (int i = 0; i < 2; ++i) {
            this.playLiquidEffect(infuser, required);
        }
        for (int i = 0; i < 7; ++i) {
            this.playLiquidPoolEffect(infuser, required);
        }
        if (!this.supportingChalices.isEmpty()) {
            this.playLiquidDrawEffect(infuser, required);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playLiquidDrawEffect(final TileInfuser infuser, final FluidStack required) {
        final Collection<BlockPos> chalices = this.supportingChalices;
        if (chalices.isEmpty()) {
            return;
        }
        final Vector3 target = new Vector3(infuser).add(0.5, 1.1, 0.5);
        final TextureAtlasSprite tas = RenderingUtils.getParticleTexture(required);
        final VFXColorFunction<?> colorFn = (fx, pTicks) -> new Color(ColorUtils.getOverlayColor(required));
        for (int i = 0; i < 2 * this.supportingChalices.size(); ++i) {
            final BlockPos chalice = MiscUtils.getRandomEntry(chalices, ActiveLiquidInfusionRecipe.rand);
            final Vector3 pos = new Vector3((Vec3i)chalice).add(0.5, 1.4, 0.5);
            int maxAge = 30;
            maxAge *= (int)Math.max(pos.distance(target) / 3.0, 1.0);
            if (ActiveLiquidInfusionRecipe.rand.nextInt(3) != 0) {
                MiscUtils.applyRandomOffset(pos, ActiveLiquidInfusionRecipe.rand, 0.3f);
                EffectHelper.of(EffectTemplatesAS.GENERIC_ATLAS_PARTICLE).spawn(pos).setSprite(tas).selectFraction(0.2f).setScaleMultiplier(0.01f + ActiveLiquidInfusionRecipe.rand.nextFloat() * 0.04f).color(colorFn).alpha(VFXAlphaFunction.proximity(() -> target, 2.0f).andThen(VFXAlphaFunction.FADE_OUT)).motion(VFXMotionController.target(target::clone, 0.08f)).setMaxAge(maxAge);
            }
            else {
                MiscUtils.applyRandomOffset(pos, ActiveLiquidInfusionRecipe.rand, 0.4f);
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).setScaleMultiplier(0.15f + ActiveLiquidInfusionRecipe.rand.nextFloat() * 0.1f).color(colorFn).alpha(VFXAlphaFunction.proximity(() -> target, 2.0f).andThen(VFXAlphaFunction.FADE_OUT)).motion(VFXMotionController.target(target::clone, 0.08f)).setMaxAge(maxAge);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playLiquidEffect(final TileInfuser infuser, final FluidStack required) {
        final Vector3 vec = infuser.getRandomInfuserOffset();
        MiscUtils.applyRandomOffset(vec, ActiveLiquidInfusionRecipe.rand, 0.05f);
        EffectHelper.of(EffectTemplatesAS.GENERIC_ATLAS_PARTICLE).spawn(vec).setSprite(RenderingUtils.getParticleTexture(required)).selectFraction(0.2f).setScaleMultiplier(0.03f + ActiveLiquidInfusionRecipe.rand.nextFloat() * 0.03f).color((fx, pTicks) -> new Color(ColorUtils.getOverlayColor(required))).alpha(VFXAlphaFunction.FADE_OUT).motion(VFXMotionController.target(() -> new Vector3(infuser).add(0.5, 1.1, 0.5), 0.3f)).setMaxAge(40);
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playLiquidPoolEffect(final TileInfuser infuser, final FluidStack required) {
        final List<BlockPos> posList = TileInfuser.getLiquidOffsets().stream().map(pos -> pos.func_177971_a((Vec3i)infuser.getBlockState())).collect((Collector<? super Object, ?, List<BlockPos>>)Collectors.toList());
        final BlockPos at = MiscUtils.getRandomEntry(posList, ActiveLiquidInfusionRecipe.rand);
        if (at != null) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vec3i)at).add(ActiveLiquidInfusionRecipe.rand.nextFloat(), 1.0f, ActiveLiquidInfusionRecipe.rand.nextFloat())).setScaleMultiplier(0.1f + ActiveLiquidInfusionRecipe.rand.nextFloat() * 0.15f).color((fx, pTicks) -> ColorizationHelper.getColor(required).orElse(Color.WHITE)).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).setMotion(new Vector3(0.0, 0.15, 0.0)).setGravityStrength(0.005f + ActiveLiquidInfusionRecipe.rand.nextFloat() * 0.008f);
        }
    }
    
    public void clearEffects() {
        if (this.orbitalLiquid != null) {
            this.clearClientEffect();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void clearClientEffect() {
        ((FXOrbitalInfuserLiquid)this.orbitalLiquid).requestRemoval();
    }
    
    public void createItemOutputs(final TileInfuser infuser, final Consumer<ItemStack> output) {
        final Consumer<ItemStack> informer = stack -> ResearchManager.informCraftedInfuser(infuser, this, stack);
        final ItemStack inputStack = infuser.getItemInput();
        Consumer<ItemStack> handleCrafted = informer.andThen(output);
        if (this.recipeToCraft.doesCopyNBTToOutputs()) {
            handleCrafted = ((Consumer<ItemStack>)(stack -> stack.setTag(inputStack.getTag()))).andThen(handleCrafted);
        }
        handleCrafted.accept(this.getRecipeToCraft().getOutput(inputStack));
        this.getRecipeToCraft().onRecipeCompletion(infuser);
    }
    
    public void consumeInputs(final TileInfuser infuser) {
        ItemUtils.decrementItem(infuser::getItemInput, infuser::setItemInput, infuser::dropItemOnTop);
    }
    
    public void consumeFluidsInput(final TileInfuser infuser) {
        float chaliceSupplied = 0.0f;
        if (!this.supportingChalices.isEmpty()) {
            final FluidStack required = this.getChaliceRequiredFluidInput();
            final Optional<List<TileChalice>> chalices = ChaliceHelper.findNearbyChalicesCombined(infuser.getLevel(), infuser.getBlockState(), required, 8);
            if (chalices.isPresent()) {
                final FluidStack left = required.copy();
                for (final TileChalice chalice : chalices.get()) {
                    left.shrink(chalice.getTank().drain(left, IFluidHandler.FluidAction.EXECUTE).getAmount());
                    if (left.isEmpty()) {
                        break;
                    }
                }
                if (left.isEmpty()) {
                    return;
                }
                chaliceSupplied = required.getAmount() / (float)left.getAmount();
            }
        }
        final LiquidInfusion infusion = this.getRecipeToCraft();
        final float chance = infusion.getConsumptionChance() * (1.0f - chaliceSupplied);
        if (infusion.doesConsumeMultipleFluids()) {
            for (final BlockPos at : TileInfuser.getLiquidOffsets()) {
                if (ActiveLiquidInfusionRecipe.rand.nextFloat() < chance) {
                    infuser.getLevel().func_180501_a(at.func_177971_a((Vec3i)infuser.getBlockState()), Blocks.AIR.defaultBlockState(), 11);
                }
            }
        }
        else {
            final BlockPos at2 = MiscUtils.getRandomEntry(TileInfuser.getLiquidOffsets(), ActiveLiquidInfusionRecipe.rand).func_177971_a((Vec3i)infuser.getBlockState());
            if (ActiveLiquidInfusionRecipe.rand.nextFloat() < chance) {
                infuser.getLevel().func_180501_a(at2, Blocks.AIR.defaultBlockState(), 11);
            }
        }
    }
    
    public FluidStack getChaliceRequiredFluidInput() {
        int amount = Math.round(1000.0f * this.recipeToCraft.getConsumptionChance());
        amount *= (int)0.75;
        amount = (this.recipeToCraft.doesConsumeMultipleFluids() ? (amount * TileInfuser.getLiquidOffsets().size()) : amount);
        return new FluidStack(this.getRecipeToCraft().getLiquidInput(), amount);
    }
    
    public int getTotalCraftingTime() {
        final int tickTime = this.recipeToCraft.getCraftingTickTime();
        final int fixTime = Math.round(tickTime * 0.25f);
        int chaliceTime = Math.round(tickTime * 0.75f);
        chaliceTime /= this.supportingChalices.size() + 1;
        return fixTime + chaliceTime;
    }
    
    public CompoundTag getCraftingData() {
        return this.craftingData;
    }
    
    public UUID getPlayerCraftingUUID() {
        return this.playerCraftingUUID;
    }
    
    public int getTicksCrafting() {
        return this.ticksCrafting;
    }
    
    public LiquidInfusion getRecipeToCraft() {
        return this.recipeToCraft;
    }
    
    public boolean isFinished() {
        return this.getTicksCrafting() >= this.getTotalCraftingTime();
    }
    
    @Nullable
    public Player tryGetCraftingPlayerServer() {
        final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        return (Player)srv.getPlayerList().getPlayer(this.getPlayerCraftingUUID());
    }
    
    @Nullable
    public static ActiveLiquidInfusionRecipe deserialize(final CompoundTag compound, @Nullable final ActiveLiquidInfusionRecipe prev) {
        final RecipeManager mgr = RecipeHelper.getRecipeManager();
        if (mgr == null) {
            return null;
        }
        final ResourceLocation recipeKey = new ResourceLocation(compound.getString("recipeToCraft"));
        final Optional<?> recipe = mgr.func_215367_a(recipeKey);
        if (!recipe.isPresent() || !(recipe.get() instanceof LiquidInfusion)) {
            AstralSorcery.log.info("Recipe with unknown/invalid name found: " + recipeKey);
            return null;
        }
        final LiquidInfusion altarRecipe = (LiquidInfusion)recipe.get();
        final UUID uuidCraft = compound.getUUID("playerCraftingUUID");
        final int tick = compound.getInt("ticksCrafting");
        final ListTag chalices = compound.getList("supportingChalices", 10);
        final Set<BlockPos> chalicePositions = new HashSet<BlockPos>();
        for (int i = 0; i < chalices.size(); ++i) {
            final CompoundTag tag = chalices.getCompound(i);
            chalicePositions.add(NBTHelper.readBlockPosFromNBT(tag));
        }
        final ActiveLiquidInfusionRecipe task = new ActiveLiquidInfusionRecipe(altarRecipe, uuidCraft);
        task.ticksCrafting = tick;
        task.craftingData = compound.func_74775_l("craftingData");
        task.supportingChalices.addAll(chalicePositions);
        if (prev != null && prev.orbitalLiquid != null) {
            task.orbitalLiquid = prev.orbitalLiquid;
        }
        return task;
    }
    
    @Nonnull
    public CompoundTag serialize() {
        final ListTag chalicePositions = new ListTag();
        this.supportingChalices.forEach(pos -> chalicePositions.add((Object)NBTHelper.writeBlockPosToNBT(pos, new CompoundTag())));
        final CompoundTag compound = new CompoundTag();
        compound.putString("recipeToCraft", this.getRecipeToCraft().func_199560_c().toString());
        compound.putUUID("playerCraftingUUID", this.getPlayerCraftingUUID());
        compound.putInt("ticksCrafting", this.getTicksCrafting());
        compound.put("craftingData", (Tag)this.craftingData);
        compound.put("supportingChalices", (Tag)chalicePositions);
        return compound;
    }
    
    static {
        rand = new Random();
    }
}
