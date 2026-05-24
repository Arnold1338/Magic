package hellfirepvp.astralsorcery.common.integration;

import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.Optional;
import java.util.function.Predicate;

public class IntegrationCurios {
    public static void initIMC() {
        // In 1.20.1, Curios slot registration is done via data packs or IMC
        InterModComms.sendTo(Mods.CURIOS.getModId(), SlotTypeMessage.REGISTER_TYPE,
            () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
    }

    public static Optional<SlotResult> getCurio(Player player, Predicate<ItemStack> match) {
        return CuriosApi.getCuriosInventory(player)
            .flatMap(handler -> handler.findFirstCurio(match).map(Optional::of).orElse(Optional.empty()));
    }
}
