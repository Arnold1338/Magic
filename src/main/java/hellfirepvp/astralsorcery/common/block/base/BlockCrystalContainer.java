package hellfirepvp.astralsorcery.common.block.base;

import net.minecraft.world.item.Item;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.ConstellationTile;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeTile;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.BaseEntityBlock;

public abstract class BlockCrystalContainer extends BaseEntityBlock
{
    protected BlockCrystalContainer(final AbstractBlock.Properties builder) {
        super(builder);
    }
    
    public ItemStack getPickBlock(final BlockState state, final HitResult target, final IBlockReader world, final BlockPos pos, final Player player) {
        final ItemStack stack = super.getPickBlock(state, target, world, pos, player);
        if (stack.getItem() instanceof CrystalAttributeItem) {
            final CrystalAttributeTile cat = MiscUtils.getTileAt(world, pos, CrystalAttributeTile.class, true);
            if (cat != null) {
                ((CrystalAttributeItem)stack.getItem()).setAttributes(stack, cat.getAttributes());
            }
        }
        if (stack.getItem() instanceof ConstellationItem) {
            final ConstellationTile ct = MiscUtils.getTileAt(world, pos, ConstellationTile.class, true);
            if (ct != null) {
                ((ConstellationItem)stack.getItem()).setAttunedConstellation(stack, ct.getAttunedConstellation());
                ((ConstellationItem)stack.getItem()).setTraitConstellation(stack, ct.getTraitConstellation());
            }
        }
        return stack;
    }
    
    public void func_180633_a(final Level world, final BlockPos pos, final BlockState state, @Nullable final LivingEntity placer, final ItemStack stack) {
        final Item i = stack.getItem();
        if (i instanceof CrystalAttributeItem) {
            final CrystalAttributeTile cat = MiscUtils.getTileAt((IBlockReader)world, pos, CrystalAttributeTile.class, true);
            if (cat != null) {
                cat.setAttributes(((CrystalAttributeItem)i).getAttributes(stack));
            }
        }
        if (i instanceof ConstellationItem) {
            final ConstellationTile ct = MiscUtils.getTileAt((IBlockReader)world, pos, ConstellationTile.class, true);
            if (ct != null) {
                ct.setAttunedConstellation(((ConstellationItem)i).getAttunedConstellation(stack));
                ct.setTraitConstellation(((ConstellationItem)i).getTraitConstellation(stack));
            }
        }
    }
}
