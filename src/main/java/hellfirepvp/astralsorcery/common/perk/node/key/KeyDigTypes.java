package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.block.Blocks;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.level.entity.Entity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyDigTypes extends KeyPerk
{
    public KeyDigTypes(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener((Consumer)this::onHarvest);
        bus.addListener((Consumer)this::onHarvestSpeed);
    }
    
    private void onHarvest(final PlayerEvent.HarvestCheck event) {
        if (event.canHarvest()) {
            return;
        }
        final Player player = event.getPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (prog.getPerkData().hasPerkEffect(this)) {
            final ItemStack heldMainHand = player.func_184614_ca();
            if (!heldMainHand.isEmpty() && heldMainHand.getItem().getToolTypes(heldMainHand).contains(ToolType.PICKAXE)) {
                final ToolType requiredTool = event.getTargetBlock().getHarvestTool();
                if (requiredTool == null || requiredTool.equals(ToolType.SHOVEL) || requiredTool.equals(ToolType.AXE)) {
                    event.setCanHarvest(true);
                }
            }
        }
    }
    
    private void onHarvestSpeed(final PlayerEvent.BreakSpeed event) {
        final Player player = event.getPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (prog.getPerkData().hasPerkEffect(this)) {
            final BlockState broken = event.getState();
            final ItemStack playerMainHand = player.func_184614_ca();
            if (!playerMainHand.isEmpty() && playerMainHand.getItem().getToolTypes(playerMainHand).contains(ToolType.PICKAXE) && !broken.isToolEffective(ToolType.PICKAXE) && (broken.isToolEffective(ToolType.AXE) || broken.isToolEffective(ToolType.SHOVEL))) {
                EventFlags.CHECK_BREAK_SPEED.executeWithFlag(() -> MiscUtils.tryMultiple(() -> player.getDigSpeed(Blocks.field_150348_b.defaultBlockState(), event.getPos()), () -> player.getDigSpeed(Blocks.field_150348_b.defaultBlockState(), (BlockPos)null), () -> BlockUtils.getSimpleBreakSpeed((LivingEntity)player, playerMainHand, Blocks.field_150348_b.defaultBlockState())).ifPresent(speed -> event.setNewSpeed(Math.max(event.getNewSpeed(), speed))));
            }
        }
    }
}
