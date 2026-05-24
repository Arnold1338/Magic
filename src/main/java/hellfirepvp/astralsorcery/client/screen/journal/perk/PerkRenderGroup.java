package hellfirepvp.astralsorcery.client.screen.journal.perk;

import java.util.Objects;
import javax.annotation.Nullable;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.util.Iterator;
import com.google.common.collect.Maps;
import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import java.util.Map;
import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PerkRenderGroup
{
    private static int counter;
    private final int id;
    private final List<BatchPerkContext.TextureObjectGroup> addedGroups;
    private final Map<AbstractRenderableTexture, Integer> underlyingTextures;
    
    public PerkRenderGroup() {
        this.addedGroups = Lists.newArrayList();
        this.underlyingTextures = Maps.newHashMap();
        this.id = PerkRenderGroup.counter++;
    }
    
    public void add(final AbstractRenderableTexture texture, final Integer priority) {
        this.underlyingTextures.put(texture, priority);
    }
    
    public void batchRegister(final BatchPerkContext ctx) {
        for (final AbstractRenderableTexture tex : this.underlyingTextures.keySet()) {
            this.addedGroups.add(ctx.addContext(tex, this.underlyingTextures.get(tex)));
        }
    }
    
    @Nullable
    public BatchPerkContext.TextureObjectGroup getGroup(final AbstractRenderableTexture texture) {
        return MiscUtils.iterativeSearch(this.addedGroups, grp -> grp.getResource().equals(texture));
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final PerkRenderGroup that = (PerkRenderGroup)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
    
    static {
        PerkRenderGroup.counter = 0;
    }
}
