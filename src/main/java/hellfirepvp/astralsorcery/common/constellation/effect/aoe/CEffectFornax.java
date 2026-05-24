package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.world.level.level.block.state.BlockState;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import hellfirepvp.astralsorcery.common.crafting.nojson.meltable.WorldMeltableRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.freezing.WorldFreezingRecipe;
import hellfirepvp.astralsorcery.common.crafting.nojson.WorldMeltableRegistry;
import net.minecraft.world.level.item.ItemStack;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.crafting.nojson.WorldFreezingRegistry;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.server.level.ServerLevel;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.util.function.Supplier;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.world.level.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockRandomPositionGenerator;
import hellfirepvp.astralsorcery.common.util.block.iterator.BlockPositionGenerator;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ListEntries;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;

public class CEffectFornax extends CEffectAbstractList<ListEntries.PosEntry>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static FornaxConfig CONFIG;
    
    public CEffectFornax(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.fornax, 1, (world, pos, state) -> true);
        this.excludeRitualPositions();
        this.selectSphericalPositions();
    }
    
    @Nonnull
    @Override
    protected BlockPositionGenerator createPositionStrategy() {
        final BlockPositionGenerator gen = new BlockRandomPositionGenerator();
        gen.andFilter(pos -> pos.getY() < 0);
        return gen;
    }
    
    @Nullable
    @Override
    public ListEntries.PosEntry recreateElement(final CompoundTag tag, final BlockPos pos) {
        return new ListEntries.PosEntry(pos);
    }
    
    @Nullable
    @Override
    public ListEntries.PosEntry createElement(final World world, final BlockPos pos) {
        return new ListEntries.PosEntry(pos);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final World world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        final Vector3 motion = Vector3.random().multiply(0.04);
        if (pos.equals((Object)pedestal.func_174877_v())) {
            motion.setY(0);
        }
        else {
            motion.setY(Math.abs(motion.getY()) * -1.0);
        }
        final Color c = MiscUtils.eitherOf(CEffectFornax.rand, (Supplier<Color>[])new Supplier[] { () -> ColorsAS.CONSTELLATION_FORNAX.brighter(), () -> ColorsAS.CONSTELLATION_FORNAX.darker(), () -> ColorsAS.CONSTELLATION_FORNAX.darker(), () -> ColorsAS.CONSTELLATION_FORNAX.darker().darker() });
        EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(new Vector3((Vector3i)pos).add(0.5, 0.2, 0.5)).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(c)).setScaleMultiplier(0.3f + CEffectFornax.rand.nextFloat() * 0.4f).setMotion(motion).setGravityStrength(-0.0015f).setMaxAge(60 + CEffectFornax.rand.nextInt(30));
    }
    
    @Override
    public boolean playEffect(final World world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        if (!(world instanceof ServerLevel)) {
            return false;
        }
        final Consumer<ItemStack> dropResult = stack -> ItemUtils.dropItemNaturally(world, pos.getX() + 0.5, pos.getY() + 1.2, pos.getZ() + 0.5, stack);
        return this.peekNewPosition(world, pos, properties).mapLeft(newEntry -> {
            final BlockPos at = newEntry.getPos();
            if (properties.isCorrupted()) {
                final WorldFreezingRecipe freezingRecipe = WorldFreezingRegistry.INSTANCE.getRecipeFor(world, at);
                if (freezingRecipe != null) {
                    freezingRecipe.doOutput(world, at, world.getBlockState(at), dropResult);
                    return true;
                }
                else {
                    this.sendConstellationPing(world, new Vector3((Vector3i)at).add(0.5, 0.5, 0.5));
                    return false;
                }
            }
            else {
                final WorldMeltableRecipe meltRecipe = WorldMeltableRegistry.INSTANCE.getRecipeFor(world, at);
                if (meltRecipe != null) {
                    meltRecipe.doOutput(world, at, world.getBlockState(at), dropResult);
                    return true;
                }
                else {
                    this.sendConstellationPing(world, new Vector3((Vector3i)at).add(0.5, 0.5, 0.5));
                    return false;
                }
            }
        }).left().orElse(false);
    }
    
    @Override
    public Config getConfig() {
        return CEffectFornax.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectFornax.FLAG;
    }
    
    static {
        CEffectFornax.FLAG = ConstellationEffect.makeAffectionFlag("fornax");
        CEffectFornax.CONFIG = new FornaxConfig();
    }
    
    private static class FornaxConfig extends Config
    {
        private final float defaultMeltFailChance = 0.0f;
        public ForgeConfigSpec.DoubleValue meltFailChance;
        
        public FornaxConfig() {
            super("fornax", 8.0, 2.0);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the chance (0% to 100% -> 0.0 to 1.0) if the block will be replaced with air instead of being properly melted into something.").translation(this.translationKey("meltFailChance"));
            final String s = "meltFailChance";
            this.getClass();
            this.meltFailChance = translation.defineInRange(s, 0.0, 0.0, 1.0);
        }
    }
}
