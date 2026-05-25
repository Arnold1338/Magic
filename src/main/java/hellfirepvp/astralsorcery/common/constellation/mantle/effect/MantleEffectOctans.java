package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import hellfirepvp.astralsorcery.common.event.EventFlags;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.EquipmentSlot;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectOctans extends MantleEffect
{
    public static OctansConfig CONFIG;
    
    public MantleEffectOctans() {
        super(ConstellationsAS.octans);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener((Consumer)this::handleUnderwaterBreakSpeed);
        bus.addListener((Consumer)this::handleUnderwaterUnwavering);
    }
    
    @Override
    protected void tickServer(final Player player) {
        super.tickServer(player);
        if (player.func_208600_a((ITag)FluidTags.field_206959_a)) {
            if (player.func_70086_ai() < player.func_205010_bg() - 20) {
                player.func_70050_g(player.func_205010_bg());
            }
            player.heal(((Double)MantleEffectOctans.CONFIG.healPerTick.get()).floatValue());
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        float chance = 0.1f;
        if (player.func_208600_a((ITag)FluidTags.field_206959_a)) {
            chance = 0.3f;
        }
        this.playCapeSparkles(player, chance);
    }
    
    private void handleUnderwaterBreakSpeed(final PlayerEvent.BreakSpeed event) {
        final Player player = event.getPlayer();
        if (player.func_208600_a((ITag)FluidTags.field_206959_a) && !EnchantmentHelper.func_185287_i((LivingEntity)player)) {
            final LogicalSide side = player.level() ? LogicalSide.CLIENT : LogicalSide.SERVER;
            final MantleEffectOctans octans = ItemMantle.getEffect((LivingEntity)player, ConstellationsAS.octans);
            if (octans != null && AlignmentChargeHandler.INSTANCE.hasCharge(player, side, (float)(int)MantleEffectOctans.CONFIG.chargeCostPerBreakSpeed.get())) {
                final ItemStack existing = player.getItemBySlot(EquipmentSlot.HEAD);
                final ItemStack st = new ItemStack((ItemLike)Items.field_151024_Q);
                st.func_77966_a(Enchantments.field_185299_g, 1);
                player.getInventory().field_70460_b.set(EquipmentSlot.HEAD.func_188454_b(), (Object)st);
                EventFlags.CHECK_UNDERWATER_BREAK_SPEED.executeWithFlag(() -> {
                    event.setNewSpeed(player.getDigSpeed(event.getState(), event.getPos()));
                    AlignmentChargeHandler.INSTANCE.drainCharge(player, side, (float)(int)MantleEffectOctans.CONFIG.chargeCostPerBreakSpeed.get(), false);
                    return;
                });
                player.getInventory().field_70460_b.set(EquipmentSlot.HEAD.func_188454_b(), (Object)existing);
            }
        }
    }
    
    private void handleUnderwaterUnwavering(final LivingKnockBackEvent event) {
        if (event.getEntityLiving().func_208600_a((ITag)FluidTags.field_206959_a)) {
            final MantleEffectOctans octans = ItemMantle.getEffect(event.getEntityLiving(), ConstellationsAS.octans);
            if (octans != null) {
                event.setCanceled(true);
            }
        }
    }
    
    public static boolean shouldPreventWaterSlowdown(final ItemStack elytraStack, final LivingEntity wearingEntity) {
        if (elytraStack.getItem() instanceof ItemMantle) {
            final MantleEffect effect = ItemMantle.getEffect(wearingEntity, ConstellationsAS.octans);
            return effect != null;
        }
        return false;
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectOctans.CONFIG;
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    static {
        MantleEffectOctans.CONFIG = new OctansConfig();
    }
    
    public static class OctansConfig extends Config
    {
        private final double defaultHealPerTick = 0.009999999776482582;
        private final int defaultChargeCostPerBreakSpeed = 30;
        public ForgeConfigSpec.DoubleValue healPerTick;
        public ForgeConfigSpec.IntValue chargeCostPerBreakSpeed;
        
        public OctansConfig() {
            super("octans");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the amount of health that is healed while the wearer is in water. Can be set to 0 to disable this.").translation(this.translationKey("healPerTick"));
            final String s = "healPerTick";
            this.getClass();
            this.healPerTick = translation.defineInRange(s, 0.009999999776482582, 0.0, 5.0);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Set the amount alignment charge consumed per accelerated underwater block breaking").translation(this.translationKey("chargeCostPerBreakSpeed"));
            final String s2 = "chargeCostPerBreakSpeed";
            this.getClass();
            this.chargeCostPerBreakSpeed = translation2.defineInRange(s2, 30, 0, 1000);
        }
    }
}
