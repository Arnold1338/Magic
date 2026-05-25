package hellfirepvp.astralsorcery.common.util;

import hellfirepvp.astralsorcery.common.base.Mods;
import net.minecraftforge.common.util.FakePlayer;
import java.util.Optional;
import net.minecraft.world.level.chunk.ChunkSource;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.util.log.LogCategory;
import java.awt.Color;
import net.minecraft.world.level.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.level.phys.BlockHitResult;
import java.util.LinkedList;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ISeedReader;
import net.minecraft.server.level.ServerPlayer;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.core.Direction;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.block.FlowingFluidBlock;
import net.minecraft.world.level.block.state.BlockState;
import java.util.function.Predicate;
import java.util.HashSet;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.BiFunction;
import org.apache.logging.log4j.util.TriConsumer;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.function.Consumer;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.GameRules;
import hellfirepvp.astralsorcery.common.lib.GameRulesAS;
import java.util.Iterator;
import net.minecraft.util.WeightedRandom;
import java.util.ArrayList;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModContainer;
import com.google.common.collect.Iterables;
import java.util.Random;
import java.util.Collection;
import net.minecraftforge.common.util.BlockSnapshot;
import java.util.List;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import javax.annotation.Nullable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;

public class MiscUtils
{
    @Nullable
    public static <T> T getTileAt(final IBlockReader world, final BlockPos pos, final Class<T> tileClass, final boolean forceChunkLoad) {
        if (world == null || pos == null) {
            return null;
        }
        if (world instanceof IWorld && !((IWorld)world).getChunkSource().func_222865_a(new ChunkPos(pos)) && !forceChunkLoad) {
            return null;
        }
        final BlockEntity te = world.func_175625_s(pos);
        if (te == null) {
            return null;
        }
        if (tileClass.isInstance(te)) {
            return (T)te;
        }
        return null;
    }
    
    public static boolean canEntityTickAt(final IWorld world, final BlockPos pos) {
        final ChunkPos chPos = new ChunkPos(pos);
        if (!world.getChunkSource().func_222865_a(chPos)) {
            return false;
        }
        if (world.level() || !(world instanceof ServerLevel)) {
            return true;
        }
        final ServerChunkProvider chunkProvider = ((ServerLevel)world).getChunkSource();
        return !chunkProvider.field_217237_a.func_219243_d(chPos);
    }
    
    public static List<BlockSnapshot> captureBlockChanges(final Level world, final Runnable r) {
        world.captureBlockSnapshots = true;
        r.run();
        world.captureBlockSnapshots = false;
        final List<BlockSnapshot> blockSnapshots = (List<BlockSnapshot>)world.capturedBlockSnapshots.clone();
        world.capturedBlockSnapshots.clear();
        return blockSnapshots;
    }
    
    @Nullable
    public static <T> T getRandomEntry(final Collection<T> collection, final Random rand) {
        if (collection == null || collection.isEmpty()) {
            return null;
        }
        final int index = rand.nextInt(collection.size());
        return (T)Iterables.get((Iterable)collection, index);
    }
    
    @Nullable
    public static <T> T getRandomEntry(final T[] array, final Random rand) {
        if (array == null || array.length <= 0) {
            return null;
        }
        return array[rand.nextInt(array.length)];
    }
    
    @Nullable
    public static ModContainer getCurrentlyActiveMod() {
        return ModLoadingContext.get().getActiveContainer();
    }
    
    @Nonnull
    public static <T> T getEnumEntry(final Class<T> enumClazz, final int index) {
        if (!enumClazz.isEnum()) {
            throw new IllegalArgumentException("Called getEnumEntry on class " + enumClazz.getName() + " which isn't an enum.");
        }
        final T[] values = enumClazz.getEnumConstants();
        if (values.length == 0) {
            throw new IllegalArgumentException(enumClazz.getName() + " has no enum constants.");
        }
        return values[Mth.getDescriptionId(index, 0, values.length - 1)];
    }
    
    @Nullable
    public static <T> T getWeightedRandomEntry(final Collection<T> list, final Random rand, final Function<T, Integer> getWeightFunction) {
        if (list.isEmpty()) {
            return null;
        }
        final List<WRItemObject<T>> weightedItems = new ArrayList<WRItemObject<T>>(list.size());
        for (final T e : list) {
            weightedItems.add(new WRItemObject<T>(getWeightFunction.apply(e), e));
        }
        final WRItemObject<T> item = (WRItemObject<T>)WeightedRandom.func_76271_a(rand, (List)weightedItems);
        return (item != null) ? item.getValue() : null;
    }
    
    public static <T, V extends Comparable<V>> V getMaxEntry(final Collection<T> elements, final Function<T, V> valueFunction) {
        return getMaxEntry((Collection<V>)transformCollection(elements, (Function<T, T>)valueFunction));
    }
    
    public static <T extends Comparable<T>> T getMaxEntry(final Collection<T> elements) {
        T maxElement = null;
        for (final T element : elements) {
            if (maxElement == null || maxElement.compareTo(element) < 0) {
                maxElement = element;
            }
        }
        return maxElement;
    }
    
    public static <T, V extends Comparable<V>> V getMinEntry(final Collection<T> elements, final Function<T, V> valueFunction) {
        return getMinEntry((Collection<V>)transformCollection(elements, (Function<T, T>)valueFunction));
    }
    
    public static <T extends Comparable<T>> T getMinEntry(final Collection<T> elements) {
        T minElement = null;
        for (final T element : elements) {
            if (minElement == null || minElement.compareTo(element) > 0) {
                minElement = element;
            }
        }
        return minElement;
    }
    
    public static boolean canSeeSky(final Level world, final BlockPos at, final boolean loadChunk, final boolean defaultValue) {
        return canSeeSky(world, at, loadChunk, false, defaultValue);
    }
    
    public static boolean canSeeSky(final Level world, final BlockPos at, final boolean loadChunk, final boolean allowInNoSkyWorlds, final boolean defaultValue) {
        if (world.func_82736_K().func_223586_b((GameRules.RuleKey)GameRulesAS.IGNORE_SKYLIGHT_CHECK_RULE)) {
            return true;
        }
        if (allowInNoSkyWorlds && !world.dimensionType().func_218272_d()) {
            return true;
        }
        if (!loadChunk) {
            return executeWithChunk((IWorldReader)world, at, () -> world.func_175710_j(at), defaultValue);
        }
        return world.func_175710_j(at);
    }
    
    public static <T> Runnable apply(final Consumer<T> func, final Supplier<T> supply) {
        return () -> func.accept(supply.get());
    }
    
    public static <T, U> Consumer<T> apply(final BiConsumer<T, U> func, final Supplier<U> supply) {
        return t -> func.accept(t, supply.get());
    }
    
    public static <T, U, V> BiConsumer<T, U> apply(final TriConsumer<T, U, V> func, final Supplier<V> supply) {
        return (t, u) -> func.accept(t, u, supply.get());
    }
    
    public static <T, R> Supplier<R> apply(final Function<T, R> func, final Supplier<T> supply) {
        return (Supplier<R>)(() -> func.apply(supply.get()));
    }
    
    public static <T, P, R> Function<P, R> apply(final BiFunction<T, P, R> func, final Supplier<T> supply) {
        return (Function<P, R>)(p -> func.apply(supply.get(), p));
    }
    
    public static <T, V> Function<T, V> nullFunction(final Runnable run) {
        return nullFunction(v -> run.run());
    }
    
    public static <T, V> Function<T, V> nullFunction(final Consumer<T> run) {
        return (Function<T, V>)(t -> {
            run.accept(t);
            return null;
        });
    }
    
    public static <T> Supplier<T> nullSupplier(final Runnable run) {
        return (Supplier<T>)(() -> {
            run.run();
            return null;
        });
    }
    
    public static <T, V> List<V> transformList(final List<T> list, final Function<T, V> map) {
        return list.stream().map((Function<? super Object, ?>)map).collect((Collector<? super Object, ?, List<V>>)Collectors.toList());
    }
    
    public static <T, V> Set<V> transformSet(final Set<T> list, final Function<T, V> map) {
        return list.stream().map((Function<? super Object, ?>)map).collect((Collector<? super Object, ?, Set<V>>)Collectors.toSet());
    }
    
    public static <T, V> Collection<V> transformCollection(final Collection<T> list, final Function<T, V> map) {
        return list.stream().map((Function<? super T, ?>)map).collect((Collector<? super Object, ?, Collection<V>>)Collectors.toList());
    }
    
    public static <K, V, N> Map<K, N> remap(final Map<K, V> map, final Function<V, N> remapFct) {
        return MapStream.of(map).mapValue(remapFct).toMap();
    }
    
    public static <T> void mergeList(final Collection<T> src, final List<T> dst) {
        for (final T element : src) {
            if (!dst.contains(element)) {
                dst.add(element);
            }
        }
    }
    
    public static <T> void cutList(final Collection<? extends T> toRemove, final List<T> from) {
        for (final T element : toRemove) {
            from.remove(element);
        }
    }
    
    public static <T> List<T> copyList(final List<T> list) {
        final List<T> l = new ArrayList<T>(list.size());
        Collections.copy((List<? super Object>)l, (List<?>)list);
        return l;
    }
    
    public static <T> Set<T> copySet(final Set<T> set) {
        final Set<T> s = new HashSet<T>(set.size());
        s.addAll((Collection<? extends T>)set);
        return s;
    }
    
    @Nullable
    public static <T> T iterativeSearch(final Collection<T> collection, final Predicate<T> matchingFct) {
        for (final T element : collection) {
            if (matchingFct.test(element)) {
                return element;
            }
        }
        return null;
    }
    
    public static <T> boolean contains(final Collection<T> collection, final Predicate<T> matchingFct) {
        return iterativeSearch(collection, matchingFct) != null;
    }
    
    public static <T> boolean matchesAny(final T element, final Collection<Predicate<T>> tests) {
        for (final Predicate<T> test : tests) {
            if (test.test(element)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isFluidBlock(final BlockState state) {
        return state.getBlock() instanceof FlowingFluidBlock;
    }
    
    @Nullable
    public static Fluid tryGetFuild(final BlockState state) {
        if (!isFluidBlock(state)) {
            return null;
        }
        if (state.getBlock() instanceof FlowingFluidBlock) {
            final FluidState fluidState = state.getFluidState();
            if (!fluidState.func_206888_e()) {
                return fluidState.func_206886_c();
            }
        }
        return null;
    }
    
    public static boolean canPlayerAttackServer(@Nullable final LivingEntity source, @Nonnull final LivingEntity target) {
        if (!target.isAlive()) {
            return false;
        }
        if (target instanceof Player) {
            final Player plTarget = (Player)target;
            if (target.level() instanceof ServerLevel && target.level().func_73046_m() != null && target.level().func_73046_m().func_71219_W()) {
                return false;
            }
            if (plTarget.func_175149_v() || plTarget.getVehicle()) {
                return false;
            }
            if (source instanceof Player && !((Player)source).func_96122_a(plTarget)) {
                return false;
            }
        }
        return true;
    }
    
    public static boolean canPlayerBreakBlockPos(final Player player, final BlockPos tryBreak) {
        final BlockEvent.BreakEvent ev = new BlockEvent.BreakEvent(player.level(), tryBreak, player.level().getBlockState(tryBreak), player);
        MinecraftForge.EVENT_BUS.post((Event)ev);
        return !ev.isCanceled();
    }
    
    public static boolean canPlayerPlaceBlockPos(final Player player, final BlockState tryPlace, final BlockPos pos, final Direction againstSide) {
        final Level world = player.level();
        world.captureBlockSnapshots = true;
        world.func_175656_a(pos, tryPlace);
        world.captureBlockSnapshots = false;
        final List<BlockSnapshot> blockSnapshots = (List<BlockSnapshot>)world.capturedBlockSnapshots.clone();
        world.capturedBlockSnapshots.clear();
        boolean cancelPlacement = false;
        if (blockSnapshots.size() > 1) {
            cancelPlacement = ForgeEventFactory.onMultiBlockPlace((Entity)player, (List)blockSnapshots, againstSide);
        }
        else if (blockSnapshots.size() == 1) {
            cancelPlacement = ForgeEventFactory.onBlockPlace((Entity)player, (BlockSnapshot)blockSnapshots.get(0), againstSide);
        }
        for (final BlockSnapshot blocksnapshot : Lists.reverse((List)blockSnapshots)) {
            blocksnapshot.restore(world.restoringBlockSnapshots = true, false);
            world.restoringBlockSnapshots = false;
        }
        return !cancelPlacement;
    }
    
    public static boolean isConnectionEstablished(final ServerPlayer player) {
        return player.field_71135_a != null && player.field_71135_a.field_147371_a != null && player.field_71135_a.field_147371_a.func_150724_d();
    }
    
    public static long getRandomWorldSeed(final ISeedReader world) {
        return new Random(world.func_72905_C()).nextLong();
    }
    
    @Nullable
    public static Tuple<Hand, ItemStack> getMainOrOffHand(final LivingEntity entity, final Item search) {
        return getMainOrOffHand(entity, stack -> !stack.isEmpty() && stack.getItem().equals(search));
    }
    
    @Nullable
    public static Tuple<Hand, ItemStack> getMainOrOffHand(final LivingEntity entity, final Predicate<ItemStack> acceptorFnc) {
        Hand hand = InteractionHand.MAIN_HAND;
        ItemStack held = entity.getItemInHand(hand);
        if (held.isEmpty() || !acceptorFnc.test(held)) {
            hand = InteractionHand.OFF_HAND;
            held = entity.getItemInHand(hand);
        }
        if (held.isEmpty() || !acceptorFnc.test(held)) {
            return null;
        }
        return (Tuple<Hand, ItemStack>)new Tuple((Object)hand, (Object)held);
    }
    
    public static String capitalizeFirst(final String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return Character.toTitleCase(str.charAt(0)) + str.substring(1);
    }
    
    @Nullable
    public static <T extends Entity> T transferEntityTo(T entity, final RegistryKey<Level> target, final BlockPos targetPos) {
        if (entity.level().isClientSide) {
            return null;
        }
        entity.func_226284_e_(false);
        final RegistryKey<Level> src = (RegistryKey<Level>)entity.level().dimension();
        if (!src.equals(target)) {
            if (!ForgeHooks.onTravelToDimension((Entity)entity, (RegistryKey)target)) {
                return null;
            }
            final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
            final ServerLevel targetWorld = srv.getLevel((RegistryKey)target);
            if (targetWorld == null) {
                return null;
            }
            if (entity instanceof ServerPlayer) {
                ((ServerPlayer)entity).func_200619_a(targetWorld, targetPos.getX() + 0.5, targetPos.getY() + 0.1, targetPos.getZ() + 0.5, entity.yRot, entity.xRot);
            }
            else {
                entity = (T)entity.changeDimension(targetWorld, (ITeleporter)new NoOpTeleporter(targetWorld, targetPos));
                if (entity == null) {
                    return null;
                }
            }
        }
        entity.func_70634_a(targetPos.getX() + 0.5, (double)targetPos.getY(), targetPos.getZ() + 0.5);
        return entity;
    }
    
    @Nullable
    public static BlockPos itDownTopBlock(final Level world, final BlockPos at) {
        final IChunk chunk = world.func_217349_x(at);
        BlockPos downPos = null;
        for (BlockPos blockpos = new BlockPos(at.getX(), chunk.func_76625_h() + 16, at.getZ()); blockpos.getY() >= 0; blockpos = downPos) {
            downPos = blockpos.renderItem();
            final BlockState test = world.getBlockState(downPos);
            if (!world.isEmptyBlock(downPos) && !test.func_235714_a_((ITag)BlockTags.field_206952_E) && test.func_224755_d((IBlockReader)world, downPos, Direction.UP)) {
                break;
            }
        }
        return downPos;
    }
    
    public static List<Vector3> getCirclePositions(final Vector3 centerOffset, final Vector3 axis, final double radius, final int amountOfPointsOnCircle) {
        final List<Vector3> out = new LinkedList<Vector3>();
        final Vector3 circleVec = axis.clone().perpendicular().normalize().multiply(radius);
        final double degPerPoint = 360.0 / amountOfPointsOnCircle;
        for (int i = 0; i < amountOfPointsOnCircle; ++i) {
            final double deg = i * degPerPoint;
            out.add(circleVec.clone().rotate(Math.toRadians(deg), axis.clone()).add(centerOffset));
        }
        return out;
    }
    
    public static Vector3 getRandomCirclePosition(final Vector3 centerOffset, final Vector3 axis, final double radius) {
        return getCirclePosition(centerOffset, axis, radius, Math.random() * 360.0);
    }
    
    public static Vector3 getCirclePosition(final Vector3 centerOffset, final Vector3 axis, final double radius, final double degree) {
        final Vector3 circleVec = axis.clone().perpendicular().normalize().multiply(radius);
        return circleVec.rotate(Math.toRadians(degree), axis.clone()).add(centerOffset);
    }
    
    public static Vector3 limitVelocityToMinecraftLimit(final Vector3 velocity) {
        final double maxDir = Math.max(Math.abs(velocity.getX()), Math.max(Math.abs(velocity.getY()), Math.abs(velocity.getZ())));
        if (maxDir <= 3.9) {
            return velocity;
        }
        return velocity.multiply(3.9 / maxDir);
    }
    
    @Nullable
    public static BlockHitResult rayTraceLookBlock(final Player player) {
        return rayTraceLookBlock(player, player.getAttribute((Attribute)ForgeMod.REACH_DISTANCE.get()).func_111126_e());
    }
    
    @Nonnull
    public static HitResult rayTraceLook(final Player player) {
        return rayTraceLook(player, player.getAttribute((Attribute)ForgeMod.REACH_DISTANCE.get()).func_111126_e());
    }
    
    @Nullable
    public static BlockHitResult rayTraceLookBlock(final Player player, final ClipContext.BlockMode blockMode, final ClipContext.FluidMode fluidMode) {
        return rayTraceLookBlock((Entity)player, blockMode, fluidMode, player.getAttribute((Attribute)ForgeMod.REACH_DISTANCE.get()).func_111126_e());
    }
    
    @Nonnull
    public static HitResult rayTraceLook(final Player player, final ClipContext.BlockMode blockMode, final ClipContext.FluidMode fluidMode) {
        return rayTraceLook((Entity)player, blockMode, fluidMode, player.getAttribute((Attribute)ForgeMod.REACH_DISTANCE.get()).func_111126_e());
    }
    
    @Nullable
    public static BlockHitResult rayTraceLookBlock(final Player player, final double reachDst) {
        return rayTraceLookBlock((Entity)player, ClipContext.BlockMode.COLLIDER, ClipContext.FluidMode.ANY, reachDst);
    }
    
    @Nonnull
    public static HitResult rayTraceLook(final Player player, final double reachDst) {
        return rayTraceLook((Entity)player, ClipContext.BlockMode.COLLIDER, ClipContext.FluidMode.ANY, reachDst);
    }
    
    @Nullable
    public static BlockHitResult rayTraceLookBlock(final Entity entity, final ClipContext.BlockMode blockMode, final ClipContext.FluidMode fluidMode, final double reachDst) {
        final HitResult rtr = rayTraceLook(entity, blockMode, fluidMode, reachDst);
        if (rtr.func_216346_c() == HitResult.Type.BLOCK && rtr instanceof BlockHitResult) {
            return (BlockHitResult)rtr;
        }
        return null;
    }
    
    @Nonnull
    public static HitResult rayTraceLook(final Entity entity, final ClipContext.BlockMode blockMode, final ClipContext.FluidMode fluidMode, final double reachDst) {
        final Vec3 pos = new Vec3(entity.getX(), entity.getY() + entity.func_70047_e(), entity.getZ());
        final Vec3 lookVec = entity.func_70040_Z();
        final Vec3 end = pos.func_72441_c(lookVec.field_72450_a * reachDst, lookVec.field_72448_b * reachDst, lookVec.field_72449_c * reachDst);
        final ClipContext ctx = new ClipContext(pos, end, blockMode, fluidMode, entity);
        return (HitResult)entity.level().func_217299_a(ctx);
    }
    
    public static Color calcRandomConstellationColor(final float perc) {
        return new Color(Color.HSBtoRGB((230.0f + 50.0f * perc) / 360.0f, 0.8f, 0.8f - 0.3f * perc));
    }
    
    public static void applyRandomOffset(final Vector3 target, final Random rand) {
        applyRandomOffset(target, rand, 1.0f);
    }
    
    public static void applyRandomOffset(final Vector3 target, final Random rand, final float multiplier) {
        target.addX(rand.nextFloat() * multiplier * (rand.nextBoolean() ? 1 : -1));
        target.addY(rand.nextFloat() * multiplier * (rand.nextBoolean() ? 1 : -1));
        target.addZ(rand.nextFloat() * multiplier * (rand.nextBoolean() ? 1 : -1));
    }
    
    public static void applyRandomCircularOffset(final Vector3 target, final Random rand) {
        applyRandomOffset(target, rand, 1.0f);
    }
    
    public static void applyRandomCircularOffset(final Vector3 target, final Random rand, final float multiplier) {
        final Vector3 v = Vector3.random().normalize().multiply(rand.nextFloat() * multiplier);
        target.addX(v.getX() * (rand.nextBoolean() ? 1 : -1));
        target.addY(v.getY() * (rand.nextBoolean() ? 1 : -1));
        target.addZ(v.getZ() * (rand.nextBoolean() ? 1 : -1));
    }
    
    public static void executeWithChunk(final IWorldReader world, final ChunkPos pos, final Runnable run) {
        executeWithChunk(world, pos.func_206849_h(), nullSupplier(run));
    }
    
    public static void executeWithChunk(final IWorldReader world, final BlockPos pos, final Runnable run) {
        executeWithChunk(world, pos, nullSupplier(run));
    }
    
    public static <T> T executeWithChunk(final IWorldReader world, final BlockPos pos, final Supplier<T> run) {
        return executeWithChunk(world, pos, run, (T)null);
    }
    
    public static <T> T executeWithChunk(final IWorldReader world, final BlockPos pos, final Supplier<T> run, final T defaultValue) {
        if (world instanceof ServerLevel && LogCategory.UNINTENDED_CHUNK_LOADING.isEnabled()) {
            final ServerChunkProvider provider = ((ServerLevel)world).getChunkSource();
            final int prev = provider.func_73152_e();
            try {
                if (provider.func_222865_a(new ChunkPos(pos))) {
                    return run.get();
                }
            }
            finally {
                final int current = ((ServerLevel)world).getChunkSource().func_73152_e();
                if (current > prev) {
                    AstralSorcery.log.warn("Astral Sorcery loaded a chunk when it intended not to!");
                    AstralSorcery.log.warn("Previous chunk count: " + prev);
                    AstralSorcery.log.warn("Current chunk count: " + current);
                    AstralSorcery.log.warn("Loaded " + (current - prev) + " chunks!");
                    AstralSorcery.log.warn("Stacktrace:", (Throwable)new Exception());
                }
            }
        }
        else if (world instanceof IWorld) {
            final AbstractChunkProvider provider2 = ((IWorld)world).getChunkSource();
            if (provider2.func_222866_a(pos)) {
                return run.get();
            }
        }
        else if (world.func_175667_e(pos)) {
            return run.get();
        }
        return defaultValue;
    }
    
    public static <T> void executeWithChunk(final IWorldReader world, final BlockPos pos, final T obj, final Consumer<T> run) {
        executeWithChunk(world, pos, nullSupplier(apply(run, () -> obj)));
    }
    
    public static <T, U> void executeWithChunk(final IWorldReader world, final BlockPos pos, final T obj, final U obj1, final BiConsumer<T, U> run) {
        executeWithChunk(world, pos, obj, (Consumer<T>)apply((BiConsumer<T, U>)run, () -> obj1));
    }
    
    public static <T, R> R executeWithChunk(final IWorldReader world, final BlockPos pos, final T obj, final Function<T, R> run) {
        return executeWithChunk(world, pos, (Supplier<R>)apply((Function<T, T>)run, () -> obj));
    }
    
    public static <T, R> R executeWithChunk(final IWorldReader world, final BlockPos pos, final T obj, final Function<T, R> run, final R _default) {
        return executeWithChunk(world, pos, (Supplier<R>)apply((Function<T, T>)run, () -> obj), _default);
    }
    
    public static <T> Function<T, T> mapWithChunk(final IWorldReader world, final Function<T, BlockPos> posFn) {
        return (Function<T, T>)(val -> executeWithChunk(world, posFn.apply(val), val, Function.identity()));
    }
    
    public static <T> T eitherOf(final Random r, final T... selection) {
        if (selection.length == 0) {
            return null;
        }
        return selection[r.nextInt(selection.length)];
    }
    
    public static <T> T eitherOf(final Random r, final Supplier<T>... selection) {
        if (selection.length == 0) {
            return null;
        }
        return selection[r.nextInt(selection.length)].get();
    }
    
    public static <T> Optional<T> tryMultiple(final Supplier<T>... suppliers) {
        final int length = suppliers.length;
        int i = 0;
        while (i < length) {
            final Supplier<T> supplier = suppliers[i];
            try {
                return Optional.ofNullable(supplier.get());
            }
            catch (final Exception exc) {
                AstralSorcery.log.error((Object)exc);
                ++i;
                continue;
            }
            break;
        }
        return Optional.empty();
    }
    
    public static boolean isPlayerFakeMP(final ServerPlayer player) {
        if (player instanceof FakePlayer) {
            return true;
        }
        boolean isModdedPlayer = false;
        for (final Mods mod : Mods.values()) {
            if (mod.isPresent()) {
                final Class<?> specificPlayerClass = mod.getExtendedPlayerClass();
                if (specificPlayerClass != null && player.getClass() != ServerPlayer.class && player.getClass() == specificPlayerClass) {
                    isModdedPlayer = true;
                    break;
                }
            }
        }
        if (!isModdedPlayer && player.getClass() != ServerPlayer.class) {
            return true;
        }
        if (player.field_71135_a == null) {
            return true;
        }
        try {
            player.func_71114_r().length();
            player.field_71135_a.field_147371_a.func_74430_c().toString();
        }
        catch (final Exception exc) {
            return true;
        }
        return false;
    }
}
