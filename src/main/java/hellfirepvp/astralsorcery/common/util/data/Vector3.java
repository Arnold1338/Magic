package hellfirepvp.astralsorcery.common.util.data;

import org.joml.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.function.Consumer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.core.BlockPos;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;
import java.util.Random;

public class Vector3
{
    private static final Random RAND;
    protected double x;
    protected double y;
    protected double z;
    
    public Vector3() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
    }
    
    public Vector3(final Vector3 copy) {
        this.x = copy.x;
        this.y = copy.y;
        this.z = copy.z;
    }
    
    public Vector3(final int x, final int y, final int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(final double x, final double y, final double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(final Vec3i pos) {
        this(pos.getX(), pos.getY(), pos.getZ());
    }
    
    public Vector3(final Vec3 vec) {
        this(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
    }
    
    public Vector3(final BlockEntity te) {
        this(te.getBlockState().getX(), te.getBlockState().getY(), te.getBlockState().getZ());
    }
    
    public static Vector3 atEntityCorner(final Entity entity) {
        return new Vector3(entity.position());
    }
    
    @Deprecated
    public static Vector3 atEntityCenter(final Entity entity) {
        return atEntityCorner(entity).addY(entity.func_213302_cg() / 2.0f);
    }
    
    public static Vector3 getMin(final AABB box) {
        return new Vector3(box.field_72340_a, box.field_72338_b, box.field_72339_c);
    }
    
    public static Vector3 getMax(final AABB box) {
        return new Vector3(box.field_72336_d, box.field_72337_e, box.field_72334_f);
    }
    
    public static Vector3 directionFromYawPitch(final float yaw, final float pitch) {
        final float radYaw = yaw * 0.017453292f;
        final float radPitch = pitch * 0.017453292f;
        final float x = -Mth.func_76126_a(radYaw) * Mth.func_76134_b(radPitch);
        final float y = -Mth.func_76126_a(radPitch);
        final float z = Mth.func_76134_b(radYaw) * Mth.func_76134_b(radPitch);
        return new Vector3(x, y, z);
    }
    
    public Vector3 add(final Vec3i vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }
    
    public Vector3 add(final Vec3 vec) {
        this.x += vec.getX();
        this.y += vec.getY();
        this.z += vec.getZ();
        return this;
    }
    
    public Vector3 add(final Vector3 vec) {
        this.x += vec.x;
        this.y += vec.y;
        this.z += vec.z;
        return this;
    }
    
    public Vector3 add(final float x, final float y, final float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3 add(final double x, final double y, final double z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }
    
    public Vector3 addX(final double x) {
        this.x += x;
        return this;
    }
    
    public Vector3 addY(final double y) {
        this.y += y;
        return this;
    }
    
    public Vector3 addZ(final double z) {
        this.z += z;
        return this;
    }
    
    public Vector3 subtract(final double vX, final double vY, final double vZ) {
        this.x -= vX;
        this.y -= vY;
        this.z -= vZ;
        return this;
    }
    
    public Vector3 subtract(final Entity e) {
        this.x -= e.getX();
        this.y -= e.getY();
        this.z -= e.getZ();
        return this;
    }
    
    public Vector3 subtract(final Vec3i vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }
    
    public Vector3 subtract(final Vec3 vec) {
        this.x -= vec.getX();
        this.y -= vec.getY();
        this.z -= vec.getZ();
        return this;
    }
    
    public Vector3 subtract(final Vector3 vec) {
        this.x -= vec.x;
        this.y -= vec.y;
        this.z -= vec.z;
        return this;
    }
    
    public Vector3 multiply(final Vector3 vec) {
        this.x *= vec.x;
        this.y *= vec.y;
        this.z *= vec.z;
        return this;
    }
    
    public Vector3 divide(final Vector3 vec) {
        this.x /= vec.x;
        this.y /= vec.y;
        this.z /= vec.z;
        return this;
    }
    
    public Vector3 divide(final double divisor) {
        this.x /= divisor;
        this.y /= divisor;
        this.z /= divisor;
        return this;
    }
    
    public Vector3 negate() {
        return this.multiply(-1);
    }
    
    public Vector3 copy(final Vector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = vec.z;
        return this;
    }
    
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }
    
    public double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }
    
    public double distance(final Entity e) {
        return Math.sqrt(this.distanceSquared(e));
    }
    
    public double distanceSquared(final Entity e) {
        return this.distanceSquared(atEntityCorner(e));
    }
    
    public double distance(final Vector3 o) {
        return Math.sqrt(this.distanceSquared(o));
    }
    
    public double distanceSquared(final Vector3 o) {
        final double difX = this.x - o.x;
        final double difY = this.y - o.y;
        final double difZ = this.z - o.z;
        return difX * difX + difY * difY + difZ * difZ;
    }
    
    public double distance(final Vec3i o) {
        return Math.sqrt(this.distanceSquared(o));
    }
    
    public double distanceSquared(final Vec3i o) {
        final double difX = this.x - o.getX();
        final double difY = this.y - o.getY();
        final double difZ = this.z - o.getZ();
        return difX * difX + difY * difY + difZ * difZ;
    }
    
    public double distance(final Vec3 o) {
        return Math.sqrt(this.distanceSquared(o));
    }
    
    public double distanceSquared(final Vec3 o) {
        final double difX = this.x - o.field_72450_a;
        final double difY = this.y - o.field_72448_b;
        final double difZ = this.z - o.field_72449_c;
        return difX * difX + difY * difY + difZ * difZ;
    }
    
    public float angle(final Vector3 other) {
        final double dot = this.dot(other) / (this.length() * other.length());
        return (float)Math.acos(dot);
    }
    
    public Vector3 getMidpoint(final Vector3 other) {
        final double x = (this.x + other.x) / 2.0;
        final double y = (this.y + other.y) / 2.0;
        final double z = (this.z + other.z) / 2.0;
        return new Vector3(x, y, z);
    }
    
    public Vector3 multiply(final int m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }
    
    public Vector3 multiply(final double m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }
    
    public Vector3 multiply(final float m) {
        this.x *= m;
        this.y *= m;
        this.z *= m;
        return this;
    }
    
    public double dot(final Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
    
    public Vector3 abs() {
        this.x = Math.abs(this.x);
        this.y = Math.abs(this.y);
        this.z = Math.abs(this.z);
        return this;
    }
    
    public Vector3 crossProduct(final Vector3 o) {
        final double newX = this.y * o.z - o.y * this.z;
        final double newY = this.z * o.x - o.z * this.x;
        final double newZ = this.x * o.y - o.x * this.y;
        this.x = newX;
        this.y = newY;
        this.z = newZ;
        return this;
    }
    
    public Vector3 perpendicular() {
        if (this.z == 0.0) {
            return this.zCrossProduct();
        }
        return this.xCrossProduct();
    }
    
    public Vector3 xCrossProduct() {
        final double d = this.z;
        final double d2 = -this.y;
        this.x = 0.0;
        this.y = d;
        this.z = d2;
        return this;
    }
    
    public Vector3 zCrossProduct() {
        final double d = this.y;
        final double d2 = -this.x;
        this.x = d;
        this.y = d2;
        this.z = 0.0;
        return this;
    }
    
    public Vector3 yCrossProduct() {
        final double d = -this.z;
        final double d2 = this.x;
        this.x = d;
        this.y = 0.0;
        this.z = d2;
        return this;
    }
    
    public Vector3 rotate(final double angle, final Vector3 axis) {
        Quat.buildQuatFrom3DVector(axis.clone().normalize(), angle).rotateWithMagnitude(this);
        return this;
    }
    
    public Vector3 normalize() {
        final double length = this.length();
        this.x /= length;
        this.y /= length;
        this.z /= length;
        return this;
    }
    
    public Vector3 fNormalize() {
        double lengthSq = this.lengthSquared();
        lengthSq = fastInvSqrt(lengthSq);
        this.x *= lengthSq;
        this.y *= lengthSq;
        this.z *= lengthSq;
        return this;
    }
    
    public static double fastInvSqrt(double x) {
        final double xhalf = 0.5 * x;
        long i = Double.doubleToLongBits(x);
        i = 6910470738111508698L - (i >> 1);
        x = Double.longBitsToDouble(i);
        for (int it = 0; it < 4; ++it) {
            x *= 1.5 - xhalf * x * x;
        }
        return x;
    }
    
    public Vector3 zero() {
        this.x = 0.0;
        this.y = 0.0;
        this.z = 0.0;
        return this;
    }
    
    public void toBytes(final ByteBuf buf) {
        buf.writeDouble(this.x);
        buf.writeDouble(this.y);
        buf.writeDouble(this.z);
    }
    
    public static Vector3 fromBytes(final ByteBuf buf) {
        return new Vector3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }
    
    public static Vector3 random() {
        return new Vector3(Vector3.RAND.nextDouble() * (Vector3.RAND.nextBoolean() ? 1 : -1), Vector3.RAND.nextDouble() * (Vector3.RAND.nextBoolean() ? 1 : -1), Vector3.RAND.nextDouble() * (Vector3.RAND.nextBoolean() ? 1 : -1));
    }
    
    public static Vector3 random(final Random rand) {
        return new Vector3(rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), rand.nextDouble() * (rand.nextBoolean() ? 1 : -1));
    }
    
    public static Vector3 positiveRandom() {
        return new Vector3(Vector3.RAND.nextDouble(), Vector3.RAND.nextDouble(), Vector3.RAND.nextDouble());
    }
    
    public static Vector3 positiveRandom(final Random rand) {
        return new Vector3(rand.nextDouble(), rand.nextDouble(), rand.nextDouble());
    }
    
    public static Vector3 positiveYRandom() {
        final Vector3 rand = random();
        return rand.setY(Math.abs(rand.getY()));
    }
    
    public static Vector3 positiveYRandom(final Random r) {
        final Vector3 rand = random(r);
        return rand.setY(Math.abs(rand.getY()));
    }
    
    public boolean isInAABB(final Vector3 min, final Vector3 max) {
        return this.x >= min.x && this.x <= max.x && this.y >= min.y && this.y <= max.y && this.z >= min.z && this.z <= max.z;
    }
    
    public boolean isInSphere(final Vector3 origin, final double radius) {
        final double difX = origin.x - this.x;
        final double difY = origin.y - this.y;
        final double difZ = origin.z - this.z;
        return difX * difX + difY * difY + difZ * difZ <= radius * radius;
    }
    
    public Vec3 toVector3d() {
        return new Vec3(this.x, this.y, this.z);
    }
    
    public BlockPos toBlockPos() {
        return new BlockPos(Mth.func_76128_c(this.x), Mth.func_76128_c(this.y), Mth.func_76128_c(this.z));
    }
    
    public ChunkPos toChunkPos() {
        return new ChunkPos(Mth.func_76128_c(this.x) >> 4, Mth.func_76128_c(this.z) >> 4);
    }
    
    public Vector3 vectorFromHereTo(final Vector3 target) {
        return new Vector3(target.x - this.x, target.y - this.y, target.z - this.z);
    }
    
    public Vector3 vectorFromHereTo(final double tX, final double tY, final double tZ) {
        return new Vector3(tX - this.x, tY - this.y, tZ - this.z);
    }
    
    public void stepAlongVector(final double stepWidth, final Consumer<Vector3> consumer) {
        final int steps = (int)Math.round(this.length() / stepWidth);
        final Vector3 step = this.clone().divide(steps);
        final Vector3 at = new Vector3();
        consumer.accept(at.clone());
        for (int i = 0; i < steps; ++i) {
            at.add(step);
            consumer.accept(at.clone());
        }
    }
    
    public Vector3 copyToPolar() {
        final double length = this.length();
        double theta = Math.acos(this.y / length);
        double phi = Math.atan2(this.x, this.z);
        theta = Math.toDegrees(theta);
        phi = 180.0 + Math.toDegrees(phi);
        return new Vector3(length, theta, phi);
    }
    
    public Vector3 copyInterpolateWith(final Vector3 next, final float partial) {
        return new Vector3((this.x == next.x) ? this.x : (this.x + (next.x - this.x) * partial), (this.y == next.y) ? this.y : (this.y + (next.y - this.y) * partial), (this.z == next.z) ? this.z : (this.z + (next.z - this.z) * partial));
    }
    
    @Deprecated
    @OnlyIn(Dist.CLIENT)
    public VertexConsumer drawPos(final VertexConsumer buf) {
        buf.func_225582_a_((double)(float)this.x, (double)(float)this.y, (double)(float)this.z);
        return buf;
    }
    
    @OnlyIn(Dist.CLIENT)
    public VertexConsumer drawPos(final Matrix4f renderMatrix, final VertexConsumer buf) {
        buf.vertex(renderMatrix, (float)this.x, (float)this.y, (float)this.z);
        return buf;
    }
    
    public double getX() {
        return this.x;
    }
    
    public int getBlockX() {
        return (int)Math.floor(this.x);
    }
    
    public double getY() {
        return this.y;
    }
    
    public int getBlockY() {
        return (int)Math.floor(this.y);
    }
    
    public double getZ() {
        return this.z;
    }
    
    public int getBlockZ() {
        return (int)Math.floor(this.z);
    }
    
    public Vector3 setX(final int x) {
        this.x = x;
        return this;
    }
    
    public Vector3 setX(final double x) {
        this.x = x;
        return this;
    }
    
    public Vector3 setX(final float x) {
        this.x = x;
        return this;
    }
    
    public Vector3 setY(final int y) {
        this.y = y;
        return this;
    }
    
    public Vector3 setY(final double y) {
        this.y = y;
        return this;
    }
    
    public Vector3 setY(final float y) {
        this.y = y;
        return this;
    }
    
    public Vector3 setZ(final int z) {
        this.z = z;
        return this;
    }
    
    public Vector3 setZ(final double z) {
        this.z = z;
        return this;
    }
    
    public Vector3 setZ(final float z) {
        this.z = z;
        return this;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Vector3)) {
            return false;
        }
        final Vector3 other = (Vector3)obj;
        return Math.abs(this.x - other.x) < 1.0E-4 && Math.abs(this.y - other.y) < 1.0E-4 && Math.abs(this.z - other.z) < 1.0E-4 && this.getClass().equals(obj.getClass());
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.x) ^ Double.doubleToLongBits(this.x) >>> 32);
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.y) ^ Double.doubleToLongBits(this.y) >>> 32);
        hash = 79 * hash + (int)(Double.doubleToLongBits(this.z) ^ Double.doubleToLongBits(this.z) >>> 32);
        return hash;
    }
    
    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }
    
    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }
    
    static {
        RAND = new Random();
    }
    
    public static class Quat
    {
        public double i;
        public double j;
        public double k;
        public double s;
        
        public Quat() {
            this(1.0);
        }
        
        public Quat(final double zeroMag) {
            this.s = zeroMag;
            this.i = 0.0;
            this.j = 0.0;
            this.k = 0.0;
        }
        
        public Quat(final Quat quat) {
            this.i = quat.i;
            this.j = quat.j;
            this.k = quat.k;
            this.s = quat.s;
        }
        
        public Quat(final double w, final double i, final double j, final double k) {
            this.i = i;
            this.j = j;
            this.k = k;
            this.s = w;
        }
        
        public void set(final Quat quat) {
            this.i = quat.i;
            this.j = quat.j;
            this.k = quat.k;
            this.s = quat.s;
        }
        
        public static Quat buildQuatWithAngle(final double ax, final double ay, final double az, double angle) {
            angle *= 0.5;
            final double d4 = Math.sin(angle);
            return new Quat(Math.cos(angle), ax * d4, ay * d4, az * d4);
        }
        
        public void leftMultiply(final Quat quat) {
            final double d = this.s * quat.s - this.i * quat.i - this.j * quat.j - this.k * quat.k;
            final double d2 = this.s * quat.i + this.i * quat.s - this.j * quat.k + this.k * quat.j;
            final double d3 = this.s * quat.j + this.i * quat.k + this.j * quat.s - this.k * quat.i;
            final double d4 = this.s * quat.k - this.i * quat.j + this.j * quat.i + this.k * quat.s;
            this.s = d;
            this.i = d2;
            this.j = d3;
            this.k = d4;
        }
        
        public void rightMultiply(final Quat quat) {
            final double d = this.s * quat.s - this.i * quat.i - this.j * quat.j - this.k * quat.k;
            final double d2 = this.s * quat.i + this.i * quat.s + this.j * quat.k - this.k * quat.j;
            final double d3 = this.s * quat.j - this.i * quat.k + this.j * quat.s + this.k * quat.i;
            final double d4 = this.s * quat.k + this.i * quat.j - this.j * quat.i + this.k * quat.s;
            this.s = d;
            this.i = d2;
            this.j = d3;
            this.k = d4;
        }
        
        public double mag() {
            return Math.sqrt(this.i * this.i + this.j * this.j + this.k * this.k + this.s * this.s);
        }
        
        public void normalize() {
            double d = this.mag();
            if (d == 0.0) {
                return;
            }
            d = 1.0 / d;
            this.i *= d;
            this.j *= d;
            this.k *= d;
            this.s *= d;
        }
        
        public void rotateWithMagnitude(final Vector3 vec) {
            final double d = -this.i * vec.x - this.j * vec.y - this.k * vec.z;
            final double d2 = this.s * vec.x + this.j * vec.z - this.k * vec.y;
            final double d3 = this.s * vec.y - this.i * vec.z + this.k * vec.x;
            final double d4 = this.s * vec.z + this.i * vec.y - this.j * vec.x;
            vec.x = d2 * this.s - d * this.i - d3 * this.k + d4 * this.j;
            vec.y = d3 * this.s - d * this.j + d2 * this.k - d4 * this.i;
            vec.z = d4 * this.s - d * this.k - d2 * this.j + d3 * this.i;
        }
        
        @Override
        public String toString() {
            return String.format("Quaternionf: { s=%f, i=%f, j=%f, k=%f }", this.s, this.i, this.j, this.k);
        }
        
        public static Quat buildQuatFrom3DVector(final Vector3 axis, final double angle) {
            return buildQuatWithAngle(axis.x, axis.y, axis.z, angle);
        }
    }
    
    public static class RotAxis
    {
        public static final Vector3 X_AXIS;
        public static final Vector3 Y_AXIS;
        public static final Vector3 Z_AXIS;
        
        static {
            X_AXIS = new Vector3(1, 0, 0);
            Y_AXIS = new Vector3(0, 1, 0);
            Z_AXIS = new Vector3(0, 0, 1);
        }
    }
}
