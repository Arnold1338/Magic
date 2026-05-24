package hellfirepvp.astralsorcery.common.perk.reader;

import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;

public class PerkStatistic
{
    private final PerkAttributeType type;
    private final String unlocPerkTypeName;
    private final String perkValue;
    private final String suffix;
    private final String postProcessInfo;
    
    public PerkStatistic(final PerkAttributeType type, final String perkValue, final String suffix, final String postProcessInfo) {
        this.type = type;
        this.unlocPerkTypeName = type.getUnlocalizedName();
        this.perkValue = perkValue;
        this.suffix = suffix;
        this.postProcessInfo = postProcessInfo;
    }
    
    public PerkAttributeType getType() {
        return this.type;
    }
    
    public String getUnlocPerkTypeName() {
        return this.unlocPerkTypeName;
    }
    
    public String getPerkValue() {
        return this.perkValue;
    }
    
    public String getSuffix() {
        return this.suffix;
    }
    
    public String getPostProcessInfo() {
        return this.postProcessInfo;
    }
}
