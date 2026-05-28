package hellfirepvp.astralsorcery.common.structure.types;

import javax.annotation.Nullable;
import hellfirepvp.observerlib.api.ObserverProvider;
import hellfirepvp.observerlib.common.change.ObserverProviderStructure;
import hellfirepvp.observerlib.api.ObserverHelper;
import hellfirepvp.observerlib.common.change.ChangeObserverStructure;
import hellfirepvp.observerlib.api.ChangeSubscriber;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.observerlib.api.util.BlockArray;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;


public class StructureType {
    private final ResourceLocation name;
    private final Supplier<BlockArray> structureSupplier;
    
    public StructureType(final ResourceLocation name, final Supplier<BlockArray> structureSupplier) {
        this.name = name;
        this.structureSupplier = structureSupplier;
    }
    
    public BlockArray getStructure() {
        return this.structureSupplier.get();
    }
    
    public Component getDisplayName() {
        return (Component)new Component(String.format("structure.%s.%s.name", this.name.func_110624_b(), this.name.addTransientModifier()));
    }
    
    public ChangeSubscriber<ChangeObserverStructure> observe(final Level world, final BlockPos pos) {
        return (ChangeSubscriber<ChangeObserverStructure>)ObserverHelper.getHelper().observeArea(world, pos, (ObserverProvider)new ObserverProviderStructure(this.getRegistryName()));
    }
    
    public final StructureType setRegistryName(final ResourceLocation name) {
        return this;
    }
    
    @Nullable
    public ResourceLocation getRegistryName() {
        return this.name;
    }
    
    public Class<StructureType> getRegistryType() {
        return StructureType.class;
    }
}
