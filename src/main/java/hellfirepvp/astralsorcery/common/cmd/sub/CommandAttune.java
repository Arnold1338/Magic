package hellfirepvp.astralsorcery.common.cmd.sub;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.world.entity.player.Player;
import com.mojang.brigadier.context.CommandContext;
import hellfirepvp.astralsorcery.common.cmd.argument.ArgumentTypeConstellation;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import com.mojang.brigadier.Command;

public class CommandAttune implements Command<CommandSource>
{
    private static final CommandAttune CMD;
    
    private CommandAttune() {
    }
    
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (ArgumentBuilder<CommandSource, ?>)((LiteralArgumentBuilder)Commands.func_197057_a("attune").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).then(Commands.func_197056_a("constellation", (ArgumentType)ArgumentTypeConstellation.major()).executes((Command)CommandAttune.CMD)));
    }
    
    public int run(final CommandContext<CommandSource> context) throws CommandSyntaxException {
        final Player player = (Player)((EntitySelector)context.getArgument("player", (Class)EntitySelector.class)).func_197340_a((CommandSource)context.getSource());
        final IMajorConstellation cst = (IMajorConstellation)context.getArgument("constellation", (Class)IConstellation.class);
        if (ResearchManager.setAttunedConstellation(player, cst)) {
            ((CommandSource)context.getSource()).func_197030_a((Component)new Component("Success! Player has been attuned to ").func_230529_a_((Component)cst.getConstellationName().func_240699_a_(ChatFormatting.BLUE)).func_240699_a_(ChatFormatting.GREEN), true);
        }
        else {
            ((CommandSource)context.getSource()).func_197030_a((Component)new Component("Failed! Player specified doesn't seem to have the research progress necessary!").func_240699_a_(ChatFormatting.RED), true);
        }
        return 0;
    }
    
    static {
        CMD = new CommandAttune();
    }
}
