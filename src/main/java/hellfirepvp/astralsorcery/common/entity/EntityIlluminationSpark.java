package hellfirepvp.astralsorcery.common.entity;

import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.util.BlockSnapshot;
import hellfirepvp.astralsorcery.common.util.block.BlockUtils;
import net.minecraft.world.item.BlockItemUseContext;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.phys.BlockHitResult;
import net.minecraft.world.level.phys.HitResult;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.projectile.ThrowableEntity;

public class EntityIlluminationSpark extends ThrowableEntity
{
    public EntityIlluminationSpark(final Level world) {
        super((EntityType)EntityTypesAS.ILLUMINATION_SPARK, world);
    }
    
    public EntityIlluminationSpark(final double x, final double y, final double z, final Level world) {
        super((EntityType)EntityTypesAS.ILLUMINATION_SPARK, x, y, z, world);
    }
    
    public EntityIlluminationSpark(final LivingEntity thrower, final Level world) {
        super((EntityType)EntityTypesAS.ILLUMINATION_SPARK, thrower, world);
        this.func_234612_a_((Entity)thrower, thrower.xRot, thrower.yRot, 0.0f, 0.7f, 0.9f);
    }
    
    public static EntityType.IFactory<EntityIlluminationSpark> factory() {
        return (EntityType.IFactory<EntityIlluminationSpark>)((type, world) -> new EntityIlluminationSpark(world));
    }
    
    protected void func_70088_a() {
    }
    
    public void func_70071_h_() {
        super.tick();
        if (this.level()) {
            this.spawnEffects();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void spawnEffects() {
        for (int i = 0; i < 6; ++i) {
            final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this)).setMotion(new Vector3(0.04f - this.random.nextFloat() * 0.08f, 0.04f - this.random.nextFloat() * 0.08f, 0.04f - this.random.nextFloat() * 0.08f)).setScaleMultiplier(0.25f);
            this.randomizeColor(p);
        }
        FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this));
        p.setScaleMultiplier(0.6f);
        this.randomizeColor(p);
        p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)this).add(this.func_213322_ci().func_216372_d(0.5, 0.5, 0.5)));
        p.setScaleMultiplier(0.6f);
        this.randomizeColor(p);
    }
    
    @OnlyIn(Dist.CLIENT)
    private void randomizeColor(final FXFacingParticle p) {
        switch (this.random.nextInt(3)) {
            case 0: {
                p.color(VFXColorFunction.constant(ColorsAS.ILLUMINATION_POWDER_1));
                break;
            }
            case 1: {
                p.color(VFXColorFunction.constant(ColorsAS.ILLUMINATION_POWDER_2));
                break;
            }
            case 2: {
                p.color(VFXColorFunction.constant(ColorsAS.ILLUMINATION_POWDER_3));
                break;
            }
        }
    }
    
    protected void func_70227_a(final HitResult result) {
        if (this.level()) {
            return;
        }
        if (!(result instanceof BlockHitResult) || !(this.func_234616_v_() instanceof Player)) {
            this.func_70106_y();
            return;
        }
        final Player player = (Player)this.func_234616_v_();
        final BlockHitResult brtr = (BlockHitResult)result;
        final BlockItemUseContext bCtx = new BlockItemUseContext(new ItemUseContext(player, InteractionHand.MAIN_HAND, brtr));
        BlockPos pos = bCtx.func_195995_a();
        if (!BlockUtils.isReplaceable(this.level(), pos)) {
            pos = pos.func_177972_a(bCtx.func_196000_l());
        }
        if (!ForgeEventFactory.onBlockPlace((Entity)player, BlockSnapshot.create(this.level().dimension(), (IWorld)this.level(), pos), bCtx.func_196000_l())) {
            this.level().func_175656_a(pos, BlocksAS.FLARE_LIGHT.defaultBlockState());
        }
        this.func_70106_y();
    }
    
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
}
