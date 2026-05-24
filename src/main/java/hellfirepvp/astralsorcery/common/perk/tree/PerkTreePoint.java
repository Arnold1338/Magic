package hellfirepvp.astralsorcery.common.perk.tree;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.util.Tuple;
import com.mojang.blaze3d.vertex.BufferBuilder;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import java.awt.geom.Rectangle2D;
import hellfirepvp.astralsorcery.common.perk.AllocationStatus;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.client.screen.journal.perk.BatchPerkContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.perk.group.PerkPointRenderGroup;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkRenderGroup;
import java.util.Collection;
import java.awt.geom.Point2D;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkRender;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;

public class PerkTreePoint<T extends AbstractPerk> implements PerkRender
{
    private final Point2D.Float offset;
    private final T perk;
    private int renderSize;
    private static final int spriteSize = 11;
    
    public PerkTreePoint(final T perk, final Point2D.Float offset) {
        this.offset = offset;
        this.perk = perk;
        this.renderSize = 11;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addGroups(final Collection<PerkRenderGroup> groups) {
        groups.add(PerkPointRenderGroup.INSTANCE);
    }
    
    public void setRenderSize(final int renderSize) {
        this.renderSize = renderSize;
    }
    
    public int getRenderSize() {
        return this.renderSize;
    }
    
    public T getPerk() {
        return this.perk;
    }
    
    public Point2D.Float getOffset() {
        return this.offset;
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    @Override
    public Rectangle2D.Float renderPerkAtBatch(final BatchPerkContext drawCtx, final PoseStack renderStack, final AllocationStatus status, final long spriteOffsetTick, final float pTicks, final float x, final float y, final float zLevel, final float scale) {
        final SpriteSheetResource tex = status.getPerkTreeSprite();
        final BatchPerkContext.TextureObjectGroup grp = PerkPointRenderGroup.INSTANCE.getGroup(tex);
        if (grp == null) {
            return new Rectangle2D.Float();
        }
        final BufferBuilder buf = drawCtx.getContext(grp);
        final float size = this.renderSize * scale;
        final Tuple<Float, Float> frameUV = tex.getUVOffset(spriteOffsetTick);
        RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, x - size, y - size, zLevel, size * 2.0f, size * 2.0f).tex((float)frameUV.func_76341_a(), (float)frameUV.func_76340_b(), tex.getULength(), tex.getVLength()).draw();
        return new Rectangle2D.Float(-size, -size, size * 2.0f, size * 2.0f);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PerkTreePoint that = (PerkTreePoint)o;
        return Objects.equals(this.offset, that.offset);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.offset);
    }
}
