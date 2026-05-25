package hellfirepvp.astralsorcery.common.item.crystal;

import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.data.research.GatedKnowledge;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.constellation.ConstellationItem;
import hellfirepvp.astralsorcery.common.item.base.IConstellationFocus;

public abstract class ItemAttunedCrystalBase extends ItemCrystalBase implements IConstellationFocus, ConstellationItem
{
    public ItemAttunedCrystalBase(final Item.Properties prop) {
        super(prop);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void func_77624_a(final ItemStack stack, @Nullable final Level world, final List<Component> toolTip, final TooltipFlag flag) {
        final CrystalAttributes.TooltipResult result = this.addCrystalPropertyToolTip(stack, toolTip);
        if (result != null) {
            final ProgressionTier tier = ResearchHelper.getClientProgress().getTierReached();
            boolean addedMissing = result != CrystalAttributes.TooltipResult.ADDED_ALL;
            final IWeakConstellation c = this.getAttunedConstellation(stack);
            if (c != null) {
                if (GatedKnowledge.CRYSTAL_TUNE.canSee(tier) && ResearchHelper.getClientProgress().hasConstellationDiscovered(c)) {
                    toolTip.add((Component)new Component("crystal.info.astralsorcery.attuned", new Object[] { c.getConstellationName().withStyle(ChatFormatting.BLUE)) }).withStyle(ChatFormatting.GRAY)));
                }
                else if (!addedMissing) {
                    toolTip.add((Component)new Component("astralsorcery.progress.missing.knowledge").withStyle(ChatFormatting.GRAY)));
                    addedMissing = true;
                }
            }
            final IMinorConstellation tr = this.getTraitConstellation(stack);
            if (tr != null) {
                if (GatedKnowledge.CRYSTAL_TUNE.canSee(tier) && ResearchHelper.getClientProgress().hasConstellationDiscovered(tr)) {
                    toolTip.add((Component)new Component("crystal.info.astralsorcery.trait", new Object[] { tr.getConstellationName().withStyle(ChatFormatting.BLUE)) }).withStyle(ChatFormatting.GRAY)));
                }
                else if (!addedMissing) {
                    toolTip.add((Component)new Component("astralsorcery.progress.missing.knowledge").withStyle(ChatFormatting.GRAY)));
                }
            }
        }
    }
    
    public Component func_200295_i(final ItemStack stack) {
        final IWeakConstellation cst = this.getAttunedConstellation(stack);
        if (cst != null) {
            return (Component)new Component(super.func_77667_c(stack) + ".typed", new Object[] { cst.getConstellationName() });
        }
        return super.func_200295_i(stack);
    }
    
    @Nullable
    @Override
    public IConstellation getFocusConstellation(final ItemStack stack) {
        return this.getAttunedConstellation(stack);
    }
    
    @Nullable
    @Override
    public IWeakConstellation getAttunedConstellation(final ItemStack stack) {
        return (IWeakConstellation)IConstellation.readFromNBT(NBTHelper.getPersistentData(stack));
    }
    
    @Override
    public boolean setAttunedConstellation(final ItemStack stack, @Nullable final IWeakConstellation cst) {
        if (cst != null) {
            cst.writeToNBT(NBTHelper.getPersistentData(stack));
        }
        else {
            NBTHelper.getPersistentData(stack).func_82580_o(IConstellation.getDefaultSaveKey());
        }
        return true;
    }
    
    @Nullable
    @Override
    public IMinorConstellation getTraitConstellation(final ItemStack stack) {
        return (IMinorConstellation)IConstellation.readFromNBT(NBTHelper.getPersistentData(stack), "constellationTrait");
    }
    
    @Override
    public boolean setTraitConstellation(final ItemStack stack, @Nullable final IMinorConstellation cst) {
        if (cst != null) {
            cst.writeToNBT(NBTHelper.getPersistentData(stack), "constellationTrait");
        }
        else {
            NBTHelper.getPersistentData(stack).func_82580_o("constellationTrait");
        }
        return true;
    }
}
