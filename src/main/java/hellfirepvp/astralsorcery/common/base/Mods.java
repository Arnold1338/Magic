package hellfirepvp.astralsorcery.common.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Supplier;

public enum Mods {
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

    Mods(String modid) { this(modid, ModList.get().isLoaded(modid)); }
    Mods(String modid, boolean loaded) { this.modid = modid; this.loaded = loaded; }

    public String getModId() { return this.modid; }
    public boolean isPresent() { return this.loaded; }

    public void executeIfPresent(Supplier<Runnable> execSupplier) {
        if (this.isPresent()) execSupplier.get().run();
    }

    @Nullable
    public <T> T getIfPresent(Supplier<Supplier<T>> supplierSupplier) {
        if (this.isPresent()) return supplierSupplier.get().get();
        return null;
    }

    @Nonnull
    public ResourceLocation key(String path) { return new ResourceLocation(this.getModId(), path); }

    public void sendIMC(String method, Supplier<?> thing) {
        if (this.isPresent()) InterModComms.sendTo("astralsorcery", this.getModId(), method, (Supplier) thing);
    }

    @Nullable
    public static Mods byModId(String modId) {
        for (Mods mod : values()) {
            if (mod.getModId().equals(modId)) return mod;
        }
        return null;
    }
}
