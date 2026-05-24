package hellfirepvp.astralsorcery.common.block.tile.altar;

import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Predicate;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import net.minecraft.world.level.item.Item;
import java.util.function.Supplier;
import hellfirepvp.astralsorcery.common.data.research.ResearchProgression;

public enum AltarType
{
    DISCOVERY(ResearchProgression.BASIC_CRAFT, 100, 1, () -> BlocksAS.ALTAR_DISCOVERY.func_199767_j(), () -> StructureTypesAS.EMPTY, new int[] { 6, 7, 8, 11, 12, 13, 16, 17, 18 }), 
    ATTUNEMENT(ResearchProgression.ATTUNEMENT, 200, 2, () -> BlocksAS.ALTAR_ATTUNEMENT.func_199767_j(), () -> StructureTypesAS.PTYPE_ALTAR_ATTUNEMENT, new int[] { 0, 4, 6, 7, 8, 11, 12, 13, 16, 17, 18, 20, 24 }), 
    CONSTELLATION(ResearchProgression.CONSTELLATION, 400, 2, () -> BlocksAS.ALTAR_CONSTELLATION.func_199767_j(), () -> StructureTypesAS.PTYPE_ALTAR_CONSTELLATION, slot -> slot != 2 && slot != 10 && slot != 14 && slot != 22), 
    RADIANCE(ResearchProgression.RADIANCE, 600, 3, () -> BlocksAS.ALTAR_RADIANCE.func_199767_j(), () -> StructureTypesAS.PTYPE_ALTAR_TRAIT, slot -> true);
    
    private final ResearchProgression associatedTier;
    private final int defaultAltarCraftingDuration;
    private final int minimumSources;
    private final Supplier<Item> altarItemSupplier;
    private final Supplier<StructureType> structureSupplier;
    private final Predicate<Integer> slotValidator;
    
    private AltarType(final ResearchProgression progressTier, final int defaultAltarCraftingDuration, final int minimumSources, final Supplier<Item> altarItemSupplier, final Supplier<StructureType> structureSupplier, final int[] validSlots) {
        this.associatedTier = progressTier;
        this.defaultAltarCraftingDuration = defaultAltarCraftingDuration;
        this.minimumSources = minimumSources;
        this.altarItemSupplier = altarItemSupplier;
        this.structureSupplier = structureSupplier;
        final List<Integer> slots = new ArrayList<Integer>();
        for (final int slot : validSlots) {
            slots.add(slot);
        }
        this.slotValidator = slots::contains;
    }
    
    private AltarType(final ResearchProgression progressTier, final int defaultAltarCraftingDuration, final int minimumSources, final Supplier<Item> altarItemSupplier, final Supplier<StructureType> structureSupplier, final Predicate<Integer> slotValidator) {
        this.associatedTier = progressTier;
        this.defaultAltarCraftingDuration = defaultAltarCraftingDuration;
        this.minimumSources = minimumSources;
        this.altarItemSupplier = altarItemSupplier;
        this.structureSupplier = structureSupplier;
        this.slotValidator = slotValidator;
    }
    
    @Nonnull
    public ResearchProgression getAssociatedTier() {
        return this.associatedTier;
    }
    
    @Nonnull
    public StructureType getRequiredStructure() {
        return this.structureSupplier.get();
    }
    
    @Nonnull
    public ItemStack getAltarItemRepresentation() {
        return new ItemStack((ItemLike)this.altarItemSupplier.get());
    }
    
    public boolean hasSlot(final int slotId) {
        return this.slotValidator.test(slotId);
    }
    
    public int getStarlightCapacity() {
        return (int)(1000.0 * Math.pow(2.0, this.ordinal()));
    }
    
    public int getDefaultAltarCraftingDuration() {
        return this.defaultAltarCraftingDuration;
    }
    
    public int getMinimumSources() {
        return this.minimumSources;
    }
    
    public boolean isThisLEThan(final AltarType type) {
        return this.ordinal() <= type.ordinal();
    }
    
    public boolean isThisGEThan(final AltarType type) {
        return this.ordinal() >= type.ordinal();
    }
}
