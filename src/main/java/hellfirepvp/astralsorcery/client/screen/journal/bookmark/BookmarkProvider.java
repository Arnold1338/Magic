package hellfirepvp.astralsorcery.client.screen.journal.bookmark;

import hellfirepvp.astralsorcery.client.lib.TexturesAS;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.client.gui.screens.Screen;
import java.util.function.Supplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BookmarkProvider
{
    private final Supplier<Screen> provider;
    private final int index;
    private final IFormattableTextComponent unlocName;
    private final Supplier<Boolean> canSeeTest;
    
    public BookmarkProvider(final String unlocName, final int bookmarkIndex, final Supplier<Screen> guiProvider, final Supplier<Boolean> canSeeTest) {
        this.unlocName = (IFormattableTextComponent)new Component(unlocName);
        this.index = bookmarkIndex;
        this.provider = guiProvider;
        this.canSeeTest = canSeeTest;
    }
    
    public Screen getGuiScreen() {
        return this.provider.get();
    }
    
    public boolean canSee() {
        return this.canSeeTest.get();
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public IFormattableTextComponent getUnlocalizedName() {
        return this.unlocName;
    }
    
    public AbstractRenderableTexture getTextureBookmark() {
        return TexturesAS.TEX_GUI_BOOKMARK;
    }
    
    public AbstractRenderableTexture getTextureBookmarkStretched() {
        return TexturesAS.TEX_GUI_BOOKMARK_STRETCHED;
    }
}
