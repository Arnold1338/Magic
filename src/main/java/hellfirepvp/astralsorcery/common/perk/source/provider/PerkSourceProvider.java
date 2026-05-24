package hellfirepvp.astralsorcery.common.perk.source.provider;

import hellfirepvp.astralsorcery.common.perk.source.ModifierSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.perk.source.ModifierManager;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.perk.source.ModifierSourceProvider;

public class PerkSourceProvider extends ModifierSourceProvider<AbstractPerk>
{
    public PerkSourceProvider() {
        super(ModifierManager.PERK_PROVIDER_KEY);
    }
    
    @Override
    protected void update(final ServerPlayer playerEntity) {
    }
    
    @Override
    protected void removeModifiers(final ServerPlayer playerEntity) {
    }
    
    @Override
    public void serialize(final AbstractPerk source, final FriendlyByteBuf buf) {
        ByteBufUtils.writeResourceLocation(buf, source.getRegistryName());
    }
    
    @Override
    public AbstractPerk deserialize(final FriendlyByteBuf buf) {
        final ResourceLocation perkKey = ByteBufUtils.readResourceLocation(buf);
        return PerkTree.PERK_TREE.getPerk(LogicalSide.CLIENT, perkKey).orElse(null);
    }
}
