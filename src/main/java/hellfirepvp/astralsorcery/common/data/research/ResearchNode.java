package hellfirepvp.astralsorcery.common.data.research;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.auxiliary.book.BookLookupRegistry;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Arrays;
import net.minecraft.world.level.level.ItemLike;
import java.util.LinkedList;
import java.util.ArrayList;
import hellfirepvp.astralsorcery.client.resource.AssetLoader;
import hellfirepvp.astralsorcery.common.data.journal.JournalPage;
import java.util.List;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.resource.query.TextureQuery;
import hellfirepvp.astralsorcery.client.resource.query.SpriteQuery;
import net.minecraft.world.level.item.ItemStack;

public class ResearchNode
{
    private static int counter;
    private final int id;
    private final NodeRenderType nodeRenderType;
    public final float renderPosX;
    public final float renderPosZ;
    private final String unlocName;
    private ItemStack[] renderItemStacks;
    private SpriteQuery renderSpriteQuery;
    private TextureQuery backgroundTextureQuery;
    private Color textureColorHint;
    private final List<ResearchNode> connectionsTo;
    private final List<JournalPage> pages;
    
    private ResearchNode(final NodeRenderType type, final String unlocName, final float rPosX, final float rPosZ) {
        this.backgroundTextureQuery = new TextureQuery(AssetLoader.TextureLocation.GUI, new String[] { "research_frame_wood" });
        this.textureColorHint = new Color(-1, true);
        this.connectionsTo = new ArrayList<ResearchNode>();
        this.pages = new LinkedList<JournalPage>();
        this.id = ResearchNode.counter;
        ++ResearchNode.counter;
        this.nodeRenderType = type;
        this.renderPosX = rPosX;
        this.renderPosZ = rPosZ;
        this.unlocName = unlocName;
    }
    
    public ResearchNode(final ItemLike item, final String unlocName, final float renderPosX, final float renderPosZ) {
        this(new ItemStack(item), unlocName, renderPosX, renderPosZ);
    }
    
    public ResearchNode(final ItemStack itemStack, final String unlocName, final float renderPosX, final float renderPosZ) {
        this(NodeRenderType.ITEMSTACK, unlocName, renderPosX, renderPosZ);
        this.renderItemStacks = new ItemStack[] { itemStack };
    }
    
    public ResearchNode(final ItemLike[] items, final String unlocName, final float renderPosX, final float renderPosZ) {
        this(NodeRenderType.ITEMSTACK, unlocName, renderPosX, renderPosZ);
        this.renderItemStacks = new ItemStack[items.length];
        for (int i = 0; i < items.length; ++i) {
            this.renderItemStacks[i] = new ItemStack(items[i]);
        }
    }
    
    public ResearchNode(final ItemStack[] stacks, final String unlocName, final float renderPosX, final float renderPosZ) {
        this(NodeRenderType.ITEMSTACK, unlocName, renderPosX, renderPosZ);
        this.renderItemStacks = stacks;
    }
    
    public ResearchNode(final SpriteQuery query, final String unlocName, final float renderPosX, final float renderPosZ) {
        this(NodeRenderType.TEXTURE_SPRITE, unlocName, renderPosX, renderPosZ);
        this.renderSpriteQuery = query;
    }
    
    public ResearchNode addSourceConnectionFrom(final ResearchNode node) {
        this.connectionsTo.add(node);
        return this;
    }
    
    public ResearchNode addSourceConnectionFrom(final ResearchNode... node) {
        return this.addSourceConnectionsFrom(Arrays.asList(node));
    }
    
    public ResearchNode addSourceConnectionsFrom(final Collection<ResearchNode> node) {
        this.connectionsTo.addAll(node);
        return this;
    }
    
    public List<ResearchNode> getConnectionsTo() {
        return this.connectionsTo;
    }
    
    public ResearchNode addPage(final JournalPage page) {
        this.pages.add(page);
        return this;
    }
    
    public boolean canSee(@Nullable final PlayerProgress progress) {
        return true;
    }
    
    public ResearchNode setTextureColorHintWithAlpha(final Color textureColorHint) {
        this.textureColorHint = textureColorHint;
        return this;
    }
    
    public ResearchNode register(final ResearchProgression progression) {
        progression.getRegistrar().accept(this);
        return this;
    }
    
    public ResearchNode addTomeLookup(final ItemLike item, final int nodePage, final ResearchProgression progression) {
        BookLookupRegistry.registerItemLookup(item, this, nodePage, progression);
        return this;
    }
    
    public ResearchNode addTomeLookup(final ItemStack item, final int nodePage, final ResearchProgression progression) {
        BookLookupRegistry.registerItemLookup(item, this, nodePage, progression);
        return this;
    }
    
    public Color getTextureColorHint() {
        return this.textureColorHint;
    }
    
    public NodeRenderType getNodeRenderType() {
        return this.nodeRenderType;
    }
    
    public ItemStack getRenderItemStack() {
        return this.getRenderItemStack(0L);
    }
    
    public ItemStack getRenderItemStack(final long tick) {
        return this.renderItemStacks[(int)(tick / 40L % this.renderItemStacks.length)];
    }
    
    public TextureQuery getBackgroundTexture() {
        return this.backgroundTextureQuery;
    }
    
    public SpriteQuery getSpriteTexture() {
        return this.renderSpriteQuery;
    }
    
    public List<JournalPage> getPages() {
        return this.pages;
    }
    
    public Component getName() {
        return (Component)new Component(String.format("astralsorcery.journal.node.%s.name", this.getKey()));
    }
    
    public String getKey() {
        return this.unlocName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final ResearchNode that = (ResearchNode)o;
        return this.id == that.id;
    }
    
    @Override
    public int hashCode() {
        return this.id;
    }
    
    static {
        ResearchNode.counter = 0;
    }
    
    public enum NodeRenderType
    {
        ITEMSTACK, 
        TEXTURE_SPRITE;
    }
}
