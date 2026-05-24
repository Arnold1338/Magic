package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraft.util.FoodStats;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.perk.tick.PlayerTickPerk;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyReducedFood extends KeyPerk implements PlayerTickPerk
{
    public KeyReducedFood(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    @Override
    public void onPlayerTick(final Player player, final LogicalSide side) {
        if (side.isServer() && KeyReducedFood.rand.nextFloat() < 0.01) {
            final FoodStats stats = player.func_71024_bL();
            if (stats.func_75116_a() < 20 || stats.func_75115_e() < 5.0f) {
                stats.func_75122_a(1, 0.3f);
            }
        }
    }
}
