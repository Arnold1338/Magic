package hellfirepvp.astralsorcery.common.base.patreon.entity;

import java.util.Objects;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectHelper;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import net.minecraft.world.level.Level;
import net.minecraft.resources.ResourceKey;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import java.util.UUID;
import java.util.Random;

public class PatreonPartialEntity
{
    protected static final Random rand;
    private final UUID ownerUUID;
    private final UUID effectUUID;
    protected Vector3 pos;
    protected Vector3 prevPos;
    protected Vector3 motion;
    protected boolean removed;
    protected boolean updatePos;
    private RegistryKey<Level> lastTickedDimension;
    
    public PatreonPartialEntity(final UUID effectUUID, final UUID ownerUUID) {
        this.pos = new Vector3();
        this.prevPos = new Vector3();
        this.motion = new Vector3();
        this.removed = false;
        this.updatePos = false;
        this.lastTickedDimension = null;
        this.effectUUID = effectUUID;
        this.ownerUUID = ownerUUID;
    }
    
    public UUID getEffectUUID() {
        return this.effectUUID;
    }
    
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }
    
    public Vector3 getPos() {
        return this.pos;
    }
    
    public void setRemoved(final boolean removed) {
        this.removed = removed;
    }
    
    @Nullable
    public PatreonEffect getEffect() {
        return PatreonEffectHelper.getEffect(this.getEffectUUID());
    }
    
    @Nullable
    public RegistryKey<Level> getLastTickedDimension() {
        return this.lastTickedDimension;
    }
    
    @OnlyIn(Dist.CLIENT)
    public void tickClient() {
    }
    
    @OnlyIn(Dist.CLIENT)
    public void tickEffects(final Level world) {
    }
    
    public boolean tick(final Level world) {
        boolean changed = this.lastTickedDimension == null || !this.lastTickedDimension.equals(world.dimension());
        this.lastTickedDimension = (RegistryKey<Level>)world.dimension();
        if (this.updateMotion((IWorld)world)) {
            changed = true;
        }
        if (this.tryMoveEntity((IWorld)world)) {
            changed = true;
        }
        if (world.level()) {
            this.tickEffects(world);
        }
        return changed;
    }
    
    private boolean updateMotion(final IWorld world) {
        final Vector3 prevMot = this.motion.clone();
        final Player target = this.findOwner(world);
        if (target == null) {
            this.motion = new Vector3();
        }
        else {
            final Vector3 moveTarget = Vector3.atEntityCenter((Entity)target).addY(1.5);
            if (moveTarget.distanceSquared(this.pos) <= 3.0) {
                this.motion.multiply(0.95f);
            }
            else {
                final double diffX = (moveTarget.getX() - this.pos.getX()) / 8.0;
                final double diffY = (moveTarget.getY() - this.pos.getY()) / 8.0;
                final double diffZ = (moveTarget.getZ() - this.pos.getZ()) / 8.0;
                final double dist = Math.sqrt(diffX * diffX + diffY * diffY + diffZ * diffZ);
                this.motion = new Vector3(diffX * dist, diffY * dist, diffZ * dist);
            }
        }
        return !this.motion.equals(prevMot);
    }
    
    private boolean tryMoveEntity(final IWorld world) {
        this.prevPos = this.pos.clone();
        final Player owner = this.findOwner(world);
        if (owner != null && this.pos.distance(Vector3.atEntityCenter((Entity)owner)) >= 16.0) {
            this.placeNear(owner);
            return true;
        }
        this.pos.add(this.motion);
        return !this.pos.equals(this.prevPos);
    }
    
    public void placeNear(final Player player) {
        this.pos = Vector3.atEntityCenter((Entity)player).setY(player.getY()).addY(player.func_213302_cg()).add(Vector3.random().setY(0).normalize());
        this.prevPos = this.pos.clone();
        this.motion = new Vector3();
        this.updatePos = true;
    }
    
    @Nullable
    public Player findOwner(final IWorld world) {
        return world.getPlayerByUUID(this.ownerUUID);
    }
    
    public void readFromNBT(final CompoundTag cmp) {
        if (cmp.contains("lastTickedDimension")) {
            final ResourceLocation worldKey = new ResourceLocation(cmp.getString("lastTickedDimension"));
            this.lastTickedDimension = (RegistryKey<Level>)RegistryKey.func_240903_a_(Registry.field_239699_ae_, worldKey);
        }
        else {
            this.lastTickedDimension = null;
        }
        if (cmp.contains("pos") && cmp.contains("prevPos")) {
            this.pos = NBTHelper.readVector3(cmp.func_74775_l("pos"));
            this.prevPos = NBTHelper.readVector3(cmp.func_74775_l("prevPos"));
        }
    }
    
    public void writeToNBT(final CompoundTag cmp) {
        if (this.lastTickedDimension != null) {
            cmp.putString("lastTickedDimension", this.lastTickedDimension.func_240901_a_().toString());
        }
        if (this.updatePos) {
            cmp.put("pos", (Tag)NBTHelper.writeVector3(this.pos));
            cmp.put("prevPos", (Tag)NBTHelper.writeVector3(this.prevPos));
            this.updatePos = false;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PatreonPartialEntity that = (PatreonPartialEntity)o;
        return Objects.equals(this.effectUUID, that.effectUUID);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.effectUUID);
    }
    
    static {
        rand = new Random();
    }
}
