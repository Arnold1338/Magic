package hellfirepvp.observerlib.client.util;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class RenderTypeDecorator extends RenderType {
    private final RenderType decorated;
    private final Runnable afterSetup;
    private final Runnable beforeClean;

    private RenderTypeDecorator(RenderType type, Runnable afterSetup, Runnable beforeClean) {
        super(
            type.toString(),
            type.format(),
            type.mode(),
            type.bufferSize(),
            type.affectsCrumbling(),
            true, // sort on upload - needed for transparency
            () -> {},
            () -> {}
        );
        this.decorated = type;
        this.afterSetup = afterSetup;
        this.beforeClean = beforeClean;
    }

    public static RenderTypeDecorator wrapSetup(RenderType type, Runnable setup, Runnable clean) {
        return new RenderTypeDecorator(type, setup, clean);
    }

    @Override
    public void setupRenderState() {
        decorated.setupRenderState();
        afterSetup.run();
    }

    @Override
    public void clearRenderState() {
        beforeClean.run();
        decorated.clearRenderState();
    }

    @Override
    public Optional<RenderType> outline() { return decorated.outline(); }

    @Override
    public boolean isOutline() { return decorated.isOutline(); }

    @Override
    public String toString() { return decorated.toString(); }
}
