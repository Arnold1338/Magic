package hellfirepvp.astralsorcery.client.screen.journal.page;

import net.minecraft.world.item.Item;
import net.minecraftforge.fluids.FluidStack;
import hellfirepvp.astralsorcery.common.crafting.helper.ingredient.FluidIngredient;
import net.minecraft.tags.TagKey;
import java.util.Collection;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.data.research.ProgressionTier;
import hellfirepvp.astralsorcery.common.crafting.recipe.altar.AltarUpgradeRecipe;
import hellfirepvp.astralsorcery.common.block.tile.altar.AltarType;
import java.util.LinkedList;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import net.minecraft.util.text.ITextProperties;
import java.util.function.Consumer;
import hellfirepvp.astralsorcery.client.util.RenderingConstellationUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.client.util.RenderingDrawUtils;
import hellfirepvp.astralsorcery.client.render.IDrawRenderTypeBuffer;
import hellfirepvp.astralsorcery.common.auxiliary.book.BookLookupInfo;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.auxiliary.book.BookLookupRegistry;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.network.chat.Component;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.client.util.RenderingUtils;
import net.minecraft.client.renderer.RenderHelper;
import hellfirepvp.astralsorcery.common.crafting.helper.WrappedIngredient;
import hellfirepvp.astralsorcery.common.crafting.recipe.SimpleAltarRecipe;
import net.minecraft.util.math.MathHelper;
import java.util.List;
import hellfirepvp.astralsorcery.client.resource.BlockAtlasTexture;
import hellfirepvp.astralsorcery.common.util.IngredientHelper;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.client.util.RenderingGuiUtils;
import hellfirepvp.astralsorcery.client.util.Blending;
import com.mojang.blaze3d.systems.RenderSystem;
import hellfirepvp.astralsorcery.client.resource.AbstractRenderableTexture;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.HashMap;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.common.data.research.ResearchNode;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Tuple;
import java.awt.Rectangle;
import java.util.Map;

public abstract class RenderPageRecipeTemplate extends RenderablePage
{
    protected Map<Rectangle, Tuple<ItemStack, Ingredient>> thisFrameInputStacks;
    protected Tuple<Rectangle, ItemStack> thisFrameOuputStack;
    protected Rectangle thisFrameInfoStar;
    
    protected RenderPageRecipeTemplate(@Nullable final ResearchNode node, final int nodePage) {
        super(node, nodePage);
        this.thisFrameInputStacks = new HashMap<Rectangle, Tuple<ItemStack, Ingredient>>();
        this.thisFrameOuputStack = null;
        this.thisFrameInfoStar = null;
    }
    
    protected void clearFrameRectangles() {
        this.thisFrameInputStacks.clear();
        this.thisFrameOuputStack = null;
        this.thisFrameInfoStar = null;
    }
    
    public void renderRecipeGrid(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final AbstractRenderableTexture tex) {
        RenderSystem.enableBlend();
        Blending.DEFAULT.apply();
        tex.bindTexture();
        RenderingGuiUtils.drawRect(renderStack, offsetX + 25.0f, offsetY, zLevel, 129.0f, 202.0f);
        RenderSystem.disableBlend();
    }
    
    public void renderExpectedIngredientInput(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float scale, final long tickOffset, final Ingredient ingredient) {
        final ItemStack expected = IngredientHelper.getRandomVisibleStack(ingredient, ClientScheduler.getClientTick() + tickOffset);
        if (!expected.isEmpty()) {
            BlockAtlasTexture.getInstance().bindTexture();
            this.renderItemStack(renderStack, offsetX, offsetY, zLevel, scale, expected);
            this.thisFrameInputStacks.put(new Rectangle((int)offsetX, (int)offsetY, (int)(16.0f * scale), (int)(16.0f * scale)), (Tuple<ItemStack, Ingredient>)new Tuple((Object)expected, (Object)ingredient));
        }
    }
    
    public void renderExpectedIngredientInput(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float scale, final long tickOffset, final List<ItemStack> displayOptions) {
        final int mod = (int)((ClientScheduler.getClientTick() + tickOffset) / 20L % displayOptions.size());
        final ItemStack expected = displayOptions.get(MathHelper.func_76125_a(mod, 0, displayOptions.size() - 1));
        if (!expected.isEmpty()) {
            BlockAtlasTexture.getInstance().bindTexture();
            this.renderItemStack(renderStack, offsetX, offsetY, zLevel, scale, expected);
            this.thisFrameInputStacks.put(new Rectangle((int)offsetX, (int)offsetY, (int)(16.0f * scale), (int)(16.0f * scale)), (Tuple<ItemStack, Ingredient>)new Tuple((Object)expected, (Object)null));
        }
    }
    
    public void renderExpectedRelayInputs(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final SimpleAltarRecipe altarRecipe) {
        final float centerX = offsetX + 80.0f;
        final float centerY = offsetY + 128.0f;
        final float perc = ClientScheduler.getClientTick() % 3000L / 3000.0f;
        final List<WrappedIngredient> ingredients = altarRecipe.getRelayInputs();
        final int amt = ingredients.size();
        for (int i = 0; i < ingredients.size(); ++i) {
            double part = i / (double)amt * 2.0 * 3.141592653589793;
            part = MathHelper.func_151237_a(part, 0.0, 6.283185307179586);
            part += 6.283185307179586 * perc + 3.141592653589793;
            final double xAdd = Math.sin(part) * 75.0;
            final double yAdd = Math.cos(part) * 75.0;
            this.renderExpectedIngredientInput(renderStack, (float)(centerX + xAdd), (float)(centerY + yAdd), zLevel, 1.0f, i * 20, ingredients.get(i).getIngredient());
        }
    }
    
    public void renderExpectedItemStackOutput(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float scale, final ItemStack stack) {
        if (!stack.isEmpty()) {
            BlockAtlasTexture.getInstance().bindTexture();
            this.renderItemStack(renderStack, offsetX, offsetY, zLevel, scale, stack);
            this.thisFrameOuputStack = (Tuple<Rectangle, ItemStack>)new Tuple((Object)new Rectangle((int)offsetX, (int)offsetY, (int)(16.0f * scale), (int)(16.0f * scale)), (Object)stack);
        }
    }
    
    protected void renderItemStack(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float scale, final ItemStack stack) {
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderHelper.func_227780_a_();
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)offsetX, (double)offsetY, (double)zLevel);
        renderStack.func_227862_a_(scale, scale, 1.0f);
        RenderingUtils.renderItemStackGUI(renderStack, stack, null);
        renderStack.func_227865_b_();
        RenderHelper.func_74518_a();
        RenderSystem.depthMask(false);
    }
    
    public boolean handleRecipeNameCopyClick(final double mouseX, final double mouseZ, final SimpleAltarRecipe recipe) {
        if (Minecraft.func_71410_x().field_71474_y.field_74330_P && Screen.func_231172_r_() && ((Rectangle)this.thisFrameOuputStack.func_76341_a()).contains(mouseX, mouseZ)) {
            final String recipeName = recipe.func_199560_c().toString();
            Minecraft.func_71410_x().field_195559_v.func_197960_a(recipeName);
            Minecraft.func_71410_x().field_71439_g.func_145747_a((Component)new Component("astralsorcery.misc.ctrlcopy.copied", new Object[] { recipeName }), Util.NIL_UUID);
            return true;
        }
        return false;
    }
    
    public boolean handleBookLookupClick(final double mouseX, final double mouseZ) {
        for (final Rectangle r : this.thisFrameInputStacks.keySet()) {
            if (r.contains(mouseX, mouseZ)) {
                final ItemStack stack = (ItemStack)this.thisFrameInputStacks.get(r).func_76341_a();
                final BookLookupInfo info = BookLookupRegistry.findPage((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT, stack);
                if (info != null && info.canSee(ResearchHelper.getProgress((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT)) && !info.getResearchNode().equals(this.getResearchNode())) {
                    info.openGui();
                    return true;
                }
                continue;
            }
        }
        if (this.thisFrameOuputStack != null && ((Rectangle)this.thisFrameOuputStack.func_76341_a()).contains(mouseX, mouseZ)) {
            final ItemStack stack2 = (ItemStack)this.thisFrameOuputStack.func_76340_b();
            final BookLookupInfo info2 = BookLookupRegistry.findPage((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT, stack2);
            if (info2 != null && info2.canSee(ResearchHelper.getProgress((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT)) && !info2.getResearchNode().equals(this.getResearchNode())) {
                info2.openGui();
                return true;
            }
        }
        return false;
    }
    
    public void renderInfoStar(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, final float pTicks) {
        renderStack.func_227860_a_();
        renderStack.func_227861_a_((double)(offsetX + 140.0f), (double)(offsetY + 20.0f), (double)zLevel);
        (this.thisFrameInfoStar = RenderingDrawUtils.drawInfoStar(renderStack, IDrawRenderTypeBuffer.defaultBuffer(), 15.0f, pTicks)).translate((int)(offsetX + 140.0f), (int)(offsetY + 20.0f));
        renderStack.func_227865_b_();
    }
    
    public void renderRequiredConstellation(final PoseStack renderStack, final float offsetX, final float offsetY, final float zLevel, @Nullable final IConstellation constellation) {
        if (constellation != null) {
            RenderSystem.enableBlend();
            Blending.DEFAULT.apply();
            RenderingConstellationUtils.renderConstellationIntoGUI(new Color(15658734), constellation, renderStack, (float)Math.round(offsetX + 30.0f), (float)Math.round(offsetY + 78.0f), zLevel, 125.0f, 125.0f, 2.0, () -> 0.4f, true, false);
            RenderSystem.disableBlend();
        }
    }
    
    public void renderInfoStarTooltips(final PoseStack renderStack, final float offsetX, final float offsetY, float zLevel, final float mouseX, final float mouseY, final Consumer<List<ITextProperties>> tooltipProvider) {
        if (this.thisFrameInfoStar == null) {
            return;
        }
        if (this.thisFrameInfoStar.contains(mouseX, mouseY)) {
            final List<ITextProperties> toolTip = new ArrayList<ITextProperties>();
            tooltipProvider.accept(toolTip);
            if (!toolTip.isEmpty()) {
                zLevel += 600.0f;
                RenderingDrawUtils.renderBlueTooltipComponents(renderStack, offsetX, offsetY, zLevel, toolTip, RenderablePage.getFontRenderer(), false);
                zLevel -= 600.0f;
            }
        }
    }
    
    public void renderHoverTooltips(final PoseStack renderStack, final float mouseX, final float mouseY, float zLevel, final ResourceLocation recipeName) {
        final List<ITextProperties> toolTip = new LinkedList<ITextProperties>();
        this.addStackTooltip(mouseX, mouseY, recipeName, toolTip);
        if (!toolTip.isEmpty()) {
            zLevel += 800.0f;
            RenderingDrawUtils.renderBlueTooltipComponents(renderStack, mouseX, mouseY, zLevel, toolTip, RenderablePage.getFontRenderer(), true);
            zLevel -= 800.0f;
        }
    }
    
    protected void addAltarRecipeTooltip(final SimpleAltarRecipe altarRecipe, final List<ITextProperties> toolTip) {
        if (altarRecipe.getStarlightRequirement() > 0) {
            AltarType highestPossible = null;
            final ProgressionTier reached = ResearchHelper.getClientProgress().getTierReached();
            for (final AltarType type : AltarType.values()) {
                if ((highestPossible == null || !type.isThisLEThan(highestPossible)) && reached.isThisLaterOrEqual(type.getAssociatedTier().getRequiredProgress())) {
                    highestPossible = type;
                }
            }
            if (highestPossible != null) {
                final long indexSel = ClientScheduler.getClientTick() / 30L % (highestPossible.ordinal() + 1);
                final AltarType typeSelected = AltarType.values()[(int)indexSel];
                final ITextProperties itemName = (ITextProperties)typeSelected.getAltarItemRepresentation().func_200301_q();
                final ITextProperties starlightRequired = this.getAltarStarlightAmountDescription(itemName, (float)altarRecipe.getStarlightRequirement(), (float)typeSelected.getStarlightCapacity());
                final ITextProperties starlightRequirementDescription = (ITextProperties)new Component("astralsorcery.journal.recipe.altar.starlight.desc");
                toolTip.add(starlightRequirementDescription);
                toolTip.add(starlightRequired);
            }
        }
        if (altarRecipe instanceof AltarUpgradeRecipe) {
            toolTip.add((ITextProperties)new Component("astralsorcery.journal.recipe.altar.upgrade"));
        }
    }
    
    protected void addConstellationInfoTooltip(@Nullable final IConstellation cst, final List<ITextProperties> toolTip) {
        if (cst != null) {
            toolTip.add((ITextProperties)new Component("astralsorcery.journal.recipe.constellation", new Object[] { cst.getConstellationName() }));
        }
    }
    
    protected ITextProperties getAltarStarlightAmountDescription(final ITextProperties altarName, final float amountRequired, final float maxAmount) {
        String base = "astralsorcery.journal.recipe.altar.starlight.";
        final float perc = amountRequired / maxAmount;
        if (perc <= 0.1) {
            base += "lowest";
        }
        else if (perc <= 0.25) {
            base += "low";
        }
        else if (perc <= 0.5) {
            base += "avg";
        }
        else if (perc <= 0.75) {
            base += "more";
        }
        else if (perc <= 0.9) {
            base += "high";
        }
        else if (perc > 1.0f) {
            base += "toomuch";
        }
        else {
            base += "highest";
        }
        return (ITextProperties)new Component("astralsorcery.journal.recipe.altar.starlight.format", new Object[] { altarName, new Component(base) });
    }
    
    protected ITextProperties getInfuserChanceDescription(final float chance) {
        String base = "astralsorcery.journal.recipe.infusion.chance.";
        if (chance <= 0.3) {
            base += "low";
        }
        else if (chance <= 0.7) {
            base += "average";
        }
        else if (chance < 1.0f) {
            base += "high";
        }
        else {
            base += "always";
        }
        return (ITextProperties)new Component(base);
    }
    
    protected void addStackTooltip(final float mouseX, final float mouseY, final ResourceLocation recipeName, final List<ITextProperties> tooltip) {
        for (final Rectangle rect : this.thisFrameInputStacks.keySet()) {
            if (rect.contains(mouseX, mouseY)) {
                final Tuple<ItemStack, Ingredient> inputInfo = this.thisFrameInputStacks.get(rect);
                this.addInputInformation((ItemStack)inputInfo.func_76341_a(), (Ingredient)inputInfo.func_76340_b(), tooltip);
                return;
            }
        }
        if (((Rectangle)this.thisFrameOuputStack.func_76341_a()).contains(mouseX, mouseY)) {
            final ItemStack stack = (ItemStack)this.thisFrameOuputStack.func_76340_b();
            this.addInputInformation(stack, null, tooltip);
            if (Minecraft.func_71410_x().field_71474_y.field_74330_P) {
                tooltip.add((ITextProperties)Component.field_240750_d_);
                tooltip.add((ITextProperties)new Component("astralsorcery.misc.recipename", new Object[] { recipeName.toString() }).func_240699_a_(ChatFormatting.LIGHT_PURPLE).func_240699_a_(ChatFormatting.ITALIC));
                tooltip.add((ITextProperties)new Component("astralsorcery.misc.ctrlcopy", new Object[] { recipeName.toString() }).func_240699_a_(ChatFormatting.LIGHT_PURPLE).func_240699_a_(ChatFormatting.ITALIC));
            }
        }
    }
    
    protected void addInputInformation(final ItemStack stack, @Nullable final Ingredient stackIngredient, final List<ITextProperties> tooltip) {
        try {
            tooltip.addAll(stack.func_82840_a((Player)Minecraft.func_71410_x().field_71439_g, (ITooltipFlag)(Minecraft.func_71410_x().field_71474_y.field_82882_x ? ITooltipFlag.TooltipFlags.ADVANCED : ITooltipFlag.TooltipFlags.NORMAL)));
        }
        catch (final Exception exc) {
            tooltip.add((ITextProperties)new Component("astralsorcery.misc.tooltipError").func_240699_a_(ChatFormatting.RED));
        }
        final BookLookupInfo info = BookLookupRegistry.findPage((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT, stack);
        if (info != null && info.canSee(ResearchHelper.getProgress((Player)Minecraft.func_71410_x().field_71439_g, LogicalSide.CLIENT)) && !info.getResearchNode().equals(this.getResearchNode())) {
            tooltip.add((ITextProperties)Component.field_240750_d_);
            tooltip.add((ITextProperties)new Component("astralsorcery.misc.craftInformation").func_240699_a_(ChatFormatting.GRAY));
        }
        if (stackIngredient != null && Minecraft.func_71410_x().field_71474_y.field_82882_x) {
            final ITag<Item> itemTag = IngredientHelper.guessTag(stackIngredient);
            if (itemTag instanceof ITag.INamedTag) {
                tooltip.add((ITextProperties)Component.field_240750_d_);
                tooltip.add((ITextProperties)new Component("astralsorcery.misc.input.tag", new Object[] { ((ITag.INamedTag)itemTag).func_230234_a_().toString() }).func_240699_a_(ChatFormatting.GRAY));
            }
            if (stackIngredient instanceof FluidIngredient) {
                final List<FluidStack> fluids = ((FluidIngredient)stackIngredient).getFluids();
                if (!fluids.isEmpty()) {
                    ITextProperties cmp = null;
                    for (final FluidStack f : fluids) {
                        if (cmp == null) {
                            cmp = (ITextProperties)f.getFluid().getAttributes().getDisplayName(f);
                        }
                        else {
                            cmp = (ITextProperties)new Component("astralsorcery.misc.input.fluid.chain", new Object[] { cmp, f.getFluid().getAttributes().getDisplayName(f) }).func_240699_a_(ChatFormatting.GRAY);
                        }
                    }
                    tooltip.add((ITextProperties)Component.field_240750_d_);
                    tooltip.add((ITextProperties)new Component("astralsorcery.misc.input.fluid", new Object[] { cmp }).func_240699_a_(ChatFormatting.GRAY));
                }
            }
        }
    }
}
