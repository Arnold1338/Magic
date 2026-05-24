package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.level.Level;
import java.util.List;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.client.util.MiscPlayEffect;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.util.block.BlockDiscoverer;
import net.minecraft.world.level.block.AirBlock;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraftforge.event.level.BlockEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectMineralis extends MantleEffect
{
    public static MineralisConfig CONFIG;
    
    public MantleEffectMineralis() {
        super(ConstellationsAS.mineralis);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onBreak);
    }
    
    private void onBreak(final BlockEvent.BreakEvent event) {
        final Player player = event.getPlayer();
        if (ItemMantle.getEffect((LivingEntity)player, ConstellationsAS.mineralis) != null) {
            final LogicalSide side = player.func_130014_f_().func_201670_d() ? LogicalSide.CLIENT : LogicalSide.SERVER;
            if (side.isServer()) {
                final float charge = Math.min(AlignmentChargeHandler.INSTANCE.getCurrentCharge(player, side), (float)(int)MantleEffectMineralis.CONFIG.chargeCostPerBreak.get());
                AlignmentChargeHandler.INSTANCE.drainCharge(player, side, charge, false);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        this.playCapeSparkles(player, 0.15f);
        if (MantleEffectMineralis.rand.nextBoolean()) {
            this.playBlockHighlight(player);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playBlockHighlight(final Player player) {
        BlockState state = null;
        if (!player.func_184586_b(InteractionHand.MAIN_HAND).isEmpty()) {
            state = ItemUtils.createBlockState(player.func_184586_b(InteractionHand.MAIN_HAND));
        }
        if (!player.func_184586_b(InteractionHand.OFF_HAND).isEmpty()) {
            state = ItemUtils.createBlockState(player.func_184586_b(InteractionHand.OFF_HAND));
        }
        if (state == null || state.getBlock() instanceof AirBlock) {
            return;
        }
        final BlockState fState = state;
        final BlockPredicate search = (world, pos, foundState) -> foundState == fState;
        final List<BlockPos> positions = BlockDiscoverer.searchForBlocksAround(player.func_130014_f_(), player.func_233580_cy_(), (int)MantleEffectMineralis.CONFIG.highlightRange.get(), search);
        if (positions.isEmpty()) {
            return;
        }
        final int index = (positions.size() > 10) ? MantleEffectMineralis.rand.nextInt(positions.size()) : MantleEffectMineralis.rand.nextInt(10);
        if (index >= positions.size()) {
            return;
        }
        final BlockPos at = positions.get(index);
        final BlockState displayState = player.func_130014_f_().getBlockState(at);
        MiscPlayEffect.playSingleBlockTumbleDepthEffect(new Vector3((Vector3i)at).add(0.5, 0.5, 0.5), displayState);
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectMineralis.CONFIG;
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    static {
        MantleEffectMineralis.CONFIG = new MineralisConfig();
    }
    
    public static class MineralisConfig extends Config
    {
        private final int defaultHighlightRange = 10;
        private final int defaultChargeCostPerBreak = 2;
        public ForgeConfigSpec.IntValue highlightRange;
        public ForgeConfigSpec.IntValue chargeCostPerBreak;
        
        public MineralisConfig() {
            super("mineralis");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Sets the highlight radius in which the cape effect will search for the block you're holding. Set to 0 to disable this effect.").translation(this.translationKey("range"));
            final String s = "range";
            this.getClass();
            this.highlightRange = translation.defineInRange(s, 10, 0, 32);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Set the amount alignment charge consumed per block break enhanced by the mantle effect").translation(this.translationKey("chargeCostPerBreak"));
            final String s2 = "chargeCostPerBreak";
            this.getClass();
            this.chargeCostPerBreak = translation2.defineInRange(s2, 2, 0, 1000);
        }
    }
}
