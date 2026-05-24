package hellfirepvp.astralsorcery.common.tile.altar;

import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.Objects;
import net.minecraft.resources.ResourceLocation;

public class AltarCollectionCategory
{
    public static final AltarCollectionCategory HEIGHT;
    public static final AltarCollectionCategory FOSIC_FIELD;
    public static final AltarCollectionCategory RELAY;
    public static final AltarCollectionCategory FOCUSED_NETWORK;
    public static final AltarCollectionCategory CONDENSED_STARLIGHT;
    private final ResourceLocation key;
    
    public AltarCollectionCategory(final ResourceLocation key) {
        this.key = key;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final AltarCollectionCategory that = (AltarCollectionCategory)o;
        return Objects.equals(this.key, that.key);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.key);
    }
    
    static {
        HEIGHT = new AltarCollectionCategory(AstralSorcery.key("height"));
        FOSIC_FIELD = new AltarCollectionCategory(AstralSorcery.key("fosic_field"));
        RELAY = new AltarCollectionCategory(AstralSorcery.key("relay"));
        FOCUSED_NETWORK = new AltarCollectionCategory(AstralSorcery.key("focused_network"));
        CONDENSED_STARLIGHT = new AltarCollectionCategory(AstralSorcery.key("condensed_starlight"));
    }
}
