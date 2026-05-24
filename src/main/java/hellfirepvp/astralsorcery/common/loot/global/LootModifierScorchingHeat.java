package hellfirepvp.astralsorcery.common.loot.global;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import javax.annotation.Nonnull;
import java.util.Optional;
import net.minecraft.server.level.ServerLevel;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.level.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.item.enchantment.Enchantments;
import net.minecraft.world.level.item.BlockItem;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import hellfirepvp.astralsorcery.common.util.loot.LootUtil;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.level.storage.loot.LootContext;
import net.minecraft.world.level.item.ItemStack;
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
            final Optional<Tuple<ItemStack, Float>> furnaceResult = RecipeHelper.findSmeltingResult((World)context.func_202879_g(), stack);
            if (context.func_216033_a(LootContextParams.THIS_ENTITY)) {
                final Entity e = (Entity)context.getParamOrNull(LootContextParams.THIS_ENTITY);
                if (e instanceof Player) {
                    furnaceResult.ifPresent(result -> BasicEventHooks.firePlayerSmeltedEvent((Player)e, (ItemStack)result.func_76341_a()));
                }
            }
            furnaceResult.ifPresent(result -> {
                final ItemStack resultStack = (ItemStack)result.func_76341_a();
                final float resultExp = (float)result.func_76340_b();
                final ItemStack tool = (ItemStack)context.getParamOrNull(LootContextParams.TOOL);
                if (!tool.isEmpty() && !(resultStack.getItem() instanceof BlockItem)) {
                    final int silkTouch = EnchantmentHelper.func_77506_a(Enchantments.field_185306_r, tool);
                    if (silkTouch <= 0) {
                        int addedCount = 0;
                        final int fortuneLevel = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, tool);
                        if (fortuneLevel > 0) {
                            addedCount = Math.max(context.func_216032_b().nextInt(fortuneLevel + 2) - 1, 0);
                            resultStack.func_190920_e(resultStack.func_190916_E() * (addedCount + 1));
                        }
                        final float resultExp2 = resultExp * (addedCount + 1);
                        if (resultExp2 > 0.0f) {
                            int iExp = (int)resultExp2;
                            final float partialExp = resultExp2 - iExp;
                            if (partialExp > 0.0f && partialExp > context.func_216032_b().nextFloat()) {
                                ++iExp;
                            }
                            if (iExp >= 1) {
                                final Vec3 blockPos = (Vec3)context.getParamOrNull(LootParameters.field_237457_g_);
                                if (blockPos != null) {
                                    final ServerLevel world = context.func_202879_g();
                                    world.func_217376_c((Entity)new ExperienceOrbEntity((World)world, blockPos.func_82615_a(), blockPos.func_82617_b(), blockPos.func_82616_c(), iExp));
                                }
                            }
                        }
                    }
                }
                return;
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
