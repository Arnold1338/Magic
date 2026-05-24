package hellfirepvp.astralsorcery.common.cmd.sub;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.phys.BlockHitResult;
import hellfirepvp.astralsorcery.common.util.block.BlockStateHelper;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.util.data.JsonHelper;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.builder.ArgumentBuilder;

public class CommandSerialize
{
    private CommandSerialize() {
    }
    
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (ArgumentBuilder<CommandSourceStack, ?>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.func_197057_a("serialize").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197057_a("hand").executes(CommandSerialize::serializeHand))).then(Commands.func_197057_a("look").executes(CommandSerialize::serializeLook));
    }
    
    private static int serializeHand(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final Player player = (Player)((CommandSourceStack)context.getSource()).func_197035_h();
        final ItemStack held = player.func_184614_ca();
        final String serialized = JsonHelper.serializeItemStack(held).toString();
        final MutableComponent msg = (MutableComponent)new Component(serialized);
        final Style s = Style.field_240709_b_.func_240712_a_(ChatFormatting.GREEN).func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_, (Object)new Component("Copy"))).func_240715_a_(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, serialized));
        msg.func_230530_a_(s);
        ((CommandSourceStack)context.getSource()).func_197030_a((Component)msg, true);
        return 1;
    }
    
    private static int serializeLook(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final Player player = (Player)((CommandSourceStack)context.getSource()).func_197035_h();
        final BlockHitResult result = MiscUtils.rayTraceLookBlock(player);
        final BlockState state = (result == null) ? Blocks.field_150350_a.defaultBlockState() : player.func_130014_f_().getBlockState(result.func_216350_a());
        final String serialized = BlockStateHelper.serialize(state);
        final MutableComponent msg = (MutableComponent)new Component(serialized);
        final Style s = Style.field_240709_b_.func_240712_a_(ChatFormatting.GREEN).func_240716_a_(new HoverEvent(HoverEvent.Action.field_230550_a_, (Object)new Component("Copy"))).func_240715_a_(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, serialized));
        msg.func_230530_a_(s);
        ((CommandSourceStack)context.getSource()).func_197030_a((Component)msg, true);
        return 1;
    }
}
