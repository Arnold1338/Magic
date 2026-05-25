package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.function.Predicate;
import java.util.Objects;
import java.util.function.Function;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.List;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.constellation.ConstellationBaseItem;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.Inventory;
import net.minecraft.world.level.Container;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LecternBlock;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.item.ItemUseContext;
import hellfirepvp.astralsorcery.common.container.factory.ContainerTomeProvider;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.PerkExperienceRevealer;
import net.minecraft.world.item.Item;

public class ItemTome extends Item implements PerkExperienceRevealer
{
    public ItemTome() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public InteractionResult<ItemStack> use(final Level world, final Player player, final Hand hand) {
        if (world.level() && !player.isCrouching()) {
            AstralSorcery.getProxy().openGui(player, GuiType.TOME, new Object[0]);
        }
        else if (!world.level().isClientSide() && player.isCrouching() && hand == InteractionHand.MAIN_HAND && player instanceof ServerPlayer) {
            new ContainerTomeProvider(player.getItemInHand(hand), player.getInventory().field_70461_c).openFor((ServerPlayer)player);
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)player.getItemInHand(hand));
    }
    
    public InteractionResult func_195939_a(final ItemUseContext context) {
        final Level world = context.func_195991_k();
        final BlockState blockstate = world.getBlockState(context.func_195995_a());
        if (blockstate.getBlock() instanceof LecternBlock) {
            return LecternBlock.func_220151_a(world, context.func_195995_a(), blockstate, context.func_195996_i()) ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }
        return InteractionResult.PASS;
    }
    
    public static Container getTomeStorage(final ItemStack stack, final Player player) {
        final Inventory inventory = new Inventory(27);
        getStoredConstellations(stack, player).stream().map(cst -> {
            final ItemStack cstPaper = new ItemStack((ItemLike)ItemsAS.CONSTELLATION_PAPER);
            if (cstPaper.getItem() instanceof ConstellationBaseItem) {
                ((ConstellationBaseItem)cstPaper.getItem()).setConstellation(cstPaper, cst);
            }
            return cstPaper;
        }).forEach((Consumer<? super Object>)inventory::func_174894_a);
        return (Container)inventory;
    }
    
    public static List<IConstellation> getStoredConstellations(final ItemStack stack, final Player player) {
        final LinkedList<IConstellation> out = new LinkedList<IConstellation>();
        final PlayerProgress prog = ResearchHelper.getProgress(player, player.level() ? LogicalSide.CLIENT : LogicalSide.SERVER);
        if (prog.isValid()) {
            prog.getStoredConstellationPapers().stream().map((Function<? super Object, ?>)ConstellationRegistry::getConstellation).filter(Objects::nonNull).forEach(out::add);
        }
        return out;
    }
}
