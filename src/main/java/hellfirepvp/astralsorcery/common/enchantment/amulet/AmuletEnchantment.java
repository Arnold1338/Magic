package hellfirepvp.astralsorcery.common.enchantment.amulet;

import javax.annotation.Nullable;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.LanguageMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.network.chat.MutableComponent;
import javax.annotation.Nonnull;
import net.minecraft.world.item.enchantment.Enchantment;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentType;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantment;

public class AmuletEnchantment extends DynamicEnchantment
{
    public AmuletEnchantment(final DynamicEnchantmentType type, @Nonnull final Enchantment enchantment, final int levelAddition) {
        super(type, enchantment, levelAddition);
    }
    
    public AmuletEnchantment(final DynamicEnchantmentType type, final int levelAddition) {
        super(type, levelAddition);
    }
    
    @OnlyIn(Dist.CLIENT)
    public MutableComponent getDisplay() {
        final String typeStr = this.getType().getDisplayName();
        final String levelsStr = I18n.func_135052_a(String.format("astralsorcery.amulet.enchantment.level.%s", (this.levelAddition > 1) ? "more" : "one"), new Object[0]);
        if (this.getType().isEnchantmentSpecific()) {
            return (MutableComponent)new Component(typeStr, new Object[] { String.valueOf(this.getLevelAddition()), levelsStr, LanguageMap.func_74808_a().func_230503_a_(this.getEnchantment().func_77320_a()) });
        }
        return (MutableComponent)new Component(typeStr, new Object[] { String.valueOf(this.getLevelAddition()), levelsStr });
    }
    
    public boolean canMerge(final AmuletEnchantment other) {
        return this.type.equals(other.type) && (!this.type.isEnchantmentSpecific() || this.enchantment.equals(other.enchantment));
    }
    
    public void merge(final AmuletEnchantment src) {
        if (this.canMerge(src)) {
            this.levelAddition += src.levelAddition;
        }
    }
    
    public CompoundTag serialize() {
        final CompoundTag cmp = new CompoundTag();
        cmp.putInt("type", this.type.ordinal());
        cmp.putInt("level", this.levelAddition);
        if (this.type.isEnchantmentSpecific()) {
            cmp.putString("ench", this.enchantment.getRegistryName().toString());
        }
        return cmp;
    }
    
    @Nullable
    public static AmuletEnchantment deserialize(final CompoundTag cmp) {
        final int typeId = cmp.getInt("type");
        final DynamicEnchantmentType type = DynamicEnchantmentType.values()[typeId];
        final int level = Math.max(0, cmp.getInt("level"));
        if (!type.isEnchantmentSpecific()) {
            return new AmuletEnchantment(type, level);
        }
        final ResourceLocation res = new ResourceLocation(cmp.getString("ench"));
        final Enchantment e = (Enchantment)ForgeRegistries.ENCHANTMENTS.getValue(res);
        if (e != null) {
            return new AmuletEnchantment(type, e, level);
        }
        return null;
    }
}
