package hellfirepvp.astralsorcery.common.util.block;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.InteractionHand;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItemUseContext;

public class TestBlockUseContext extends BlockItemUseContext
{
    private final Entity entity;
    
    private TestBlockUseContext(final World worldIn, @Nullable final Entity usingEntity, final Hand hand, final ItemStack stack, final BlockPos at, final Direction side) {
        super(worldIn, (Player)null, hand, stack, new BlockHitResult(Vec3.func_237489_a_((Vector3i)at), side, at, false));
        this.entity = usingEntity;
    }
    
    public static BlockItemUseContext getHandContext(final World worldIn, @Nullable final Entity usingEntity, final Hand usedHand, final BlockPos at, final Direction side) {
        return getHandContextWithItem(worldIn, usingEntity, usedHand, ItemStack.EMPTY, at, side);
    }
    
    public static BlockItemUseContext getHandContextWithItem(final World worldIn, @Nullable final Entity usingEntity, final Hand usedHand, final ItemStack stack, final BlockPos at, final Direction side) {
        return new TestBlockUseContext(worldIn, usingEntity, usedHand, stack, at, side);
    }
    
    public Direction func_195992_f() {
        return (this.entity == null) ? Direction.NORTH : Direction.func_176733_a((double)this.entity.field_70177_z);
    }
    
    public Direction func_196010_d() {
        return Direction.func_196054_a(this.entity)[0];
    }
    
    public Direction[] func_196009_e() {
        final Direction[] adirection = Direction.func_196054_a(this.entity);
        if (this.field_196013_a) {
            return adirection;
        }
        Direction direction;
        int i;
        for (direction = this.func_196000_l(), i = 0; i < adirection.length && adirection[i] != direction.func_176734_d(); ++i) {}
        if (i > 0) {
            System.arraycopy(adirection, 0, adirection, 1, i);
            adirection[0] = direction.func_176734_d();
        }
        return adirection;
    }
    
    public boolean func_225518_g_() {
        return false;
    }
    
    public float func_195990_h() {
        return 0.0f;
    }
}
