package hellfirepvp.astralsorcery.common.data.config.registry.sets;

import javax.annotation.Nullable;
import javax.annotation.Nonnull;
import java.util.Random;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigDataSet;

public class FluidRarityEntry implements ConfigDataSet
{
    private final ResourceLocation fluidName;
    private final int guaranteedAmount;
    private final int additionalRandomAmount;
    private final int rarity;
    
    public FluidRarityEntry(final ResourceLocation fluidName, final int rarity, final int guaranteedAmount) {
        this(fluidName, rarity, guaranteedAmount, guaranteedAmount / 2);
    }
    
    public FluidRarityEntry(final ResourceLocation fluidName, final int rarity, final int guaranteedAmount, final int additionalRandomAmount) {
        this.fluidName = fluidName;
        this.rarity = rarity;
        this.guaranteedAmount = guaranteedAmount;
        this.additionalRandomAmount = additionalRandomAmount;
    }
    
    public Fluid getFluid() {
        return (Fluid)ForgeRegistries.FLUIDS.getValue(this.fluidName);
    }
    
    public int getRarity() {
        return this.rarity;
    }
    
    public int getRandomAmount(final Random rand) {
        return this.guaranteedAmount + ((this.additionalRandomAmount > 0) ? rand.nextInt(this.additionalRandomAmount) : 0);
    }
    
    @Nonnull
    @Override
    public String serialize() {
        return this.fluidName + ";" + this.guaranteedAmount + ";" + this.additionalRandomAmount + ";" + this.rarity;
    }
    
    @Nullable
    public static FluidRarityEntry deserialize(final String str) throws IllegalArgumentException {
        final String[] split = str.split(";");
        if (split.length != 4) {
            return null;
        }
        final ResourceLocation fluidName = new ResourceLocation(split[0]);
        if (ForgeRegistries.FLUIDS.getValue(fluidName) == null) {
            throw new IllegalArgumentException("Unknown Fluid: " + fluidName);
        }
        final String strGAmount = split[1];
        final String strRAmount = split[2];
        final String strRarity = split[3];
        int guaranteed;
        int randomAmt;
        int rarity;
        try {
            guaranteed = Integer.parseInt(strGAmount);
            randomAmt = Integer.parseInt(strRAmount);
            rarity = Integer.parseInt(strRarity);
        }
        catch (final NumberFormatException exc) {
            return null;
        }
        return new FluidRarityEntry(fluidName, rarity, guaranteed, randomAmt);
    }
}
