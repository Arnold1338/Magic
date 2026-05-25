package hellfirepvp.astralsorcery.common.item.armor;

import hellfirepvp.astralsorcery.common.perk.type.ModifierType;
import hellfirepvp.astralsorcery.common.lib.PerkAttributeTypesAS;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.client.model.armor.ModelArmorMantle;
import net.minecraft.client.model.BipedModel;
import java.awt.Color;
import java.util.Collections;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.perk.modifier.PerkAttributeModifier;
import java.util.Collection;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.ChatFormatting;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.common.constellation.mantle.effect.MantleEffectVicio;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.entity.EquipmentSlot;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.perk.modifier.DynamicAttributeModifier;
import hellfirepvp.astralsorcery.common.util.object.CacheReference;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.item.base.AlignmentChargeConsumer;
import hellfirepvp.astralsorcery.common.perk.source.provider.equipment.EquipmentAttributeModifierProvider;
import hellfirepvp.astralsorcery.common.constellation.ConstellationBaseItem;
import hellfirepvp.astralsorcery.common.item.base.client.ItemDynamicColor;
import net.minecraft.world.item.ArmorItem;

public class ItemMantle extends ArmorItem implements ItemDynamicColor, ConstellationBaseItem, EquipmentAttributeModifierProvider, AlignmentChargeConsumer
{
    private static final UUID MODIFIER_ID;
    private static final CacheReference<DynamicAttributeModifier> MINING_SIZE_MODIFIER;
    private static Object modelArmor;
    
    public ItemMantle() {
        super(CommonProxy.ARMOR_MATERIAL_IMBUED_LEATHER, EquipmentSlot.CHEST, new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            items.add((Object)new ItemStack((ItemLike)this));
            for (final IConstellation cst : RegistriesAS.REGISTRY_CONSTELLATIONS.getValues()) {
                if (!(cst instanceof IWeakConstellation)) {
                    continue;
                }
                final ItemStack stack = new ItemStack((ItemLike)this);
                this.setConstellation(stack, cst);
                items.add((Object)stack);
            }
        }
    }
    
    public boolean canElytraFly(final ItemStack stack, final LivingEntity entity) {
        return entity instanceof Player && MantleEffectVicio.isUsableElytra(stack, (Player)entity);
    }
    
    public boolean elytraFlightTick(final ItemStack stack, final LivingEntity entity, final int flightTicks) {
        return entity instanceof Player && MantleEffectVicio.isUsableElytra(stack, (Player)entity);
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final Level worldIn, final List<Component> tooltip, final TooltipFlag flagIn) {
        final IConstellation cst = this.getConstellation(stack);
        if (cst instanceof IWeakConstellation) {
            tooltip.add((Component)cst.getConstellationName().toString()ChatFormatting.BLUE));
        }
    }
    
    public Collection<PerkAttributeModifier> getModifiers(final ItemStack stack, final Player player, final LogicalSide side, final boolean ignoreRequirements) {
        if (getEffect(stack, ConstellationsAS.evorsio) == null) {
            return (Collection<PerkAttributeModifier>)Collections.emptyList();
        }
        return (Collection<PerkAttributeModifier>)Collections.singletonList(ItemMantle.MINING_SIZE_MODIFIER.get());
    }
    
    public int getColor(final ItemStack stack, final int tintIndex) {
        if (tintIndex != 1) {
            return 16777215;
        }
        final IConstellation cst = this.getConstellation(stack);
        if (cst != null) {
            final Color c = cst.getConstellationColor();
            return 0xFF000000 | c.getRGB();
        }
        return -16777216;
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(final LivingEntity entityLiving, final ItemStack itemStack, final EquipmentSlot armorSlot, final A _default) {
        if (ItemMantle.modelArmor == null) {
            ItemMantle.modelArmor = new ModelArmorMantle();
        }
        return (A)ItemMantle.modelArmor;
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(final ItemStack stack, final Entity entity, final EquipmentSlot slot, final String type) {
        return AstralSorcery.key("textures/model/armor/mantle.png").toString();
    }
    
    @Nullable
    public static <V extends MantleEffect> V getEffect(@Nullable final LivingEntity entity) {
        return getEffect(entity, null);
    }
    
    @Nullable
    public static <V extends MantleEffect> V getEffect(@Nullable final LivingEntity entity, @Nullable final IWeakConstellation expected) {
        if (entity == null) {
            return null;
        }
        final ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemMantle)) {
            return null;
        }
        return getEffect(stack, expected);
    }
    
    @Nullable
    public static <V extends MantleEffect> V getEffect(@Nonnull final ItemStack stack, @Nullable final IWeakConstellation expected) {
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemMantle)) {
            return null;
        }
        final IConstellation cst = ((ItemMantle)stack.getItem()).getConstellation(stack);
        if (!(cst instanceof IWeakConstellation)) {
            return null;
        }
        if (expected != null && !expected.equals(cst)) {
            return null;
        }
        final MantleEffect effect = ((IWeakConstellation)cst).getMantleEffect();
        if (effect == null || !(boolean)effect.getConfig().enabled.get()) {
            return null;
        }
        return (V)effect;
    }
    
    @Nullable
    public IConstellation getConstellation(final ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        return IConstellation.readFromNBT(NBTHelper.getPersistentData(stack), IConstellation.getDefaultSaveKey());
    }
    
    public boolean setConstellation(final ItemStack stack, @Nullable final IConstellation cst) {
        if (stack.isEmpty()) {
            return false;
        }
        if (cst == null) {
            NBTHelper.getPersistentData(stack).func_82580_o(IConstellation.getDefaultSaveKey());
        }
        else {
            cst.writeToNBT(NBTHelper.getPersistentData(stack), IConstellation.getDefaultSaveKey());
        }
        return true;
    }
    
    public float getAlignmentChargeCost(final Player player, final ItemStack stack) {
        return 0.0f;
    }
    
    static {
        MODIFIER_ID = UUID.fromString("aae54b9d-e1c8-4e74-8ac6-efa06093bd1a");
        MINING_SIZE_MODIFIER = new CacheReference<DynamicAttributeModifier>(() -> new DynamicAttributeModifier(ItemMantle.MODIFIER_ID, PerkAttributeTypesAS.ATTR_TYPE_MINING_SIZE, ModifierType.ADDITION, 2.0f));
        ItemMantle.modelArmor = null;
    }
}
