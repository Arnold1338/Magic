package hellfirepvp.astralsorcery.common.integration;

import net.minecraft.world.entity.LivingEntity;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import java.util.Optional;
import net.minecraft.world.item.ItemStack;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.SlotTypePreset;
import hellfirepvp.astralsorcery.common.base.Mods;

public class IntegrationCurios
{
    public static void initIMC() {
        InterModComms.sendTo(Mods.CURIOS.getModId(), "register_type", () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
    }
    
    public static Optional<SlotResult> getCurio(final Player player, final Predicate<ItemStack> match) {
        return CuriosApi.getCuriosHelper().findFirstCurio((Predicate)match, (LivingEntity)player);
    }
}
