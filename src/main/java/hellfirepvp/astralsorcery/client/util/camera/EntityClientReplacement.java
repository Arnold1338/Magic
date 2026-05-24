package hellfirepvp.astralsorcery.client.util.camera;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayerEntity;

public class EntityClientReplacement extends AbstractClientPlayerEntity
{
    public EntityClientReplacement() {
        super(Minecraft.func_71410_x().field_71441_e, Minecraft.func_71410_x().field_71439_g.func_146103_bH());
    }
    
    @OnlyIn(Dist.CLIENT)
    public boolean func_175148_a(final PlayerModelPart part) {
        return Minecraft.func_71410_x().field_71439_g != null && Minecraft.func_71410_x().field_71439_g.func_175148_a(part);
    }
}
