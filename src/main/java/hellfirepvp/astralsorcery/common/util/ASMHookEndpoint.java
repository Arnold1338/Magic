package hellfirepvp.astralsorcery.common.util;

import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyEntityReach;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ASMHookEndpoint
{
    public static double getOverriddenSeenEntityReachMaximum(final ServerPlayNetHandler handler, final double original) {
        final Player player = (Player)handler.field_147369_b;
        final PlayerProgress prog = ResearchHelper.getProgress(player, player.level().level() ? LogicalSide.CLIENT : LogicalSide.SERVER);
        if (prog.isValid() && prog.getPerkData().hasPerkEffect(perk -> perk instanceof KeyEntityReach)) {
            return 9.99999999E8;
        }
        return original;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static double getOverriddenCreativeEntityReach(final double defaultExtendedReach) {
        final PlayerProgress prog = ResearchHelper.getProgress((Player)Minecraft.getInstance().player, LogicalSide.CLIENT);
        if (prog.isValid() && prog.getPerkData().hasPerkEffect(perk -> perk instanceof KeyEntityReach)) {
            return Math.max(defaultExtendedReach, Minecraft.getInstance().gameMode.func_78757_d());
        }
        return defaultExtendedReach;
    }
}
