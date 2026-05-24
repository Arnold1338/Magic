package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.lib.GameRulesAS;
import net.minecraft.world.level.GameRules;

public class RegistryGameRules
{
    private RegistryGameRules() {
    }
    
    public static void init() {
        GameRulesAS.IGNORE_SKYLIGHT_CHECK_RULE = (GameRules.RuleKey<GameRules.BooleanValue>)GameRules.func_234903_a_("asIgnoreSkylightCheck", GameRules.Category.UPDATES, GameRules.BooleanValue.func_223568_b(false));
    }
}
