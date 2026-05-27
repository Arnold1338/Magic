package hellfirepvp.astralsorcery.common.item.wand;

import hellfirepvp.astralsorcery.common.util.block.BlockGeometry;
import net.minecraft.world.level.phys.HitResult;
import hellfirepvp.astralsorcery.common.util.RaytraceAssist;
import java.util.ArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import java.util.Random;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import com.google.common.collect.Iterables;
import java.util.Collections;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import javax.annotation.Nonnull;
import net.minecraft.world.level.phys.BlockHitResult;
import java.util.HashMap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import java.util.Iterator;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.core.Direction;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import java.util.function.Function;
import java.util.stream.Collectors;
import hellfirepvp.astralsorcery.common.util.MapStream;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.RenderingOverlayUtils;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.Map;
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
import net.minecraft.world.entity.player.Player;
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

public class ItemArchitectWand extends Item implements ItemBlockStorage, ItemOverlayRender, ItemHeldRender, AlignmentChargeConsumer
{
    private static final float COST_PER_PLACEMENT = 8.0f;
    
    public ItemArchitectWand() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        tooltip.add(getPlaceMode(stack).getDisplay().withStyle(ChatFormatting.GOLD));

    }
    
    public float getAlignmentChargeCost(final Player player, final ItemStack stack) {
        final PlaceMode mode = getPlaceMode(stack);
        return this.getPlayerPlaceableStates(player, stack).size() * 8.0f * mode.getPlaceCostMulitplier();
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean renderInHand(final ItemStack stack, final PoseStack renderStack, final float pTicks) {
        final Player player = (Player)Minecraft.getInstance().player;
        final Map<BlockPos, BlockState> placeStates = this.getPlayerPlaceableStates(player, stack);
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
        final Player player = context.func_195999_j();
        final ItemStack held = player.getItemInHand(context.func_221531_n());
        final BlockPos pos = context.func_195995_a();
        if (world.level() || !(player instanceof ServerPlayer) || held.isEmpty()) {
            return InteractionResult.SUCCESS;
        }
        if (player.isCrouching()) {
            ItemBlockStorage.storeBlockState(held, world, pos);
            return InteractionResult.SUCCESS;
        }
        return this.attemptPlaceBlocks(world, player, held).func_188397_a();
    }
    
    public InteractionResult<ItemStack> use(final Level world, final Player player, final Hand hand) {
        final ItemStack held = player.getItemInHand(hand);
        final PlaceMode mode = getPlaceMode(held);
        if (player.isCrouching()) {
            final PlaceMode nextMode = mode.next();
            setPlaceMode(held, nextMode);
            player.func_146105_b((Component)nextMode.getDisplay(), true);
            return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
        }
        if (world.level()) {
            return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
        }
        return this.attemptPlaceBlocks(world, player, held);
    }
    
    private InteractionResult<ItemStack> attemptPlaceBlocks(final Level world, final Player player, final ItemStack held) {
        final Map<BlockPos, BlockState> placeStates = this.getPlayerPlaceableStates(player, held);
        if (placeStates.isEmpty()) {
            return (InteractionResult<ItemStack>)InteractionResult.func_226251_d_((Object)held);
        }
        final Map<BlockState, Tuple<ItemStack, Integer>> availableStacks = MapStream.of(ItemBlockStorage.getInventoryMatching(player, held)).filter(tpl -> placeStates.containsValue(tpl.getA())).collect(Collectors.toMap((Function<? super net.minecraft.util.Tuple<BlockState, Tuple<ItemStack, Integer>>, ? extends BlockState>)Tuple::func_76341_a, (Function<? super net.minecraft.util.Tuple<BlockState, Tuple<ItemStack, Integer>>, ? extends Tuple<ItemStack, Integer>>)Tuple::func_76340_b));
        for (final BlockPos placePos : placeStates.keySet()) {
            final BlockState stateToPlace = placeStates.get(placePos);
            final Tuple<ItemStack, Integer> availableStack = availableStacks.get(stateToPlace);
            if (availableStack == null) {
                continue;
            }
            final ItemStack extractable = ItemUtils.copyStackWithSize((ItemStack)availableStack.getA(), 1);
            boolean canExtract = player.getVehicle();
            if (!canExtract && ItemUtils.consumeFromPlayerInventory(player, held, extractable, true)) {
                canExtract = true;
            }
            if (!canExtract) {
                continue;
            }
            if (!AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, 8.0f, true) || (!player.getVehicle() && !ItemUtils.consumeFromPlayerInventory(player, held, extractable, true)) || !MiscUtils.canPlayerPlaceBlockPos(player, stateToPlace, placePos, Direction.UP) || (!player.getVehicle() && !ItemUtils.consumeFromPlayerInventory(player, held, extractable, false)) || !AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, 8.0f, false) || !world.func_175656_a(placePos, stateToPlace)) {
                continue;
            }
            final PktPlayEffect ev = new PktPlayEffect(PktPlayEffect.Type.BLOCK_EFFECT).addData(buf -> {
                ByteBufUtils.writePos(buf, placePos);
                ByteBufUtils.writeBlockState(buf, stateToPlace);
                return;
            });
            PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(world, (Vector3i)placePos, 32.0));
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
    }
    
    @Nonnull
    private Map<BlockPos, BlockState> getPlayerPlaceableStates(final Player player, final ItemStack stack) {
        final PlaceMode mode = getPlaceMode(stack);
        final Level world = player.level();
        final BlockHitResult rtr = MiscUtils.rayTraceLookBlock((Entity)player, ClipContext.BlockMode.OUTLINE, ClipContext.FluidMode.ANY, 60.0);
        if (rtr == null && mode.needsOffset()) {
            return new HashMap<BlockPos, BlockState>();
        }
        Map<BlockPos, BlockState> placeStates;
        if (rtr != null) {
            final Direction placingAgainst = rtr.func_216354_b();
            final BlockPos at = rtr.func_216350_a().func_177972_a(rtr.func_216354_b());
            placeStates = this.getPlaceStates(player, world, at, placingAgainst, stack);
        }
        else {
            placeStates = this.getPlaceStates(player, world, null, null, stack);
        }
        return placeStates;
    }
    
    @Nonnull
    private Map<BlockPos, BlockState> getPlaceStates(final Player placer, final Level world, @Nullable final BlockPos origin, @Nullable final Direction placingAgainst, final ItemStack refStack) {
        final Map<BlockState, Tuple<ItemStack, Integer>> tplStates = ItemBlockStorage.getInventoryMatching(placer, refStack);
        final PlaceMode placeMode = getPlaceMode(refStack);
        final Map<BlockPos, BlockState> placeables = Maps.newHashMap();
        int totalItems = 0;
        if (placer.getVehicle()) {
            totalItems = Integer.MAX_VALUE;
        }
        else {
            for (final Tuple<ItemStack, Integer> amountTpl : tplStates.values()) {
                totalItems += (int)(((int)amountTpl.getB() == -1) ? 500000 : amountTpl.getB());
            }
        }
        List<BlockPos> foundPositions = placeMode.generatePlacementPositions(world, placer, placingAgainst, origin);
        if (foundPositions.isEmpty()) {
            return placeables;
        }
        foundPositions = foundPositions.subList(0, Math.min(foundPositions.size(), totalItems));
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
            MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> {
                if (BlockUtils.isReplaceable(world, pos)) {
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
                return;
            });
        }
        return placeables;
    }
    
    public static void setPlaceMode(@Nonnull final ItemStack stack, @Nonnull final PlaceMode mode) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemArchitectWand)) {
            return;
        }
        final CompoundTag nbt = NBTHelper.getPersistentData(stack);
        nbt.putInt("placeMode", mode.ordinal());
    }
    
    @Nonnull
    public static PlaceMode getPlaceMode(@Nonnull final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemArchitectWand)) {
            return PlaceMode.TOWARDS_PLAYER;
        }
        final CompoundTag nbt = NBTHelper.getPersistentData(stack);
        return MiscUtils.getEnumEntry(PlaceMode.class, nbt.getInt("placeMode"));
    }
    
    public enum PlaceMode
    {
        TOWARDS_PLAYER("towards", true, 3.0f) {
            @Override
            public List<BlockPos> generatePlacementPositions(final Level world, final Player player, final Direction placedAgainst, final BlockPos center) {
                final List<BlockPos> blocks = new ArrayList<BlockPos>();
                double cmpFrom = 0.0;
                double cmpTo = 0.0;
                switch (placedAgainst.func_176740_k()) {
                    case X: {
                        cmpFrom = center.getX();
                        cmpTo = player.getX();
                        break;
                    }
                    case Y: {
                        cmpFrom = center.getY();
                        cmpTo = player.getY();
                        break;
                    }
                    case Z: {
                        cmpFrom = center.getZ();
                        cmpTo = player.getZ();
                        break;
                    }
                    default: {
                        return Lists.newLinkedList();
                    }
                }
                for (int length = (int)Math.min(20.0, Math.abs(cmpFrom + 0.5 - cmpTo)), i = 0; i < length; ++i) {
                    final BlockPos at = center.func_177967_a(placedAgainst, i);
                    if (MiscUtils.executeWithChunk((IWorldReader)world, at, () -> !BlockUtils.isReplaceable(world, at), true)) {
                        break;
                    }
                    blocks.add(at);
                }
                return blocks;
            }
        }, 
        FROM_PLAYER("line", false) {
            @Override
            public List<BlockPos> generatePlacementPositions(final Level world, final Player player, final Direction placedAgainst, final BlockPos center) {
                final BlockPos origin = player.func_233580_cy_().renderItem();
                final HitResult result = player.func_213324_a(60.0, 1.0f, false);
                BlockPos hit;
                if (result instanceof BlockHitResult) {
                    hit = ((BlockHitResult)result).func_216350_a();
                }
                else {
                    hit = new BlockPos(result.func_216347_e());
                }
                final List<BlockPos> line = new ArrayList<BlockPos>();
                final RaytraceAssist rta = new RaytraceAssist(origin, hit);
                rta.forEachBlockPos(pos -> MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> {
                    if (BlockUtils.isReplaceable(world, pos)) {
                        line.add(pos);
                        return Boolean.valueOf(true);
                    }
                    else {
                        return Boolean.valueOf(false);
                    }
                }, false));
                return line;
            }
        }, 
        H_PLANE("plane", true) {
            @Override
            public List<BlockPos> generatePlacementPositions(final Level world, final Player player, final Direction placedAgainst, final BlockPos center) {
                return MiscUtils.transformList(BlockGeometry.getPlane(Direction.UP, 5), at -> at.func_177971_a((Vector3i)center));
            }
        }, 
        V_PLANE("wall", true) {
            @Override
            public List<BlockPos> generatePlacementPositions(final Level world, final Player player, final Direction placedAgainst, final BlockPos center) {
                return MiscUtils.transformList(BlockGeometry.getPlane(player.func_174811_aO(), 5), at -> at.func_177971_a((Vector3i)center));
            }
        }, 
        SPHERE("sphere", true, 0.2f) {
            @Override
            public List<BlockPos> generatePlacementPositions(final Level world, final Player player, final Direction placedAgainst, final BlockPos center) {
                return MiscUtils.transformList(BlockGeometry.getSphere(5.0), at -> at.func_177971_a((Vector3i)center));
            }
        }, 
        SPHERE_HOLLOW("sphere_hollow", true, 0.5f) {
            @Override
            public List<BlockPos> generatePlacementPositions(final Level world, final Player player, final Direction placedAgainst, final BlockPos center) {
                return MiscUtils.transformList(BlockGeometry.getHollowSphere(5.0, 4.0), at -> at.func_177971_a((Vector3i)center));
            }
        };
        
        private final String name;
        private final boolean needsOffset;
        private final float placeCostMulitplier;
        
        private PlaceMode(final String name, final boolean needsOffset) {
            this(name, needsOffset, 1.0f);
        }
        
        private PlaceMode(final String name, final boolean needsOffset, final float placeCostMultiplier) {
            this.name = name;
            this.needsOffset = needsOffset;
            this.placeCostMulitplier = placeCostMultiplier;
        }
        
        public MutableComponent getName() {
            return (MutableComponent)Component.literal("astralsorcery.misc.architect.mode." + this.name);
        }
        
        public MutableComponent getDisplay() {
            return (MutableComponent)Component.translatable("astralsorcery.misc.architect.mode");
        }
        
        public float getPlaceCostMulitplier() {
            return this.placeCostMulitplier;
        }
        
        public boolean needsOffset() {
            return this.needsOffset;
        }
        
        public abstract List<BlockPos> generatePlacementPositions(final Level p0, final Player p1, final Direction p2, final BlockPos p3);
        
        @Nonnull
        private PlaceMode next() {
            final int next = (this.ordinal() + 1) % values().length;
            return MiscUtils.getEnumEntry(PlaceMode.class, next);
        }
    }
}
