package hellfirepvp.astralsorcery.common.item.quality;

import net.minecraft.network.chat.Component;
import java.util.Locale;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.ChatFormatting;

public enum GemQuality
{
    BROKEN(ChatFormatting.GRAY, 0.1f), 
    FLAWED(ChatFormatting.GRAY, 0.35f), 
    MUNDANE(ChatFormatting.WHITE, 0.5f), 
    CLEAR(ChatFormatting.AQUA, 0.6f), 
    FACETED(ChatFormatting.AQUA, 0.7f), 
    GLEAMING(ChatFormatting.GOLD, 0.8f), 
    FLAWLESS(ChatFormatting.GOLD, 1.0f);
    
    private final ChatFormatting color;
    private final float degree;
    
    private GemQuality(final ChatFormatting color, final float degree) {
        this.color = color;
        this.degree = degree;
    }
    
    public float getDegree() {
        return this.degree;
    }
    
    public MutableComponent getDisplayName() {
        return new Component("item.astralsorcery.gem_quality.%s", new Object[] { this.name().toLowerCase(Locale.ROOT) }).toString()this.color);
    }
}
