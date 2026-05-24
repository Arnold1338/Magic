package hellfirepvp.astralsorcery.common.cmd.sub;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.command.ICommandSource;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.command.arguments.EntityArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import hellfirepvp.astralsorcery.common.cmd.argument.ArgumentTypeConstellation;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.command.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import com.mojang.brigadier.builder.ArgumentBuilder;

public class CommandConstellation
{
    private CommandConstellation() {
    }
    
    public static ArgumentBuilder<CommandSource, ?> register() {
        return (ArgumentBuilder<CommandSource, ?>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.func_197057_a("constellation").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197057_a("memorize").then(((RequiredArgumentBuilder)Commands.func_197056_a("constellation", (ArgumentType)ArgumentTypeConstellation.any()).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes(ctx -> {
            final Player target = (Player)EntityArgument.func_197089_d(ctx, "player");
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return markConstellationMemorized((CommandSource)ctx.getSource(), target, cst);
        }))).executes(ctx -> {
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return markConstellationMemorized((CommandSource)ctx.getSource(), null, cst);
        })))).then(Commands.func_197057_a("discover").then(((RequiredArgumentBuilder)Commands.func_197056_a("constellation", (ArgumentType)ArgumentTypeConstellation.any()).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes(ctx -> {
            final Player target = (Player)EntityArgument.func_197089_d(ctx, "player");
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return discoverConstellation((CommandSource)ctx.getSource(), target, cst);
        }))).executes(ctx -> {
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return discoverConstellation((CommandSource)ctx.getSource(), null, cst);
        })));
    }
    
    private static int markConstellationMemorized(final CommandSource src, @Nullable Player target, final IConstellation cst) throws CommandSyntaxException {
        final Player source = (Player)src.func_197035_h();
        target = ((target != null) ? target : source);
        final Component targetName = target.func_145748_c_();
        final PlayerProgress progress = ResearchHelper.getProgress(target, LogicalSide.SERVER);
        if (!progress.isValid() || progress.hasSeenConstellation(cst)) {
            source.func_145747_a((Component)new Component("Failed! ").func_230529_a_(targetName).func_240702_b_(" has already seen ").func_230529_a_((Component)cst.getConstellationName()).func_240699_a_(ChatFormatting.RED), Util.NIL_UUID);
            return 0;
        }
        if (ResearchManager.memorizeConstellation(cst, target)) {
            ResearchHelper.sendConstellationMemorizationMessage((ICommandSource)target, progress, cst);
            source.func_145747_a((Component)new Component("Success! ").func_240699_a_(ChatFormatting.GREEN), Util.NIL_UUID);
            return 1;
        }
        source.func_145747_a((Component)new Component("Failed!").func_240699_a_(ChatFormatting.RED), Util.NIL_UUID);
        return 0;
    }
    
    private static int discoverConstellation(final CommandSource src, @Nullable Player target, final IConstellation cst) throws CommandSyntaxException {
        final Player source = (Player)src.func_197035_h();
        target = ((target != null) ? target : source);
        final Component targetName = target.func_145748_c_();
        final PlayerProgress progress = ResearchHelper.getProgress(target, LogicalSide.SERVER);
        if (!progress.isValid() || progress.hasConstellationDiscovered(cst)) {
            source.func_145747_a((Component)new Component("Failed! ").func_230529_a_(targetName).func_240702_b_(" has already discovered ").func_230529_a_((Component)cst.getConstellationName()).func_240699_a_(ChatFormatting.RED), Util.NIL_UUID);
            return 0;
        }
        if (ResearchManager.discoverConstellation(cst, target)) {
            ResearchHelper.sendConstellationDiscoveryMessage((ICommandSource)target, cst);
            source.func_145747_a((Component)new Component("Success! ").func_240699_a_(ChatFormatting.GREEN), Util.NIL_UUID);
            return 1;
        }
        source.func_145747_a((Component)new Component("Failed!").func_240699_a_(ChatFormatting.RED), Util.NIL_UUID);
        return 0;
    }
}
