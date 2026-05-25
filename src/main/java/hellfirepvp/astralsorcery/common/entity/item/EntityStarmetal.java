package hellfirepvp.astralsorcery.common.entity.item;

import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.Pose;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import net.minecraft.world.level.ItemLike;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import hellfirepvp.astralsorcery.common.item.ItemStarmetalIngot;
import hellfirepvp.astralsorcery.common.item.ItemChisel;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.reflection.ReflectionHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.entity.InteractableEntity;

public class EntityStarmetal extends EntityCustomItemReplacement implements InteractableEntity
{
    public EntityStarmetal(final EntityType<? extends ItemEntity> type, final Level world) {
        super(type, world);
        ReflectionHelper.setSkipItemPhysicsRender(this);
        this.func_213323_x_();
    }
    
    public EntityStarmetal(final EntityType<? extends ItemEntity> type, final Level world, final double x, final double y, final double z) {
        this(type, world);
        this.setPos(x, y, z);
        this.yRot = this.random.nextFloat() * 360.0f;
        this.func_213293_j(this.random.nextDouble() * 0.2 - 0.1, 0.2, this.random.nextDouble() * 0.2 - 0.1);
    }
    
    public EntityStarmetal(final EntityType<? extends ItemEntity> type, final Level world, final double x, final double y, final double z, final ItemStack stack) {
        this(type, world, x, y, z);
        this.func_92058_a(stack);
        this.lifespan = (stack.isEmpty() ? 6000 : stack.getEntityLifespan(world));
    }
    
    public static EntityType.IFactory<EntityStarmetal> factoryStarmetalIngot() {
        return (EntityType.IFactory<EntityStarmetal>)((spawnEntity, world) -> new EntityStarmetal(EntityTypesAS.ITEM_STARMETAL_INGOT, world));
    }
    
    public boolean func_70067_L() {
        return true;
    }
    
    public boolean func_70075_an() {
        return true;
    }
    
    public boolean func_85031_j(final Entity entity) {
        if (!this.level().isClientSide() && entity instanceof ServerPlayer) {
            final ItemStack held = ((ServerPlayer)entity).getItemInHand(InteractionHand.MAIN_HAND);
            if (!held.isEmpty() && held.getItem() instanceof ItemChisel) {
                final ItemStack thisStack = this.func_92059_d();
                if (!thisStack.isEmpty() && thisStack.getItem() instanceof ItemStarmetalIngot) {
                    boolean doDamage = false;
                    if (this.random.nextFloat() < 0.4f) {
                        final int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, held);
                        doDamage = this.createStardust(fortuneLevel);
                    }
                    if (doDamage || this.random.nextFloat() < 0.35f) {
                        held.func_222118_a(1, (LivingEntity)entity, player -> player.func_213334_d(InteractionHand.MAIN_HAND));
                    }
                }
            }
        }
        return true;
    }
    
    private boolean createStardust(final int fortuneLevel) {
        final ItemStack created = new ItemStack((ItemLike)ItemsAS.STARDUST);
        ItemUtils.dropItemNaturally(this.level(), this.getX(), this.getY() + 0.25, this.getZ(), created);
        float breakIngot = 0.9f;
        breakIngot -= Mth.getDescriptionId(fortuneLevel, 0, 10) * 0.06f;
        if (this.random.nextFloat() < breakIngot) {
            final ItemStack thisStack = this.func_92059_d();
            thisStack.shrink(1);
            this.func_92058_a(thisStack);
        }
        return true;
    }
    
    @Override
    public void func_70071_h_() {
        final boolean onGround = this.func_233570_aj_();
        super.tick();
        if (this.func_233570_aj_() != onGround) {
            this.func_213323_x_();
        }
    }
    
    public void func_230245_c_(final boolean grounded) {
        final boolean updateSize = this.func_233570_aj_() != grounded;
        super.func_230245_c_(grounded);
        if (updateSize) {
            this.func_213323_x_();
        }
    }
    
    public EntityDimensions func_213305_a(final Pose poseIn) {
        if (!this.func_233570_aj_()) {
            return EntityType.field_200765_E.func_220334_j();
        }
        return this.level().func_220334_j();
    }
    
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
}
