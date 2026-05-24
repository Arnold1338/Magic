package hellfirepvp.astralsorcery.common.item.tool;

import net.minecraft.world.level.entity.player.Player;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import net.minecraft.world.level.entity.LivingEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.item.ItemUseContext;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.world.level.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import net.minecraft.world.level.level.ItemLike;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.item.CreativeModeTab;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.world.level.level.material.Material;
import net.minecraft.world.level.item.Item;
import net.minecraftforge.common.ToolAction;
import hellfirepvp.astralsorcery.common.item.base.TypeEnchantableItem;

public class ItemCrystalAxe extends ItemCrystalTierItem implements TypeEnchantableItem
{
    public ItemCrystalAxe() {
        super(ToolType.AXE, new Item.Properties(), Sets.newHashSet((Object[])new Material[] { Material.field_151575_d, Material.field_151585_k, Material.field_151582_l, Material.field_215713_z, Material.field_151584_j }));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> stacks) {
        if (this.func_194125_a(group)) {
            final ItemStack stack = new ItemStack((ItemLike)this);
            CrystalPropertiesAS.CREATIVE_CRYSTAL_TOOL_ATTRIBUTES.store(stack);
            stacks.add((Object)stack);
        }
    }
    
    @Override
    public boolean canEnchantItem(final ItemStack stack, final EnchantmentCategory type) {
        return type == EnchantmentCategory.BREAKABLE || type == EnchantmentCategory.DIGGER;
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        final EnchantmentCategory type = enchantment.field_77351_y;
        return type == EnchantmentCategory.DIGGER || type == EnchantmentCategory.BREAKABLE;
    }
    
    @Override
    double getAttackDamage() {
        return 11.0;
    }
    
    @Override
    double getAttackSpeed() {
        return -3.0;
    }
    
    @Override
    protected boolean isToolEfficientAgainst(final BlockState state) {
        return state.func_185904_a() == Material.field_151584_j;
    }
    
    public InteractionResult func_195939_a(final ItemUseContext context) {
        final World world = context.func_195991_k();
        final BlockPos blockpos = context.func_195995_a();
        final BlockState blockstate = world.getBlockState(blockpos);
        final BlockState block = blockstate.getToolModifiedState(world, blockpos, context.func_195999_j(), context.func_195996_i(), ToolType.AXE);
        if (block != null) {
            final Player playerentity = context.func_195999_j();
            world.func_184133_a(playerentity, blockpos, SoundEvents.field_203255_y, SoundSource.BLOCKS, 1.0f, 1.0f);
            if (!world.func_201670_d()) {
                world.func_180501_a(blockpos, block, 11);
                if (playerentity != null) {
                    context.func_195996_i().func_222118_a(1, (LivingEntity)playerentity, stack -> stack.func_213334_d(context.func_221531_n()));
                }
            }
            return InteractionResult.func_233537_a_(world.func_201670_d());
        }
        return InteractionResult.PASS;
    }
}
