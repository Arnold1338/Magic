package hellfirepvp.astralsorcery.client.util;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.DiggingParticle;
import hellfirepvp.observerlib.client.util.RenderTypeDecorator;
import hellfirepvp.observerlib.client.util.BufferDecoratorBuilder;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.core.Registry;
import hellfirepvp.observerlib.common.util.RegistryUtil;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.client.color.ItemColors;
import net.minecraft.client.resources.model.BakedQuad;
import java.util.List;
import net.minecraft.core.Direction;
import java.util.Iterator;
import net.minecraft.world.item.CompassItem;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.ItemRenderer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Items;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.player.ClientPlayerEntity;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.ObjectUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraft.client.resources.model.ItemTransforms;
import hellfirepvp.astralsorcery.client.ClientScheduler;
import hellfirepvp.astralsorcery.common.util.reflection.ReflectionHelper;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.LanguageMap;
import net.minecraft.client.renderer.MultiBufferSource;
import org.joml.Vector3f;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.util.FormattedCharSequence;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.RenderType;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Function;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.function.Consumer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.client.data.config.entry.RenderingConfig;
import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import net.minecraft.util.Mth;
import java.awt.Color;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraft.world.item.ItemStack;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.IBlockDisplayReader;
import java.util.Random;

public class RenderingUtils
{
    private static final Random rand;
    private static IBlockDisplayReader plainRenderWorld;
    
    public static long getPositionSeed(final BlockPos pos) {
        long seed = 1553015L;
        seed ^= pos.getX();
        seed ^= pos.getY();
        seed ^= pos.getZ();
        return seed;
    }
    
    @Nullable
    public static TextureAtlasSprite getParticleTexture(final FluidStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        final ResourceLocation res = stack.getFluid().getAttributes().getStillTexture(stack);
        if (MissingTextureSprite.func_195675_b().equals((Object)res)) {
            return null;
        }
        return Minecraft.getInstance().func_209506_al().func_229356_a_(AtlasTexture.field_110575_b).func_195424_a(res);
    }
    
    @Nullable
    public static TextureAtlasSprite getParticleTexture(final ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        }
        final ItemModelMesher imm = Minecraft.getInstance().func_175599_af().func_175037_a();
        final BakedModel mdl = imm.func_178089_a(stack);
        if (mdl.equals(imm.func_178083_a().func_174951_a())) {
            return null;
        }
        return mdl.getParticleTexture((IModelData)EmptyModelData.INSTANCE);
    }
    
    @Nullable
    public static TextureAtlasSprite getParticleTexture(final BlockState state, @Nullable final BlockPos positionHint) {
        final Level world = (Level)Minecraft.getInstance().level;
        if (world == null) {
            return null;
        }
        final BlockPos pos = (positionHint != null) ? positionHint : BlockPos.field_177992_a;
        try {
            if (state.isAir((IBlockReader)world, pos)) {
                return null;
            }
        }
        catch (final Exception exc) {
            return null;
        }
        return Minecraft.getInstance().func_175602_ab().func_175023_a().getTexture(state, world, pos);
    }
    
    public static void playBlockBreakParticles(final BlockPos pos, @Nullable final BlockState actualState, final BlockState particleState) {
        final ClientLevel world = Minecraft.getInstance().level;
        final ParticleEngine mgr = Minecraft.getInstance().field_71452_i;
        VoxelShape voxelshape;
        try {
            voxelshape = ((actualState == null) ? VoxelShapes.func_197868_b() : actualState.func_196954_c((IBlockReader)world, pos));
        }
        catch (final Exception exc) {
            voxelshape = VoxelShapes.func_197868_b();
        }
        voxelshape.func_197755_b((minX, minY, minZ, maxX, maxY, maxZ) -> {
            final double xDist = Math.min(1.0, maxX - minX);
            final double yDist = Math.min(1.0, maxY - minY);
            final double zDist = Math.min(1.0, maxZ - minZ);
            final double i = Math.max(2, Mth.func_76143_f(xDist / 0.25));
            final double j = Math.max(2, Mth.func_76143_f(yDist / 0.25));
            final double k = Math.max(2, Mth.func_76143_f(zDist / 0.25));
            for (int xx = 0; xx < i; ++xx) {
                for (int yy = 0; yy < j; ++yy) {
                    for (int zz = 0; zz < k; ++zz) {
                        final double d4 = (xx + 0.5) / i;
                        final double d5 = (yy + 0.5) / j;
                        final double d6 = (zz + 0.5) / k;
                        final double d7 = d4 * xDist + minX;
                        final double d8 = d5 * yDist + minY;
                        final double d9 = d6 * zDist + minZ;
                        final DiggingParticle p = new DiggingParticle(world, pos.getX() + d7, pos.getY() + d8, pos.getZ() + d9, d4 - 0.5, d5 - 0.5, d6 - 0.5, particleState);
                        p.func_174845_l();
                        p.func_174846_a(pos);
                        mgr.func_78873_a((Particle)p);
                    }
                }
            }
        });
    }
    
    public static Color clampToColor(final int rgb) {
        return clampToColorWithMultiplier(rgb, 1.0f);
    }
    
    public static Color clampToColorWithMultiplier(final int rgb, final float mul) {
        final int r = rgb >> 16 & 0xFF;
        final int g = rgb >> 8 & 0xFF;
        final int b = rgb >> 0 & 0xFF;
        return new Color(Mth.getDescriptionId((int)(r * mul), 0, 255), Mth.getDescriptionId((int)(g * mul), 0, 255), Mth.getDescriptionId((int)(b * mul), 0, 255));
    }
    
    public static Color clampToColor(final int r, final int g, final int b) {
        return new Color(Mth.getDescriptionId((int)(float)r, 0, 255), Mth.getDescriptionId((int)(float)g, 0, 255), Mth.getDescriptionId((int)(float)b, 0, 255));
    }
    
    public static boolean canEffectExist(final EntityComplexFX fx) {
        Entity view = Minecraft.getInstance().func_175606_aa();
        if (view == null) {
            view = (Entity)Minecraft.getInstance().player;
        }
        return view != null && fx.getPosition().distanceSquared(view) <= RenderingConfig.CONFIG.getMaxEffectRenderDistanceSq();
    }
    
    public static void translate(final PoseStack renderStack, final float x, final float y, final float z, final Consumer<PoseStack> fn) {
        renderStack.popPose();
        renderStack.translate((double)x, (double)y, (double)z);
        fn.accept(renderStack);
        renderStack.popPose();
    }
    
    public static void draw(final int drawMode, final VertexFormat format, final Consumer<BufferBuilder> fn) {
        draw(drawMode, format, bufferBuilder -> {
            fn.accept(bufferBuilder);
            return null;
        });
    }
    
    public static <R> R draw(final int drawMode, final VertexFormat format, final Function<BufferBuilder, R> fn) {
        final BufferBuilder buf = Tessellator.func_178181_a().func_178180_c();
        buf.func_181668_a(drawMode, format);
        final R result = fn.apply(buf);
        finishDrawing(buf);
        return result;
    }
    
    public static void finishDrawing(final BufferBuilder buf) {
        finishDrawing(buf, null);
    }
    
    public static void finishDrawing(final BufferBuilder buf, @Nullable final RenderType type) {
        if (buf.func_227834_j_()) {
            if (type != null) {
                type.func_228631_a_(buf, 0, 0, 0);
            }
            else {
                buf.func_178977_d();
                WorldVertexBufferUploader.func_181679_a(buf);
            }
        }
    }
    
    public static void refreshDrawing(final VertexConsumer vb, final RenderType type) {
        if (vb instanceof BufferBuilder) {
            type.func_228631_a_((BufferBuilder)vb, 0, 0, 0);
            ((BufferBuilder)vb).func_181668_a(type.func_228664_q_(), type.func_228663_p_());
        }
    }
    
    public static int renderInWorldText(final FormattedCharSequence text, final Color color, final Vector3 at, final PoseStack renderStack, final float pTicks, final boolean facePlayer) {
        final float scale = (float)Minecraft.getInstance().func_228018_at_().func_198100_s();
        return renderInWorldText(text, color, 0.02f * (Minecraft.getInstance().options.field_74335_Z / scale), at, renderStack, pTicks, facePlayer);
    }
    
    public static int renderInWorldText(final FormattedCharSequence text, final Color color, final float scale, final Vector3 at, final PoseStack renderStack, final float pTicks, final boolean facePlayer) {
        final Font fr = Minecraft.getInstance().font;
        renderStack.popPose();
        renderStack.translate(at.getX(), at.getY(), at.getZ());
        renderStack.translate(scale, -scale, scale);
        if (facePlayer) {
            Entity le = Minecraft.getInstance().field_175622_Z;
            if (le == null) {
                le = (Entity)Minecraft.getInstance().player;
            }
            final float iYaw = RenderingVectorUtils.interpolate(Mth.func_76142_g(le.field_70126_B), Mth.func_76142_g(le.yRot), pTicks);
            renderStack.mulPose(new org.joml.Quaternionf().rotateY((float)Math.toRadians(-iYaw + 180.0f)));
        }
        final Matrix4f matr = renderStack.last().pose();
        final int length = fr.func_238414_a_(text);
        final MultiBufferSource.Impl buffers = MultiBufferSource.func_228455_a_(Tessellator.func_178181_a().func_178180_c());
        final FormattedCharSequence processedText = LanguageMap.func_74808_a().func_241870_a(text);
        final int drawnLength = fr.func_238416_a_(processedText, -(length / 2.0f), 0.0f, color.getRGB(), false, matr, (MultiBufferSource)buffers, true, 0, LightmapUtil.getPackedFullbrightCoords());
        buffers.func_228461_a_();
        renderStack.popPose();
        return drawnLength;
    }
    
    public static void renderItemAsEntity(final ItemStack stack, final PoseStack renderStack, final MultiBufferSource buffers, final double x, final double y, final double z, final int combinedLight, final float pTicks, final int age) {
        final ItemEntity ei = new ItemEntity((Level)Minecraft.getInstance().level, x, y, z, stack);
        ei.field_70292_b = age;
        ei.field_70290_d = 0.0f;
        ReflectionHelper.setSkipItemPhysicsRender(ei);
        Minecraft.getInstance().func_175598_ae().func_229084_a_((Entity)ei, x, y, z, 0.0f, pTicks, renderStack, buffers, combinedLight);
    }
    
    public static void renderItemStackGUI(final PoseStack renderStack, final ItemStack stack, @Nullable final String alternativeText) {
        renderStack.popPose();
        renderStack.translate(0.0, 0.0, 100.0);
        Font font = stack.getItem().getFontRenderer(stack);
        if (font == null) {
            font = Minecraft.getInstance().font;
        }
        renderTranslucentItemStackModelGUI(stack, renderStack, Color.WHITE, Blending.DEFAULT, 255);
        mcdefault_renderItemOverlayIntoGUI(font, renderStack, stack, Minecraft.getInstance().func_184121_ak(), alternativeText);
        renderStack.popPose();
    }
    
    public static void renderTranslucentItemStack(final ItemStack stack, final PoseStack renderStack, final float pTicks) {
        renderTranslucentItemStack(stack, renderStack, pTicks, Color.WHITE, 25);
    }
    
    public static void renderTranslucentItemStack(final ItemStack stack, final PoseStack renderStack, final float pTicks, final Color overlayColor, final int alpha) {
        renderStack.popPose();
        final float sinBobY = Mth.func_76126_a((ClientScheduler.getClientTick() + pTicks) / 10.0f) * 0.1f + 0.1f;
        renderStack.translate(0.0, (double)sinBobY, 0.0);
        final float ageRotate = (ClientScheduler.getClientTick() + pTicks) / 20.0f;
        renderStack.mulPose(new org.joml.Vector3f(0, 1, 0).func_229193_c_(ageRotate));
        renderTranslucentItemStackModelGround(stack, renderStack, overlayColor, Blending.PREALPHA, alpha);
        renderStack.popPose();
    }
    
    public static void renderTranslucentItemStackModelGround(final ItemStack stack, final PoseStack renderStack, final Color overlayColor, final Blending blendMode, final int alpha) {
        final BakedModel bakedModel = getItemModel(stack);
        ForgeHooksClient.handleCameraTransforms(renderStack, bakedModel, ItemTransforms.TransformType.GROUND, false);
        final TextureManager textureManager = Minecraft.getInstance().func_110434_K();
        textureManager.func_110577_a(AtlasTexture.field_110575_b);
        textureManager.func_229267_b_(AtlasTexture.field_110575_b).setBlurMipmap(false, false);
        final MultiBufferSource.Impl buffer = Minecraft.getInstance().func_228019_au_().func_228487_b_();
        renderItemModelWithColor(stack, ItemTransforms.TransformType.GROUND, bakedModel, renderStack, renderType -> {
            final RenderTypeDecorator decorated = RenderTypeDecorator.wrapSetup(renderType, () -> {
                RenderSystem.enableBlend();
                blendMode.apply();
                return;
            }, () -> {
                Blending.DEFAULT.apply();
                RenderSystem.disableBlend();
                return;
            });
            return buffer.getBuffer((RenderType)decorated);
        }, LightmapUtil.getPackedFullbrightCoords(), OverlayTexture.field_229196_a_, overlayColor, alpha);
        buffer.func_228461_a_();
    }
    
    public static void renderTranslucentItemStackModelGUI(final ItemStack stack, final PoseStack renderStack, final Color overlayColor, final Blending blendMode, final int alpha) {
        final TextureManager textureManager = Minecraft.getInstance().func_110434_K();
        textureManager.func_110577_a(AtlasTexture.field_110575_b);
        textureManager.func_229267_b_(AtlasTexture.field_110575_b).setBlurMipmap(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        blendMode.apply();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        renderStack.popPose();
        renderStack.translate(8.0, 8.0, 0.0);
        renderStack.translate(16.0f, -16.0f, 16.0f);
        final BakedModel bakedModel = ForgeHooksClient.handleCameraTransforms(renderStack, getItemModel(stack), ItemTransforms.TransformType.GUI, false);
        final boolean isSideLit = bakedModel.func_230044_c_();
        if (!isSideLit) {
            RenderHelper.func_227783_c_();
        }
        final MultiBufferSource.Impl buffer = Minecraft.getInstance().func_228019_au_().func_228487_b_();
        renderItemModelWithColor(stack, ItemTransforms.TransformType.GUI, bakedModel, renderStack, (MultiBufferSource)buffer, LightmapUtil.getPackedFullbrightCoords(), OverlayTexture.field_229196_a_, overlayColor, Mth.getDescriptionId(alpha, 0, 255));
        buffer.func_228461_a_();
        if (!isSideLit) {
            RenderHelper.func_227784_d_();
        }
        Blending.DEFAULT.apply();
        RenderSystem.disableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.enableDepthTest();
        renderStack.popPose();
    }
    
    @Deprecated
    public static void mcdefault_renderItemOverlayIntoGUI(final Font fr, final PoseStack renderStack, final ItemStack stack, final float pTicks, @Nullable final String text) {
        if (stack.isEmpty()) {
            return;
        }
        RenderSystem.disableLighting();
        renderStack.popPose();
        renderStack.translate(0.0, 0.0, 100.0);
        if (stack.getCount() > 1 || text != null) {
            final FormattedCharSequence display = (FormattedCharSequence)new Component((String)ObjectUtils.firstNonNull((Object[])new String[] { text, String.valueOf(stack.getCount()) }));
            final int length = fr.func_238414_a_(display);
            renderStack.popPose();
            renderStack.translate((double)(17 - length), 9.0, 0.0);
            RenderingDrawUtils.renderStringAt(display, renderStack, fr, -1, true);
            renderStack.popPose();
        }
        if (stack.getItem().showDurabilityBar(stack)) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();
            final float health = (float)stack.getItem().getDurabilityForDisplay(stack);
            final float durabilityPercent = 13.0f - health * 13.0f;
            final int color = stack.getItem().getRGBDurabilityForDisplay(stack);
            draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> {
                RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, 2.0f, 13.0f, 0.0f, 13.0f, 2.0f).color(0, 0, 0, 255).draw();
                RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, 2.0f, 13.0f, 0.0f, durabilityPercent, 1.0f).color(color >> 16 & 0xFF, color >> 8 & 0xFF, color & 0xFF, 255).draw();
                return;
            });
            RenderSystem.enableBlend();
            RenderSystem.enableAlphaTest();
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
        }
        final ClientPlayerEntity player = Minecraft.getInstance().player;
        final float cooldownPercent = (player == null) ? 0.0f : player.isSleeping().func_185143_a(stack.getItem(), pTicks);
        if (cooldownPercent > 0.0f) {
            RenderSystem.disableDepthTest();
            RenderSystem.disableTexture();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            draw(7, DefaultVertexFormat.POSITION_TEX_COLOR, buf -> RenderingGuiUtils.rect((VertexConsumer)buf, renderStack, 0.0f, 16.0f * (1.0f - cooldownPercent), 0.0f, 16.0f, 16.0f * cooldownPercent).color(255, 255, 255, 127).draw());
            RenderSystem.enableTexture();
            RenderSystem.enableDepthTest();
        }
        renderStack.popPose();
    }
    
    private static BakedModel getItemModel(final ItemStack stack) {
        return Minecraft.getInstance().func_175599_af().func_184393_a(stack, (Level)Minecraft.getInstance().level, (LivingEntity)Minecraft.getInstance().player);
    }
    
    private static void renderItemModelWithColor(final ItemStack stack, final ItemTransforms.TransformType transformType, final BakedModel model, final PoseStack renderStack, final MultiBufferSource buffer, final int combinedLight, final int combinedOverlay, final Color c, final int alpha) {
        if (!stack.isEmpty()) {
            renderStack.popPose();
            renderStack.translate(-0.5, -0.5, -0.5);
            final boolean renderThirdPersonView = transformType == ItemTransforms.TransformType.GUI || transformType == ItemTransforms.TransformType.GROUND || transformType == ItemTransforms.TransformType.FIXED;
            if (model.func_188618_c() || (stack.getItem() == Items.field_203184_eO && !renderThirdPersonView)) {
                final int[] colors = { c.getRed(), c.getGreen(), c.getBlue(), alpha };
                final MultiBufferSource decoratedBuffer = type -> BufferDecoratorBuilder.withColor((r, g, b, a) -> colors).decorate(buffer.getBuffer(type));
                stack.getItem().getItemStackTileEntityRenderer().func_239207_a_(stack, transformType, renderStack, decoratedBuffer, combinedLight, combinedOverlay);
            }
            else if (model.isLayered()) {
                for (final Pair<BakedModel, RenderType> layerModel : model.getLayerModels(stack, true)) {
                    final BakedModel layer = (BakedModel)layerModel.getFirst();
                    final RenderType rType = (RenderType)layerModel.getSecond();
                    ForgeHooksClient.setRenderLayer(rType);
                    try {
                        final VertexConsumer vertexBuilder = ItemRenderer.func_239391_c_(buffer, rType, true, stack.func_77962_s());
                        renderColoredItemModel(stack, layer, renderStack, vertexBuilder, combinedLight, combinedOverlay, c, alpha);
                    }
                    finally {
                        ForgeHooksClient.setRenderLayer((RenderType)null);
                    }
                }
            }
            else {
                final RenderType rType2 = RenderTypeLookup.func_239219_a_(stack, true);
                VertexConsumer vertexBuilder2;
                if (stack.getItem() instanceof CompassItem && stack.func_77962_s()) {
                    renderStack.popPose();
                    final PoseStack.Entry topEntry = renderStack.last();
                    if (transformType == ItemTransforms.TransformType.GUI) {
                        topEntry.translate().func_226592_a_(0.5f);
                    }
                    else if (transformType.func_241716_a_()) {
                        topEntry.translate().func_226592_a_(0.75f);
                    }
                    vertexBuilder2 = ItemRenderer.func_241732_b_(buffer, rType2, topEntry);
                    renderStack.popPose();
                }
                else {
                    vertexBuilder2 = ItemRenderer.func_239391_c_(buffer, rType2, true, stack.func_77962_s());
                }
                renderColoredItemModel(stack, model, renderStack, vertexBuilder2, combinedLight, combinedOverlay, c, alpha);
            }
            renderStack.popPose();
        }
    }
    
    private static void renderColoredItemModel(final ItemStack stack, final BakedModel model, final PoseStack renderStack, final VertexConsumer buffer, final int combinedLight, final int combinedOverlay, final Color color, final int alpha) {
        final Color alphaColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
        final Random renderRand = new Random();
        final IModelData data = (IModelData)EmptyModelData.INSTANCE;
        for (final Direction dir : Direction.values()) {
            renderRand.setSeed(42L);
            renderColoredQuads(buffer, renderStack, model.getQuads((BlockState)null, dir, renderRand, data), alphaColor, combinedLight, combinedOverlay, stack);
        }
        renderRand.setSeed(42L);
        renderColoredQuads(buffer, renderStack, model.getQuads((BlockState)null, (Direction)null, renderRand, data), alphaColor, combinedLight, combinedOverlay, stack);
    }
    
    private static void renderColoredQuads(final VertexConsumer vb, final PoseStack renderStack, final List<BakedQuad> quads, final Color color, final int combinedLight, final int combinedOverlay, final ItemStack stack) {
        final boolean useOverlayColors = (color.getRGB() & 0xFFFFFF) == 0xFFFFFF && !stack.isEmpty();
        int i = 0;
        final ItemColors itemColors = Minecraft.getInstance().getItemColors();
        for (int j = quads.size(); i < j; ++i) {
            final BakedQuad bakedquad = quads.get(i);
            int col = color.getRGB();
            if (useOverlayColors && bakedquad.func_178212_b()) {
                col = itemColors.func_186728_a(stack, bakedquad.func_178211_c());
            }
            final float r = (col >> 16 & 0xFF) / 255.0f;
            final float g = (col >> 8 & 0xFF) / 255.0f;
            final float b = (col & 0xFF) / 255.0f;
            final float a = color.getAlpha() / 255.0f;
            vb.addVertexData(renderStack.last(), bakedquad, r, g, b, a, combinedLight, combinedOverlay, true);
        }
    }
    
    public static void renderSimpleBlockModel(final BlockState state, final PoseStack renderStack, final VertexConsumer vb) {
        renderSimpleBlockModel(state, renderStack, vb, BlockPos.field_177992_a, null, false);
    }
    
    public static void renderSimpleBlockModel(final BlockState state, final PoseStack renderStack, final VertexConsumer vb, final BlockPos pos, @Nullable final BlockEntity te, final boolean checkRenderSide) {
        if (RenderingUtils.plainRenderWorld == null) {
            RenderingUtils.plainRenderWorld = (IBlockDisplayReader)new EmptyRenderWorld(() -> RegistryUtil.client().getValue(Registries.BIOME, Biomes.field_76772_c));
        }
        final RenderShape brt = state.func_185901_i();
        if (brt == RenderShape.INVISIBLE) {
            return;
        }
        final BlockRenderDispatcher brd = Minecraft.getInstance().func_175602_ab();
        IModelData data = (IModelData)EmptyModelData.INSTANCE;
        if (te != null) {
            data = te.getModelData();
        }
        brd.renderModel(state, pos, RenderingUtils.plainRenderWorld, renderStack, vb, checkRenderSide, RenderingUtils.rand, data);
    }
    
    public static void renderSimpleBlockModelCurrentWorld(final BlockState state, final PoseStack renderStack, final VertexConsumer buf, final int combinedOverlayIn) {
        renderSimpleBlockModelCurrentWorld(state, renderStack, buf, BlockPos.field_177992_a, null, combinedOverlayIn, false);
    }
    
    public static void renderSimpleBlockModelCurrentWorld(final BlockState state, final PoseStack renderStack, final VertexConsumer buf, final BlockPos pos, @Nullable final BlockEntity te, final int combinedOverlayIn, final boolean checkRenderSide) {
        final RenderShape brt = state.func_185901_i();
        if (brt == RenderShape.INVISIBLE) {
            return;
        }
        final BlockRenderDispatcher brd = Minecraft.getInstance().func_175602_ab();
        IModelData data = (IModelData)EmptyModelData.INSTANCE;
        if (te != null) {
            data = te.getModelData();
        }
        if (brt == RenderShape.MODEL) {
            final BakedModel model = brd.func_184389_a(state);
            brd.func_175019_b().renderModel((IBlockDisplayReader)Minecraft.getInstance().level, model, state, pos, renderStack, buf, checkRenderSide, RenderingUtils.rand, state.func_209533_a(pos), combinedOverlayIn, data);
        }
    }
    
    static {
        rand = new Random();
        RenderingUtils.plainRenderWorld = null;
    }
}
