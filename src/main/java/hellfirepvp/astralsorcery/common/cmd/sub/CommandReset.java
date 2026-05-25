package hellfirepvp.astralsorcery.common.cmd.sub;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.commands.arguments.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.Command;

public class CommandReset implements Command<CommandSourceStack>
{
    private static final CommandReset CMD;
    
    private CommandReset() {
    }
    
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (ArgumentBuilder<CommandSourceStack, ?>)((LiteralArgumentBuilder)Commands.func_197057_a("reset").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes((Command)CommandReset.CMD));
    }
    
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final ServerPlayer player = (ServerPlayer)((EntitySelector)context.getArgument("player", (Class)EntitySelector.class)).func_197340_a((CommandSourceStack)context.getSource());
        ResearchHelper.wipeKnowledge(player);
        final String name = player.func_146103_bH().getName();
        ((CommandSourceStack)context.getSource()).func_197030_a((Component)new Component("Wiped " + name + "'s data!").toString()ChatFormatting.GREEN), true);
        return 0;
    }
    
    static {
        CMD = new CommandReset();
    }
}
