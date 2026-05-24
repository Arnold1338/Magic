package hellfirepvp.astralsorcery.common.item.dust;

import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.entity.EntityNocturnalSpark;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.BlockSource;

public class ItemNocturnalPowder extends ItemUsableDust
{
    @Override
    boolean dispense(final BlockSource dispenser) {
        final BlockPos at = dispenser.func_180699_d();
        final Direction face = (Direction)dispenser.func_189992_e().getValue((Property)DispenserBlock.field_176441_a);
        final EntityNocturnalSpark nocSpark = new EntityNocturnalSpark(at.getX(), at.getY(), at.getZ(), (World)dispenser.func_197524_h());
        nocSpark.func_70186_c((double)face.func_82601_c(), (double)(face.func_96559_d() + 0.1f), (double)face.func_82599_e(), 0.7f, 0.9f);
        return dispenser.func_197524_h().func_217376_c((Entity)nocSpark);
    }
    
    @Override
    boolean rightClickAir(final World world, final Player player, final ItemStack dust) {
        return world.func_217376_c((Entity)new EntityNocturnalSpark((LivingEntity)player, world));
    }
    
    @Override
    boolean rightClickBlock(final ItemUseContext ctx) {
        final BlockPos pos = ctx.func_195995_a().func_177972_a(ctx.func_196000_l());
        final EntityNocturnalSpark noc = new EntityNocturnalSpark((LivingEntity)ctx.func_195999_j(), ctx.func_195991_k());
        noc.setPos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        noc.setSpawning();
        return ctx.func_195991_k().func_217376_c((Entity)noc);
    }
}
