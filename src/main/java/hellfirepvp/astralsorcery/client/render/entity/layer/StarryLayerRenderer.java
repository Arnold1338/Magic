package hellfirepvp.astralsorcery.client.render.entity.layer;

import net.minecraft.client.model.PlayerModel;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import hellfirepvp.astralsorcery.client.registry.RegistryRenderTypes;
import net.minecraft.world.entity.Entity;
import java.util.Iterator;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import java.util.function.BiPredicate;
import net.minecraft.client.renderer.RenderType;
import hellfirepvp.astralsorcery.common.util.object.CacheReference;
import java.util.List;
import net.minecraft.client.renderer.entity.layers.BipedArmorLayer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.LivingEntity;

public class StarryLayerRenderer<E extends LivingEntity, M extends HumanoidModel<E>> extends BipedArmorLayer<E, M, HumanoidModel<E>>
{
    private static final List<CacheReference<RenderType>> RENDER_TYPES;
    private static final HumanoidModel MODEL_HEAD;
    private static final HumanoidModel MODEL_ARMOR;
    private static final HumanoidModel MODEL_ARMOR_SMALL;
    private static BiPredicate<Player, EquipmentSlot> renderTest;
    private final boolean slimRender;
    
    public StarryLayerRenderer(final IEntityRenderer<E, M> entityRendererIn, final boolean slimRender) {
        super((IEntityRenderer)entityRendererIn, StarryLayerRenderer.MODEL_ARMOR, StarryLayerRenderer.MODEL_ARMOR);
        this.slimRender = slimRender;
    }
    
    public static void addRender(final BiPredicate<Player, EquipmentSlot> render) {
        StarryLayerRenderer.renderTest = StarryLayerRenderer.renderTest.or(render);
    }
    
    public void func_225628_a_(final PoseStack renderStack, final MultiBufferSource buffer, final int light, final E entity, final float limbSwing, final float limbSwingAmount, final float partialTicks, final float ageInTicks, final float netHeadYaw, final float headPitch) {
        if (!(entity instanceof Player)) {
            return;
        }
        for (final EquipmentSlot slotType : EquipmentSlot.values()) {
            if (slotType.func_188453_a() == EquipmentSlot.Group.ARMOR && StarryLayerRenderer.renderTest.test((Player)entity, slotType)) {
                final HumanoidModel<E> model = (HumanoidModel<E>)((slotType == EquipmentSlot.HEAD) ? StarryLayerRenderer.MODEL_HEAD : (this.slimRender ? StarryLayerRenderer.MODEL_ARMOR_SMALL : StarryLayerRenderer.MODEL_ARMOR));
                this.renderArmorPart(renderStack, buffer, slotType, light, model);
            }
        }
    }
    
    private void renderArmorPart(final PoseStack renderStack, final MultiBufferSource buffer, final EquipmentSlot slotType, final int light, final HumanoidModel<E> model) {
        ((HumanoidModel)this.func_215332_c()).func_217148_a((HumanoidModel)model);
        this.func_188359_a((HumanoidModel)model, slotType);
        for (final CacheReference<RenderType> renderType : StarryLayerRenderer.RENDER_TYPES) {
            model.func_225598_a_(renderStack, buffer.getBuffer((RenderType)renderType.get()), light, OverlayTexture.field_229196_a_, 0.4f, 0.4f, 1.0f, 0.1f);
        }
    }
    
    static {
        RENDER_TYPES = IntStream.range(0, 2).mapToObj(i -> new CacheReference(() -> RegistryRenderTypes.createDepthProjectionType(i))).collect((Collector<? super Object, ?, List<CacheReference<RenderType>>>)Collectors.toList());
        MODEL_HEAD = (HumanoidModel)new PlayerModel(-0.5f, false);
        MODEL_ARMOR = (HumanoidModel)new PlayerModel(0.0f, false);
        MODEL_ARMOR_SMALL = (HumanoidModel)new PlayerModel(0.0f, true);
        StarryLayerRenderer.renderTest = ((p, type) -> false);
    }
}
