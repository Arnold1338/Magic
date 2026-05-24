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

public class ReaderAddedPercentage extends PerkAttributeReader
{
    protected float defaultValue;
    
    public ReaderAddedPercentage(final PerkAttributeType attribute) {
        super(attribute);
        this.defaultValue = (attribute.isMultiplicative() ? 1.0f : 0.0f);
    }
    
    public <T extends ReaderAddedPercentage> T setDefaultValue(final float defaultValue) {
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
        double value = statMap.getModifier(player, ResearchHelper.getProgress(player, side), this.getType(), mode);
        if (mode == ModifierType.ADDITION) {
            value /= 100.0;
            ++value;
        }
        return value;
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
        if (this.getType().isMultiplicative()) {
            --value;
        }
        String postProcess = "";
        final double postValue = AttributeEvent.postProcessModded(player, this.getType(), value);
        if (Math.abs(value - postValue) > 1.0E-4 && (limit == null || Math.abs(postValue - limit) > 1.0E-4)) {
            if (Math.abs(postValue) >= 1.0E-4) {
                postProcess = I18n.func_135052_a("perk.reader.astralsorcery.postprocess.default", new Object[] { ((postValue >= 0.0) ? "+" : "") + PerkAttributeReader.formatDecimal(postValue) + "%" });
            }
            value = postValue;
        }
        final String strOut = ((value >= 0.0) ? "+" : "") + PerkAttributeReader.formatDecimal(value) + "%";
        return new PerkStatistic(this.getType(), strOut, limitStr, postProcess);
    }
}
