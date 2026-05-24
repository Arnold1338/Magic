package hellfirepvp.astralsorcery.common.block.tile;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.pathfinding.PathType;
import java.util.UUID;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeHooks;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.GatedKnowledge;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.crystal.CalculationContext;
import hellfirepvp.astralsorcery.common.crystal.calc.PropertySource;
import hellfirepvp.astralsorcery.common.crystal.source.AttunedSourceInstance;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.item.block.ItemBlockCollectorCrystal;
import net.minecraft.world.level.block.SoundType;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import hellfirepvp.astralsorcery.common.block.tile.crystal.CollectorCrystalType;
import net.minecraft.world.level.phys.shapes.VoxelShape;
import hellfirepvp.astralsorcery.common.block.base.CustomItemBlock;
import hellfirepvp.observerlib.api.block.BlockStructureObserver;
import hellfirepvp.astralsorcery.common.block.base.BlockStarlightNetwork;

public abstract class BlockCollectorCrystal extends BlockStarlightNetwork implements BlockStructureObserver, CustomItemBlock
{
    private static final VoxelShape SHAPE;
    private static final float PLAYER_HARVEST_HARDNESS = 4.0f;
    
    public BlockCollectorCrystal(final CollectorCrystalType type) {
        super(AbstractBlock.Properties.func_200949_a(Material.field_151592_s, type.getMaterialColor()).func_200948_a(-1.0f, 3600000.0f).harvestTool(ToolType.PICKAXE).harvestLevel(1).func_200947_a(SoundType.field_185853_f).func_235838_a_(state -> 11));
    }
    
    public abstract Class<? extends ItemBlockCollectorCrystal> getItemBlockClass();
    
    @OnlyIn(Dist.CLIENT)
    public void func_190948_a(final ItemStack stack, @Nullable final IBlockReader world, final List<Component> toolTip, final TooltipFlag flag) {
        super.func_190948_a(stack, world, (List)toolTip, flag);
        final CrystalAttributes attr = CrystalAttributes.getCrystalAttributes(stack);
        CrystalAttributes.TooltipResult result = null;
        if (attr != null) {
            result = attr.addTooltip(toolTip, CalculationContext.Builder.withSource(new AttunedSourceInstance(CrystalPropertiesAS.Sources.SOURCE_TILE_COLLECTOR_CRYSTAL, ((ConstellationItem)stack.getItem()).getAttunedConstellation(stack))).addUsage(CrystalPropertiesAS.Usages.USE_COLLECTOR_CRYSTAL).addUsage(CrystalPropertiesAS.Usages.USE_LENS_TRANSFER).build());
        }
        if (result != null) {
            final PlayerProgress clientProgress = ResearchHelper.getClientProgress();
            final ProgressionTier tier = clientProgress.getTierReached();
            final boolean addedMissing = result != CrystalAttributes.TooltipResult.ADDED_ALL;
            final IWeakConstellation c = ((ConstellationItem)stack.getItem()).getAttunedConstellation(stack);
            if (c != null) {
                if (GatedKnowledge.COLLECTOR_TYPE.canSee(tier) && clientProgress.hasConstellationDiscovered(c)) {
                    toolTip.add((Component)new Component("crystal.info.astralsorcery.collect.type", new Object[] { c.getConstellationName().func_240699_a_(ChatFormatting.BLUE) }).func_240699_a_(ChatFormatting.GRAY));
                }
                else if (!addedMissing) {
                    toolTip.add((Component)new Component("astralsorcery.progress.missing.knowledge").func_240699_a_(ChatFormatting.GRAY));
                }
            }
            final IMinorConstellation tr = ((ConstellationItem)stack.getItem()).getTraitConstellation(stack);
            if (tr != null) {
                if (GatedKnowledge.CRYSTAL_TRAIT.canSee(tier) && clientProgress.hasConstellationDiscovered(tr)) {
                    toolTip.add((Component)new Component("crystal.info.astralsorcery.trait", new Object[] { tr.getConstellationName().func_240699_a_(ChatFormatting.BLUE) }).func_240699_a_(ChatFormatting.GRAY));
                }
                else if (!addedMissing) {
                    toolTip.add((Component)new Component("astralsorcery.progress.missing.knowledge").func_240699_a_(ChatFormatting.GRAY));
                }
            }
        }
    }
    
    public VoxelShape func_220053_a(final BlockState p_220053_1_, final IBlockReader p_220053_2_, final BlockPos p_220053_3_, final CollisionContext p_220053_4_) {
        return BlockCollectorCrystal.SHAPE;
    }
    
    public float func_180647_a(final BlockState state, final Player player, final IBlockReader world, final BlockPos pos) {
        final TileCollectorCrystal crystal = MiscUtils.getTileAt(world, pos, TileCollectorCrystal.class, false);
        if (crystal != null && crystal.isPlayerMade()) {
            final int i = ForgeHooks.canHarvestBlock(state, player, world, pos) ? 30 : 100;
            return player.getDigSpeed(state, pos) / 4.0f / i;
        }
        return super.func_180647_a(state, player, world, pos);
    }
    
    public void func_180633_a(final World world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity entity, final ItemStack stack) {
        final TileCollectorCrystal tcc = MiscUtils.getTileAt((IBlockReader)world, pos, TileCollectorCrystal.class, true);
        final Item i = stack.getItem();
        if (tcc != null && i instanceof ItemBlockCollectorCrystal) {
            final ItemBlockCollectorCrystal ibcc = (ItemBlockCollectorCrystal)i;
            UUID playerUUID = null;
            if (entity instanceof Player) {
                playerUUID = entity.getUUID();
            }
            tcc.updateData(playerUUID, ibcc.getCollectorType());
        }
        super.func_180633_a(world, pos, state, entity, stack);
    }
    
    public boolean func_196266_a(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final PathType type) {
        return false;
    }
    
    public RenderShape func_149645_b(final BlockState p_149645_1_) {
        return RenderShape.MODEL;
    }
    
    @Nullable
    public BlockEntity func_196283_a_(final IBlockReader worldIn) {
        return new TileCollectorCrystal();
    }
    
    static {
        SHAPE = Block.func_208617_a(4.5, 0.0, 4.5, 11.5, 16.0, 11.5);
    }
}
