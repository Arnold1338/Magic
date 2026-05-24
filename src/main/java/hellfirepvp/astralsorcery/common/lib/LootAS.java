package hellfirepvp.astralsorcery.common.lib;

import net.minecraft.loot.LootFunctionType;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.resources.ResourceLocation;

public class LootAS
{
    public static final ResourceLocation SHRINE_CHEST;
    public static final ResourceLocation STARFALL_SHOOTING_STAR_REWARD;
    
    private LootAS() {
    }
    
    static {
        SHRINE_CHEST = AstralSorcery.key("shrine_chest");
        STARFALL_SHOOTING_STAR_REWARD = AstralSorcery.key("gameplay/starfall/shooting_star");
    }
    
    public static class Functions
    {
        public static LootFunctionType LINEAR_LUCK_BONUS;
        public static LootFunctionType RANDOM_CRYSTAL_PROPERTIES;
        public static LootFunctionType COPY_CRYSTAL_PROPERTIES;
        public static LootFunctionType COPY_CONSTELLATION;
        public static LootFunctionType COPY_GATEWAY_COLOR;
    }
}
