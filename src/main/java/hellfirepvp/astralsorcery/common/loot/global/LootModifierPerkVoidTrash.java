package hellfirepvp.astralsorcery.common.loot.global;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import javax.annotation.Nonnull;
import net.minecraft.world.item.Item;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.data.config.registry.OreItemRarityRegistry;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.perk.PerkAttributeHelper;
import hellfirepvp.astralsorcery.common.perk.PerkTree;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyVoidTrash;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.loot.LootUtil;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.world.level.storage.loot.predicates.ILootCondition;
import net.minecraftforge.common.loot.LootModifier;

public class LootModifierPerkVoidTrash extends LootModifier
{
    private LootModifierPerkVoidTrash(final ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    
    @Nonnull
    protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
        if (!LootUtil.doesContextFulfillSet(context, LootContextParamSets.ENTITY)) {
            return generatedLoot;
        }
        final Entity e = (Entity)context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(e instanceof Player)) {
            return generatedLoot;
        }
        final Player player = (Player)e;
        final PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!prog.isValid() || !prog.getPerkData().hasPerkEffect(perk -> perk instanceof KeyVoidTrash)) {
            return generatedLoot;
        }
        if (!PerkTree.PERK_TREE.getPerk(LogicalSide.SERVER, perk -> perk instanceof KeyVoidTrash).isPresent()) {
            return generatedLoot;
        }
        final double chance = KeyVoidTrash.CONFIG.getOreChance() * PerkAttributeHelper.getOrCreateMap(player, LogicalSide.SERVER).getModifier(player, prog, PerkAttributeTypesAS.ATTR_TYPE_INC_PERK_EFFECT);
        return generatedLoot.stream().filter(stack -> !stack.isEmpty()).map(result -> {
            if (KeyVoidTrash.CONFIG.isTrash(result)) {
                result = ItemStack.EMPTY;
                if (context.func_216032_b().nextFloat() < chance) {
                    final Item drop = OreItemRarityRegistry.VOID_TRASH_REWARD.getRandomItem(context.func_216032_b());
                    if (drop != null) {
                        result = new ItemStack((ItemLike)drop);
                    }
                }
            }
            return result;
        }).filter(stack -> !stack.isEmpty()).collect((Collector<? super Object, ?, List<ItemStack>>)Collectors.toList());
    }
    
    public static class Serializer extends GlobalLootModifierSerializer<LootModifierPerkVoidTrash>
    {
        public LootModifierPerkVoidTrash read(final ResourceLocation location, final JsonObject object, final ILootCondition[] lootConditions) {
            return new LootModifierPerkVoidTrash(lootConditions, null);
        }
        
        public JsonObject write(final LootModifierPerkVoidTrash instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
