package hellfirepvp.astralsorcery.common.effect;

import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import java.util.Iterator;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.level.effect.MobEffect;
import hellfirepvp.astralsorcery.common.lib.EffectsAS;
import net.minecraft.world.level.GameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import java.util.ArrayList;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraft.world.effect.EffectType;

public class EffectDropModifier extends EffectCustomTexture
{
    public EffectDropModifier() {
        super(EffectType.BENEFICIAL, ColorsAS.EFFECT_DROP_MODIFIER);
    }
    
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<ItemStack>(0);
    }
    
    @Override
    public void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener(EventPriority.HIGH, (Consumer)this::onDrops);
    }
    
    private void onDrops(final LivingDropsEvent event) {
        final LivingEntity le = event.getEntityLiving();
        if (le.func_130014_f_().func_201670_d() || !(le instanceof MobEntity) || !(le.func_130014_f_() instanceof ServerLevel) || !le.func_130014_f_().func_82736_K().func_223586_b(GameRules.field_223602_e)) {
            return;
        }
        if (le.func_70644_a((Effect)EffectsAS.EFFECT_DROP_MODIFIER)) {
            final DamageSource src = event.getSource();
            final int amplifier = le.func_184596_c((Effect)EffectsAS.EFFECT_DROP_MODIFIER).func_76458_c();
            if (amplifier == 0) {
                event.getDrops().clear();
            }
            else {
                for (int i = 0; i < amplifier; ++i) {
                    final List<ItemStack> loot = EntityUtils.generateLoot(le, EffectDropModifier.rand, src, event.isRecentlyHit() ? le.func_94060_bK() : null);
                    for (final ItemStack stack : loot) {
                        if (stack.isEmpty()) {
                            continue;
                        }
                        event.getDrops().add(le.func_199701_a_(stack));
                    }
                }
            }
        }
    }
    
    @Override
    public SpriteQuery getSpriteQuery() {
        return new SpriteQuery(AssetLoader.TextureLocation.GUI, 1, 1, new String[] { "effect", "drop_modifier" });
    }
}
