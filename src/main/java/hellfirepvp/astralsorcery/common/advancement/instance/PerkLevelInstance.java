package hellfirepvp.astralsorcery.common.advancement.instance;

import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.JSONUtils;
import com.google.gson.JsonObject;
import net.minecraft.loot.ConditionArraySerializer;
import hellfirepvp.astralsorcery.common.advancement.PerkLevelTrigger;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.advancements.criterion.CriterionInstance;

public class PerkLevelInstance extends CriterionInstance
{
    private int levelNeeded;
    
    private PerkLevelInstance(final ResourceLocation criterionIn) {
        super(criterionIn, EntityPredicate.AndPredicate.field_234582_a_);
        this.levelNeeded = 0;
    }
    
    public static PerkLevelInstance reachLevel(final int level) {
        final PerkLevelInstance instance = new PerkLevelInstance(PerkLevelTrigger.ID);
        instance.levelNeeded = level;
        return instance;
    }
    
    public JsonObject func_230240_a_(final ConditionArraySerializer conditions) {
        final JsonObject out = super.func_230240_a_(conditions);
        out.addProperty("levelNeeded", (Number)this.levelNeeded);
        return out;
    }
    
    public static PerkLevelInstance deserialize(final ResourceLocation id, final JsonObject json) {
        final PerkLevelInstance instance = new PerkLevelInstance(id);
        instance.levelNeeded = JSONUtils.func_151203_m(json, "levelNeeded");
        return instance;
    }
    
    public boolean test(final ServerPlayer player) {
        return ResearchHelper.getProgress((Player)player, LogicalSide.SERVER).getPerkData().getPerkLevel((Player)player, LogicalSide.SERVER) >= this.levelNeeded;
    }
}
