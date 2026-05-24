package hellfirepvp.astralsorcery.common.integration;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import com.blamejared.crafttweaker.api.CraftTweakerAPI;
import com.mojang.brigadier.context.CommandContext;
import com.blamejared.crafttweaker.impl.commands.CTCommandCollectionEvent;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;

public class IntegrationCraftTweaker
{
    public static void attachListeners(final IEventBus eventBus) {
        eventBus.addListener((Consumer)IntegrationCraftTweaker::onCommandCollection);
    }
    
    public static void onCommandCollection(final CTCommandCollectionEvent event) {
        event.registerDump("astralConstellations", "Lists the different Astral Sorcery Constellations", commandContext -> {
            CraftTweakerAPI.logDump("List of all known Astral Sorcery Constellations: ", new Object[0]);
            RegistriesAS.REGISTRY_CONSTELLATIONS.getKeys().forEach(resourceLocation -> {
                final IConstellation constellation = (IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(resourceLocation);
                CraftTweakerAPI.logDump("%s\tis weak: %s, is major: %s", new Object[] { resourceLocation.toString(), constellation instanceof IWeakConstellation, constellation instanceof IMajorConstellation });
                return;
            });
            final Component message = new Component(ChatFormatting.GREEN + "Constellations written to the log" + ChatFormatting.RESET);
            ((CommandSourceStack)commandContext.getSource()).func_197030_a((Component)message, true);
            return 0;
        });
        event.registerDump("astralAltarTypes", "Lists the different Astral Sorcery Altar Types", commandContext -> {
            CraftTweakerAPI.logDump("List of all known Astral Sorcery Altar Types: ", new Object[0]);
            for (final AltarType value : AltarType.values()) {
                CraftTweakerAPI.logDump(value.name(), new Object[0]);
            }
            final Component message = new Component(ChatFormatting.GREEN + "Altar Types written to the log" + ChatFormatting.RESET);
            ((CommandSourceStack)commandContext.getSource()).func_197030_a((Component)message, true);
            return 0;
        });
    }
}
