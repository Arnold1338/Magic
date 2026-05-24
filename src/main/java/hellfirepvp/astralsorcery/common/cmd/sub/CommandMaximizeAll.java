package hellfirepvp.astralsorcery.common.cmd.sub;

import net.minecraft.command.arguments.EntitySelector;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import com.mojang.brigadier.Command;

public class CommandMaximizeAll implements Command<CommandSource>
{
    private static final CommandMaximizeAll CMD;
    
    private CommandMaximizeAll() {
    }
    
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (ArgumentBuilder<CommandSource, ?>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.func_197057_a("maximize").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes(ctx -> {
            final Player target = (Player)((EntitySelector)ctx.getArgument("player", (Class)EntitySelector.class)).func_197340_a((CommandSource)ctx.getSource());
            ((CommandSource)ctx.getSource()).func_197030_a((Component)new Component("Success!").func_240699_a_(ChatFormatting.GREEN), true);
            maximizeAll(target);
            return 0;
        }))).executes((Command)CommandMaximizeAll.CMD);
    }
    
    public int run(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        maximizeAll((Player)((CommandSource)context.getSource()).func_197035_h());
        ((CommandSource)context.getSource()).func_197030_a((Component)new Component("Success!").func_240699_a_(ChatFormatting.GREEN), true);
        return 0;
    }
    
    private static boolean maximizeAll(final Player entity) {
        return ResearchManager.forceMaximizeAll(entity);
    }
    
    static {
        CMD = new CommandMaximizeAll();
    }
}
