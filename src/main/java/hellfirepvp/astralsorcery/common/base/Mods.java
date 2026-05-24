package hellfirepvp.astralsorcery.common.base;

import net.minecraftforge.fml.InterModComms;
import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import javax.annotation.Nullable;
import java.util.function.Supplier;
import net.minecraftforge.fml.ModList;

public enum Mods
{
    MINECRAFT("minecraft", true), 
    FORGE("forge", true), 
    ASTRAL_SORCERY("astralsorcery", true), 
    DRACONIC_EVOLUTION("draconicevolution"), 
    CURIOS("curios"), 
    JEI("jei"), 
    BOTANIA("botania"), 
    CRAFTTWEAKER("crafttweaker");
    
    private final String modid;
    private final boolean loaded;
    
    private Mods(final String modid) {
        this(modid, ModList.get().isLoaded(modid));
    }
    
    private Mods(final String modid, final boolean loaded) {
        this.modid = modid;
        this.loaded = loaded;
    }
    
    public String getModId() {
        return this.modid;
    }
    
    public boolean isPresent() {
        return this.loaded;
    }
    
    public void executeIfPresent(final Supplier<Runnable> execSupplier) {
        if (this.isPresent()) {
            execSupplier.get().run();
        }
    }
    
    @Nullable
    public <T> T getIfPresent(final Supplier<Supplier<T>> supplierSupplier) {
        if (this.isPresent()) {
            return supplierSupplier.get().get();
        }
        return null;
    }
    
    public boolean owns(final IForgeRegistryEntry<?> entry) {
        return this.isPresent() && entry.getRegistryName() != null && entry.getRegistryName().func_110624_b().equals(this.modid);
    }
    
    @Nonnull
    public ResourceLocation key(final String path) {
        return new ResourceLocation(this.getModId(), path);
    }
    
    public void sendIMC(final String method, final Supplier<?> thing) {
        if (this.isPresent()) {
            InterModComms.sendTo("astralsorcery", this.getModId(), method, (Supplier)thing);
        }
    }
    
    @Nullable
    public static Mods byModId(final String modId) {
        for (final Mods mod : values()) {
            if (mod.getModId().equals(modId)) {
                return mod;
            }
        }
        return null;
    }
    
    @Nullable
    public Class<?> getExtendedPlayerClass() {
        if (!this.isPresent()) {
            return null;
        }
        return null;
    }
}
