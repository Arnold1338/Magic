package hellfirepvp.astralsorcery.common.cmd.sub;

import net.minecraft.commands.arguments.EntitySelector;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.Command;

public class CommandMaximizeAll implements Command<CommandSourceStack>
{
    private static final CommandMaximizeAll CMD;
    
    private CommandMaximizeAll() {
    }
    
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (ArgumentBuilder<CommandSourceStack, ?>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.func_197057_a("maximize").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes(ctx -> {
            final Player target = (Player)((EntitySelector)ctx.getArgument("player", (Class)EntitySelector.class)).func_197340_a((CommandSourceStack)ctx.getSource());
            ((CommandSourceStack)ctx.getSource()).func_197030_a((Component)new Component("Success!").withStyle(ChatFormatting.GREEN)), true);
            maximizeAll(target);
            return 0;
        }))).executes((Command)CommandMaximizeAll.CMD);
    }
    
    public int run(final CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        maximizeAll((Player)((CommandSourceStack)context.getSource()).func_197035_h());
        ((CommandSourceStack)context.getSource()).func_197030_a((Component)new Component("Success!").withStyle(ChatFormatting.GREEN)), true);
        return 0;
    }
    
    private static boolean maximizeAll(final Player entity) {
        return ResearchManager.forceMaximizeAll(entity);
    }
    
    static {
        CMD = new CommandMaximizeAll();
    }
}
