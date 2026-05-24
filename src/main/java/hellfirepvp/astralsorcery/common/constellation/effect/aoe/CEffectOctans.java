package hellfirepvp.astralsorcery.common.constellation.effect.aoe;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffect;
import net.minecraft.world.entity.item.ItemEntity;
import java.util.Iterator;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import com.mojang.datafixers.util.Either;
import net.minecraft.world.level.LevelAccessor;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.server.level.ServerLevel;
import hellfirepvp.astralsorcery.common.constellation.IMinorConstellation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.effect.ConstellationEffectProperties;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.util.function.Supplier;
import java.awt.Color;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import net.minecraft.world.level.Level;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.FlowingFluidBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.util.block.ILocatable;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import hellfirepvp.astralsorcery.common.constellation.effect.base.ListEntries;
import hellfirepvp.astralsorcery.common.constellation.effect.base.CEffectAbstractList;

public class CEffectOctans extends CEffectAbstractList<ListEntries.CounterMaxEntry>
{
    public static PlayerAffectionFlags.AffectionFlag FLAG;
    public static OctansConfig CONFIG;
    private static boolean corruptedSkipWaterCheck;
    
    public CEffectOctans(@Nonnull final ILocatable origin) {
        super(origin, ConstellationsAS.octans, (int)CEffectOctans.CONFIG.maxAmount.get(), (world, pos, state) -> {
            if (!CEffectOctans.corruptedSkipWaterCheck) {
                pos = world.func_205770_a(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).func_177977_b();
            }
            return CEffectOctans.corruptedSkipWaterCheck || (world.isEmptyBlock(pos.above()) && state.getBlock() instanceof FlowingFluidBlock && state.func_185904_a() == Material.field_151586_h && (int)state.getValue((Property)FlowingFluidBlock.field_176367_b) == 0) || state.getBlock() instanceof BubbleColumnBlock;
        });
        this.excludeRitualColumn();
    }
    
    @Nullable
    @Override
    public ListEntries.CounterMaxEntry recreateElement(final CompoundTag tag, final BlockPos pos) {
        return new ListEntries.CounterMaxEntry(pos, 1);
    }
    
    @Nullable
    @Override
    public ListEntries.CounterMaxEntry createElement(final World world, BlockPos pos) {
        pos = world.func_205770_a(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos).func_177977_b();
        return new ListEntries.CounterMaxEntry(pos, 1);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void playClientEffect(final World world, final BlockPos pos, final TileRitualPedestal pedestal, final float alphaMultiplier, final boolean extended) {
        final ConstellationEffectProperties prop = this.createProperties(pedestal.getMirrorCount());
        final Vector3 at = new Vector3((Vector3i)pos).add(0.5, 0.5, 0.5);
        at.addY(prop.getSize() * 0.75);
        for (int i = 0; i < Math.max(1.0, prop.getSize() / 6.0); ++i) {
            final Vector3 vec = at.clone().add(Vector3.random().setY(0).multiply(CEffectOctans.rand.nextFloat() * prop.getSize()));
            final Color c = MiscUtils.eitherOf(CEffectOctans.rand, (Supplier<Color>[])new Supplier[] { () -> ColorsAS.CONSTELLATION_OCTANS, () -> ColorsAS.CONSTELLATION_OCTANS.darker() });
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(vec).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(c)).setScaleMultiplier(0.6f + CEffectOctans.rand.nextFloat() * 0.3f).setGravityStrength(4.0E-4f + CEffectOctans.rand.nextFloat() * 8.0E-4f).setMaxAge(100 + CEffectOctans.rand.nextInt(60));
        }
    }
    
    @Override
    public boolean playEffect(final World world, final BlockPos pos, final ConstellationEffectProperties properties, @Nullable final IMinorConstellation trait) {
        if (!(world instanceof ServerLevel)) {
            return false;
        }
        boolean update = false;
        if (properties.isCorrupted()) {
            CEffectOctans.corruptedSkipWaterCheck = true;
            final Either<ListEntries.CounterMaxEntry, BlockPos> newEntry = this.peekNewPosition(world, pos, properties);
            CEffectOctans.corruptedSkipWaterCheck = false;
            final ListEntries.CounterMaxEntry entry;
            return newEntry.mapLeft(entry -> {
                final BlockState state = world.getBlockState(entry.getPos());
                final BlockPos offset = entry.getPos().func_177973_b((Vector3i)pos);
                if (world.isEmptyBlock(entry.getPos()) && (this.isLinkedRitual || Math.abs(offset.getX()) > 5 || Math.abs(offset.getZ()) > 5 || offset.getY() < 0)) {
                    if (!world.dimensionType().func_236040_e_() && world.func_175656_a(entry.getPos(), Blocks.field_150355_j.defaultBlockState())) {
                        for (int i = 0; i < 3; ++i) {
                            this.spawnFishingDropsAt((ServerLevel)world, entry.getPos());
                        }
                        world.func_190524_a(entry.getPos(), Blocks.field_150355_j, entry.getPos());
                    }
                }
                else if (BlockUtils.isFluidBlock(state)) {
                    if (state.getBlock() == Blocks.field_150355_j) {
                        if (CEffectOctans.rand.nextInt(100) == 0) {
                            this.spawnFishingDropsAt((ServerLevel)world, entry.getPos());
                        }
                    }
                    else {
                        world.func_175656_a(entry.getPos(), Blocks.field_150354_m.defaultBlockState());
                    }
                }
                else if (state.getBlock() instanceof BubbleColumnBlock && CEffectOctans.rand.nextInt(70) == 0) {
                    this.spawnFishingDropsAt((ServerLevel)world, entry.getPos());
                }
                return true;
            }).left().orElse(false);
        }
        final ListEntries.CounterMaxEntry entry = this.getRandomElementChanced();
        if (entry != null && MiscUtils.canEntityTickAt((IWorld)world, entry.getPos())) {
            if (!this.isValid(world, entry)) {
                this.removeElement(entry);
            }
            else {
                this.sendConstellationPing(world, new Vector3((Vector3i)entry.getPos()).add(0.5, 1.0, 0.5));
                int count = entry.getCounter();
                ++count;
                entry.setCounter(count);
                if (count >= entry.getMaxCount()) {
                    final int min = Math.min((int)CEffectOctans.CONFIG.minFishTickTime.get(), (int)CEffectOctans.CONFIG.maxFishTickTime.get());
                    final int max = Math.max((int)CEffectOctans.CONFIG.minFishTickTime.get(), (int)CEffectOctans.CONFIG.maxFishTickTime.get());
                    final int diff = Math.max(1, max - min + 1);
                    entry.setMaxCount(min + CEffectOctans.rand.nextInt(diff));
                    entry.setCounter(0);
                    this.spawnFishingDropsAt((ServerLevel)world, entry.getPos());
                }
            }
            update = true;
        }
        if (this.findNewPosition(world, pos, properties).ifRight(attemptedPos -> this.sendConstellationPing(world, new Vector3((Vector3i)world.func_205770_a(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, attemptedPos).func_177977_b()).add(0.5, 0.5, 0.5))).left().isPresent()) {
            update = true;
        }
        return update;
    }
    
    private void spawnFishingDropsAt(final ServerLevel world, final BlockPos pos) {
        final Vector3 dropLoc = new Vector3((Vector3i)pos).add(0.5, 0.85, 0.5);
        final ItemStack tool = new ItemStack((ItemLike)Items.field_151112_aM);
        tool.func_77966_a(Enchantments.field_151370_z, 2);
        ResourceLocation fromTable = LootTables.field_186390_ao;
        if (CEffectOctans.rand.nextFloat() < 0.1f) {
            fromTable = LootTables.field_186389_an;
        }
        final LootContext.Builder builder = new LootContext.Builder(world);
        builder.func_186469_a(CEffectOctans.rand.nextInt(2) * CEffectOctans.rand.nextFloat());
        builder.func_216023_a(CEffectOctans.rand);
        builder.func_216015_a(LootContextParams.TOOL, (Object)tool);
        builder.func_216015_a(LootParameters.field_237457_g_, (Object)Vec3.func_237489_a_((Vector3i)pos));
        final LootTable lootTable = world.func_73046_m().func_200249_aQ().func_186521_a(fromTable);
        for (final ItemStack loot : lootTable.func_216113_a(builder.func_216022_a(LootParameterSets.field_216262_c))) {
            final ItemEntity ei = ItemUtils.dropItemNaturally((World)world, dropLoc.getX(), dropLoc.getY(), dropLoc.getZ(), loot);
            final Vector3 motion = new Vector3(ei.func_213322_ci());
            motion.setY(Math.abs(motion.getY()));
            ei.func_213317_d(motion.toVector3d());
        }
    }
    
    @Override
    public Config getConfig() {
        return CEffectOctans.CONFIG;
    }
    
    @Override
    public PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return CEffectOctans.FLAG;
    }
    
    static {
        CEffectOctans.FLAG = ConstellationEffect.makeAffectionFlag("octans");
        CEffectOctans.CONFIG = new OctansConfig();
        CEffectOctans.corruptedSkipWaterCheck = false;
    }
    
    private static class OctansConfig extends CountConfig
    {
        private final int defaultMinFishTickTime = 20;
        private final int defaultMaxFishTickTime = 60;
        public ForgeConfigSpec.IntValue minFishTickTime;
        public ForgeConfigSpec.IntValue maxFishTickTime;
        
        public OctansConfig() {
            super("octans", 8.0, 1.0, 64);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the minimum default tick-time until a fish may be fished by the ritual. Gets reduced internally the more starlight was provided at the ritual.").translation(this.translationKey("minFishTickTime"));
            final String s = "minFishTickTime";
            this.getClass();
            this.minFishTickTime = translation.defineInRange(s, 20, 5, Integer.MAX_VALUE);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Defines the maximum default tick-time until a fish may be fished by the ritual. Gets reduced internally the more starlight was provided at the ritual. Has to be bigger as the minimum time; if it isn't it'll be set to the minimum.").translation(this.translationKey("maxFishTickTime"));
            final String s2 = "maxFishTickTime";
            this.getClass();
            this.maxFishTickTime = translation2.defineInRange(s2, 60, 10, Integer.MAX_VALUE);
        }
    }
}
