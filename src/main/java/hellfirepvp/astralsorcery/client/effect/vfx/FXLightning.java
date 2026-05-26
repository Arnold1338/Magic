package hellfirepvp.astralsorcery.client.effect.vfx;

import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import org.joml.Matrix4f;
import hellfirepvp.astralsorcery.client.util.RenderingVectorUtils;
import java.awt.Color;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.effect.context.base.BatchRenderContext;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import com.google.common.collect.Lists;
import java.util.Random;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;

public class FXLightning extends EntityVisualFX
{
    private static final float optimalLightningLength = 7.0f;
    private static final float growSpeed = 0.09f;
    private static final float fadeTime = 0.03f;
    private static final float defaultMinJitterDst = 0.2f;
    private static final float defaultMaxJitterDst = 0.7f;
    private static final float defaultForkChance = 1.0f;
    private static final float defaultMinForkAngleDeg = 15.0f;
    private static final float defaultMaxForkAngleDeg = 35.0f;
    private LightningVertex root;
    private float buildSpeed;
    private float buildWaitTime;
    private float bufRenderDepth;
    
    public FXLightning(final Vector3 pos) {
        super(pos);
        this.root = null;
        this.buildSpeed = 0.2f;
        this.buildWaitTime = 0.01f;
        this.bufRenderDepth = -1.0f;
    }
    
    public FXLightning setBuildSpeed(final float buildSpeed) {
        this.buildSpeed = buildSpeed;
        return this;
    }
    
    public FXLightning setBuildWaitTime(final float buildWaitTime) {
        this.buildWaitTime = buildWaitTime;
        return this;
    }
    
    public FXLightning makeDefault(final Vector3 to) {
        final double dstLength = to.clone().subtract(this.getPosition()).length();
        float perc = 1.0f;
        if (dstLength > 7.0) {
            perc = MathHelper.func_76133_a(dstLength / 7.0);
        }
        else if (dstLength < 7.0) {
            perc = (float)Math.pow(dstLength / 7.0, 2.0);
        }
        this.make(FXLightning.rand.nextLong(), this.getPosition(), to, 0.2f * perc, 0.7f * perc, 1.0f, 15.0f, 35.0f);
        this.setBuildSpeed(Math.max(0.01f, 0.09f * perc));
        this.setBuildWaitTime(Math.max(0.0067f, 0.03f * perc));
        return this;
    }
    
    private void make(final long seed, final Vector3 source, final Vector3 destination, final float minJitterDistance, final float maxJitterDistance, final float forkChance, final float minForkAngle, final float maxForkAngle) {
        final Vector3 directionVector = destination.clone().subtract(source);
        final Random lightningSeed = new Random(seed);
        List<LightningVertex> rootVertices = Lists.newLinkedList();
        this.root = new LightningVertex(source);
        this.root.next.add(new LightningVertex(destination));
        rootVertices.add(this.root);
        final double l = directionVector.length();
        for (int iterations = Math.min(MathHelper.func_76141_d((float)Math.round(Math.sqrt(l))), 200), i = 0; i < iterations; ++i) {
            final LinkedList<LightningVertex> newRootVertices = new LinkedList<LightningVertex>();
            for (final LightningVertex sourceVertex : rootVertices) {
                final LinkedList<LightningVertex> newNext = new LinkedList<LightningVertex>();
                for (final LightningVertex nextVertex : Lists.newArrayList((Iterable)sourceVertex.next)) {
                    final Vector3 direction = nextVertex.offset.clone().subtract(sourceVertex.offset);
                    final Vector3 split = direction.clone().multiply(0.5f).add(sourceVertex.offset);
                    final float jitDst = (minJitterDistance + (maxJitterDistance - minJitterDistance) * lightningSeed.nextFloat()) * ((iterations - i) / (float)iterations);
                    final Vector3 axPerp = direction.clone().perpendicular().rotate(lightningSeed.nextFloat() * 2.0f * 3.141592653589793, direction).normalize().multiply(jitDst);
                    split.add(axPerp);
                    final LightningVertex newVertex = new LightningVertex(split);
                    newVertex.next.add(nextVertex);
                    newNext.add(newVertex);
                    if (lightningSeed.nextFloat() < forkChance) {
                        final Vector3 dirFork = split.clone().subtract(sourceVertex.offset);
                        float forkAngle = minForkAngle + (maxForkAngle - minForkAngle) * lightningSeed.nextFloat();
                        forkAngle = (float)Math.toRadians(forkAngle);
                        final Vector3 perpAxis = dirFork.clone().perpendicular().rotate(lightningSeed.nextFloat() * 2.0f * 3.141592653589793, dirFork);
                        final Vector3 dirPos = dirFork.clone().rotate(forkAngle, perpAxis).normalize().multiply(dirFork.length() * 3.0 / 4.0).add(split);
                        final LightningVertex forkVertex = new LightningVertex(dirPos);
                        newVertex.next.add(forkVertex);
                    }
                    newRootVertices.add(newVertex);
                }
                sourceVertex.next = newNext;
                newRootVertices.add(sourceVertex);
            }
            rootVertices = newRootVertices;
        }
    }
    
    @Override
    public <T extends EntityVisualFX> void render(final BatchRenderContext<T> ctx, final PoseStack renderStack, final VertexConsumer vb, final float pTicks) {
        if (this.root == null) {
            return;
        }
        final Color c = this.getColor(pTicks);
        final float alpha = (float)this.getAlpha(pTicks);
        this.bufRenderDepth = Math.min(1.0f, (this.age + pTicks) / (this.buildSpeed * 20.0f));
        this.renderRec(this.root, vb, renderStack, pTicks, c.getRed() / 255.0f, c.getGreen() / 255.0f, c.getBlue() / 255.0f, alpha / 255.0f);
    }
    
    private void renderRec(final LightningVertex root, final VertexConsumer vb, final PoseStack renderStack, final float pTicks, final float r, final float g, final float b, final float a) {
        final int allDepth = root.followingDepth;
        final boolean mayRenderNext = 1.0f - root.followingDepth / (float)allDepth <= this.bufRenderDepth;
        final Vector3 playerOffset = RenderingVectorUtils.getStandardTranslationRemovalVector(pTicks);
        for (final LightningVertex next : root.next) {
            final Vector3 from = root.offset.clone().subtract(playerOffset);
            final Vector3 to = next.offset.clone().subtract(playerOffset);
            this.drawLine(from, to, vb, renderStack, r, g, b, a);
            if (mayRenderNext) {
                this.renderRec(next, vb, renderStack, pTicks, r, g, b, a);
            }
        }
    }
    
    private void drawLine(final Vector3 from, final Vector3 to, final VertexConsumer vb, final PoseStack renderStack, final float r, final float g, final float b, final float a) {
        this.renderCurrentTextureAroundAxis(from, to, Math.toRadians(0.0), 0.03500000014901161, vb, renderStack, r, g, b, a);
        this.renderCurrentTextureAroundAxis(from, to, Math.toRadians(90.0), 0.03500000014901161, vb, renderStack, r, g, b, a);
    }
    
    private void renderCurrentTextureAroundAxis(final Vector3 from, final Vector3 to, final double angle, final double size, final VertexConsumer buf, final PoseStack renderStack, final float r, final float g, final float b, final float a) {
        final Vector3 aim = to.clone().subtract(from).normalize();
        final Vector3 aimPerp = aim.clone().perpendicular().normalize();
        final Vector3 perp = aimPerp.clone().rotate(angle, aim).normalize();
        final Vector3 perpFrom = perp.clone().multiply(size);
        final Vector3 perpTo = perp.multiply(size);
        final Matrix4f matr = renderStack.last().func_227870_a_();
        Vector3 vec = from.clone().add(perpFrom.clone().multiply(-1));
        vec.drawPos(matr, buf).color(r, g, b, a).func_225583_a_(1.0f, 1.0f).endVertex();
        vec = from.clone().add(perpFrom);
        vec.drawPos(matr, buf).color(r, g, b, a).func_225583_a_(1.0f, 0.0f).endVertex();
        vec = to.clone().add(perpTo);
        vec.drawPos(matr, buf).color(r, g, b, a).func_225583_a_(0.0f, 0.0f).endVertex();
        vec = to.clone().add(perpTo.clone().multiply(-1));
        vec.drawPos(matr, buf).color(r, g, b, a).func_225583_a_(0.0f, 1.0f).endVertex();
    }
    
    @Override
    public boolean canRemove() {
        return Math.max((this.buildSpeed + this.buildWaitTime) * 20.0f, 1.0f) < this.age;
    }
    
    private static class LightningVertex
    {
        private final Vector3 offset;
        private List<LightningVertex> next;
        private int followingDepth;
        
        private LightningVertex(final Vector3 offset) {
            this.next = new LinkedList<LightningVertex>();
            this.followingDepth = -1;
            this.offset = offset;
        }
        
        public void calcDepthRec() {
            if (this.next.isEmpty()) {
                this.followingDepth = 0;
            }
            else {
                for (final LightningVertex vertex : this.next) {
                    vertex.calcDepthRec();
                }
                this.followingDepth = MiscUtils.getMaxEntry((Collection<LightningVertex>)this.next, v -> v.followingDepth) + 1;
            }
        }
    }
}
