package hellfirepvp.astralsorcery.common.item.lens;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.phys.Vec3;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.AstralSorcery;

public class ItemColoredLensPush extends ItemColoredLens
{
    private static final ColorTypePush COLOR_TYPE_PUSH;
    
    public ItemColoredLensPush() {
        super(ItemColoredLensPush.COLOR_TYPE_PUSH);
    }
    
    static {
        COLOR_TYPE_PUSH = new ColorTypePush();
    }
    
    private static class ColorTypePush extends LensColorType
    {
        private ColorTypePush() {
            super(AstralSorcery.key("push"), TargetType.ENTITY, () -> new ItemStack((ItemLike)ItemsAS.COLORED_LENS_PUSH), ColorsAS.COLORED_LENS_PUSH, 0.25f, false);
        }
        
        @Override
        public void entityInBeam(final Level world, final Vector3 origin, final Vector3 target, final Entity entity, final PartialEffectExecutor executor) {
            if (entity instanceof Player && !(boolean)GeneralConfig.CONFIG.doColoredLensesAffectPlayers.get() && executor.canExecute()) {
                return;
            }
            final Vector3 dir = target.clone().subtract(origin).normalize().multiply(0.4f);
            final Vec3 eMotion = entity.func_213322_ci();
            final Vector3 motion = new Vector3(Math.min(1.0, eMotion.field_72450_a + dir.getX()), dir.getY() + 0.03999999910593033, Math.min(1.0, eMotion.field_72449_c + dir.getZ()));
            entity.func_213317_d(MiscUtils.limitVelocityToMinecraftLimit(motion).toVector3d());
        }
        
        @Override
        public void blockInBeam(final Level world, final BlockPos pos, final BlockState state, final PartialEffectExecutor executor) {
        }
    }
}
