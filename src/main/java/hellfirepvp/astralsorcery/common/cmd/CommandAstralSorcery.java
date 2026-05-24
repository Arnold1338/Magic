package hellfirepvp.astralsorcery.common.cmd;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.CommandNode;
import hellfirepvp.astralsorcery.common.cmd.sub.CommandSerialize;
import hellfirepvp.astralsorcery.common.cmd.sub.CommandProgress;
import hellfirepvp.astralsorcery.common.cmd.sub.CommandReset;
import hellfirepvp.astralsorcery.common.cmd.sub.CommandMaximizeAll;
import hellfirepvp.astralsorcery.common.cmd.sub.CommandExp;
import hellfirepvp.astralsorcery.common.cmd.sub.CommandConstellation;
import com.mojang.brigadier.builder.ArgumentBuilder;
import hellfirepvp.astralsorcery.common.cmd.sub.CommandAttune;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.CommandDispatcher;

public class CommandAstralSorcery
{
    public static void register(final CommandDispatcher<CommandSourceStack> dispatcher) {
        final LiteralCommandNode<CommandSourceStack> cmdAstralSorcery = (LiteralCommandNode<CommandSourceStack>)dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.func_197057_a("as").then((ArgumentBuilder)CommandAttune.register())).then((ArgumentBuilder)CommandConstellation.register())).then((ArgumentBuilder)CommandExp.register())).then((ArgumentBuilder)CommandMaximizeAll.register())).then((ArgumentBuilder)CommandReset.register())).then((ArgumentBuilder)CommandProgress.register())).then((ArgumentBuilder)CommandSerialize.register()));
        dispatcher.register((LiteralArgumentBuilder)Commands.func_197057_a("astralsorcery").redirect((CommandNode)cmdAstralSorcery));
    }
}
