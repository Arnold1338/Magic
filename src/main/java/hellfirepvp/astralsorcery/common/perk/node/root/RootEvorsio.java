package hellfirepvp.astralsorcery.common.perk.node.root;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.level.BlockEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.DiminishingMultiplier;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.RootPerk;

public class RootEvorsio extends RootPerk
{
    public static final Config CONFIG;
    
    public RootEvorsio(final ResourceLocation name, final float x, final float y) {
        super(name, RootEvorsio.CONFIG, ConstellationsAS.evorsio, x, y);
    }
    
    @Nonnull
    @Override
    protected DiminishingMultiplier createMultiplier() {
        return new DiminishingMultiplier(1000L, 0.1f, 0.005f, 0.15f);
    }
    
    @Override
    protected void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener(EventPriority.LOWEST, (Consumer)this::onBreak);
    }
    
    private void onBreak(final BlockEvent.BreakEvent event) {
        final Player player = event.getPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        if (!side.isServer()) {
            return;
        }
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (!prog.getPerkData().hasPerkEffect(this)) {
            return;
        }
        final BlockState broken = event.getState();
        final IWorld world = event.getWorld();
        float gainedExp;
        try {
            gainedExp = broken.func_185887_b((IBlockReader)world, event.getPos());
        }
        catch (final Exception exc) {
            gainedExp = 0.5f;
        }
        if (gainedExp < 0.0f) {
            return;
        }
        gainedExp *= (float)this.getExpMultiplier();
        gainedExp *= this.getDiminishingReturns(player);
        gainedExp *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
        gainedExp *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP);
        gainedExp = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP, gainedExp);
        ResearchManager.modifyExp(player, gainedExp);
    }
    
    static {
        CONFIG = new Config("root.evorsio");
    }
}
