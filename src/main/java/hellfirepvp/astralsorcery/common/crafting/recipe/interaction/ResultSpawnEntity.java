package hellfirepvp.astralsorcery.common.crafting.recipe.interaction;


import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import net.minecraft.network.FriendlyByteBuf;
import com.google.gson.JsonParseException;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import com.google.gson.JsonObject;
import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ResultSpawnEntity extends InteractionResult
{
    private EntityType<?> entityType;
    
    ResultSpawnEntity() {
        super(InteractionResultRegistry.ID_SPAWN_ENTITY);
    }
    
    public static ResultSpawnEntity spawnEntity(final EntityType<?> type) {
        if (!type.func_200720_b()) {
            throw new IllegalArgumentException("EntityType " + type.getRegistryName() + " is not summonable!");
        }
        final ResultSpawnEntity drop = new ResultSpawnEntity();
        drop.entityType = type;
        return drop;
    }
    
    public EntityType<?> getEntityType() {
        return this.entityType;
    }
    
    @Override
    public void doResult(final Level world, final Vector3 at) {
        final Entity e = this.entityType.func_200721_a(world);
        if (!(e instanceof LivingEntity)) {
            return;
        }
        e.func_70012_b(at.getX(), at.getY(), at.getZ(), world.field_73012_v.nextFloat() * 360.0f, 0.0f);
        world.addFreshEntity(e);
    }
    
    @Override
    public void read(final JsonObject json) throws JsonParseException {
        final ResourceLocation key = new ResourceLocation(JSONUtils.func_151200_h(json, "entityType"));
        final EntityType<?> type = (EntityType<?>)ForgeRegistries.ENTITIES.getValue(key);
        if (type == null) {
            throw new JsonParseException("Unknown entity type: " + key);
        }
        this.entityType = type;
    }
    
    @Override
    public void write(final JsonObject json) {
        json.addProperty("entityType", this.entityType.getRegistryName().toString());
    }
    
    @Override
    public void read(final FriendlyByteBuf buf) {
        this.entityType = (EntityType<?>)ByteBufUtils.readRegistryEntry(buf);
    }
    
    @Override
    public void write(final FriendlyByteBuf buf) {
        ByteBufUtils.writeRegistryEntry(buf, (net.minecraftforge.registries.Object<Object>)this.entityType);
    }
}
