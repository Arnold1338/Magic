package hellfirepvp.astralsorcery.common.perk.node;

import com.google.gson.JsonParseException;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreeConstellation;
import org.apache.commons.lang3.Validate;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierPerk;

public class ConstellationPerk extends AttributeModifierPerk
{
    private IConstellation constellation;
    
    public ConstellationPerk(final ResourceLocation name, final IConstellation cst, final float x, final float y) {
        super(name, x, y);
        this.setConstellation(cst);
    }
    
    public static ConstellationPerk convertToThis(final ResourceLocation perkKey, final float x, final float y) {
        return new ConstellationPerk(perkKey, null, x, y);
    }
    
    @Override
    protected PerkTreePoint<? extends ConstellationPerk> initPerkTreePoint() {
        Validate.notNull((Object)this.constellation);
        return new PerkTreeConstellation<ConstellationPerk>((ConstellationPerk)this, this.getOffset(), this.constellation, 40);
    }
    
    public void setConstellation(final IConstellation constellation) {
        Validate.isTrue(this.constellation == null);
        Validate.notNull((Object)constellation);
        this.constellation = constellation;
    }
    
    public IConstellation getConstellation() {
        Validate.notNull((Object)this.constellation);
        return this.constellation;
    }
    
    @Override
    public void deserializeData(final JsonObject perkData) {
        super.deserializeData(perkData);
        this.constellation = null;
        if (perkData.has("constellation")) {
            final String cstKey = JSONUtils.func_151200_h(perkData, "constellation");
            final IConstellation cst = ConstellationRegistry.getConstellation(new ResourceLocation(cstKey));
            if (cst == null) {
                throw new JsonParseException("Unknown constellation: " + cstKey);
            }
            this.setConstellation(cst);
        }
    }
    
    @Override
    public void serializeData(final JsonObject perkData) {
        super.serializeData(perkData);
        if (this.constellation != null) {
            perkData.addProperty("constellation", this.constellation.getRegistryName().toString());
        }
    }
}
