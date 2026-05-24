package hellfirepvp.astralsorcery.common.item.useables;

import net.minecraft.item.UseAction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import net.minecraft.core.Vec3i;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import net.minecraft.util.SoundEvents;
import net.minecraft.Util;
import net.minecraft.util.math.MathHelper;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.PerkExperienceRevealer;
import net.minecraft.world.item.Item;

public class ItemShiftingStar extends Item implements PerkExperienceRevealer
{
    public ItemShiftingStar() {
        super(new Item.Properties().func_200917_a(1).func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final World worldIn, final List<Component> tooltip, final ITooltipFlag flagIn) {
        final IConstellation cst = this.getBaseConstellation();
        if (cst != null) {
            if (ResearchHelper.getClientProgress().hasConstellationDiscovered(cst)) {
                tooltip.add((Component)cst.getConstellationName().func_240699_a_(ChatFormatting.BLUE));
            }
            else {
                tooltip.add((Component)new Component("astralsorcery.misc.noinformation").func_240699_a_(ChatFormatting.GRAY));
            }
        }
    }
    
    public InteractionResult<ItemStack> func_77659_a(final World worldIn, final Player playerIn, final Hand handIn) {
        playerIn.func_184598_c(handIn);
        return (InteractionResult<ItemStack>)super.func_77659_a(worldIn, playerIn, handIn);
    }
    
    public ItemStack func_77654_b(final ItemStack stack, final World worldIn, final LivingEntity entityLiving) {
        if (!worldIn.func_201670_d() && entityLiving instanceof ServerPlayer) {
            final ServerPlayer player = (ServerPlayer)entityLiving;
            final IMajorConstellation cst = this.getBaseConstellation();
            if (cst != null) {
                final PlayerProgress prog = ResearchHelper.getProgress((Player)player, LogicalSide.SERVER);
                if (!prog.isValid() || !prog.wasOnceAttuned() || !prog.hasConstellationDiscovered(cst)) {
                    return stack;
                }
                final double perkExp = prog.getPerkData().getPerkExp();
                if (ResearchManager.setAttunedConstellation((Player)player, cst)) {
                    ResearchManager.setExp((Player)player, MathHelper.func_76124_d(perkExp));
                    player.func_145747_a((Component)new Component("astralsorcery.progress.switch.attunement").func_240699_a_(ChatFormatting.BLUE), Util.NIL_UUID);
                    SoundHelper.playSoundAround(SoundEvents.field_187561_bM, worldIn, (Vector3i)entityLiving.func_233580_cy_(), 1.0f, 1.0f);
                    return ItemStack.field_190927_a;
                }
            }
            else if (ResearchManager.setAttunedConstellation((Player)player, null)) {
                player.func_145747_a((Component)new Component("astralsorcery.progress.remove.attunement").func_240699_a_(ChatFormatting.BLUE), Util.NIL_UUID);
                SoundHelper.playSoundAround(SoundEvents.field_187561_bM, worldIn, (Vector3i)entityLiving.func_233580_cy_(), 1.0f, 1.0f);
                return ItemStack.field_190927_a;
            }
        }
        return stack;
    }
    
    public void onUsingTick(final ItemStack stack, final LivingEntity player, final int count) {
        if (player.func_130014_f_().func_201670_d()) {
            this.playUseEffects(player, this.func_77626_a(stack) - count, this.func_77626_a(stack));
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playUseEffects(final LivingEntity player, final int tick, final int total) {
        final IMajorConstellation cst = this.getBaseConstellation();
        if (cst == null) {
            final FXFacingParticle p = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(Vector3.atEntityCorner((Entity)player).addY(player.func_213302_cg() / 2.0f)).setMotion(new Vector3(-0.1 + ItemShiftingStar.field_77697_d.nextFloat() * 0.2, 0.01, -0.1 + ItemShiftingStar.field_77697_d.nextFloat() * 0.2)).setScaleMultiplier(0.2f + ItemShiftingStar.field_77697_d.nextFloat());
            if (ItemShiftingStar.field_77697_d.nextBoolean()) {
                p.color(VFXColorFunction.WHITE);
            }
        }
        else {
            final float percCycle = (float)(tick % total / (float)total * 2.0f * 3.141592653589793);
            for (int parts = 5, i = 0; i < parts; ++i) {
                final float angleSwirl = 75.0f;
                final Vector3 center = Vector3.atEntityCorner((Entity)player).addY(player.func_213302_cg() / 2.0f);
                final Vector3 v = Vector3.RotAxis.X_AXIS.clone();
                final float originalAngle = i / (float)parts * 360.0f;
                final double angle = originalAngle + MathHelper.func_76126_a(percCycle) * angleSwirl;
                v.rotate(-Math.toRadians(angle), Vector3.RotAxis.Y_AXIS).normalize().multiply(4);
                final Vector3 pos = center.clone().add(v);
                final Vector3 mot = center.clone().subtract(pos).normalize().multiply(0.1);
                final FXFacingParticle p2 = EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).setScaleMultiplier(0.25f + ItemShiftingStar.field_77697_d.nextFloat() * 0.4f).setMotion(mot).setMaxAge(50);
                if (ItemShiftingStar.field_77697_d.nextInt(4) == 0) {
                    p2.color(VFXColorFunction.WHITE);
                }
                else if (ItemShiftingStar.field_77697_d.nextInt(3) == 0) {
                    p2.color(VFXColorFunction.constant(cst.getConstellationColor().brighter()));
                }
                else {
                    p2.color(VFXColorFunction.constant(cst.getConstellationColor()));
                }
            }
        }
    }
    
    public int func_77626_a(final ItemStack stack) {
        return (this.getBaseConstellation() == null) ? 60 : 100;
    }
    
    public UseAction func_77661_b(final ItemStack stack) {
        return UseAction.BOW;
    }
    
    @Nullable
    public IMajorConstellation getBaseConstellation() {
        return null;
    }
}
