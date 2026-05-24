package hellfirepvp.astralsorcery.common.crafting.nojson.meltable;

import net.minecraft.world.level.item.ItemStack;
import java.util.function.Consumer;
import net.minecraft.world.level.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.level.Level;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.block.BlockPredicate;
import hellfirepvp.astralsorcery.common.crafting.nojson.CustomRecipe;

public abstract class WorldMeltableRecipe extends CustomRecipe
{
    private final BlockPredicate matcher;
    
    public WorldMeltableRecipe(final ResourceLocation key, final BlockPredicate matcher) {
        super(key);
        this.matcher = matcher;
    }
    
    public boolean canMelt(final World world, final BlockPos pos) {
        return this.matcher.test(world, pos, world.getBlockState(pos));
    }
    
    public abstract void doOutput(final World p0, final BlockPos p1, final BlockState p2, final Consumer<ItemStack> p3);
}
