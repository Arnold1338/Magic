package hellfirepvp.astralsorcery.client.screen.journal.perk;

import javax.annotation.Nullable;
import java.awt.geom.Rectangle2D;
import hellfirepvp.astralsorcery.common.perk.AllocationStatus;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Collection;

public interface PerkRender
{
    @OnlyIn(Dist.CLIENT)
    void addGroups(final Collection<PerkRenderGroup> p0);
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    Rectangle2D.Float renderPerkAtBatch(final BatchPerkContext p0, final PoseStack p1, final AllocationStatus p2, final long p3, final float p4, final float p5, final float p6, final float p7, final float p8);
}
