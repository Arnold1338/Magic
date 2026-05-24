package hellfirepvp.astralsorcery.client.model.builtin;

import net.minecraft.client.resources.model.ModelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import java.util.function.Function;
import net.minecraft.client.resources.model.Model;

public abstract class CustomModel extends Model
{
    public CustomModel(final Function<ResourceLocation, RenderType> renderTypeIn) {
        super((Function)renderTypeIn);
    }
    
    public final RenderType getGeneralType() {
        return this.func_228282_a_(AtlasTexture.field_110575_b);
    }
    
    public void render(final PoseStack matrixStackIn, final MultiBufferSource buffer, final int packedLightIn, final int packedOverlayIn) {
        this.func_225598_a_(matrixStackIn, buffer.getBuffer(this.getGeneralType()), packedLightIn, packedOverlayIn, 1.0f, 1.0f, 1.0f, 1.0f);
    }
    
    protected void setRotateAngle(final ModelRenderer modelPart, final float x, final float y, final float z) {
        modelPart.field_78795_f = x;
        modelPart.field_78796_g = y;
        modelPart.field_78808_h = z;
    }
}
