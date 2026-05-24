package hellfirepvp.astralsorcery.common.cmd.sub;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.commands.arguments.EntitySelector;
import net.minecraft.world.entity.player.Player;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.Command;

public class CommandExp implements Command<CommandSourceStack>
{
    private static final CommandExp CMD;
    
    private CommandExp() {
    }
    
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (ArgumentBuilder<CommandSourceStack, ?>)((LiteralArgumentBuilder)Commands.func_197057_a("exp").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).then(Commands.func_197056_a("exp", (ArgumentType)LongArgumentType.longArg()).executes((Command)CommandExp.CMD)));
    }
    
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        final Player player = (Player)((EntitySelector)context.getArgument("player", (Class)EntitySelector.class)).func_197340_a((CommandSourceStack)context.getSource());
        final long exp = LongArgumentType.getLong((CommandContext)context, "exp");
        if (ResearchManager.setExp(player, exp)) {
            ((CommandSourceStack)context.getSource()).func_197030_a((Component)new Component("Success! Player exp has been set to " + exp).func_240699_a_(ChatFormatting.GREEN), true);
        }
        else {
            ((CommandSourceStack)context.getSource()).func_197030_a((Component)new Component("Failed! Player specified doesn't seem to have a research progress!").func_240699_a_(ChatFormatting.RED), true);
        }
        return 0;
    }
    
    static {
        CMD = new CommandExp();
    }
}
