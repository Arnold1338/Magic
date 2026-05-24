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

public class ReaderPercentageAttribute extends PerkAttributeReader
{
    private double defaultValue;
    
    public ReaderPercentageAttribute(final PerkAttributeType type) {
        super(type);
        this.defaultValue = (type.isMultiplicative() ? 1.0 : 0.0);
    }
    
    public <T extends ReaderPercentageAttribute> T setDefaultValue(final float defaultValue) {
        if (!this.getType().isMultiplicative()) {
            this.defaultValue = defaultValue;
        }
        return (T)this;
    }
    
    @Override
    public double getDefaultValue(final PerkAttributeMap statMap, final Player player, final LogicalSide side) {
        return this.defaultValue;
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
            limitStr = I18n.func_135052_a("perk.reader.astralsorcery.limit.percent", new Object[] { MathHelper.func_76128_c(limit * 100.0) });
        }
        double value = statMap.modifyValue(player, ResearchHelper.getProgress(player, LogicalSide.CLIENT), this.getType(), (float)this.getDefaultValue(statMap, player, LogicalSide.CLIENT));
        String postProcess = "";
        final double postValue = AttributeEvent.postProcessModded(player, this.getType(), value);
        if (Math.abs(value - postValue) > 1.0E-4 && (limit == null || Math.abs(postValue - limit) > 1.0E-4)) {
            if (Math.abs(postValue) >= 1.0E-4) {
                postProcess = I18n.func_135052_a("perk.reader.astralsorcery.postprocess.default", new Object[] { this.formatForDisplay(postValue) });
            }
            value = postValue;
        }
        if (this.getType().isMultiplicative()) {
            --value;
        }
        return new PerkStatistic(this.getType(), this.formatForDisplay(value), limitStr, postProcess);
    }
    
    protected String formatForDisplay(double value) {
        value *= 100.0;
        return ((value >= 0.0) ? "+" : "") + PerkAttributeReader.formatDecimal(value) + "%";
    }
}
