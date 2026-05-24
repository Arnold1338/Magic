package hellfirepvp.astralsorcery.common.base.patreon.types.provider;

import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.level.level.block.Block;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import java.util.HashMap;
import com.google.gson.JsonArray;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.List;
import java.util.UUID;
import com.google.gson.JsonParser;
import hellfirepvp.astralsorcery.common.base.patreon.types.TypeBlockRing;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffectProvider;

public class ProviderBlockRing implements PatreonEffectProvider<TypeBlockRing>
{
    private static final JsonParser PARSER;
    
    @Override
    public TypeBlockRing buildEffect(final UUID playerUUID, final List<String> effectParameters) throws Exception {
        final UUID effectUniqueId = UUID.fromString(effectParameters.get(0));
        FlareColor fc = null;
        if (!"null".equals(effectParameters.get(1))) {
            fc = FlareColor.valueOf(effectParameters.get(1));
        }
        final float distance = Float.parseFloat(effectParameters.get(2));
        final float rotationAngle = Float.parseFloat(effectParameters.get(3));
        final int repeats = Integer.parseInt(effectParameters.get(4));
        final int tickRotationSpeed = Integer.parseInt(effectParameters.get(5));
        final JsonArray jo = (JsonArray)ProviderBlockRing.PARSER.parse((String)effectParameters.get(6));
        final HashMap<BlockPos, BlockState> pattern = new HashMap<BlockPos, BlockState>();
        for (final JsonElement patternElement : jo) {
            final JsonObject obj = (JsonObject)patternElement;
            final BlockPos pos = new BlockPos(obj.getAsJsonPrimitive("posX").getAsInt(), obj.getAsJsonPrimitive("posY").getAsInt(), obj.getAsJsonPrimitive("posZ").getAsInt());
            final Block b = (Block)ForgeRegistries.BLOCKS.getValue(new ResourceLocation(obj.getAsJsonPrimitive("block").getAsString()));
            pattern.put(pos, b.defaultBlockState());
        }
        return new TypeBlockRing(effectUniqueId, fc, playerUUID, distance, rotationAngle, repeats, tickRotationSpeed, pattern);
    }
    
    static {
        PARSER = new JsonParser();
    }
}
