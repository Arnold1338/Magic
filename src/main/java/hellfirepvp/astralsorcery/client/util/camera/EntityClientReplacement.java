package hellfirepvp.astralsorcery.client.util.camera;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayerEntity;

public class EntityClientReplacement extends AbstractClientPlayerEntity
{
    public EntityClientReplacement() {
        super(Minecraft.getInstance().level, Minecraft.getInstance().player.func_146103_bH());
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean func_175148_a(final PlayerModelPart part) {
        return Minecraft.getInstance().player != null && Minecraft.getInstance().player.func_175148_a(part);
    }
}
