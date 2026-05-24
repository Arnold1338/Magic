package hellfirepvp.astralsorcery.common.item.lens;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileLens;
import net.minecraft.world.InteractionResult;
import net.minecraft.item.ItemUseContext;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.item.base.client.ItemDynamicColor;
import net.minecraft.world.item.Item;

public abstract class ItemColoredLens extends Item implements ItemDynamicColor
{
    private final LensColorType lensColorType;
    
    protected ItemColoredLens(final LensColorType colorType) {
        this(colorType, new Item.Properties().func_200916_a(CommonProxy.ITEM_GROUP_AS));
    }
    
    protected ItemColoredLens(final LensColorType colorType, final Item.Properties properties) {
        super(properties);
        this.lensColorType = colorType;
    }
    
    public InteractionResult func_195939_a(final ItemUseContext ctx) {
        final Player player = ctx.func_195999_j();
        final World world = ctx.func_195991_k();
        if (!world.func_201670_d() && player != null) {
            final TileLens lens = MiscUtils.getTileAt((IBlockReader)world, ctx.func_195995_a(), TileLens.class, false);
            if (lens != null) {
                final ItemStack held = ctx.func_195996_i();
                final LensColorType oldType = lens.setColorType(this.lensColorType);
                if (!player.func_184812_l_()) {
                    held.func_190920_e(held.func_190916_E() - 1);
                    if (held.func_190916_E() <= 0) {
                        player.func_184611_a(ctx.func_221531_n(), ItemStack.field_190927_a);
                    }
                }
                SoundHelper.playSoundAround(SoundsAS.BLOCK_COLOREDLENS_ATTACH, world, (Vector3i)ctx.func_195995_a(), 0.8f, 1.5f);
                if (oldType != null) {
                    player.getInventory().func_191975_a(world, oldType.getStack());
                }
            }
        }
        return InteractionResult.PASS;
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getColor(final ItemStack stack, final int tintIndex) {
        return this.lensColorType.getColor().getRGB();
    }
}
