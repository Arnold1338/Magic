package hellfirepvp.astralsorcery.common.registry;

import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.AstralSorcery;
import com.mojang.datafixers.types.Type;
import hellfirepvp.astralsorcery.common.util.NameUtil;
import net.minecraft.world.level.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.render.tile.RenderWell;
import hellfirepvp.astralsorcery.client.render.tile.RenderTileFakedState;
import hellfirepvp.astralsorcery.client.render.tile.RenderTelescope;
import hellfirepvp.astralsorcery.client.render.tile.RenderSpectralRelay;
import hellfirepvp.astralsorcery.client.render.tile.RenderRitualPedestal;
import hellfirepvp.astralsorcery.client.render.tile.RenderRefractionTable;
import hellfirepvp.astralsorcery.client.render.tile.RenderPrism;
import hellfirepvp.astralsorcery.client.render.tile.RenderObservatory;
import hellfirepvp.astralsorcery.client.render.tile.RenderLens;
import hellfirepvp.astralsorcery.client.render.tile.RenderInfuser;
import hellfirepvp.astralsorcery.client.render.tile.RenderCollectorCrystal;
import hellfirepvp.astralsorcery.client.render.tile.RenderChalice;
import hellfirepvp.astralsorcery.client.render.tile.RenderAttunementAltar;
import java.util.function.Function;
import net.minecraft.world.level.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import hellfirepvp.astralsorcery.client.render.tile.RenderAltar;
import hellfirepvp.astralsorcery.common.tile.TileWell;
import hellfirepvp.astralsorcery.common.tile.TileVanishing;
import hellfirepvp.astralsorcery.common.tile.TileTreeBeaconComponent;
import hellfirepvp.astralsorcery.common.tile.TileTreeBeacon;
import hellfirepvp.astralsorcery.common.tile.TileTranslucentBlock;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import hellfirepvp.astralsorcery.common.tile.TileRitualPedestal;
import hellfirepvp.astralsorcery.common.tile.TileRitualLink;
import hellfirepvp.astralsorcery.common.tile.TileRefractionTable;
import hellfirepvp.astralsorcery.common.tile.TilePrism;
import hellfirepvp.astralsorcery.common.tile.TileObservatory;
import hellfirepvp.astralsorcery.common.tile.TileLens;
import hellfirepvp.astralsorcery.common.tile.TileInfuser;
import hellfirepvp.astralsorcery.common.tile.TileIlluminator;
import hellfirepvp.astralsorcery.common.tile.TileGemCrystals;
import hellfirepvp.astralsorcery.common.tile.TileFountain;
import hellfirepvp.astralsorcery.common.tile.TileCollectorCrystal;
import hellfirepvp.astralsorcery.common.tile.TileChalice;
import hellfirepvp.astralsorcery.common.tile.TileCelestialGateway;
import hellfirepvp.astralsorcery.common.tile.TileCelestialCrystals;
import hellfirepvp.astralsorcery.common.tile.TileAttunementAltar;
import hellfirepvp.astralsorcery.common.tile.altar.TileAltar;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import net.minecraft.world.level.level.block.Block;
import hellfirepvp.astralsorcery.common.tile.TileSpectralRelay;

public class RegistryTileEntities
{
    private RegistryTileEntities() {
    }
    
    public static void registerTiles() {
        TileEntityTypesAS.SPECTRAL_RELAY = registerTile(TileSpectralRelay.class, (Block)BlocksAS.SPECTRAL_RELAY);
        TileEntityTypesAS.ALTAR = registerTile(TileAltar.class, (Block)BlocksAS.ALTAR_DISCOVERY, (Block)BlocksAS.ALTAR_ATTUNEMENT, (Block)BlocksAS.ALTAR_CONSTELLATION, (Block)BlocksAS.ALTAR_RADIANCE);
        TileEntityTypesAS.ATTUNEMENT_ALTAR = registerTile(TileAttunementAltar.class, (Block)BlocksAS.ATTUNEMENT_ALTAR);
        TileEntityTypesAS.CELESTIAL_CRYSTAL_CLUSTER = registerTile(TileCelestialCrystals.class, (Block)BlocksAS.CELESTIAL_CRYSTAL_CLUSTER);
        TileEntityTypesAS.GATEWAY = registerTile(TileCelestialGateway.class, (Block)BlocksAS.GATEWAY);
        TileEntityTypesAS.CHALICE = registerTile(TileChalice.class, (Block)BlocksAS.CHALICE);
        TileEntityTypesAS.COLLECTOR_CRYSTAL = registerTile(TileCollectorCrystal.class, (Block)BlocksAS.ROCK_COLLECTOR_CRYSTAL, (Block)BlocksAS.CELESTIAL_COLLECTOR_CRYSTAL);
        TileEntityTypesAS.FOUNTAIN = registerTile(TileFountain.class, (Block)BlocksAS.FOUNTAIN);
        TileEntityTypesAS.GEM_CRYSTAL_CLUSTER = registerTile(TileGemCrystals.class, (Block)BlocksAS.GEM_CRYSTAL_CLUSTER);
        TileEntityTypesAS.ILLUMINATOR = registerTile(TileIlluminator.class, (Block)BlocksAS.ILLUMINATOR);
        TileEntityTypesAS.INFUSER = registerTile(TileInfuser.class, (Block)BlocksAS.INFUSER);
        TileEntityTypesAS.LENS = registerTile(TileLens.class, (Block)BlocksAS.LENS);
        TileEntityTypesAS.OBSERVATORY = registerTile(TileObservatory.class, (Block)BlocksAS.OBSERVATORY);
        TileEntityTypesAS.PRISM = registerTile(TilePrism.class, (Block)BlocksAS.PRISM);
        TileEntityTypesAS.REFRACTION_TABLE = registerTile(TileRefractionTable.class, (Block)BlocksAS.REFRACTION_TABLE);
        TileEntityTypesAS.RITUAL_LINK = registerTile(TileRitualLink.class, (Block)BlocksAS.RITUAL_LINK);
        TileEntityTypesAS.RITUAL_PEDESTAL = registerTile(TileRitualPedestal.class, (Block)BlocksAS.RITUAL_PEDESTAL);
        TileEntityTypesAS.TELESCOPE = registerTile(TileTelescope.class, (Block)BlocksAS.TELESCOPE);
        TileEntityTypesAS.TRANSLUCENT_BLOCK = registerTile(TileTranslucentBlock.class, (Block)BlocksAS.TRANSLUCENT_BLOCK);
        TileEntityTypesAS.TREE_BEACON = registerTile(TileTreeBeacon.class, (Block)BlocksAS.TREE_BEACON);
        TileEntityTypesAS.TREE_BEACON_COMPONENT = registerTile(TileTreeBeaconComponent.class, (Block)BlocksAS.TREE_BEACON_COMPONENT);
        TileEntityTypesAS.VANISHING = registerTile(TileVanishing.class, (Block)BlocksAS.VANISHING);
        TileEntityTypesAS.WELL = registerTile(TileWell.class, (Block)BlocksAS.WELL);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void initClient() {
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.ALTAR, (Function)RenderAltar::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.ATTUNEMENT_ALTAR, (Function)RenderAttunementAltar::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.CHALICE, (Function)RenderChalice::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.COLLECTOR_CRYSTAL, (Function)RenderCollectorCrystal::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.INFUSER, (Function)RenderInfuser::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.LENS, (Function)RenderLens::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.OBSERVATORY, (Function)RenderObservatory::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.PRISM, (Function)RenderPrism::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.REFRACTION_TABLE, (Function)RenderRefractionTable::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.RITUAL_PEDESTAL, (Function)RenderRitualPedestal::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.SPECTRAL_RELAY, (Function)RenderSpectralRelay::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.TELESCOPE, (Function)RenderTelescope::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.TRANSLUCENT_BLOCK, (Function)RenderTileFakedState::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.TREE_BEACON_COMPONENT, (Function)RenderTileFakedState::new);
        ClientRegistry.bindTileEntityRenderer((BlockEntityType)TileEntityTypesAS.WELL, (Function)RenderWell::new);
    }
    
    private static <T extends BlockEntity> BlockEntityType<T> registerTile(final Class<T> tileClass, final Block... validBlocks) {
        final ResourceLocation name = NameUtil.fromClass(tileClass, "Tile");
        final BlockEntityType.Builder<T> typeBuilder = (BlockEntityType.Builder<T>)BlockEntityType.Builder.func_223042_a(() -> {
            try {
                return (BlockEntity)tileClass.newInstance();
            }
            catch (final Exception exc) {
                exc.printStackTrace();
                throw new IllegalArgumentException("Unexpected Constructor for class: " + tileClass.getName());
            }
        }, validBlocks);
        final BlockEntityType<T> type = (BlockEntityType<T>)typeBuilder.func_206865_a((Type)null);
        type.setRegistryName(name);
        AstralSorcery.getProxy().getRegistryPrimer().register(type);
        return type;
    }
}
