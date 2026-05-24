package hellfirepvp.astralsorcery.common.perk.reader;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import hellfirepvp.astralsorcery.common.event.AttributeEvent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeLimiter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeBreakSpeed;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeMap;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;

public class ReaderBreakSpeed extends ReaderFlatAttribute
{
    public ReaderBreakSpeed(final PerkAttributeType type) {
        super(type, 1.0);
    }
    
    @Override
    public double getDefaultValue(final PerkAttributeMap statMap, final Player player, final LogicalSide side) {
        AttributeTypeBreakSpeed.evaluateBreakSpeedWithoutPerks = true;
        double speed;
        try {
            speed = player.getDigSpeed(Blocks.field_150347_e.defaultBlockState(), BlockPos.field_177992_a);
        }
        finally {
            AttributeTypeBreakSpeed.evaluateBreakSpeedWithoutPerks = false;
        }
        return speed;
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
        double value = player.getDigSpeed(Blocks.field_150347_e.defaultBlockState(), BlockPos.field_177992_a);
        String postProcess = "";
        final double post = AttributeEvent.postProcessModded(player, this.getType(), value);
        if (Math.abs(value - post) > 1.0E-4 && (limit == null || Math.abs(post - limit) > 1.0E-4)) {
            if (Math.abs(post) >= 1.0E-4) {
                postProcess = I18n.func_135052_a("perk.reader.astralsorcery.postprocess.default", new Object[] { ((post >= 0.0) ? "+" : "") + PerkAttributeReader.formatDecimal(post) });
            }
            value = post;
        }
        final String strOut = ((value >= 0.0) ? "+" : "") + PerkAttributeReader.formatDecimal(value);
        return new PerkStatistic(this.getType(), strOut, limitStr, postProcess);
    }
}
