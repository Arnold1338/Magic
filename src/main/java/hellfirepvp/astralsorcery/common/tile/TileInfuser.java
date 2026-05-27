package hellfirepvp.astralsorcery.common.tile;

import com.google.common.collect.ImmutableSet;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MapStream;
import net.minecraft.world.level.material.Fluid;
import java.util.Map;
import javax.annotation.Nonnull;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusionContext;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeHooks;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingAtlasParticle;
import net.minecraft.world.level.Level;
import net.minecraft.core.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInfusion;
import net.minecraft.world.item.crafting.RecipeType;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.sound.CategorizedSoundEvent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import hellfirepvp.astralsorcery.client.util.sound.PositionedLoopSound;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.sounds.SoundSource;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.astralsorcery.common.tile.base.TileEntitySynchronized;
import net.minecraft.core.Direction;
import java.util.HashSet;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.crafting.recipe.infusion.ActiveLiquidInfusionRecipe;
import hellfirepvp.astralsorcery.common.util.tile.TileInventory;
import net.minecraft.core.BlockPos;
import java.util.Set;
import hellfirepvp.astralsorcery.common.item.wand.WandInteractable;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileInfuser extends TileEntityTick implements WandInteractable
{
    private static final Set<BlockPos> LIQUID_OFFSETS;
    private TileInventory inventory;
    private ActiveLiquidInfusionRecipe activeRecipe;
    private Set<ResourceLocation> knownRecipes;
    private Object clientCraftSound;
    
    public TileInfuser() {
        super(TileEntityTypesAS.INFUSER);
        this.activeRecipe = null;
        this.knownRecipes = new HashSet<ResourceLocation>();
        this.clientCraftSound = null;
        this.inventory = new TileInventory(this, () -> 1, new Direction[0]);
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        return StructureTypesAS.PTYPE_INFUSER;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (!this.getLevel().level()) {
            this.doCraftingCycle();
        }
        else if (this.getActiveRecipe() != null) {
            this.getActiveRecipe().tickClient(this);
            this.doCraftSound();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void doCraftSound() {
        if (SoundHelper.getSoundVolume(SoundSource.BLOCKS) > 0.0f) {
            if (this.clientCraftSound == null || ((PositionedLoopSound)this.clientCraftSound).hasStoppedPlaying()) {
                final CategorizedSoundEvent sound = SoundsAS.INFUSER_CRAFT_LOOP;
                this.clientCraftSound = SoundHelper.playSoundLoopFadeInClient(sound, new Vector3(this).add(0.5, 0.5, 0.5), 1.0f, 1.0f, false, s -> this.func_145837_r() || SoundHelper.getSoundVolume(SoundSource.BLOCKS) <= 0.0f || this.getActiveRecipe() == null).setFadeInTicks(30.0f).setFadeOutTicks(20.0f);
            }
        }
        else {
            this.clientCraftSound = null;
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void finishCraftingEffects(final PktPlayEffect pkt) {
        final ResourceLocation recipeName = ByteBufUtils.readResourceLocation(pkt.getExtraData());
        final BlockPos at = ByteBufUtils.readPos(pkt.getExtraData());
        final Level world = (Level)Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        final TileInfuser thisInfuser = MiscUtils.getTileAt((IBlockReader)world, at, TileInfuser.class, false);
        if (thisInfuser != null) {
            final Recipe<?> recipe = (Recipe<?>)world.func_199532_z().getRecipeFor((RecipeType)RecipeTypesAS.TYPE_INFUSION.getType()).get(recipeName);
            if (recipe instanceof LiquidInfusion) {
                final FluidStack stack = new FluidStack(((LiquidInfusion)recipe).getLiquidInput(), 1000);
                final Vector3 pos = new Vector3((Vector3i)at).add(0.5, 1.0, 0.5);
                for (int i = 0; i < 30; ++i) {
                    playLiquidFinish(pos, stack);
                }
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private static void playLiquidFinish(final Vector3 at, final FluidStack stack) {
        final Vector3 motion = new Vector3();
        MiscUtils.applyRandomOffset(motion, TileInfuser.rand, 0.05f);
        EffectHelper.of(EffectTemplatesAS.GENERIC_ATLAS_PARTICLE).spawn(at).setSprite(RenderingUtils.getParticleTexture(stack)).selectFraction(0.2f).setScaleMultiplier(0.02f + TileInfuser.rand.nextFloat() * 0.05f).setMotion(motion).color((fx, pTicks) -> new Color(ColorUtils.getOverlayColor(stack))).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(40);
    }
    
    private void doCraftingCycle() {
        if (this.activeRecipe == null) {
            return;
        }
        if (!this.hasMultiblock() || !this.activeRecipe.matches(this)) {
            this.abortCrafting();
            return;
        }
        if (this.activeRecipe.isFinished()) {
            this.finishRecipe();
            return;
        }
        this.activeRecipe.tick();
        this.markForUpdate();
    }
    
    private void finishRecipe() {
        final ResourceLocation recipeName = this.activeRecipe.getRecipeToCraft().func_199560_c();
        ForgeHooks.setCraftingPlayer(this.activeRecipe.tryGetCraftingPlayerServer());
        this.activeRecipe.createItemOutputs(this, this::dropItemOnTop);
        this.activeRecipe.consumeInputs(this);
        this.activeRecipe.consumeFluidsInput(this);
        ForgeHooks.setCraftingPlayer((Player)null);
        this.abortCrafting();
        SoundHelper.playSoundAround(SoundsAS.INFUSER_CRAFT_FINISH, this.getLevel(), (Vector3i)this.getBlockState(), 1.0f, 1.0f);
        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.INFUSER_RECIPE_FINISH).addData(buf -> {
            ByteBufUtils.writeResourceLocation(buf, recipeName);
            ByteBufUtils.writePos(buf, this.getBlockState());
            return;
        });
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(this.getLevel(), (Vector3i)this.getBlockState(), 32.0));
        EntityFlare.spawnAmbientFlare(this.getLevel(), this.getBlockState().offset(-3 + TileInfuser.rand.nextInt(7), 1 + TileInfuser.rand.nextInt(3), -3 + TileInfuser.rand.nextInt(7)));
        this.knownRecipes.add(recipeName);
    }
    
    private void abortCrafting() {
        this.activeRecipe.clearEffects();
        this.activeRecipe = null;
        this.markForUpdate();
    }
    
    protected LiquidInfusion findRecipe(final Player crafter) {
        return RecipeTypesAS.TYPE_INFUSION.findRecipe(new LiquidInfusionContext(this, crafter, LogicalSide.SERVER));
    }
    
    protected boolean startCrafting(final LiquidInfusion recipe, final Player crafter) {
        if (this.getActiveRecipe() != null) {
            return false;
        }
        this.activeRecipe = new ActiveLiquidInfusionRecipe(this.getLevel(), this.getBlockState(), recipe, crafter.getUUID());
        this.markForUpdate();
        SoundHelper.playSoundAround(SoundsAS.INFUSER_CRAFT_START, SoundSource.BLOCKS, this.level, new Vector3(this).add(0.5, 0.5, 0.5), 1.0f, 1.0f);
        return true;
    }
    
    @Override
    public boolean onInteract(final Level world, final BlockPos pos, final Player player, final Direction side, final boolean sneak) {
        if (!world.level().isClientSide() && this.hasMultiblock() && !this.getItemInput().isEmpty()) {
            if (this.getActiveRecipe() != null) {
                if (this.getActiveRecipe().matches(this)) {
                    return true;
                }
                this.abortCrafting();
            }
            final LiquidInfusion recipe = this.findRecipe(player);
            if (recipe != null) {
                this.startCrafting(recipe, player);
            }
            return true;
        }
        return false;
    }
    
    @Nonnull
    public ItemStack getItemInput() {
        return this.inventory.getStackInSlot(0);
    }
    
    @Nonnull
    public ItemStack setItemInput(@Nonnull final ItemStack stack) {
        final ItemStack prev = this.getItemInput();
        this.inventory.setStackInSlot(0, stack);
        return prev;
    }
    
    @Nullable
    public ActiveLiquidInfusionRecipe getActiveRecipe() {
        return this.activeRecipe;
    }
    
    @Nonnull
    public static Set<BlockPos> getLiquidOffsets() {
        return TileInfuser.LIQUID_OFFSETS;
    }
    
    @Nonnull
    public Map<BlockPos, Fluid> getLiquids() {
        return MapStream.ofKeys((Collection<BlockPos>)getLiquidOffsets(), pos -> this.getLevel().func_204610_c(this.getBlockState().func_177971_a((Vector3i)pos)).func_206886_c()).toMap();
    }
    
    @Nonnull
    public Vector3 getRandomInfuserOffset() {
        final Vector3 vec = new Vector3(this).add(0.0, 0.8, 0.0);
        switch (TileInfuser.rand.nextInt(4)) {
            case 3: {
                vec.add(0.5, 0.0, 0.875);
                break;
            }
            case 2: {
                vec.add(0.5, 0.0, 0.125);
                break;
            }
            case 1: {
                vec.add(0.125, 0.0, 0.5);
                break;
            }
            case 0: {
                vec.add(0.875, 0.0, 0.5);
                break;
            }
        }
        return vec;
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.inventory = this.inventory.deserialize(compound.func_74775_l("inventory"));
        this.knownRecipes = NBTHelper.readSet(compound, "knownRecipes", 8, nbt -> new ResourceLocation(nbt.func_150285_a_()));
        if (compound.func_150297_b("activeRecipe", 10)) {
            this.activeRecipe = ActiveLiquidInfusionRecipe.deserialize(compound.func_74775_l("activeRecipe"), this.activeRecipe);
        }
        else {
            if (this.activeRecipe != null) {
                this.activeRecipe.clearEffects();
            }
            this.activeRecipe = null;
        }
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.put("inventory", (Tag)this.inventory.serialize());
        NBTHelper.writeList(compound, "knownRecipes", (Collection<ResourceLocation>)this.knownRecipes, key -> StringTag.valueOf(key.toString()));
        if (this.activeRecipe != null) {
            compound.put("activeRecipe", (Tag)this.activeRecipe.serialize());
        }
    }
    
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull final Capability<T> cap, @Nullable final Direction side) {
        if (this.inventory.hasCapability(cap, side)) {
            return (LazyOptional<T>)this.inventory.getCapability().cast();
        }
        return (LazyOptional<T>)super.getCapability((Capability)cap, side);
    }
    
    static {
        LIQUID_OFFSETS = (Set)ImmutableSet.of((Object)new BlockPos(1, -1, 2), (Object)new BlockPos(0, -1, 2), (Object)new BlockPos(-1, -1, 2), (Object)new BlockPos(1, -1, -2), (Object)new BlockPos(0, -1, -2), (Object)new BlockPos(-1, -1, -2), (Object[])new BlockPos[] { new BlockPos(2, -1, 1), new BlockPos(2, -1, 0), new BlockPos(2, -1, -1), new BlockPos(-2, -1, 1), new BlockPos(-2, -1, 0), new BlockPos(-2, -1, -1) });
    }
}
