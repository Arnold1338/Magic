package hellfirepvp.astralsorcery.common.perk.node.socket;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.IFormattableTextComponent;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.PerkAllocationType;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreeGem;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import hellfirepvp.astralsorcery.common.lib.PerkNamesAS;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.node.MajorPerk;

public class GemSocketMajorPerk extends MajorPerk implements GemSocketPerk
{
    public GemSocketMajorPerk(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.setName(PerkNamesAS.name("gem_socket"));
        this.disableTooltipCaching();
    }
    
    @Override
    protected PerkTreePoint<? extends GemSocketMajorPerk> initPerkTreePoint() {
        return new PerkTreeGem<GemSocketMajorPerk>((GemSocketMajorPerk)this, this.getOffset());
    }
    
    @Override
    public Collection<PerkAttributeModifier> getModifiers(final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        final Collection<PerkAttributeModifier> mods = super.getModifiers(player, side, ignoreRequirements);
        final ItemStack contained = this.getContainedItem(player, side);
        if (!contained.isEmpty() && contained.getItem() instanceof GemSocketItem) {
            mods.addAll(((GemSocketItem)contained.getItem()).getModifiers(contained, this, player, side));
        }
        return mods;
    }
    
    @Override
    public void onRemovePerkServer(final Player player, final PerkAllocationType allocationType, final PlayerProgress progress, final CompoundTag dataStorage) {
        super.onRemovePerkServer(player, allocationType, progress, dataStorage);
        if (progress.getPerkData().getAllocationTypes(this).size() <= 1) {
            this.dropItemToPlayer(player, dataStorage);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean addLocalizedTooltip(final Collection<IFormattableTextComponent> tooltip) {
        if (super.addLocalizedTooltip(tooltip)) {
            tooltip.add((IFormattableTextComponent)new Component(""));
        }
        if (this.canSeeClient()) {
            this.addTooltipInfo(tooltip);
        }
        return true;
    }
}
