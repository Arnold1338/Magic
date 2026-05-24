package hellfirepvp.astralsorcery.common.effect;

import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.potion.EffectType;

public class EffectBleed extends EffectCustomTexture
{
    public EffectBleed() {
        super(EffectType.HARMFUL, ColorsAS.EFFECT_BLEED);
    }
    
    public boolean func_76397_a(final int duration, final int amplifier) {
        return duration % 20 == 0;
    }
    
    public void func_76394_a(final LivingEntity entity, final int amplifier) {
        if (entity instanceof Player && !entity.func_130014_f_().func_201670_d() && entity.func_130014_f_() instanceof ServerLevel && !((MinecraftServer)ServerLifecycleHooks.getCurrentServer()).func_71219_W()) {
            return;
        }
        DamageUtil.shotgunAttack(entity, e -> DamageUtil.attackEntityFrom((Entity)e, CommonProxy.DAMAGE_SOURCE_BLEED, 0.5f * (amplifier + 1)));
    }
    
    @Override
    public SpriteQuery getSpriteQuery() {
        return new SpriteQuery(AssetLoader.TextureLocation.GUI, 1, 1, new String[] { "effect", "bleed" });
    }
}
