package hellfirepvp.astralsorcery.common.loot.global;


import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.BlockItem;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import hellfirepvp.astralsorcery.common.util.loot.LootUtil;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import net.minecraft.world.level.storage.loot.predicates.ILootCondition;
import net.minecraftforge.common.loot.LootModifier;

public class LootModifierScorchingHeat extends LootModifier
{
    private LootModifierScorchingHeat(final ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }
    
    @Nonnull
    protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
        if (!LootUtil.doesContextFulfillSet(context, LootContextParamSets.ENTITY)) {
            return generatedLoot;
        }
        return generatedLoot.stream().filter(stack -> !stack.isEmpty()).map(stack -> {
            final Optional<Tuple<ItemStack, Float>> furnaceResult = RecipeHelper.findSmeltingResult((Level)context.getLevel(), stack);
            if (context.hasParam(LootContextParams.THIS_ENTITY)) {
                final Entity e = (Entity)context.getParamOrNull(LootContextParams.THIS_ENTITY);
                if (e instanceof Player) {
                    furnaceResult.ifPresent(result -> BasicEventHooks.firePlayerSmeltedEvent((Player)e, (ItemStack)result.getA()));
                }
            }
            furnaceResult.ifPresent(result -> {
                final ItemStack resultStack = (ItemStack)result.getA();
                final float resultExp = (float)result.getB();
                final ItemStack tool = (ItemStack)context.getParamOrNull(LootContextParams.TOOL);
                if (!tool.isEmpty() && !(resultStack.getItem() instanceof BlockItem)) {
                    final int silkTouch = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool);
                    if (silkTouch <= 0) {
                        int addedCount = 0;
                        final int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
                        if (fortuneLevel > 0) {
                            addedCount = Math.max(context.getRandom().nextInt(fortuneLevel + 2) - 1, 0);
                            resultStack.setCount(resultStack.getCount() * (addedCount + 1));
                        }
                        final float resultExp2 = resultExp * (addedCount + 1);
                        if (resultExp2 > 0.0f) {
                            int iExp = (int)resultExp2;
                            final float partialExp = resultExp2 - iExp;
                            if (partialExp > 0.0f && partialExp > context.getRandom().nextFloat()) {
                                ++iExp;
                            }
                            if (iExp >= 1) {
                                final Vec3 blockPos = (Vec3)context.getParamOrNull(LootContextParams.LAST_DAMAGE_PLAYER);
                                if (blockPos != null) {
                                    final ServerLevel world = context.getLevel();
                                    world.addFreshEntity((Entity)new ExperienceOrbEntity((Level)world, blockPos.getX(), blockPos.getY(), blockPos.getZ(), iExp));
                                }
                            }
                        }
                    }
                }

            });
            return (ItemStack)furnaceResult.map((Function<? super Tuple<ItemStack, Float>, ? extends ItemStack>)Tuple::func_76341_a).orElse(stack);
        }).collect((Collector<? super Object, ?, List<ItemStack>>)Collectors.toList());
    }
    
    public static class Serializer extends GlobalLootModifierSerializer<LootModifierScorchingHeat>
    {
        public LootModifierScorchingHeat read(final ResourceLocation location, final JsonObject object, final ILootCondition[] lootConditions) {
            return new LootModifierScorchingHeat(lootConditions, null);
        }
        
        public JsonObject write(final LootModifierScorchingHeat instance) {
            return this.makeConditions(instance.conditions);
        }
    }
}
