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
import java.util.Random;

public class ItemColoredLensRegeneration extends ItemColoredLens
{
    private static final ColorTypeRegeneration COLOR_TYPE_REGENERATION;
    
    public ItemColoredLensRegeneration() {
        super(ItemColoredLensRegeneration.COLOR_TYPE_REGENERATION);
    }
    
    static {
        COLOR_TYPE_REGENERATION = new ColorTypeRegeneration();
    }
    
    private static class ColorTypeRegeneration extends LensColorType
    {
        private ColorTypeRegeneration() {
            super(AstralSorcery.key("regeneration"), TargetType.ENTITY, () -> new ItemStack((ItemLike)ItemsAS.COLORED_LENS_REGENERATION), ColorsAS.COLORED_LENS_REGEN, 0.1f, false);
        }
        
        @Override
        public void entityInBeam(final Level world, final Vector3 origin, final Vector3 target, final Entity entity, final PartialEffectExecutor executor) {
            if (world.level() || !(entity instanceof LivingEntity) || !entity.isAlive()) {

            }
            if (entity instanceof Player && !(boolean)GeneralConfig.CONFIG.doColoredLensesAffectPlayers.get()) {

            }
            final LivingEntity le = (LivingEntity)entity;
            executor.executeAll(() -> {
                if (ItemColoredLensRegeneration.count.nextInt(8) == 0) {
                    if (le.func_70662_br()) {
                        DamageUtil.shotgunAttack(le, e -> DamageUtil.attackEntityFrom((Entity)e, CommonProxy.DAMAGE_SOURCE_STELLAR, 0.5f));
                    }
                    else {
                        le.heal(0.5f);
                    }
                }
            });
        }
        
        @Override
        public void blockInBeam(final Level world, final BlockPos pos, final BlockState state, final PartialEffectExecutor executor) {
        }
    }
}
