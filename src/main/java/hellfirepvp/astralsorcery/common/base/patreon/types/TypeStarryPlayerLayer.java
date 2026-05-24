package hellfirepvp.astralsorcery.common.base.patreon.types;

import net.minecraft.world.level.entity.EquipmentSlot;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.render.entity.layer.StarryLayerRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.base.patreon.FlareColor;
import java.util.UUID;
import hellfirepvp.astralsorcery.common.base.patreon.PatreonEffect;

public class TypeStarryPlayerLayer extends PatreonEffect
{
    private final UUID playerUUID;
    
    public TypeStarryPlayerLayer(final UUID effectUUID, @Nullable final FlareColor flareColor, final UUID playerUUID) {
        super(effectUUID, flareColor);
        this.playerUUID = playerUUID;
    }
    
    @Override
    public void initialize() {
        super.initialize();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            this.registerPlayerLayer();
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void registerPlayerLayer() {
        StarryLayerRenderer.addRender((player, slot) -> this.playerUUID.equals(player.getUUID()));
    }
}
