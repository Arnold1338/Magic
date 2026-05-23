package hellfirepvp.observerlib.client.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Consumer;

@OnlyIn(Dist.CLIENT)
public class BufferDecoratorBuilder {
    private PositionDecorator positionDecorator;
    private ColorDecorator colorDecorator;
    private UVDecorator uvDecorator;
    private IntMapDecorator overlayDecorator;
    private IntMapDecorator lightmapDecorator;
    private NormalDecorator normalDecorator;

    public BufferDecoratorBuilder setPositionDecorator(PositionDecorator d) { positionDecorator = d; return this; }
    public BufferDecoratorBuilder setColorDecorator(ColorDecorator d) { colorDecorator = d; return this; }
    public BufferDecoratorBuilder setUvDecorator(UVDecorator d) { uvDecorator = d; return this; }
    public BufferDecoratorBuilder setOverlayDecorator(IntMapDecorator d) { overlayDecorator = d; return this; }
    public BufferDecoratorBuilder setLightmapDecorator(IntMapDecorator d) { lightmapDecorator = d; return this; }
    public BufferDecoratorBuilder setNormalDecorator(NormalDecorator d) { normalDecorator = d; return this; }

    public void decorate(VertexConsumer builder, Consumer<VertexConsumer> runDecorated) {
        runDecorated.accept(new DecoratedConsumer(builder, this));
    }

    public VertexConsumer decorate(VertexConsumer builder) { return new DecoratedConsumer(builder, this); }

    private static class DecoratedConsumer implements VertexConsumer {
        final VertexConsumer delegate;
        final BufferDecoratorBuilder dec;

        DecoratedConsumer(VertexConsumer delegate, BufferDecoratorBuilder dec) {
            this.delegate = delegate; this.dec = dec;
        }

        @Override
        public VertexConsumer vertex(double x, double y, double z) {
            if (dec.positionDecorator != null) { double[] p = dec.positionDecorator.decorate(x,y,z); delegate.vertex(p[0],p[1],p[2]); }
            else delegate.vertex(x,y,z);
            return this;
        }

        @Override
        public VertexConsumer color(int r, int g, int b, int a) {
            if (dec.colorDecorator != null) { int[] c = dec.colorDecorator.decorate(r,g,b,a); delegate.color(c[0],c[1],c[2],c[3]); }
            else delegate.color(r,g,b,a);
            return this;
        }

        @Override
        public VertexConsumer uv(float u, float v) {
            if (dec.uvDecorator != null) { float[] uv = dec.uvDecorator.decorate(u,v); delegate.uv(uv[0],uv[1]); }
            else delegate.uv(u,v);
            return this;
        }

        @Override
        public VertexConsumer overlayCoords(int u, int v) {
            if (dec.overlayDecorator != null) { int[] ov = dec.overlayDecorator.decorate(u,v); delegate.overlayCoords(ov[0],ov[1]); }
            else delegate.overlayCoords(u,v);
            return this;
        }

        @Override
        public VertexConsumer uv2(int u, int v) {
            if (dec.lightmapDecorator != null) { int[] lm = dec.lightmapDecorator.decorate(u,v); delegate.uv2(lm[0],lm[1]); }
            else delegate.uv2(u,v);
            return this;
        }

        @Override
        public VertexConsumer normal(float x, float y, float z) {
            if (dec.normalDecorator != null) { float[] n = dec.normalDecorator.decorate(x,y,z); delegate.normal(n[0],n[1],n[2]); }
            else delegate.normal(x,y,z);
            return this;
        }

        @Override public void endVertex() { delegate.endVertex(); }
        @Override public void defaultColor(int r, int g, int b, int a) { delegate.defaultColor(r,g,b,a); }
        @Override public void unsetDefaultColor() { delegate.unsetDefaultColor(); }
    }

    public interface ColorDecorator { int[] decorate(int r, int g, int b, int a); }
    public interface UVDecorator { float[] decorate(float u, float v); }
    public interface IntMapDecorator { int[] decorate(int u, int v); }
    public interface NormalDecorator { float[] decorate(float x, float y, float z); }
    public interface PositionDecorator { double[] decorate(double x, double y, double z); }
}
