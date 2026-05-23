package hellfirepvp.observerlib.common.registry;

import hellfirepvp.observerlib.api.ObserverHelper;
import hellfirepvp.observerlib.common.block.BlockAirRequirement;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegistryBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "observerlib");

    public static final RegistryObject<BlockAirRequirement> AIR_PREVIEW =
        BLOCKS.register("air_preview", BlockAirRequirement::new);

    public static void init() {
        // Blocks will be registered via DeferredRegister in CommonProxy
    }

    public static void onBlocksReady() {
        ObserverHelper.blockAirRequirement = AIR_PREVIEW.get();
    }
}
