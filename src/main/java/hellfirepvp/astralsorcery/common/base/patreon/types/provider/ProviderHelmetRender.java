package hellfirepvp.astralsorcery.common.base.patreon.types.provider;

import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.item.Items;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.item.Item;
import java.util.List;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeHelmetRender;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectProvider;

public class ProviderHelmetRender implements PatreonEffectProvider<TypeHelmetRender>
{
    @Override
    public TypeHelmetRender buildEffect(final UUID playerUUID, final List<String> effectParameters) throws Exception {
        final UUID effectUniqueId = UUID.fromString(effectParameters.get(0));
        if (effectParameters.get(1).equals("astralsorcery:blockaltar;3")) {
            effectParameters.set(1, "astralsorcery:altar_radiance");
        }
        final String[] itemInfo = effectParameters.get(1).split(";");
        final Item item = (Item)ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemInfo[0]));
        if (item == null || item == Items.field_190931_a) {
            throw new IllegalArgumentException("Unknown item: " + itemInfo[0]);
        }
        final ItemStack stack = new ItemStack((ItemLike)item);
        if (itemInfo.length > 1) {
            final int data = Integer.parseInt(itemInfo[1]);
            stack.setDamageValue(data);
        }
        final FlareColor flColor = (effectParameters.size() > 2) ? FlareColor.valueOf(effectParameters.get(2)) : null;
        return new TypeHelmetRender(effectUniqueId, flColor, playerUUID, stack);
    }
}
