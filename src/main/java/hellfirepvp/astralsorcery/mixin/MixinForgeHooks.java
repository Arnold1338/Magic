package hellfirepvp.astralsorcery.mixin;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyMagnetDrops;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import hellfirepvp.astralsorcery.common.util.loot.LootUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.level.storage.loot.LootContext;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.LogicalSide;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.theillusivec4.curios.api.CuriosApi;
import java.util.List;

@Mixin(ForgeHooks.class)
public class MixinForgeHooks {
    @Inject(method = "modifyLoot(Lnet/minecraft/resources/ResourceLocation;Ljava/util/List;Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/List;",
            at = @At("RETURN"), cancellable = true, remap = false)
    private static void runLootTeleportation(ResourceLocation lootTableId, List<ItemStack> generatedLoot,
                                              LootContext context, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> loot = cir.getReturnValue();
        if (!LootUtil.doesContextFulfillSet(context, LootContextParamSets.ENTITY)) return;
        Entity e = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        if (!(e instanceof Player player)) return;
        PlayerProgress prog = ResearchHelper.getProgress(player, LogicalSide.SERVER);
        if (!prog.isValid() || !prog.getPerkData().hasPerkEffect(perk -> perk instanceof KeyMagnetDrops)) return;
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        if (tool != null && tool.hasTag() && tool.getTag().contains("HasCuriosFortuneBonus")) {
            loot.removeIf(result -> ItemUtils.dropItemToPlayer(player, result).isEmpty());
        }
        // 1.20.1 Curios: getCuriosInventory replaces getCuriosHelper().getCuriosHandler()
        int curiosFortuneBonus = CuriosApi.getCuriosInventory(player)
            .map(h -> h.getFortuneLevel(null)).orElse(0);
        if (curiosFortuneBonus > 0) return;
        loot.removeIf(result -> ItemUtils.dropItemToPlayer(player, result).isEmpty());
    }
}
