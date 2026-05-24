package hellfirepvp.astralsorcery.common.effect;

import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import java.util.Iterator;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.level.effect.MobEffects;
import net.minecraft.world.level.effect.MobEffect;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import java.util.ArrayList;
import net.minecraft.world.level.item.ItemStack;
import java.util.List;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.effect.EffectType;

public class EffectCheatDeath extends EffectCustomTexture
{
    public EffectCheatDeath() {
        super(EffectType.BENEFICIAL, ColorsAS.EFFECT_CHEAT_DEATH);
    }
    
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<ItemStack>(0);
    }
    
    @Override
    public void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener(EventPriority.LOW, (Consumer)this::onDeath);
    }
    
    private void onDeath(final LivingDeathEvent event) {
        final LivingEntity le = event.getEntityLiving();
        if (!le.func_130014_f_().func_201670_d() && le.func_70644_a((Effect)EffectsAS.EFFECT_CHEAT_DEATH)) {
            event.setCanceled(true);
            final int level = le.func_184596_c((Effect)EffectsAS.EFFECT_CHEAT_DEATH).func_76458_c();
            le.func_70606_j(Math.min(le.func_110138_aP(), (float)(4 + level * 2)));
            le.func_195064_c(new MobEffectInstance(Effects.field_76428_l, 200, 2, false, false, true));
            le.func_195064_c(new MobEffectInstance(Effects.field_76426_n, 500, 1, false, false, true));
            final List<LivingEntity> others = le.func_130014_f_().func_175647_a((Class)LivingEntity.class, le.func_174813_aQ().func_186662_g(3.0), e -> e.isAlive() && e != le);
            for (final LivingEntity lb : others) {
                lb.func_70015_d(10);
                lb.func_233627_a_(2.0f, lb.func_226277_ct_() - le.func_226277_ct_(), lb.func_226281_cx_() - le.func_226281_cx_());
            }
        }
    }
    
    @Override
    public SpriteQuery getSpriteQuery() {
        return new SpriteQuery(AssetLoader.TextureLocation.GUI, 1, 1, new String[] { "effect", "cheat_death" });
    }
}
