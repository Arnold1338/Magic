package hellfirepvp.astralsorcery.common.item.block;

import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.world.item.BlockItemUseContext;
import java.util.Iterator;
import net.minecraft.world.level.level.ItemLike;
import hellfirepvp.astralsorcery.common.block.tile.BlockGemCrystalCluster;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.item.CreativeModeTab;
import net.minecraft.world.level.item.Item;
import net.minecraft.world.level.level.block.Block;

public class ItemBlockGemCrystalCluster extends ItemBlockCustom
{
    public ItemBlockGemCrystalCluster(final Block block, final Item.Properties itemProperties) {
        super(block, itemProperties);
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            for (final BlockGemCrystalCluster.GrowthStageType stage : BlockGemCrystalCluster.STAGE.func_177700_c()) {
                final ItemStack cluster = new ItemStack((ItemLike)this);
                this.setDamage(cluster, stage.ordinal());
                items.add((Object)cluster);
            }
        }
    }
    
    @Nullable
    protected BlockState func_195945_b(final BlockItemUseContext context) {
        final BlockState toPlace = super.func_195945_b(context);
        if (toPlace != null) {
            return (BlockState)toPlace.func_206870_a((Property)BlockGemCrystalCluster.STAGE, (Comparable)this.getGrowthStage(context.func_195996_i()));
        }
        return null;
    }
    
    @Nonnull
    private BlockGemCrystalCluster.GrowthStageType getGrowthStage(final ItemStack stack) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemBlockGemCrystalCluster)) {
            return BlockGemCrystalCluster.GrowthStageType.STAGE_0;
        }
        return MiscUtils.getEnumEntry(BlockGemCrystalCluster.GrowthStageType.class, this.getDamage(stack));
    }
    
    public String func_77667_c(final ItemStack stack) {
        final BlockGemCrystalCluster.GrowthStageType stage = this.getGrowthStage(stack);
        switch (stage) {
            case STAGE_2_SKY: {
                return super.func_77667_c(stack) + ".sky";
            }
            case STAGE_2_DAY: {
                return super.func_77667_c(stack) + ".day";
            }
            case STAGE_2_NIGHT: {
                return super.func_77667_c(stack) + ".night";
            }
            default: {
                return super.func_77667_c(stack);
            }
        }
    }
}
