package hellfirepvp.astralsorcery.common.item.lens;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.AstralSorcery;

public class ItemColoredLensDamage extends ItemColoredLens
{
    private static final ColorTypeDamage COLOR_TYPE_DAMAGE;
    
    public ItemColoredLensDamage() {
        super(ItemColoredLensDamage.COLOR_TYPE_DAMAGE);
    }
    
    static {
        COLOR_TYPE_DAMAGE = new ColorTypeDamage();
    }
    
    private static class ColorTypeDamage extends LensColorType
    {
        public ColorTypeDamage() {
            super(AstralSorcery.key("damage"), TargetType.ENTITY, () -> new ItemStack((ItemLike)ItemsAS.COLORED_LENS_DAMAGE), ColorsAS.COLORED_LENS_DAMAGE, 0.2f, false);
        }
        
        @Override
        public void entityInBeam(final World world, final Vector3 origin, final Vector3 target, final Entity entity, final PartialEffectExecutor executor) {
            if (world.func_201670_d() || !(entity instanceof LivingEntity)) {
                return;
            }
            executor.executeAll(() -> {
                if (!(entity instanceof Player) || ((boolean)GeneralConfig.CONFIG.doColoredLensesAffectPlayers.get() && entity.func_184102_h() != null && entity.func_184102_h().func_71219_W())) {
                    DamageUtil.attackEntityFrom(entity, CommonProxy.DAMAGE_SOURCE_STELLAR, 1.5f);
                }
            });
        }
        
        @Override
        public void blockInBeam(final World world, final BlockPos pos, final BlockState state, final PartialEffectExecutor executor) {
        }
    }
}
