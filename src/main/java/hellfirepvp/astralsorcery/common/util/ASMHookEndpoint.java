package hellfirepvp.astralsorcery.common.util;

import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyEntityReach;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class ASMHookEndpoint
{
    public static double getOverriddenSeenEntityReachMaximum(final ServerPlayNetHandler handler, final double original) {
        final Player player = (Player)handler.field_147369_b;
        final PlayerProgress prog = ResearchHelper.getProgress(player, player.func_130014_f_().func_201670_d() ? LogicalSide.CLIENT : LogicalSide.SERVER);
        if (prog.isValid() && prog.getPerkData().hasPerkEffect(perk -> perk instanceof KeyEntityReach)) {
            return 9.99999999E8;
        }
        return original;
    }
    
    @OnlyIn(Dist.CLIENT)
    public static double getOverriddenCreativeEntityReach(final double defaultExtendedReach) {
        final PlayerProgress prog = ResearchHelper.getProgress((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT);
        if (prog.isValid() && prog.getPerkData().hasPerkEffect(perk -> perk instanceof KeyEntityReach)) {
            return Math.max(defaultExtendedReach, Minecraft.func_71410_x().field_71442_b.func_78757_d());
        }
        return defaultExtendedReach;
    }
}
