package hellfirepvp.astralsorcery.common.data.journal;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.ITextProperties;
import hellfirepvp.observerlib.api.structure.Structure;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderPageStructure;
import hellfirepvp.astralsorcery.client.screen.journal.page.RenderablePage;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.network.chat.Component;
import hellfirepvp.observerlib.api.util.BlockArray;

public class JournalPageStructure implements JournalPage
{
    private final BlockArray structure;
    private final Component name;
    private final Vector3 shift;
    
    public JournalPageStructure(final BlockArray struct) {
        this(struct, null);
    }
    
    public JournalPageStructure(final BlockArray struct, @Nullable final Component name) {
        this(struct, name, new Vector3());
    }
    
    public JournalPageStructure(final BlockArray struct, @Nullable final Component name, @Nonnull final Vector3 shift) {
        this.structure = struct;
        this.name = name;
        this.shift = shift;
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public RenderablePage buildRenderPage(final ResearchNode node, final int nodePage) {
        return new RenderPageStructure(node, nodePage, (Structure)this.structure, (ITextProperties)this.name, this.shift.clone());
    }
}
