package hellfirepvp.astralsorcery.common.effect;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.minecraft.client.gui.AbstractGui;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.world.level.effect.MobEffectInstance;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraft.world.effect.EffectType;
import java.awt.Color;
import java.util.Random;
import net.minecraft.world.level.effect.MobEffect;

public abstract class EffectCustomTexture extends Effect
{
    protected static final Random rand;
    private final Color colorAsObj;
    
    public EffectCustomTexture(final EffectType type, final Color color) {
        super(type, color.getRGB());
        this.colorAsObj = color;
    }
    
    public void attachEventListeners(final IEventBus bus) {
    }
    
    public abstract SpriteQuery getSpriteQuery();
    
    @OnlyIn(Dist.CLIENT)
    public void renderInventoryEffect(final MobEffectInstance effect, final DisplayEffectsScreen<?> gui, final PoseStack renderStack, final int x, final int y, final float z) {
        final float wh = 18.0f;
        final float offsetX = (float)(x + 6);
        final float offsetY = (float)(y + 7);
        final float red = this.colorAsObj.getRed() / 255.0f;
        final float green = this.colorAsObj.getGreen() / 255.0f;
        final float blue = this.colorAsObj.getBlue() / 255.0f;
        final SpriteSheetResource ssr = this.getSpriteQuery().resolveSprite();
        ssr.bindTexture();
        final Tuple<Float, Float> uvTpl = ssr.getUVOffset(ClientScheduler.getClientTick());
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, offsetX, offsetY, z, wh, wh).color(red, green, blue, 1.0f).tex((float)uvTpl.getA(), (float)uvTpl.getB(), ssr.getUWidth(), ssr.getVWidth()).draw());
    }
    
    @OnlyIn(Dist.CLIENT)
    public void renderHUDEffect(final MobEffectInstance effect, final AbstractGui gui, final PoseStack renderStack, final int x, final int y, final float z, final float alpha) {
        final float wh = 18.0f;
        final float offsetX = (float)(x + 3);
        final float offsetY = (float)(y + 3);
        final float red = this.colorAsObj.getRed() / 255.0f;
        final float green = this.colorAsObj.getGreen() / 255.0f;
        final float blue = this.colorAsObj.getBlue() / 255.0f;
        final SpriteSheetResource ssr = this.getSpriteQuery().resolveSprite();
        ssr.bindTexture();
        final Tuple<Float, Float> uvTpl = ssr.getUVOffset(ClientScheduler.getClientTick());
        RenderingUtils.draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, offsetX, offsetY, z, wh, wh).color(red, green, blue, 1.0f).tex((float)uvTpl.getA(), (float)uvTpl.getB(), ssr.getUWidth(), ssr.getVWidth()).draw());
    }
    
    static {
        rand = new Random();
    }
}
