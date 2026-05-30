package hellfirepvp.astralsorcery.common.perk.reader;

import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeMap;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import java.text.DecimalFormat;


public abstract class PerkAttributeReader {
    private static final DecimalFormat percentageFormat;
    private final PerkAttributeType type;
    
    protected PerkAttributeReader(final PerkAttributeType type) {
        this.type = type;

    }
    
    public final PerkAttributeType getType() {
        return this.type;
    }
    
    @OnlyIn(Dist.CLIENT)
    public abstract PerkStatistic getStatistics(final PerkAttributeMap p0, final Player p1);
    
    public abstract double getDefaultValue(final PerkAttributeMap p0, final Player p1, final LogicalSide p2);
    
    public abstract double getModifierValueForMode(final PerkAttributeMap p0, final Player p1, final LogicalSide p2, final ModifierType p3);
    
    public static String formatDecimal(final double decimal) {
        return PerkAttributeReader.percentageFormat.format(decimal);
    }
    
    static {
        percentageFormat = new DecimalFormat("0.00");
    }
}
