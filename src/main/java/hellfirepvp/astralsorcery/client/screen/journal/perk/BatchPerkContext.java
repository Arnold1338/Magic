package hellfirepvp.astralsorcery.client.screen.journal.perk;

import java.util.Objects;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.util.Iterator;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.util.draw.BufferBatchHelper;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import hellfirepvp.astralsorcery.client.util.draw.BufferContext;
import java.util.TreeMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BatchPerkContext
{
    public static final int PRIORITY_BACKGROUND = 100;
    public static final int PRIORITY_FOREGROUND = 200;
    public static final int PRIORITY_OVERLAY = 300;
    private final TreeMap<TextureObjectGroup, BufferContext> bufferGroups;
    
    public BatchPerkContext() {
        this.bufferGroups = new TreeMap<TextureObjectGroup, BufferContext>();
    }
    
    public TextureObjectGroup addContext(final AbstractRenderableTexture tex, final int sortPriority) {
        TextureObjectGroup group = MiscUtils.iterativeSearch(this.bufferGroups.keySet(), gr -> gr.getResource().equals(tex));
        if (group == null) {
            group = new TextureObjectGroup(tex, sortPriority);
            this.bufferGroups.put(group, BufferBatchHelper.make());
        }
        return group;
    }
    
    @Nonnull
    public BufferContext getContext(final TextureObjectGroup grp) {
        final BufferContext ctx = this.bufferGroups.get(grp);
        if (ctx == null) {
            throw new IllegalArgumentException("Unknown TextureGroup!");
        }
        return ctx;
    }
    
    public void draw() {
        for (final TextureObjectGroup group : this.bufferGroups.keySet()) {
            final BufferContext batch = this.bufferGroups.get(group);
            group.getResource().bindTexture();
            batch.draw();
        }
    }
    
    public void beginDrawingPerks() {
        for (final TextureObjectGroup group : this.bufferGroups.keySet()) {
            this.bufferGroups.get(group).func_181668_a(7, DefaultVertexFormat.POSITION_TEX_COLOR);
        }
    }
    
    public static class TextureObjectGroup implements Comparable<TextureObjectGroup>
    {
        private final AbstractRenderableTexture resource;
        private final int priority;
        
        private TextureObjectGroup(final AbstractRenderableTexture resource, final int priority) {
            this.resource = resource;
            this.priority = priority;
        }
        
        public AbstractRenderableTexture getResource() {
            return this.resource;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final TextureObjectGroup that = (TextureObjectGroup)o;
            return Objects.equals(this.resource, that.resource);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(this.resource);
        }
        
        @Override
        public int compareTo(final TextureObjectGroup o) {
            return Integer.compare(this.priority, o.priority);
        }
    }
}
