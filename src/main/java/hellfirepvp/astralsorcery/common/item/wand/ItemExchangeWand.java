package hellfirepvp.astralsorcery.common.item.wand;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import javax.annotation.Nonnull;
import java.util.Random;
import com.google.common.collect.Iterables;
import java.util.Collections;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import hellfirepvp.astralsorcery.common.data.config.entry.WandsConfig;
import net.minecraft.world.level.BlockGetter;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import com.google.common.collect.Maps;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import net.minecraft.core.Direction;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import java.util.function.Function;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.util.MapStream;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.RenderingOverlayUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.util.Blending;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.phys.BlockHitResult;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.ClipContext;
import com.google.common.collect.Sets;
import java.util.Set;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.AlignmentChargeConsumer;
import hellfirepvp.astralsorcery.common.item.base.client.ItemHeldRender;
import hellfirepvp.astralsorcery.common.item.base.client.ItemOverlayRender;
import hellfirepvp.astralsorcery.common.item.base.ItemBlockStorage;
import net.minecraft.world.item.Item;

public class ItemExchangeWand extends Item implements ItemBlockStorage, ItemOverlayRender, ItemHeldRender, AlignmentChargeConsumer
{
    private static final float COST_PER_EXCHANGE = 5.0f;
    
    public ItemExchangeWand() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        tooltip.add(getSizeMode(stack).getDisplay().withStyle(ChatFormatting.GOLD));

    }
    
    public float func_150893_a(final ItemStack stack, final BlockState state) {
        return 0.0f;
    }
    
    public int getHarvestLevel(final ItemStack stack, final ToolType tool, @Nullable final Player player, @Nullable final BlockState blockState) {
        return 3;
    }
    
    public Set<ToolType> getToolTypes(final ItemStack stack) {
        return Sets.newHashSet((Object[])new ToolType[] { ToolType.PICKAXE, ToolType.AXE, ToolType.SHOVEL });
    }
    
    public boolean func_150897_b(final BlockState blockIn) {
        return true;
    }
    
    public boolean canHarvestBlock(final ItemStack stack, final BlockState state) {
        return true;
    }
    
    public float getAlignmentChargeCost(final Player player, final ItemStack stack) {
        final BlockHitResult hitResult = MiscUtils.rayTraceLookBlock(player, ClipContext.BlockMode.OUTLINE, ClipContext.FluidMode.NONE);
        if (hitResult == null) {
            return 0.0f;
        }
        return this.getPlaceStates(player, player.level(), hitResult.func_216350_a(), stack).size() * 5.0f;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean renderInHand(final ItemStack stack, final PoseStack renderStack, final float pTicks) {
        final BlockHitResult hitResult = MiscUtils.rayTraceLookBlock((Player)Minecraft.getInstance().player, ClipContext.BlockMode.OUTLINE, ClipContext.FluidMode.NONE);
        if (hitResult == null) {
            return true;
        }
        final Level world = (Level)Minecraft.getInstance().level;
        final BlockPos at = hitResult.func_216350_a();
        final Map<BlockPos, BlockState> placeStates = this.getPlaceStates((Player)Minecraft.getInstance().player, world, at, stack);
        if (placeStates.isEmpty()) {
            return true;
        }
        RenderSystem.enableTexture();
        BlockAtlasTexture.getInstance().bindTexture();
        final int[] fullBright = { 15, 15 };
        final BufferDecoratorBuilder decorator = BufferDecoratorBuilder.withLightmap((skyLight, blockLight) -> fullBright);
        final Vector3 offset = RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks);
        RenderSystem.enableBlend();
        Blending.ADDITIVEDARK.apply();
        RenderSystem.disableDepthTest();
        RenderSystem.disableAlphaTest();
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX, buf -> placeStates.forEach((pos, state) -> {
            renderStack.popPose();
            renderStack.translate(pos.getX() - offset.getX() + 0.10000000149011612, pos.getY() - offset.getY() + 0.10000000149011612, pos.getZ() - offset.getZ() + 0.10000000149011612);
            renderStack.translate(0.8f, 0.8f, 0.8f);
            RenderingUtils.renderSimpleBlockModel(state, renderStack, (VertexConsumer)decorator.decorate(buf), pos, null, false);
            renderStack.popPose();
        }));
        RenderSystem.enableAlphaTest();
        RenderSystem.enableDepthTest();
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean renderOverlay(final PoseStack renderStack, final ItemStack stack, final float pTicks) {
        final List<Tuple<ItemStack, Integer>> foundStacks = ItemBlockStorage.getInventoryMatchingItemStacks((Player)Minecraft.getInstance().player, stack);
        RenderingOverlayUtils.renderDefaultItemDisplay(renderStack, foundStacks);
        return true;
    }
    
    public InteractionResult func_195939_a(final ItemUseContext context) {
        final Level world = context.func_195991_k();
        final ItemStack stack = context.func_195996_i();
        final Player player = context.func_195999_j();
        final BlockPos pos = context.func_195995_a();
        if (world.level() || !(player instanceof ServerPlayer) || stack.isEmpty()) {
            return InteractionResult.SUCCESS;
        }
        if (player.isCrouching()) {
            ItemBlockStorage.storeBlockState(stack, world, pos);
            return InteractionResult.SUCCESS;
        }
        final Map<BlockPos, BlockState> placeStates = this.getPlaceStates(player, world, pos, stack);
        final Map<BlockState, Tuple<ItemStack, Integer>> availableStacks = MapStream.of(ItemBlockStorage.getInventoryMatching(player, stack)).filter(tpl -> placeStates.containsValue(tpl.getA())).collect(Collectors.toMap((Function<? super net.minecraft.util.Tuple<BlockState, Tuple<ItemStack, Integer>>, ? extends BlockState>)Tuple::func_76341_a, (Function<? super net.minecraft.util.Tuple<BlockState, Tuple<ItemStack, Integer>>, ? extends Tuple<ItemStack, Integer>>)Tuple::func_76340_b));
        for (final BlockPos placePos : placeStates.keySet()) {
            final BlockState stateToPlace = placeStates.get(placePos);
            final Tuple<ItemStack, Integer> availableStack = availableStacks.get(stateToPlace);
            if (availableStack == null) {
                continue;
            }
            final ItemStack extractable = ItemUtils.copyStackWithSize((ItemStack)availableStack.getA(), 1);
            boolean canExtract = player.getVehicle();
            if (!canExtract && ItemUtils.consumeFromPlayerInventory(player, stack, extractable, true)) {
                canExtract = true;
            }
            if (!canExtract) {
                continue;
            }
            final BlockState prevState = world.getBlockState(placePos);
            if ((!player.getVehicle() && !ItemUtils.consumeFromPlayerInventory(player, stack, extractable, true)) || !AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, 5.0f, false) || !((ServerPlayer)player).field_71134_c.func_180237_b(placePos) || !MiscUtils.canPlayerPlaceBlockPos(player, stateToPlace, placePos, Direction.UP) || (!player.getVehicle() && !ItemUtils.consumeFromPlayerInventory(player, stack, extractable, false)) || !world.func_175656_a(placePos, stateToPlace)) {
                continue;
            }
            final PktPlayEffect ev = new PktPlayEffect(PktPlayEffect.Type.BLOCK_EFFECT).addData(buf -> {
                ByteBufUtils.writePos(buf, placePos);
                ByteBufUtils.writeBlockState(buf, prevState);
                return;
            });
            PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(world, (Vector3i)placePos, 32.0));
        }
        return InteractionResult.SUCCESS;
    }
    
    public InteractionResult<ItemStack> use(final Level worldIn, final Player playerIn, final Hand handIn) {
        final ItemStack held = playerIn.getItemInHand(handIn);
        if (playerIn.isCrouching()) {
            final SizeMode nextMode = getSizeMode(held).next();
            setSizeMode(held, nextMode);
            playerIn.func_146105_b((Component)nextMode.getDisplay(), true);
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
    }
    
    @Nonnull
    private Map<BlockPos, BlockState> getPlaceStates(final Player placer, final Level world, final BlockPos origin, final ItemStack refStack) {
        final Map<BlockState, Tuple<ItemStack, Integer>> tplStates = ItemBlockStorage.getInventoryMatching(placer, refStack);
        final BlockState atState = world.getBlockState(origin);
        final SizeMode mode = getSizeMode(refStack);
        final Map<BlockPos, BlockState> placeables = Maps.newHashMap();
        final BlockState match = BlockUtils.getMatchingState(tplStates.keySet(), atState);
        if (match != null && tplStates.size() <= 1) {
            return placeables;
        }
        final float hardness = atState.func_185887_b((IBlockReader)world, origin);
        final int cfgHardness = (int)WandsConfig.CONFIG.exchangeWandMaxHardness.get();
        if (hardness == -1.0f || (cfgHardness != -1 && hardness > cfgHardness)) {
            return placeables;
        }
        int totalItems = 0;
        if (placer.getVehicle()) {
            totalItems = Integer.MAX_VALUE;
        }
        else {
            for (final Tuple<ItemStack, Integer> amountTpl : tplStates.values()) {
                totalItems += (int)(((int)amountTpl.getB() == -1) ? 500000 : amountTpl.getB());
            }
        }
        final List<BlockPos> foundPositions = BlockDiscoverer.discoverBlocksWithSameStateAround(world, origin, true, mode.getSearchRadius(), totalItems, false);
        if (foundPositions.isEmpty()) {
            return placeables;
        }
        final Map<BlockState, Integer> placeAmounts = Maps.newHashMap();
        for (final BlockState state : tplStates.keySet()) {
            placeAmounts.put(state, placer.getVehicle() ? Integer.valueOf(Integer.MAX_VALUE) : ((Integer)tplStates.get(state).getB()));
        }
        final List<BlockState> placeableStates = Lists.newArrayList((Iterable)placeAmounts.keySet());
        final Random rand = ItemBlockStorage.getPreviewRandomFromWorld(world);
        for (final BlockPos pos : foundPositions) {
            Collections.shuffle(placeableStates, rand);
            final BlockState toPlace = (BlockState)Iterables.getFirst((Iterable)placeableStates, (Object)null);
            if (toPlace == null) {
                continue;
            }
            if (!placer.getVehicle()) {
                int count = placeAmounts.get(toPlace);
                if (--count <= 0) {
                    placeAmounts.remove(toPlace);
                    placeableStates.remove(toPlace);
                }
                else {
                    placeAmounts.put(toPlace, count);
                }
            }
            placeables.put(pos, toPlace);
        }
        return placeables;
    }
    
    public static void setSizeMode(@Nonnull final ItemStack stack, @Nonnull final SizeMode mode) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemExchangeWand)) {
            return;
        }
        final CompoundTag nbt = NBTHelper.getPersistentData(stack);
        nbt.putInt("sizeMode", mode.ordinal());
    }
    
    @Nonnull
    public static SizeMode getSizeMode(@Nonnull final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemExchangeWand)) {
            return SizeMode.RANGE_2;
        }
        final CompoundTag nbt = NBTHelper.getPersistentData(stack);
        return MiscUtils.getEnumEntry(SizeMode.class, nbt.getInt("sizeMode"));
    }
    
    public enum SizeMode
    {
        RANGE_2(2), 
        RANGE_3(3), 
        RANGE_4(4), 
        RANGE_5(5);
        
        private final int searchRadius;
        
        private SizeMode(final int searchRadius) {
            this.searchRadius = searchRadius;
        }
        
        public int getSearchRadius() {
            return this.searchRadius;
        }
        
        public MutableComponent getName() {
            return (MutableComponent)new Component("astralsorcery.misc.exchange.size." + this.searchRadius);
        }
        
        public MutableComponent getDisplay() {
            return (MutableComponent)new Component("astralsorcery.misc.exchange.size", new Object[] { this.getName() });
        }
        
        @Nonnull
        private SizeMode next() {
            final int next = (this.ordinal() + 1) % values().length;
            return MiscUtils.getEnumEntry(SizeMode.class, next);
        }
    }
}
