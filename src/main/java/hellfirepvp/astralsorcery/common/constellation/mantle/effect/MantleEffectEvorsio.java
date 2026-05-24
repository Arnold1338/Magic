package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraftforge.event.level.BlockEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectEvorsio extends MantleEffect
{
    public static EvorsioConfig CONFIG;
    
    public MantleEffectEvorsio() {
        super(ConstellationsAS.evorsio);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onBreak);
    }
    
    private void onBreak(final BlockEvent.BreakEvent event) {
        final Player player = event.getPlayer();
        if (ItemMantle.getEffect((LivingEntity)player, ConstellationsAS.evorsio) != null) {
            final LogicalSide side = player.func_130014_f_().func_201670_d() ? LogicalSide.CLIENT : LogicalSide.SERVER;
            if (side.isServer()) {
                final float charge = Math.min(AlignmentChargeHandler.INSTANCE.getCurrentCharge(player, side), (float)(int)MantleEffectEvorsio.CONFIG.chargeCostPerBreak.get());
                AlignmentChargeHandler.INSTANCE.drainCharge(player, side, charge, false);
            }
        }
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        this.playCapeSparkles(player, 0.1f);
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectEvorsio.CONFIG;
    }
    
    static {
        MantleEffectEvorsio.CONFIG = new EvorsioConfig();
    }
    
    public static class EvorsioConfig extends Config
    {
        private final int defaultChargeCostPerBreak = 2;
        public ForgeConfigSpec.IntValue chargeCostPerBreak;
        
        public EvorsioConfig() {
            super("evorsio");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set the amount alignment charge consumed per block break enhanced by the mantle effect").translation(this.translationKey("chargeCostPerBreak"));
            final String s = "chargeCostPerBreak";
            this.getClass();
            this.chargeCostPerBreak = translation.defineInRange(s, 2, 0, 1000);
        }
    }
}
