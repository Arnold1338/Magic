package hellfirepvp.astralsorcery.common.item.dust;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.util.BlockSnapshot;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.entity.EntityIlluminationSpark;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;

public class ItemIlluminationPowder extends ItemUsableDust
{
    @Override
    boolean dispense(final BlockSource dispenser) {
        final BlockPos at = dispenser.func_180699_d();
        final Direction face = (Direction)dispenser.func_189992_e().getValue((Property)DispenserBlock.field_176441_a);
        final EntityIlluminationSpark nocSpark = new EntityIlluminationSpark(at.getX(), at.getY(), at.getZ(), (World)dispenser.func_197524_h());
        nocSpark.func_70186_c((double)face.func_82601_c(), (double)(face.func_96559_d() + 0.1f), (double)face.func_82599_e(), 0.7f, 0.9f);
        return dispenser.func_197524_h().func_217376_c((Entity)nocSpark);
    }
    
    @Override
    boolean rightClickAir(final World world, final Player player, final ItemStack dust) {
        return world.func_217376_c((Entity)new EntityIlluminationSpark((LivingEntity)player, world));
    }
    
    @Override
    boolean rightClickBlock(final ItemUseContext ctx) {
        final World world = ctx.func_195991_k();
        BlockPos pos = ctx.func_195995_a();
        final Player player = ctx.func_195999_j();
        if (player == null) {
            return false;
        }
        if (!BlockUtils.isReplaceable(world, pos)) {
            pos = pos.func_177972_a(ctx.func_196000_l());
        }
        return BlockUtils.isReplaceable(world, pos) && (player.func_175151_a(pos, ctx.func_196000_l(), ctx.func_195996_i()) && !ForgeEventFactory.onBlockPlace((Entity)player, BlockSnapshot.create(world.dimension(), (IWorld)world, pos), ctx.func_196000_l())) && world.func_175656_a(pos, BlocksAS.FLARE_LIGHT.defaultBlockState());
    }
}
