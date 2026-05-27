package hellfirepvp.astralsorcery.common.cmd.sub;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.commands.CommandSourceStack;
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
import net.minecraft.commands.arguments.EntityArgument;
import com.mojang.brigadier.arguments.ArgumentType;
import hellfirepvp.astralsorcery.common.cmd.argument.ArgumentTypeConstellation;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.builder.ArgumentBuilder;

public class CommandConstellation
{
    private CommandConstellation() {
    }
    
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (ArgumentBuilder<CommandSourceStack, ?>)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.func_197057_a("constellation").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197057_a("memorize").then(((RequiredArgumentBuilder)Commands.func_197056_a("constellation", (ArgumentType)ArgumentTypeConstellation.any()).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes(ctx -> {
            final Player target = (Player)EntityArgument.func_197089_d(ctx, "player");
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return markConstellationMemorized((CommandSourceStack)ctx.getSource(), target, cst);
        }))).executes(ctx -> {
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return markConstellationMemorized((CommandSourceStack)ctx.getSource(), null, cst);
        })))).then(Commands.func_197057_a("discover").then(((RequiredArgumentBuilder)Commands.func_197056_a("constellation", (ArgumentType)ArgumentTypeConstellation.any()).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).executes(ctx -> {
            final Player target = (Player)EntityArgument.func_197089_d(ctx, "player");
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return discoverConstellation((CommandSourceStack)ctx.getSource(), target, cst);
        }))).executes(ctx -> {
            final IConstellation cst = (IConstellation)ctx.getArgument("constellation", (Class)IConstellation.class);
            return discoverConstellation((CommandSourceStack)ctx.getSource(), null, cst);
        })));
    }
    
    private static int markConstellationMemorized(final CommandSourceStack src, @Nullable Player target, final IConstellation cst) throws CommandSyntaxException {
        final Player source = (Player)src.func_197035_h();
        target = ((target != null) ? target : source);
        final Component targetName = target.getDisplayName();
        final PlayerProgress progress = ResearchHelper.getProgress(target, LogicalSide.SERVER);
        if (!progress.isValid() || progress.hasSeenConstellation(cst)) {
            source.sendSuccess(() -> Component.translatable("Failed! ").append(targetName).append(" has already seen ").append(cst.getConstellationName()).withStyle(ChatFormatting.RED), false);
            return 0;
        }
        if (ResearchManager.memorizeConstellation(cst, target)) {
            ResearchHelper.sendConstellationMemorizationMessage((CommandSource)target, progress, cst);
            source.sendSuccess(() -> Component.translatable("Success! ").withStyle(ChatFormatting.GREEN));
            return 1;
        }
        source.sendSuccess(() -> Component.translatable("Failed!").withStyle(ChatFormatting.RED));
        return 0;
    }
    
    private static int discoverConstellation(final CommandSourceStack src, @Nullable Player target, final IConstellation cst) throws CommandSyntaxException {
        final Player source = (Player)src.func_197035_h();
        target = ((target != null) ? target : source);
        final Component targetName = target.getDisplayName();
        final PlayerProgress progress = ResearchHelper.getProgress(target, LogicalSide.SERVER);
        if (!progress.isValid() || progress.hasConstellationDiscovered(cst)) {
            source.sendSuccess(() -> Component.translatable("Failed! ").append(targetName).append(" has already discovered ").append(cst.getConstellationName()).withStyle(ChatFormatting.RED), false);
            return 0;
        }
        if (ResearchManager.discoverConstellation(cst, target)) {
            ResearchHelper.sendConstellationDiscoveryMessage((CommandSource)target, cst);
            source.sendSuccess(() -> Component.translatable("Success! ").withStyle(ChatFormatting.GREEN));
            return 1;
        }
        source.sendSuccess(() -> Component.translatable("Failed!").withStyle(ChatFormatting.RED));
        return 0;
    }
}
