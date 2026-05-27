package hellfirepvp.astralsorcery.common.util.entity;

import net.minecraft.world.entity.item.ItemEntity;
import java.util.Iterator;
import java.util.function.Function;
import net.minecraft.world.item.Item;
import com.google.common.base.Predicate;
import net.minecraft.world.phys.AABB;
import javax.annotation.Nonnull;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.LootContext;
import java.util.Collections;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.damagesource.DamageSource;
import net.minecraftforge.eventbus.api.Event;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.spawner.WorldEntitySpawner;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.IServerWorld;
import net.minecraft.world.level.spawner.AbstractSpawner;
import net.minecraftforge.common.ForgeHooks;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.world.level.biome.MobSpawnInfo;
import java.util.List;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.function.Supplier;
import net.minecraft.world.level.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;
import net.minecraftforge.server.ServerLifecycleHooks;
import net.minecraft.server.MinecraftServer;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import java.util.UUID;
import java.util.Random;

public class EntityUtils
{
    private static final Random rand;
    
    @Nullable
    public static Player getPlayer(final UUID playerUUID, final LogicalSide side) {
        return side.isClient() ? getPlayerClient(playerUUID) : getPlayerServer(playerUUID);
    }
    
    @Nullable
    public static Player getPlayerServer(final UUID playerUUID) {
        final MinecraftServer server = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return null;
        }
        return (Player)server.getPlayerList().getPlayer(playerUUID);
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public static Player getPlayerClient(final UUID playerUUID) {
        final ClientLevel clWorld = Minecraft.getInstance().level;
        if (clWorld == null) {
            return null;
        }
        return clWorld.getPlayerByUUID(playerUUID);
    }
    
    public static void applyPotionEffectAtHalf(final LivingEntity entity, final MobEffectInstance effect) {
        final MobEffectInstance activeEffect = entity.func_70660_b(effect.func_188419_a());
        if (activeEffect != null) {
            if (activeEffect.field_76460_b <= effect.field_76460_b / 2) {
                entity.func_195064_c(effect);
            }
        }
        else {
            entity.func_195064_c(effect);
        }
    }
    
    public static void applyVortexMotion(final Supplier<Vector3> positionSupplier, final Consumer<Vector3> addMotion, final Vector3 to, final double vortexRange, final double multiplier) {
        final Vector3 pos = positionSupplier.get();
        final double diffX = (to.getX() - pos.getX()) / vortexRange;
        final double diffY = (to.getY() - pos.getY()) / vortexRange;
        final double diffZ = (to.getZ() - pos.getZ()) / vortexRange;
        final double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
        if (1.0 - dist > 0.0) {
            final double dstFactorSq = (1.0 - dist) * (1.0 - dist);
            final Vector3 toAdd = new Vector3();
            toAdd.setX(diffX / dist * dstFactorSq * 0.15 * multiplier);
            toAdd.setY(diffY / dist * dstFactorSq * 0.15 * multiplier);
            toAdd.setZ(diffZ / dist * dstFactorSq * 0.15 * multiplier);
            addMotion.accept(toAdd);
        }
    }
    
    @Nullable
    public static LivingEntity performWorldSpawningAt(final ServerLevel world, final BlockPos pos, final MobCategory category, final MobSpawnType reason, final boolean ignoreWeighting, final int ignoreSpawnCheckFlags) {
        final Biome b = world.func_226691_t_(pos);
        final StructureManager mgr = world.func_241112_a_();
        List<MobSpawnInfo.Spawners> spawnList = world.getChunkSource().func_201711_g().func_230353_a_(b, mgr, MobCategory.MONSTER, pos);
        spawnList = ForgeEventFactory.getPotentialSpawns((IWorld)world, category, pos, (List)spawnList);
        spawnList.removeIf(s -> !s.field_242588_c.func_200720_b());
        MobSpawnInfo.Spawners entry;
        if (ignoreWeighting) {
            entry = MiscUtils.getRandomEntry(spawnList, EntityUtils.rand);
        }
        else {
            entry = MiscUtils.getWeightedRandomEntry((Collection<MobSpawnInfo.Spawners>)spawnList, EntityUtils.rand, ee -> ee.field_76292_a);
        }
        if (entry != null) {
            final float x = pos.getX() + 0.5f;
            final float y = (float)pos.getY();
            final float z = pos.getZ() + 0.5f;
            final BlockState state = world.getBlockState(pos);
            if (!state.func_215686_e((IBlockReader)world, pos) && canEntitySpawnHere(world, pos, (EntityType<? extends Entity>)entry.field_242588_c, reason, ignoreSpawnCheckFlags, null)) {
                MobEntity entity;
                try {
                    entity = (MobEntity)entry.field_242588_c.func_200721_a((Level)world);
                }
                catch (final Exception exception) {
                    return null;
                }
                if (entity == null) {
                    return null;
                }
                entity.func_70012_b((double)x, (double)y, (double)z, EntityUtils.rand.nextFloat() * 360.0f, 0.0f);
                final int result = ForgeHooks.canEntitySpawn(entity, (IWorld)world, (double)x, (double)y, (double)z, (AbstractSpawner)null, reason);
                if (result == -1) {
                    return null;
                }
                if (!ForgeEventFactory.doSpecialSpawn(entity, (Level)world, x, y, z, (AbstractSpawner)null, reason)) {
                    entity.func_213386_a((IServerWorld)world, world.func_175649_E(pos), reason, (SpawnGroupData)null, (CompoundTag)null);
                }
                world.func_242417_l((Entity)entity);
                return (LivingEntity)entity;
            }
        }
        return null;
    }
    
    public static boolean canEntitySpawnHere(final ServerLevel world, final BlockPos at, final EntityType<? extends Entity> type, final MobSpawnType spawnReason, final int ignoreCheckFlags, @Nullable final Consumer<Entity> preCheckEntity) {
        if (type.func_220339_d() == MobCategory.MISC || !type.func_200720_b() || !world.func_175723_af().func_177746_a(at)) {
            return false;
        }
        if (!SpawnConditionFlags.isSet(ignoreCheckFlags, 1)) {
            final SpawnPlacements.PlacementType placementType = SpawnPlacements.func_209344_a((EntityType)type);
            if (!WorldEntitySpawner.canSpawnAtBody(placementType, (IWorldReader)world, at, (EntityType)type)) {
                return false;
            }
            if (!SpawnPlacements.func_223515_a((EntityType)type, (IServerWorld)world, spawnReason, at, EntityUtils.rand)) {
                return false;
            }
        }
        if (!SpawnConditionFlags.isSet(ignoreCheckFlags, 4) && !world.func_226664_a_(type.func_220328_a(at.getX() + 0.5, (double)at.getY(), at.getZ() + 0.5))) {
            return false;
        }
        final Entity entity = type.func_200721_a((Level)world);
        if (entity == null) {
            return false;
        }
        entity.func_70012_b(at.getX() + 0.5, at.getY() + 0.5, at.getZ() + 0.5, world.field_73012_v.nextFloat() * 360.0f, 0.0f);
        if (preCheckEntity != null) {
            preCheckEntity.accept(entity);
        }
        if (entity instanceof LivingEntity && entity instanceof MobEntity) {
            final MobEntity mobEntity = (MobEntity)entity;
            final Event.Result canSpawn = ForgeEventFactory.canEntitySpawn(mobEntity, (IWorld)world, entity.getX(), entity.getY(), entity.getZ(), (AbstractSpawner)null, spawnReason);
            if (canSpawn == Event.Result.DENY) {
                return false;
            }
            if (canSpawn == Event.Result.DEFAULT) {
                if (!SpawnConditionFlags.isSet(ignoreCheckFlags, 8) && !mobEntity.func_213380_a((IWorld)world, spawnReason)) {
                    return false;
                }
                if (!SpawnConditionFlags.isSet(ignoreCheckFlags, 2) && !mobEntity.func_205019_a((IWorldReader)world)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Nonnull
    public static List<ItemStack> generateLoot(final LivingEntity entity, final Random rand, final DamageSource srcDeath, @Nullable final LivingEntity lastAttacker) {
        final MinecraftServer srv = (MinecraftServer)ServerLifecycleHooks.getCurrentServer();
        final ServerLevel sw = (ServerLevel)entity.level();
        if (!sw.func_82736_K().func_223586_b(GameRules.field_223602_e)) {
            return Collections.emptyList();
        }
        final ResourceLocation lootTableKey = entity.getType().getDefaultLootTable();
        final LootTable table = srv.getLootTables().get(lootTableKey);
        final LootContext.Builder builder = new LootContext.Builder(sw).withRandom(rand).withParameter(LootContextParams.THIS_ENTITY, (Object)entity).withParameter(LootContextParams.LAST_DAMAGE_PLAYER, (Object)entity.position()).withParameter(LootContextParams.THIS_ENTITY, (Object)srcDeath).withOptionalParameter(LootContextParams.DAMAGE_SOURCE, srcDeath.getDirectEntity()).withOptionalParameter(LootContextParams.KILLER_ENTITY, srcDeath.getDirectEntity());
        if (lastAttacker != null && lastAttacker instanceof Player) {
            builder.withParameter(LootContextParams.KILLER_ENTITY, (Object)lastAttacker).withLuck(((Player)lastAttacker).getLuck());
        }
        return table.func_216113_a(builder.func_216022_a(LootParameterSets.field_216263_d));
    }
    
    @Nullable
    public static <T extends Entity> T getClosestEntity(final IWorld world, final Class<T> type, final AABB box, final Vector3 closestTo) {
        final List<T> entities = world.func_175647_a((Class)type, box, Entity::func_70089_S);
        return selectClosest(entities, closestTo::distanceSquared);
    }
    
    public static Predicate<? super Entity> selectEntities(final Class<? extends Entity>... entities) {
        return (Predicate<? super Entity>)(entity -> {
            if (entity == null || !entity.isAlive()) {
                return false;
            }
            final Class<? extends Entity> clazz = entity.getClass();
            for (final Class<? extends Entity> test : entities) {
                if (test.isAssignableFrom(clazz)) {
                    return true;
                }
            }
            return false;
        });
    }
    
    public static Predicate<? super Entity> selectItemClassInstanceof(final Class<?> itemClass) {
        return (Predicate<? super Entity>)(entity -> {
            if (entity == null || !entity.isAlive()) {
                return false;
            }
            if (!(entity instanceof ItemEntity)) {
                return false;
            }
            final ItemStack i = ((ItemEntity)entity).func_92059_d();
            return !i.isEmpty() && itemClass.isAssignableFrom(i.getItem().getClass());
        });
    }
    
    public static Predicate<? super Entity> selectItem(final Item item) {
        return (Predicate<? super Entity>)(entity -> {
            if (entity == null || !entity.isAlive()) {
                return false;
            }
            if (!(entity instanceof ItemEntity)) {
                return false;
            }
            final ItemStack i = ((ItemEntity)entity).func_92059_d();
            return !i.isEmpty() && i.getItem().equals(item);
        });
    }
    
    public static Predicate<? super Entity> selectItemStack(final Function<ItemStack, Boolean> acceptor) {
        return (Predicate<? super Entity>)(entity -> {
            if (entity == null || !entity.isAlive()) {
                return false;
            }
            if (!(entity instanceof ItemEntity)) {
                return false;
            }
            final ItemStack i = ((ItemEntity)entity).func_92059_d();
            return !i.isEmpty() && acceptor.apply(i);
        });
    }
    
    @Nullable
    public static <T> T selectClosest(final Collection<T> elements, final Function<T, Double> dstFunc) {
        if (elements.isEmpty()) {
            return null;
        }
        double dstClosest = Double.MAX_VALUE;
        T closestElement = null;
        for (final T element : elements) {
            final double dst = dstFunc.apply(element);
            if (dst < dstClosest) {
                closestElement = element;
                dstClosest = dst;
            }
        }
        return closestElement;
    }
    
    static {
        rand = new Random();
    }
    
    public static class SpawnConditionFlags
    {
        public static final int IGNORE_PLACEMENT_RULES = 1;
        public static final int IGNORE_ENTITY_COLLISION = 2;
        public static final int IGNORE_BLOCK_COLLISION = 4;
        public static final int IGNORE_ENTITY_SPAWN_CONDITIONS = 8;
        public static final int IGNORE_COLLISIONS = 6;
        public static final int IGNORE_SPAWN_CONDITIONS = 9;
        public static final int IGNORE_ALL = 15;
        
        public static boolean isSet(final int flags, final int flag) {
            return (flags & flag) != 0x0;
        }
    }
}
