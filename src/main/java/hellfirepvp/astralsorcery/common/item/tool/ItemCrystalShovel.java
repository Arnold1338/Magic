package hellfirepvp.astralsorcery.common.item.tool;

import net.minecraft.world.level.block.Blocks;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.state.Property;
import net.minecraft.block.CampfireBlock;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.item.ItemUseContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import hellfirepvp.astralsorcery.common.lib.CrystalPropertiesAS;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import java.util.Set;
import com.google.common.collect.Sets;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ToolAction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import java.util.Map;
import hellfirepvp.astralsorcery.common.item.base.TypeEnchantableItem;

public class ItemCrystalShovel extends ItemCrystalTierItem implements TypeEnchantableItem
{
    private static final Map<Block, BlockState> BLOCK_PAVE_MAP;
    
    public ItemCrystalShovel() {
        super(ToolType.SHOVEL, new Item.Properties(), Sets.newHashSet((Object[])new Material[] { Material.field_151597_y, Material.field_151596_z }));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> stacks) {
        if (this.func_194125_a(group)) {
            final ItemStack stack = new ItemStack((ItemLike)this);
            CrystalPropertiesAS.CREATIVE_CRYSTAL_TOOL_ATTRIBUTES.store(stack);
            stacks.add((Object)stack);
        }
    }
    
    @Override
    public boolean canEnchantItem(final ItemStack stack, final EnchantmentType type) {
        return type == EnchantmentType.BREAKABLE || type == EnchantmentType.DIGGER;
    }
    
    public boolean canApplyAtEnchantingTable(final ItemStack stack, final Enchantment enchantment) {
        final EnchantmentType type = enchantment.field_77351_y;
        return type == EnchantmentType.DIGGER || type == EnchantmentType.BREAKABLE;
    }
    
    @Override
    double getAttackDamage() {
        return 3.0;
    }
    
    @Override
    double getAttackSpeed() {
        return -1.5;
    }
    
    public InteractionResult func_195939_a(final ItemUseContext context) {
        final World world = context.func_195991_k();
        final BlockPos pos = context.func_195995_a();
        final BlockState state = world.getBlockState(pos);
        if (context.func_196000_l() == Direction.DOWN) {
            return InteractionResult.PASS;
        }
        final Player playerentity = context.func_195999_j();
        final BlockState modifiedState = state.getToolModifiedState(world, pos, playerentity, context.func_195996_i(), ToolType.SHOVEL);
        BlockState targetState = null;
        if (modifiedState != null && world.isEmptyBlock(pos.above())) {
            world.func_184133_a(playerentity, pos, SoundEvents.field_187771_eN, SoundSource.BLOCKS, 1.0f, 1.0f);
            targetState = modifiedState;
        }
        else if (state.getBlock() instanceof CampfireBlock && (boolean)state.getValue((Property)CampfireBlock.field_220101_b)) {
            if (!world.func_201670_d()) {
                world.func_217378_a((Player)null, 1009, pos, 0);
            }
            CampfireBlock.func_235475_c_((IWorld)world, pos, state);
            targetState = (BlockState)state.func_206870_a((Property)CampfireBlock.field_220101_b, (Comparable)false);
        }
        if (targetState != null) {
            if (!world.func_201670_d()) {
                world.func_180501_a(pos, targetState, 11);
                if (playerentity != null) {
                    context.func_195996_i().func_222118_a(1, (LivingEntity)playerentity, player -> player.func_213334_d(context.func_221531_n()));
                }
            }
            return InteractionResult.func_233537_a_(world.func_201670_d());
        }
        return InteractionResult.PASS;
    }
    
    static {
        BLOCK_PAVE_MAP = (Map)new ImmutableMap.Builder().put((Object)Blocks.field_196658_i, (Object)Blocks.field_185774_da.defaultBlockState()).build();
    }
}
