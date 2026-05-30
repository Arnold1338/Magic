package hellfirepvp.astralsorcery.common.perk.type;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.event.CooldownSetEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;

public class AttributeTypeCooldown extends PerkAttributeType
{
    public AttributeTypeCooldown() {
        super(PerkAttributeTypesAS.KEY_ATTR_TYPE_COOLDOWN_REDUCTION, true);
    }
    
    @Override
    protected void attachListeners(final IEventBus eventBus) {
        super.attachListeners(eventBus);
        eventBus.addListener((Consumer)this::onCooldown);
    }
    
    private void onCooldown(final CooldownSetEvent event) {
        final Player player = event.getPlayer();
        final Level world = player.level();
        if (world.level()) {

        }
        final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!prog.isValid()) {

        }
        if (player instanceof ServerPlayer && MiscUtils.isPlayerFakeMP((ServerPlayer)player)) {

        }
        float multiplier = PerkAttributeHelper.getOrCreateMap(player, LogicalSide.SERVER).modifyValue(player, prog, this, 1.0f);
        --multiplier;
        multiplier = AttributeEvent.postProcessModded(player, this, multiplier);
        multiplier = 1.0f - Mth.canEnchant(multiplier, 0.0f, 1.0f);
        event.setCooldown(Math.round(event.getResultCooldown() * multiplier));
    }
}
