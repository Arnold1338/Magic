package hellfirepvp.astralsorcery.common.constellation;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.world.level.entity.player.Player;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import java.awt.Color;
import java.util.function.Supplier;
import net.minecraft.world.level.item.Item;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import hellfirepvp.astralsorcery.common.constellation.engraving.EngravingEffect;
import net.minecraft.world.level.item.crafting.Ingredient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.IFormattableTextComponent;
import java.util.List;
import hellfirepvp.astralsorcery.common.constellation.star.StarConnection;
import hellfirepvp.astralsorcery.common.constellation.star.StarLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public interface IConstellation extends IForgeRegistryEntry<IConstellation>, Comparable<IConstellation>
{
    public static final int STAR_GRID_INDEX = 31;
    public static final int STAR_GRID_WIDTH_HEIGHT = 32;
    
    StarLocation addStar(final int p0, final int p1);
    
    StarConnection addConnection(final StarLocation p0, final StarLocation p1);
    
    int getSortingId();
    
    List<StarLocation> getStars();
    
    List<StarConnection> getStarConnections();
    
    String getSimpleName();
    
    String getTranslationKey();
    
    default IFormattableTextComponent getConstellationName() {
        return (IFormattableTextComponent)new Component(this.getTranslationKey());
    }
    
    default IFormattableTextComponent getConstellationTypeDescription() {
        String type = "unknown";
        if (this instanceof IMajorConstellation) {
            type = "major";
        }
        else if (this instanceof IWeakConstellation) {
            type = "weak";
        }
        else if (this instanceof IMinorConstellation) {
            type = "minor";
        }
        return (IFormattableTextComponent)new Component(String.format("astralsorcery.journal.constellation.type.%s", type));
    }
    
    default IFormattableTextComponent getConstellationTag() {
        return (IFormattableTextComponent)new Component(this.getTranslationKey() + ".tag");
    }
    
    default IFormattableTextComponent getConstellationDescription() {
        return (IFormattableTextComponent)new Component(this.getTranslationKey() + ".description");
    }
    
    default IFormattableTextComponent getConstellationEnchantmentDescription() {
        return (IFormattableTextComponent)new Component(this.getTranslationKey() + ".enchantments");
    }
    
    default String getDefaultSaveKey() {
        return "constellationName";
    }
    
    List<Ingredient> getConstellationSignatureItems();
    
    @Nullable
    default EngravingEffect getEngravingEffect() {
        return (EngravingEffect)RegistriesAS.REGISTRY_ENGRAVING_EFFECT.getValue(this.getRegistryName());
    }
    
    default IConstellation addSignatureItem(final ItemStack item) {
        return this.addSignatureItem(() -> Ingredient.func_193369_a(new ItemStack[] { item }));
    }
    
    default IConstellation addSignatureItem(final ItemLike item) {
        return this.addSignatureItem(() -> Ingredient.func_199804_a(new ItemLike[] { item }));
    }
    
    default IConstellation addSignatureItem(final ITag<Item> tag) {
        return this.addSignatureItem(() -> Ingredient.func_199805_a(tag));
    }
    
    IConstellation addSignatureItem(final Supplier<Ingredient> p0);
    
    Color getConstellationColor();
    
    default Color getTierRenderColor() {
        if (this instanceof IMinorConstellation) {
            return ColorsAS.CONSTELLATION_TYPE_MINOR;
        }
        if (this instanceof IMajorConstellation) {
            return ColorsAS.CONSTELLATION_TYPE_MAJOR;
        }
        return ColorsAS.CONSTELLATION_TYPE_WEAK;
    }
    
    boolean canDiscover(final Player p0, final PlayerProgress p1);
    
    default void writeToNBT(final CompoundTag compound) {
        this.writeToNBT(compound, getDefaultSaveKey());
    }
    
    default void writeToNBT(final CompoundTag compound, final String key) {
        compound.putString(key, this.getRegistryName().toString());
    }
    
    @Nullable
    default IConstellation readFromNBT(final CompoundTag compound) {
        return readFromNBT(compound, getDefaultSaveKey());
    }
    
    @Nullable
    default IConstellation readFromNBT(final CompoundTag compound, final String key) {
        return ConstellationRegistry.getConstellation(new ResourceLocation(compound.getString(key)));
    }
    
    default Class<IConstellation> getRegistryType() {
        return IConstellation.class;
    }
}
