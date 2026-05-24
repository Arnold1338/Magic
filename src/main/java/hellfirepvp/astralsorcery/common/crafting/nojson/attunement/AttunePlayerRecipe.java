package hellfirepvp.astralsorcery.common.crafting.nojson.attunement;

import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import java.util.List;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import javax.annotation.Nonnull;
import net.minecraft.server.level.ServerPlayer;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.world.DayTimeHelper;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.level.phys.AABB;
import hellfirepvp.astralsorcery.common.crafting.nojson.attunement.active.ActivePlayerAttunementRecipe;

public class AttunePlayerRecipe extends AttunementRecipe<ActivePlayerAttunementRecipe>
{
    private static final AABB BOX;
    
    public AttunePlayerRecipe() {
        super(AstralSorcery.key("attune_player"));
    }
    
    @Override
    public boolean canStartCrafting(final TileAttunementAltar altar) {
        final World world = altar.func_145831_w();
        return DayTimeHelper.isNight(world) && findEligiblePlayer(altar) != null;
    }
    
    @Nonnull
    @Override
    public ActivePlayerAttunementRecipe createRecipe(final TileAttunementAltar altar) {
        final ServerPlayer player = findEligiblePlayer(altar);
        return new ActivePlayerAttunementRecipe(this, (IMajorConstellation)altar.getActiveConstellation(), player.getUUID());
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    @Override
    public ActivePlayerAttunementRecipe deserialize(final TileAttunementAltar altar, final CompoundTag nbt, @Nullable final ActivePlayerAttunementRecipe previousInstance) {
        final ActivePlayerAttunementRecipe recipe = new ActivePlayerAttunementRecipe(this, nbt);
        if (previousInstance != null) {
            recipe.cameraHack = previousInstance.cameraHack;
        }
        return recipe;
    }
    
    @Nullable
    private static ServerPlayer findEligiblePlayer(final TileAttunementAltar altar) {
        if (!(altar.getActiveConstellation() instanceof IMajorConstellation)) {
            return null;
        }
        final AABB boxAt = AttunePlayerRecipe.BOX.func_186670_a(altar.func_174877_v().above()).func_186662_g(1.0);
        final Vector3 thisVec = new Vector3(altar).add(0.5, 1.5, 0.5);
        final List<ServerPlayer> players = altar.func_145831_w().func_217357_a((Class)ServerPlayer.class, boxAt);
        if (!players.isEmpty()) {
            final ServerPlayer pl = EntityUtils.selectClosest((Collection<ServerPlayer>)players, player -> thisVec.distanceSquared(player.func_213303_ch()));
            if (isEligablePlayer(pl, altar.getActiveConstellation())) {
                return pl;
            }
        }
        return null;
    }
    
    public static boolean isEligablePlayer(final ServerPlayer player, final IConstellation attuneTo) {
        if (player != null && player.isAlive() && !MiscUtils.isPlayerFakeMP(player) && !player.func_225608_bj_()) {
            final PlayerProgress prog = ResearchHelper.getProgress((Player)player, LogicalSide.SERVER);
            return prog.isValid() && attuneTo instanceof IMajorConstellation && !prog.isAttuned() && prog.getTierReached().isThisLaterOrEqual(ProgressionTier.ATTUNEMENT) && prog.hasConstellationDiscovered(attuneTo);
        }
        return false;
    }
    
    static {
        BOX = new AABB(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    }
}
