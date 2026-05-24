package hellfirepvp.astralsorcery.common.item.lens;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.Blocks;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.data.config.entry.GeneralConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.util.item.ItemUtils;
import java.util.function.Function;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.common.util.RecipeHelper;
import net.minecraft.world.entity.item.ItemEntity;
import hellfirepvp.astralsorcery.common.util.PartialEffectExecutor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import hellfirepvp.astralsorcery.common.lib.ItemsAS;
import hellfirepvp.astralsorcery.AstralSorcery;
import java.util.Random;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.ByteBufUtils;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;

public class ItemColoredLensFire extends ItemColoredLens
{
    private static final ColorTypeFire COLOR_TYPE_FIRE;
    
    public ItemColoredLensFire() {
        super(ItemColoredLensFire.COLOR_TYPE_FIRE);
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playParticles(final PktPlayEffect event) {
        final Vector3 at = ByteBufUtils.readVector(event.getExtraData());
        for (int i = 0; i < 5; ++i) {
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(at.clone().add(ItemColoredLensFire.field_77697_d.nextFloat(), 0.2, ItemColoredLensFire.field_77697_d.nextFloat())).setMotion(new Vector3(0.0, 0.016 + ItemColoredLensFire.field_77697_d.nextFloat() * 0.02, 0.0)).setScaleMultiplier(0.2f).color(VFXColorFunction.constant(ColorsAS.COLORED_LENS_FIRE));
        }
    }
    
    static {
        COLOR_TYPE_FIRE = new ColorTypeFire();
    }
    
    private static class ColorTypeFire extends LensColorType
    {
        private ColorTypeFire() {
            super(AstralSorcery.key("fire"), TargetType.ANY, () -> new ItemStack((ItemLike)ItemsAS.COLORED_LENS_FIRE), ColorsAS.COLORED_LENS_FIRE, 0.1f, false);
        }
        
        @Override
        public void entityInBeam(final World world, final Vector3 origin, final Vector3 target, final Entity entity, final PartialEffectExecutor executor) {
            if (world.func_201670_d()) {
                return;
            }
            if (entity instanceof ItemEntity) {
                final ItemStack current = ((ItemEntity)entity).func_92059_d();
                final ItemStack result = RecipeHelper.findSmeltingResult(entity.func_130014_f_(), current).map((Function<? super Tuple<ItemStack, Float>, ? extends ItemStack>)Tuple::func_76341_a).orElse(ItemStack.EMPTY);
                if (result.isEmpty()) {
                    return;
                }
                while (executor.canExecute()) {
                    executor.markExecution();
                    if (ItemColoredLensFire.field_77697_d.nextInt(10) != 0) {
                        continue;
                    }
                    final Vector3 entityPos = Vector3.atEntityCorner(entity);
                    ItemUtils.dropItemNaturally(entity.func_130014_f_(), entityPos.getX(), entityPos.getY(), entityPos.getZ(), ItemUtils.copyStackWithSize(result, result.func_190916_E()));
                    if (current.func_190916_E() > 1) {
                        current.shrink(1);
                        ((ItemEntity)entity).func_92058_a(current);
                    }
                    else {
                        entity.func_70106_y();
                    }
                }
            }
            else if (entity instanceof LivingEntity) {
                if (entity instanceof Player && (!(boolean)GeneralConfig.CONFIG.doColoredLensesAffectPlayers.get() || entity.func_184102_h() == null || !entity.func_184102_h().func_71219_W())) {
                    return;
                }
                entity.hurt(DamageSource.field_76370_b, 0.5f);
                entity.func_70015_d(5);
            }
        }
        
        @Override
        public void blockInBeam(final World world, final BlockPos pos, final BlockState state, final PartialEffectExecutor executor) {
            if (!(world instanceof ServerLevel)) {
                return;
            }
            final ItemStack blockStack = ItemUtils.createBlockStack(state);
            if (blockStack.isEmpty()) {
                return;
            }
            final ItemStack result = RecipeHelper.findSmeltingResult(world, blockStack).map((Function<? super Tuple<ItemStack, Float>, ? extends ItemStack>)Tuple::func_76341_a).orElse(ItemStack.EMPTY);
            if (result.isEmpty()) {
                return;
            }
            final PktPlayEffect ev = new PktPlayEffect(PktPlayEffect.Type.MELT_BLOCK).addData(buf -> ByteBufUtils.writeVector(buf, new Vector3((Vector3i)pos)));
            PacketChannel.CHANNEL.sendToAllAround(ev, PacketChannel.pointFromPos(world, (Vector3i)pos, 16.0));
            while (executor.canExecute()) {
                executor.markExecution();
                if (ItemColoredLensFire.field_77697_d.nextInt(6) != 0) {
                    continue;
                }
                final BlockState resState = ItemUtils.createBlockState(result);
                if (resState != null) {
                    world.func_180501_a(pos, resState, 3);
                }
                else if (world.func_180501_a(pos, Blocks.field_150350_a.defaultBlockState(), 3)) {
                    ItemUtils.dropItemNaturally(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, result);
                }
            }
        }
    }
}
