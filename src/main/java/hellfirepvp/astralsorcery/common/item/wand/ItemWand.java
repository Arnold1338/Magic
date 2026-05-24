package hellfirepvp.astralsorcery.common.item.wand;

import net.minecraft.network.FriendlyByteBuf;
import hellfirepvp.observerlib.api.block.MatchableState;
import net.minecraft.world.gen.Heightmap;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.observerlib.client.preview.StructurePreview;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import hellfirepvp.observerlib.api.util.BlockArray;
import net.minecraft.world.level.block.Block;
import hellfirepvp.observerlib.api.structure.MatchableStructure;
import hellfirepvp.astralsorcery.common.tile.base.TileRequiresMultiblock;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.block.state.BlockState;
import java.util.Iterator;
import net.minecraft.world.level.LevelReader;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.block.ore.BlockRockCrystalOre;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.astralsorcery.common.data.world.RockCrystalBuffer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.OverrideInteractItem;
import net.minecraft.world.item.Item;

public class ItemWand extends Item implements OverrideInteractItem
{
    public ItemWand() {
        super(new Item.Properties().func_200917_a(1).func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    public void func_77663_a(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
        final boolean active = isSelected || (entity instanceof Player && ((Player)entity).func_184592_cb() == stack);
        if (!world.func_201670_d() && active && entity instanceof ServerPlayer) {
            final RockCrystalBuffer buf = (RockCrystalBuffer)DataAS.DOMAIN_AS.getData(world, (WorldCacheDomain.SaveKey)DataAS.KEY_ROCK_CRYSTAL_BUFFER);
            final ChunkPos pos = new ChunkPos(entity.func_233580_cy_());
            for (final BlockPos rPos : buf.collectPositions(pos, 6)) {
                MiscUtils.executeWithChunk((IWorldReader)world, rPos, () -> {
                    final BlockState state = world.getBlockState(rPos);
                    if (!(state.getBlock() instanceof BlockRockCrystalOre)) {
                        buf.removeOre(rPos);
                    }
                    else {
                        if (!DayTimeHelper.isDay(world) && ItemWand.field_77697_d.nextInt(600) == 0) {
                            final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.ROCK_CRYSTAL_COLUMN).addData(b -> ByteBufUtils.writeVector(b, new Vector3((Vector3i)rPos.above())));
                            PacketChannel.CHANNEL.sendToPlayer((Player)entity, pkt);
                        }
                        if (ItemWand.field_77697_d.nextInt(800) == 0) {
                            final PktPlayEffect pkt2 = new PktPlayEffect(PktPlayEffect.Type.ROCK_CRYSTAL_SPARKS).addData(b -> ByteBufUtils.writeVector(b, new Vector3((Vector3i)rPos.above())));
                            PacketChannel.CHANNEL.sendToPlayer((Player)entity, pkt2);
                        }
                    }
                });
            }
        }
    }
    
    public boolean shouldInterceptBlockInteract(final LogicalSide side, final Player player, final Hand hand, final BlockPos pos, final Direction face) {
        return true;
    }
    
    public boolean doBlockInteract(final LogicalSide side, final Player player, final Hand hand, final BlockPos pos, final Direction face) {
        final World world = player.func_130014_f_();
        final BlockState state = world.getBlockState(pos);
        final Block b = state.getBlock();
        if (b instanceof WandInteractable && ((WandInteractable)b).onInteract(world, pos, player, face, player.func_225608_bj_())) {
            return true;
        }
        final WandInteractable wandTe = MiscUtils.getTileAt((IBlockReader)world, pos, WandInteractable.class, true);
        if (wandTe != null && wandTe.onInteract(world, pos, player, face, player.func_225608_bj_())) {
            return true;
        }
        final TileRequiresMultiblock mbTe = MiscUtils.getTileAt((IBlockReader)world, pos, TileRequiresMultiblock.class, true);
        if (mbTe != null && mbTe.getRequiredStructureType() != null && mbTe.getRequiredStructureType().getStructure() instanceof MatchableStructure && !((MatchableStructure)mbTe.getRequiredStructureType().getStructure()).matches((IBlockReader)world, pos)) {
            if (world.func_201670_d()) {
                this.displayClientStructurePreview(world, pos, mbTe.getRequiredStructureType());
            }
            else if (player.func_213453_ef() && player.func_184812_l_()) {
                final BlockArray structure = mbTe.getRequiredStructureType().getStructure();
                structure.getContents().forEach((offset, rState) -> world.func_175656_a(pos.func_177971_a((Vector3i)offset), rState.getDescriptiveState(0L)));
            }
            return true;
        }
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    private void displayClientStructurePreview(final World world, final BlockPos pos, final StructureType type) {
        StructurePreview.newBuilder(world.dimension(), pos, (MatchableStructure)type.getStructure()).removeIfOutInDifferentWorld().andPersistOnlyIf((inWorld, at) -> MiscUtils.executeWithChunk((IWorldReader)world, pos, () -> {
            final TileRequiresMultiblock tileFound = MiscUtils.getTileAt((IBlockReader)world, pos, TileRequiresMultiblock.class, true);
            if (tileFound == null) {
                return Boolean.valueOf(false);
            }
            else {
                return Boolean.valueOf(tileFound.getRequiredStructureType() != null && tileFound.getRequiredStructureType().equals(type));
            }
        }, true)).andPersistOnlyIf((inWorld, at) -> !((MatchableStructure)type.getStructure()).matches((IBlockReader)world, pos)).showBar(type.getDisplayName()).buildAndSet();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playUndergroundEffect(final PktPlayEffect effect) {
        final Vector3 at = ByteBufUtils.readVector(effect.getExtraData());
        final World world = (World)Minecraft.func_71410_x().field_71441_e;
        if (world == null) {
            return;
        }
        final float dstr = 0.4f + 0.6f * DayTimeHelper.getCurrentDaytimeDistribution(world);
        final Vector3 plVec = Vector3.atEntityCorner((Entity)Minecraft.func_71410_x().field_71439_g);
        final float dst = (float)at.distance(plVec);
        final float dstMul = (dst <= 25.0f) ? 1.0f : ((dst >= 50.0f) ? 0.0f : (1.0f - (dst - 25.0f) / 25.0f));
        for (int i = 0; i < 3; ++i) {
            if (ItemWand.field_77697_d.nextBoolean()) {
                EffectHelper.of(EffectTemplatesAS.GENERIC_DEPTH_PARTICLE).spawn(at.clone().add(-1.0f + ItemWand.field_77697_d.nextFloat() * 3.0f, -1.0f + ItemWand.field_77697_d.nextFloat() * 3.0f, -1.0f + ItemWand.field_77697_d.nextFloat() * 3.0f)).color(VFXColorFunction.constant(ColorsAS.ROCK_CRYSTAL)).setScaleMultiplier(0.4f).setAlphaMultiplier(150.0f * dstr / 255.0f * dstMul).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(30 + ItemWand.field_77697_d.nextInt(10));
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playEffect(final PktPlayEffect effect) {
        final Vector3 pos = ByteBufUtils.readVector(effect.getExtraData());
        final World world = (World)Minecraft.func_71410_x().field_71441_e;
        if (world == null) {
            return;
        }
        final BlockPos at = pos.toBlockPos();
        final BlockPos top = world.func_205770_a(Heightmap.Type.WORLD_SURFACE, at);
        final Vector3 columnDisplay = new Vector3((Vector3i)top);
        MiscUtils.applyRandomOffset(columnDisplay, ItemWand.field_77697_d, 2.0f);
        final double mX = ItemWand.field_77697_d.nextFloat() * 0.01f * (ItemWand.field_77697_d.nextBoolean() ? 1 : -1);
        final double mY = ItemWand.field_77697_d.nextFloat() * 0.5f;
        final double mZ = ItemWand.field_77697_d.nextFloat() * 0.01f * (ItemWand.field_77697_d.nextBoolean() ? 1 : -1);
        final float dstr = DayTimeHelper.getCurrentDaytimeDistribution(world);
        for (int i = 0; i < 8 + ItemWand.field_77697_d.nextInt(10); ++i) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(columnDisplay).setMotion(new Vector3(mX * (0.2 + 0.8 * ItemWand.field_77697_d.nextFloat()), mY * ItemWand.field_77697_d.nextFloat(), mZ * (0.2 + 0.8 * ItemWand.field_77697_d.nextFloat()))).color(VFXColorFunction.constant(ColorsAS.ROCK_CRYSTAL)).setAlphaMultiplier(150.0f * dstr / 255.0f).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.3f + 0.3f * ItemWand.field_77697_d.nextFloat()).setMaxAge(25 + ItemWand.field_77697_d.nextInt(30));
        }
    }
}
