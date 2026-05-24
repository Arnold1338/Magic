package hellfirepvp.astralsorcery.common.entity;

import net.minecraft.world.entity.ai.control.MoveControl;
import hellfirepvp.astralsorcery.common.entity.goal.SpectralToolMeleeAttackGoal;
import hellfirepvp.astralsorcery.common.entity.goal.SpectralToolBreakLogGoal;
import hellfirepvp.astralsorcery.common.entity.goal.SpectralToolBreakBlockGoal;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.Items;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectPelotrio;
import java.util.function.BiFunction;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.EntityDataSerializers;
import javax.annotation.Nonnull;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.common.util.DamageUtil;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.entity.Mob;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.level.entity.EntityType;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.level.level.Level;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.entity.goal.SpectralToolGoal;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.FlyingMob;

public class EntitySpectralTool extends FlyingMob
{
    private static final EntityDataAccessor<ItemStack> ITEM;
    private LivingEntity owningEntity;
    private SpectralToolGoal task;
    private BlockPos startPosition;
    private int remainingTime;
    private int idleTime;
    
    public EntitySpectralTool(final World worldIn) {
        super((EntityType)EntityTypesAS.SPECTRAL_TOOL, worldIn);
        this.owningEntity = null;
        this.task = null;
        this.startPosition = null;
        this.remainingTime = 0;
        this.idleTime = 0;
        this.field_70765_h = (MoveControl)new FlyingMoveControl((MobEntity)this, 10, false);
    }
    
    public EntitySpectralTool(final World worldIn, final BlockPos spawnPos, final LivingEntity owner, final ToolTask task) {
        this(worldIn);
        this.setPos(spawnPos.getX() + 0.5, spawnPos.getY() + 0.5, (double)spawnPos.getZ());
        this.setItem(task.displayStack);
        this.startPosition = spawnPos;
        this.owningEntity = owner;
        this.task = task.createGoal(this);
        this.field_70714_bg.func_75776_a(1, (Goal)this.task);
        this.remainingTime = task.maxAge + this.field_70146_Z.nextInt(task.maxAge);
    }
    
    public static EntityType.IFactory<EntitySpectralTool> factory() {
        return (EntityType.IFactory<EntitySpectralTool>)((type, world) -> new EntitySpectralTool(world));
    }
    
    public static AttributeSupplier.MutableAttribute createAttributes() {
        return MobEntity.func_233666_p_().func_233815_a_(Attributes.field_233818_a_, 3.0).func_233815_a_(Attributes.field_233822_e_, 0.85);
    }
    
    protected void func_70088_a() {
        super.func_70088_a();
        this.func_184212_Q().func_187214_a((EntityDataAccessor)EntitySpectralTool.ITEM, (Object)ItemStack.field_190927_a);
    }
    
    public boolean func_241849_j(final Entity entity) {
        return !(entity instanceof Player);
    }
    
    public boolean func_70104_M() {
        return false;
    }
    
    protected boolean func_225502_at_() {
        return false;
    }
    
    public void func_70071_h_() {
        super.tick();
        if (this.func_130014_f_().func_201670_d()) {
            this.tickClient();
        }
        else {
            if (this.startPosition == null) {
                this.func_70106_y();
                return;
            }
            if (!this.task.func_75250_a()) {
                ++this.idleTime;
                if (this.idleTime >= 30) {
                    this.func_70106_y();
                    return;
                }
            }
            else {
                this.idleTime = 0;
            }
            --this.remainingTime;
            if (this.remainingTime <= 0) {
                DamageUtil.attackEntityFrom((Entity)this, CommonProxy.DAMAGE_SOURCE_STELLAR, 50.0f);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void tickClient() {
        if (this.field_70146_Z.nextFloat() < 0.2f) {
            final Vector3 at = Vector3.atEntityCorner((Entity)this).add(this.field_70146_Z.nextFloat() * 0.3 * (this.field_70146_Z.nextBoolean() ? 1 : -1), this.field_70146_Z.nextFloat() * 0.3 * (this.field_70146_Z.nextBoolean() ? 1 : -1) + this.func_213302_cg() / 2.0f, this.field_70146_Z.nextFloat() * 0.3 * (this.field_70146_Z.nextBoolean() ? 1 : -1));
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.constant(ColorsAS.CONSTELLATION_TYPE_WEAK)).setScaleMultiplier(0.35f + this.field_70146_Z.nextFloat() * 0.25f).setMaxAge(30 + this.field_70146_Z.nextInt(20));
            if (this.field_70146_Z.nextBoolean()) {
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at).alpha(VFXAlphaFunction.FADE_OUT).color(VFXColorFunction.WHITE).setScaleMultiplier(0.2f + this.field_70146_Z.nextFloat() * 0.15f).setMaxAge(20 + this.field_70146_Z.nextInt(10));
            }
        }
    }
    
    public BlockPos getStartPosition() {
        return this.startPosition;
    }
    
    public LivingEntity getOwningEntity() {
        return this.owningEntity;
    }
    
    private void setItem(@Nonnull final ItemStack tool) {
        this.field_70180_af.func_187227_b((EntityDataAccessor)EntitySpectralTool.ITEM, (Object)tool);
    }
    
    @Nonnull
    public ItemStack getItem() {
        return (ItemStack)this.field_70180_af.func_187225_a((EntityDataAccessor)EntitySpectralTool.ITEM);
    }
    
    public void func_70108_f(final Entity entityIn) {
        if (!(entityIn instanceof Player) && !(entityIn instanceof EntitySpectralTool)) {
            super.func_70108_f(entityIn);
        }
    }
    
    protected void func_82167_n(final Entity entityIn) {
        if (!(entityIn instanceof Player) && !(entityIn instanceof EntitySpectralTool)) {
            super.func_70108_f(entityIn);
        }
    }
    
    static {
        ITEM = SynchedEntityData.func_187226_a((Class)EntitySpectralTool.class, EntityDataSerializers.field_187196_f);
    }
    
    public static class ToolTask
    {
        private final int maxAge;
        private final double speed;
        private final ItemStack displayStack;
        private final BiFunction<EntitySpectralTool, Double, SpectralToolGoal> toolGoal;
        
        protected ToolTask(final int maxAge, final double speed, final ItemStack displayStack, final BiFunction<EntitySpectralTool, Double, SpectralToolGoal> toolGoal) {
            this.maxAge = maxAge;
            this.speed = speed;
            this.displayStack = displayStack;
            this.toolGoal = toolGoal;
        }
        
        public static ToolTask createPickaxeTask() {
            return new ToolTask((int)MantleEffectPelotrio.CONFIG.durationPickaxe.get(), (double)MantleEffectPelotrio.CONFIG.speedPickaxe.get(), new ItemStack((ItemLike)Items.field_151046_w), (BiFunction<EntitySpectralTool, Double, SpectralToolGoal>)SpectralToolBreakBlockGoal::new);
        }
        
        public static ToolTask createLogTask() {
            return new ToolTask((int)MantleEffectPelotrio.CONFIG.durationAxe.get(), (double)MantleEffectPelotrio.CONFIG.speedAxe.get(), new ItemStack((ItemLike)Items.field_151056_x), (BiFunction<EntitySpectralTool, Double, SpectralToolGoal>)SpectralToolBreakLogGoal::new);
        }
        
        public static ToolTask createAttackTask() {
            return new ToolTask((int)MantleEffectPelotrio.CONFIG.durationSword.get(), (double)MantleEffectPelotrio.CONFIG.speedSword.get(), new ItemStack((ItemLike)Items.field_151048_u), (BiFunction<EntitySpectralTool, Double, SpectralToolGoal>)SpectralToolMeleeAttackGoal::new);
        }
        
        private SpectralToolGoal createGoal(final EntitySpectralTool tool) {
            return this.toolGoal.apply(tool, this.speed);
        }
    }
}
