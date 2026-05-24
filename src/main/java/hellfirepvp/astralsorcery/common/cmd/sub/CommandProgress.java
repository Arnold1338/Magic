package hellfirepvp.astralsorcery.common.cmd.sub;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.context.CommandContext;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.server.command.EnumArgument;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import com.mojang.brigadier.builder.ArgumentBuilder;

public class CommandProgress
{
    private CommandProgress() {
    }
    
    public static ArgumentBuilder<CommandSourceStack, ?> register() {
        return (ArgumentBuilder<CommandSourceStack, ?>)((LiteralArgumentBuilder)Commands.func_197057_a("progress").requires(cs -> cs.func_197034_c(2))).then(Commands.func_197056_a("player", (ArgumentType)EntityArgument.func_197096_c()).then(Commands.func_197056_a("progress", (ArgumentType)EnumArgument.enumArgument((Class)ProgressionTier.class)).executes(ctx -> {
            final Player src = (Player)((CommandSourceStack)ctx.getSource()).func_197035_h();
            final Player target = (Player)EntityArgument.func_197089_d(ctx, "player");
            final ProgressionTier goal = (ProgressionTier)ctx.getArgument("progress", (Class)ProgressionTier.class);
            return pushPlayerToProgress((CommandSource)src, target, goal);
        })));
    }
    
    private static int pushPlayerToProgress(final CommandSource src, final Player target, final ProgressionTier goal) {
        final Component targetName = target.func_145748_c_();
        final PlayerProgress progress = ResearchHelper.getProgress(target, LogicalSide.SERVER);
        if (!progress.isValid() || progress.getTierReached().isThisLaterOrEqual(goal)) {
            src.func_145747_a((Component)new Component("Failed! ").func_230529_a_(targetName).func_240702_b_("'s progress is higher or equal to ").func_240702_b_(goal.name()).func_240699_a_(ChatFormatting.RED), Util.NIL_UUID);
            return 0;
        }
        ResearchProgression research = null;
        switch (goal) {
            case DISCOVERY: {
                research = ResearchProgression.DISCOVERY;
                break;
            }
            case BASIC_CRAFT: {
                research = ResearchProgression.BASIC_CRAFT;
                break;
            }
            case ATTUNEMENT: {
                research = ResearchProgression.ATTUNEMENT;
                break;
            }
            case CONSTELLATION_CRAFT: {
                research = ResearchProgression.CONSTELLATION;
                break;
            }
            case TRAIT_CRAFT: {
                research = ResearchProgression.RADIANCE;
                break;
            }
            case BRILLIANCE: {
                research = ResearchProgression.BRILLIANCE;
                break;
            }
        }
        if (research == null) {
            src.func_145747_a((Component)new Component("Invalid progression tier: " + goal.name()).func_240699_a_(ChatFormatting.RED), Util.NIL_UUID);
        }
        if (ResearchManager.grantProgress(target, goal) && ResearchManager.grantResearch(target, research)) {
            src.func_145747_a((Component)new Component("Success!").func_240699_a_(ChatFormatting.GREEN), Util.NIL_UUID);
            return 1;
        }
        src.func_145747_a((Component)new Component("Failed!").func_240699_a_(ChatFormatting.RED), Util.NIL_UUID);
        return 0;
    }
}
