package hellfirepvp.astralsorcery.common.starlight.network.handler;

import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import java.util.HashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutation;
import java.util.Random;
import net.minecraft.world.level.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.crafting.recipe.BlockTransmutationContext;
import hellfirepvp.astralsorcery.common.lib.RecipeTypesAS;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import hellfirepvp.astralsorcery.common.util.block.WorldBlockPos;
import java.util.Map;
import hellfirepvp.astralsorcery.common.starlight.network.StarlightNetworkRegistry;

public class BlockTransmutationHandler implements StarlightNetworkRegistry.IStarlightBlockHandler
{
    private static final Map<WorldBlockPos, ActiveTransmutation> runningTransmutations;
    
    @Override
    public boolean isApplicable(final World world, final BlockPos pos, final BlockState state, final IWeakConstellation starlightType) {
        return RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION.findRecipe(new BlockTransmutationContext((IWorld)world, pos, state, starlightType)) != null;
    }
    
    @Override
    public void receiveStarlight(final World world, final Random rand, final BlockPos pos, final BlockState state, final IWeakConstellation starlightType, final double amount) {
        final BlockTransmutation recipe = RecipeTypesAS.TYPE_BLOCK_TRANSMUTATION.findRecipe(new BlockTransmutationContext((IWorld)world, pos, state, starlightType));
        if (recipe == null) {
            return;
        }
        final WorldBlockPos at = WorldBlockPos.wrapServer(world, pos);
        ActiveTransmutation activeRecipe = BlockTransmutationHandler.runningTransmutations.get(at);
        if (activeRecipe == null || !activeRecipe.recipe.equals(recipe)) {
            activeRecipe = new ActiveTransmutation(recipe);
            BlockTransmutationHandler.runningTransmutations.put(at, activeRecipe);
        }
        activeRecipe.acceptStarlight(amount);
        final PktPlayEffect pkt = new PktPlayEffect(PktPlayEffect.Type.BLOCK_TRANSMUTATION_TICK).addData(buf -> ByteBufUtils.writePos(buf, pos));
        PacketChannel.CHANNEL.sendToAllAround(pkt, PacketChannel.pointFromPos(world, (Vector3i)pos, 24.0));
        if (activeRecipe.isFinished() && activeRecipe.finish((IWorld)world, pos)) {
            BlockTransmutationHandler.runningTransmutations.remove(at);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playTransmutation(final PktPlayEffect effect) {
        final Random rand = new Random();
        final BlockPos pos = ByteBufUtils.readPos(effect.getExtraData());
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vector3i)pos).add(rand.nextFloat(), rand.nextFloat(), rand.nextFloat())).setAlphaMultiplier(1.0f).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(ColorsAS.ROCK_CRYSTAL)).setScaleMultiplier(0.2f + rand.nextFloat() * 0.15f).setGravityStrength(-0.0014f).setMaxAge(40 + rand.nextInt(20));
    }
    
    static {
        runningTransmutations = new HashMap<WorldBlockPos, ActiveTransmutation>();
    }
    
    private static class ActiveTransmutation
    {
        private static final int MS_THRESHOLD = 15000;
        private final BlockTransmutation recipe;
        private long lastMillisecondStarlightReceived;
        private double accumulatedStarlight;
        
        private ActiveTransmutation(final BlockTransmutation recipe) {
            this.lastMillisecondStarlightReceived = System.currentTimeMillis();
            this.accumulatedStarlight = 0.0;
            this.recipe = recipe;
        }
        
        private void acceptStarlight(final double amount) {
            final long msReceived = System.currentTimeMillis();
            final long receiveDiff = msReceived - this.lastMillisecondStarlightReceived;
            if (receiveDiff >= 15000L) {
                this.accumulatedStarlight = 0.0;
            }
            this.accumulatedStarlight += amount;
            this.lastMillisecondStarlightReceived = msReceived;
        }
        
        private boolean isFinished() {
            return this.accumulatedStarlight >= this.recipe.getStarlightRequired();
        }
        
        private boolean finish(final IWorld world, final BlockPos pos) {
            final BlockState out = this.recipe.getOutput();
            if (world.func_180501_a(pos, out, 11)) {
                final ItemStack stack = ItemUtils.createBlockStack(out);
                if (!stack.isEmpty()) {
                    world.func_217369_A().stream().filter(player -> player.func_70092_e((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()) <= 225.0).forEach(player -> ResearchManager.informCrafted(player, stack));
                }
                return true;
            }
            this.accumulatedStarlight *= 0.8999999761581421;
            return false;
        }
    }
}
