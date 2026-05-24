package hellfirepvp.astralsorcery.common.crafting.recipe.interaction.jei;

import net.minecraft.world.level.entity.Entity;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.InteractionResult;
import hellfirepvp.astralsorcery.client.util.LightmapUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Vector3f;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.world.level.level.Level;
import net.minecraft.client.Minecraft;
import hellfirepvp.astralsorcery.common.crafting.recipe.interaction.ResultSpawnEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import mezz.jei.api.ingredients.IIngredients;
import hellfirepvp.astralsorcery.common.crafting.recipe.LiquidInteraction;
import mezz.jei.api.gui.IRecipeLayout;

public class JEIHandlerSpawnEntity extends JEIInteractionResultHandler
{
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addToRecipeLayout(final IRecipeLayout recipeLayout, final LiquidInteraction recipe, final IIngredients ingredients) {
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void addToRecipeIngredients(final LiquidInteraction recipe, final IIngredients ingredients) {
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    public void drawRecipe(final LiquidInteraction recipe, final PoseStack renderStack, final double mouseX, final double mouseY) {
        final InteractionResult result = recipe.getResult();
        if (!(result instanceof ResultSpawnEntity)) {
            return;
        }
        final Entity le = ((ResultSpawnEntity)result).getEntityType().func_200721_a((World)Minecraft.func_71410_x().field_71441_e);
        if (!(le instanceof LivingEntity)) {
            return;
        }
        renderStack.func_227860_a_();
        renderStack.func_227861_a_(55.0, 35.0, 500.0);
        renderStack.func_227862_a_(15.0f, 15.0f, 15.0f);
        renderStack.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(180.0f));
        renderStack.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(145.0f));
        final MultiBufferSource.Impl buffer = MultiBufferSource.func_228455_a_(Tessellator.func_178181_a().func_178180_c());
        Minecraft.func_71410_x().func_175598_ae().func_229084_a_(le, 0.0, 0.0, 0.0, 0.0f, 0.0f, renderStack, (MultiBufferSource)buffer, LightmapUtil.getPackedFullbrightCoords());
        buffer.func_228461_a_();
        renderStack.func_227865_b_();
    }
}
