package hellfirepvp.astralsorcery.common.cmd.sub;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.server.level.ServerPlayer;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import com.mojang.brigadier.Command;

public class CommandReset implements Command<CommandSource>
{
    private static final CommandReset CMD;
    
    private CommandReset() {
    }
    
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (ArgumentBuilder<CommandSource, ?>)((LiteralArgumentBuilder)Commands.func_197057_a("reset").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes((Command)CommandReset.CMD));
    }
    
    public int run(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final ServerPlayer player = (ServerPlayer)((EntitySelector)context.getArgument("player", (Class)EntitySelector.class)).func_197340_a((CommandSource)context.getSource());
        ResearchHelper.wipeKnowledge(player);
        final String name = player.func_146103_bH().getName();
        ((CommandSource)context.getSource()).func_197030_a((Component)new Component("Wiped " + name + "'s data!").func_240699_a_(ChatFormatting.GREEN), true);
        return 0;
    }
    
    static {
        CMD = new CommandReset();
    }
}
