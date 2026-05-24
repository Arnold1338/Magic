package hellfirepvp.astralsorcery.common.entity.item;

import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraft.network.IPacket;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.crystal.CrystalProperty;
import net.minecraft.util.Mth;
import net.minecraft.world.level.level.ItemLike;
import hellfirepvp.astralsorcery.common.crystal.CrystalAttributes;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.entity.player.Player;
import net.minecraft.world.level.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.item.enchantment.Enchantments;
import hellfirepvp.astralsorcery.common.item.crystal.ItemCrystalBase;
import hellfirepvp.astralsorcery.common.item.ItemChisel;
import net.minecraft.world.level.InteractionHand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.Entity;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.entity.item.ItemEntity;
import net.minecraft.world.level.entity.EntityType;
import hellfirepvp.astralsorcery.common.entity.InteractableEntity;

public class EntityCrystal extends EntityItemExplosionResistant implements InteractableEntity
{
    public EntityCrystal(final EntityType<? extends ItemEntity> type, final World world) {
        super(type, world);
    }
    
    public EntityCrystal(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z) {
        super(type, world, x, y, z);
    }
    
    public EntityCrystal(final EntityType<? extends ItemEntity> type, final World world, final double x, final double y, final double z, final ItemStack stack) {
        super(type, world, x, y, z, stack);
    }
    
    public static EntityType.IFactory<EntityCrystal> factoryCrystal() {
        return (EntityType.IFactory<EntityCrystal>)((spawnEntity, world) -> new EntityCrystal(EntityTypesAS.ITEM_CRYSTAL, world));
    }
    
    public boolean func_70067_L() {
        return true;
    }
    
    public boolean func_70075_an() {
        return true;
    }
    
    public boolean func_85031_j(final Entity entity) {
        if (!this.func_130014_f_().func_201670_d() && entity instanceof ServerPlayer) {
            final ItemStack held = ((ServerPlayer)entity).func_184586_b(InteractionHand.MAIN_HAND);
            if (!held.isEmpty() && held.getItem() instanceof ItemChisel) {
                final ItemStack thisStack = this.func_92059_d();
                if (!thisStack.isEmpty() && thisStack.getItem() instanceof ItemCrystalBase) {
                    final CrystalAttributes thisAttributes = ((ItemCrystalBase)thisStack.getItem()).getAttributes(thisStack);
                    if (thisAttributes != null) {
                        boolean doDamage = false;
                        if (this.field_70146_Z.nextFloat() < 0.35f) {
                            final int fortuneLevel = EnchantmentHelper.func_77506_a(Enchantments.field_185308_t, held);
                            doDamage = this.splitCrystal(thisAttributes, fortuneLevel);
                        }
                        if (doDamage || this.field_70146_Z.nextFloat() < 0.35f) {
                            held.func_222118_a(1, (LivingEntity)entity, player -> player.func_213334_d(InteractionHand.MAIN_HAND));
                        }
                    }
                }
            }
        }
        return true;
    }
    
    private boolean splitCrystal(final CrystalAttributes thisAttributes, final int fortuneLevel) {
        final ItemCrystalBase newBase = ((ItemCrystalBase)this.func_92059_d().getItem()).getInertDuplicateItem();
        if (newBase == null) {
            return false;
        }
        final ItemStack created = new ItemStack((ItemLike)newBase);
        if (created.isEmpty()) {
            return false;
        }
        final int maxSplit = Mth.func_76123_f(thisAttributes.getTotalTierLevel() / 2.0f);
        if (maxSplit >= thisAttributes.getTotalTierLevel()) {
            return false;
        }
        int lostModifiers = 0;
        if (maxSplit > 1 && this.field_70146_Z.nextFloat() < 0.6f / (fortuneLevel + 1)) {
            ++lostModifiers;
            if (maxSplit > 2 && this.field_70146_Z.nextFloat() < 0.2f / (fortuneLevel + 1)) {
                ++lostModifiers;
            }
        }
        CrystalAttributes resultThisAttributes = thisAttributes;
        final CrystalAttributes.Builder resultSplitAttributes = CrystalAttributes.Builder.newBuilder(false);
        for (int i = 0; i < maxSplit; ++i) {
            final CrystalProperty prop = MiscUtils.getRandomEntry(resultThisAttributes.getProperties(), this.field_70146_Z);
            if (prop == null) {
                break;
            }
            resultThisAttributes = resultThisAttributes.modifyLevel(prop, -1);
            if (lostModifiers > 0) {
                --lostModifiers;
            }
            else {
                resultSplitAttributes.addProperty(prop, 1);
            }
        }
        ((ItemCrystalBase)this.func_92059_d().getItem()).setAttributes(this.func_92059_d(), resultThisAttributes);
        newBase.setAttributes(created, resultSplitAttributes.build());
        ItemUtils.dropItemNaturally(this.func_130014_f_(), this.func_226277_ct_(), this.func_226278_cu_() + 0.25, this.func_226281_cx_(), created);
        return true;
    }
    
    @Override
    public void func_70071_h_() {
        super.tick();
        if (!this.field_70170_p.func_201670_d() && this.field_70292_b + 10 >= this.lifespan) {
            this.field_70292_b = 0;
        }
    }
    
    @Override
    public IPacket<?> func_213297_N() {
        return (IPacket<?>)NetworkHooks.getEntitySpawningPacket((Entity)this);
    }
}
