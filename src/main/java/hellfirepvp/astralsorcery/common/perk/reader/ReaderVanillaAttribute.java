package hellfirepvp.astralsorcery.common.perk.reader;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeLimiter;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeMap;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.fml.RegistryObject;

public class ReaderVanillaAttribute extends PerkAttributeReader
{
    protected final RegistryObject<Attribute> attribute;
    protected boolean formatAsDecimal;
    
    public ReaderVanillaAttribute(final PerkAttributeType type, final RegistryObject<Attribute> reference) {
        super(type);
        this.formatAsDecimal = false;
        this.attribute = reference;
    }
    
    public <T extends ReaderVanillaAttribute> T formatAsDecimal() {
        this.formatAsDecimal = true;
        return (T)this;
    }
    
    @Override
    public double getDefaultValue(final PerkAttributeMap statMap, final Player player, final LogicalSide side) {
        return player.func_110148_a((Attribute)this.attribute.get()).func_111125_b();
    }
    
    @Override
    public double getModifierValueForMode(final PerkAttributeMap statMap, final Player player, final LogicalSide side, final ModifierType mode) {
        return statMap.getModifier(player, ResearchHelper.getProgress(player, side), this.getType(), mode);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public PerkStatistic getStatistics(final PerkAttributeMap statMap, final Player player) {
        String limitStr = "";
        Double limit = null;
        if (PerkAttributeLimiter.hasLimit(this.getType())) {
            final Pair<Double, Double> limits = PerkAttributeLimiter.getLimit(this.getType());
            limit = (Double)limits.getRight();
            limitStr = I18n.func_135052_a("perk.reader.astralsorcery.limit.default", new Object[] { MathHelper.func_76128_c((double)limit) });
        }
        double value = this.getDefaultValue(statMap, player, LogicalSide.CLIENT);
        value = statMap.modifyValue(player, ResearchHelper.getProgress(player, LogicalSide.CLIENT), this.getType(), (float)value);
        String postProcess = "";
        final double post = AttributeEvent.postProcessVanilla(value, player.func_110148_a((Attribute)this.attribute.get()));
        if (Math.abs(value - post) > 1.0E-4 && (limit == null || Math.abs(post - limit) > 1.0E-4)) {
            if (Math.abs(post) >= 1.0E-4) {
                postProcess = I18n.func_135052_a("perk.reader.astralsorcery.postprocess.default", new Object[] { this.formatForDisplay(post) });
            }
            value = post;
        }
        return new PerkStatistic(this.getType(), this.formatForDisplay(value), limitStr, postProcess);
    }
    
    protected String formatForDisplay(final double value) {
        String valueStr;
        if (this.formatAsDecimal) {
            valueStr = PerkAttributeReader.formatDecimal(value);
        }
        else {
            valueStr = String.valueOf(MathHelper.func_76128_c(value));
        }
        return valueStr;
    }
}
