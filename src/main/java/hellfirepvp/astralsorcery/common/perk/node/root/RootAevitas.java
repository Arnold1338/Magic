package hellfirepvp.astralsorcery.common.perk.node.root;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.level.BlockEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.DiminishingMultiplier;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.RootPerk;

public class RootAevitas extends RootPerk
{
    public static final Config CONFIG;
    
    public RootAevitas(final ResourceLocation name, final float x, final float y) {
        super(name, RootAevitas.CONFIG, ConstellationsAS.aevitas, x, y);
    }
    
    @Nonnull
    @Override
    protected DiminishingMultiplier createMultiplier() {
        return new DiminishingMultiplier(600L, 1.0f, 1.0f, 0.01f);
    }
    
    @Override
    protected void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        bus.addListener((Consumer)this::onPlace);
    }
    
    private void onPlace(final BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof Player)) {

        }
        final Player player = (Player)event.getEntity();
        final LogicalSide side = this.getSide((Entity)player);
        if (!side.isServer()) {

        }
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (!prog.getPerkData().hasPerkEffect(this)) {

        }
        float hardness;
        try {
            hardness = Math.max(event.getPlacedBlock().func_185887_b((IBlockReader)event.getWorld(), event.getPos()), 1.0f);
        }
        catch (final Exception exc) {
            hardness = 1.0f;
        }
        float xp = Math.min(hardness * 4.0f, 100.0f);
        xp *= (float)this.getExpMultiplier();
        xp *= this.getDiminishingReturns(player);
        xp *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
        xp *= PerkAttributeHelper.getOrCreateMap(player, side).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP);
        xp = AttributeEvent.postProcessModded(player, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EXP, xp);
        ResearchManager.modifyExp(player, xp);
    }
    
    static {
        CONFIG = new Config("root.aevitas");
    }
}
