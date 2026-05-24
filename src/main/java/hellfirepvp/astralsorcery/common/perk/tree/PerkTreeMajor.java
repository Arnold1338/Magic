package hellfirepvp.astralsorcery.common.perk.tree;

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
import hellfirepvp.astralsorcery.client.screen.journal.perk.group.PerkPointHaloRenderGroup;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkRenderGroup;
import java.util.Collection;
import java.awt.geom.Point2D;
import hellfirepvp.astralsorcery.common.perk.node.MajorPerk;

public class PerkTreeMajor<T extends MajorPerk> extends PerkTreePoint<T>
{
    public PerkTreeMajor(final T perk, final Point2D.Float offset) {
        super(perk, offset);
        this.setRenderSize((int)(this.getRenderSize() * 1.4));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addGroups(final Collection<PerkRenderGroup> groups) {
        super.addGroups(groups);
        groups.add(PerkPointHaloRenderGroup.INSTANCE);
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    @Override
    public Rectangle2D.Float renderPerkAtBatch(final BatchPerkContext drawCtx, final PoseStack renderStack, final AllocationStatus status, final long spriteOffsetTick, final float pTicks, final float x, final float y, final float zLevel, final float scale) {
        final SpriteSheetResource tex = status.getPerkTreeHaloSprite();
        final BatchPerkContext.TextureObjectGroup grp = PerkPointHaloRenderGroup.INSTANCE.getGroup(tex);
        if (grp == null) {
            return new Rectangle2D.Float();
        }
        final BufferBuilder buf = drawCtx.getContext(grp);
        float haloSize = this.getRenderSize() * 0.8f * scale;
        if (status.isAllocated()) {
            haloSize *= 1.5;
        }
        final Tuple<Float, Float> frameUV = tex.getUVOffset(spriteOffsetTick);
        RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, x - haloSize, y - haloSize, zLevel, haloSize * 2.0f, haloSize * 2.0f).color(1.0f, 1.0f, 1.0f, 0.85f).tex((float)frameUV.func_76341_a(), (float)frameUV.func_76340_b(), tex.getULength(), tex.getVLength()).draw();
        super.renderPerkAtBatch(drawCtx, renderStack, status, spriteOffsetTick, pTicks, x, y, zLevel, scale);
        final float actualSize = this.getRenderSize() * scale;
        return new Rectangle2D.Float(-actualSize, -actualSize, actualSize * 2.0f, actualSize * 2.0f);
    }
}
