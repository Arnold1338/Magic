package hellfirepvp.astralsorcery.common.block.tile;

import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import net.minecraftforge.items.IItemHandler;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.container.factory.CustomContainerProvider;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarRadianceProvider;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarConstellationProvider;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarAttunementProvider;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.container.factory.ContainerAltarDiscoveryProvider;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.block.properties.PropertiesMarble;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public abstract class BlockAltar extends BlockStarlightNetwork implements CustomItemBlock
{
    private final AltarType type;
    
    public BlockAltar(final AltarType type) {
        super(PropertiesMarble.defaultMarble().harvestLevel(1).harvestTool(ToolType.PICKAXE));
        this.type = type;
    }
    
    public AltarType getAltarType() {
        return this.type;
    }
    
    public InteractionResult func_225533_a_(final BlockState state, final World world, final BlockPos pos, final Player player, final Hand hand, final BlockHitResult hit) {
        if (!world.func_201670_d() && player instanceof ServerPlayer) {
            final TileAltar altar = MiscUtils.getTileAt((IBlockReader)world, pos, TileAltar.class, true);
            if (altar != null) {
                CustomContainerProvider<?> provider = null;
                switch (altar.getAltarType()) {
                    case DISCOVERY: {
                        provider = new ContainerAltarDiscoveryProvider(altar);
                        if (!ResearchHelper.getProgress(player, LogicalSide.SERVER).getTierReached().isThisLaterOrEqual(ProgressionTier.BASIC_CRAFT)) {
                            ResearchManager.informCrafted(player, new ItemStack((ItemLike)BlocksAS.ALTAR_DISCOVERY));
                            break;
                        }
                        break;
                    }
                    case ATTUNEMENT: {
                        provider = new ContainerAltarAttunementProvider(altar);
                        break;
                    }
                    case CONSTELLATION: {
                        provider = new ContainerAltarConstellationProvider(altar);
                        break;
                    }
                    case RADIANCE: {
                        provider = new ContainerAltarRadianceProvider(altar);
                        break;
                    }
                    default: {
                        provider = null;
                        break;
                    }
                }
                if (provider != null) {
                    provider.openFor((ServerPlayer)player);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
    
    @Override
    public void func_196243_a(final BlockState state, final World worldIn, final BlockPos pos, final BlockState newState, final boolean isMoving) {
        if (!(newState.getBlock() instanceof BlockAltar)) {
            final TileAltar ta = MiscUtils.getTileAt((IBlockReader)worldIn, pos, TileAltar.class, true);
            if (ta != null && !worldIn.isClientSide) {
                ItemUtils.dropInventory((IItemHandler)ta.getInventory(), worldIn, pos);
            }
            super.func_196243_a(state, worldIn, pos, newState, isMoving);
        }
        else {
            final AltarType thisType = ((BlockAltar)state.getBlock()).type;
            final AltarType thatType = ((BlockAltar)newState.getBlock()).type;
            if (thisType != thatType) {
                final TileAltar ta2 = MiscUtils.getTileAt((IBlockReader)worldIn, pos, TileAltar.class, true);
                if (ta2 != null) {
                    ta2.updateType(thatType, false);
                }
            }
        }
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState state) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader world) {
        return new TileAltar().updateType(this.type, true);
    }
}
