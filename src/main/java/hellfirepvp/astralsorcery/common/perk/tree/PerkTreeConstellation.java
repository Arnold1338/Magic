package hellfirepvp.astralsorcery.common.perk.tree;

import javax.annotation.Nullable;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.client.util.draw.BufferContext;
import hellfirepvp.astralsorcery.client.resource.SpriteSheetResource;
import com.mojang.blaze3d.vertex.VertexConsumer;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import java.awt.geom.Rectangle2D;
import hellfirepvp.astralsorcery.client.screen.journal.perk.BatchPerkContext;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import hellfirepvp.astralsorcery.common.perk.AllocationStatus;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.screen.journal.perk.group.PerkPointHaloRenderGroup;
import hellfirepvp.astralsorcery.client.screen.journal.perk.PerkRenderGroup;
import java.util.Collection;
import java.awt.geom.Point2D;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.screen.journal.perk.DynamicPerkRender;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;

public class PerkTreeConstellation<T extends AbstractPerk> extends PerkTreePoint<T> implements DynamicPerkRender
{
    public static final int ROOT_SPRITE_SIZE = 50;
    public static final int MINOR_SPRITE_SIZE = 40;
    private final IConstellation associatedConstellation;
    private final int perkSpriteSize;
    
    public PerkTreeConstellation(final T perk, final Point2D.Float offset, final IConstellation associatedConstellation, final int perkSpriteSize) {
        super(perk, offset);
        this.associatedConstellation = associatedConstellation;
        this.setRenderSize((this.perkSpriteSize = perkSpriteSize) / 2);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addGroups(final Collection<PerkRenderGroup> groups) {
        super.addGroups(groups);
        groups.add(PerkPointHaloRenderGroup.INSTANCE);
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderAt(final AllocationStatus status, final PoseStack renderStack, final long spriteOffsetTick, final float pTicks, final float x, final float y, final float zLevel, final float scale) {
        if (this.associatedConstellation == null) {
            return;
        }
        final PlayerProgress prog = ResearchHelper.getClientProgress();
        if (!prog.hasConstellationDiscovered(this.associatedConstellation)) {
            return;
        }
        final float size = this.perkSpriteSize * scale * 0.85f;
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        RenderingConstellationUtils.renderConstellationIntoGUI(Color.WHITE, this.associatedConstellation, renderStack, x - size, y - size, 0.0f, size * 2.0f, size * 2.0f, 3.0f * scale, () -> 0.8f, true, false);
        RenderSystem.disableBlend();
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
        final BufferContext buf = drawCtx.getContext(grp);
        float haloSize = this.perkSpriteSize * scale;
        if (status.isAllocated()) {
            haloSize *= 1.3f;
        }
        final Tuple<Float, Float> frameUV = tex.getUVOffset(spriteOffsetTick);
        RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, x - haloSize, y - haloSize, zLevel, haloSize * 2.0f, haloSize * 2.0f).color(1.0f, 1.0f, 1.0f, 0.85f).tex((float)frameUV.getA(), (float)frameUV.getB(), tex.getULength(), tex.getVLength()).draw();
        super.renderPerkAtBatch(drawCtx, renderStack, status, spriteOffsetTick, pTicks, x, y, zLevel, scale);
        final float actualSize = this.perkSpriteSize * scale;
        return new Rectangle2D.Float(-actualSize, -actualSize, actualSize * 2.0f, actualSize * 2.0f);
    }
}
