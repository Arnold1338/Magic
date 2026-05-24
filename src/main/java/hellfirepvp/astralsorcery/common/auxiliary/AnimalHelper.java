package hellfirepvp.astralsorcery.common.auxiliary;

import net.minecraft.entity.passive.SquidEntity;
import hellfirepvp.astralsorcery.common.util.entity.EntityUtils;
import hellfirepvp.astralsorcery.common.CommonProxy;
import net.minecraft.world.item.ItemStack;
import java.util.List;
import java.util.Random;
import net.minecraft.world.level.Level;
import net.minecraft.entity.passive.AnimalEntity;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Iterator;
import net.minecraft.world.entity.LivingEntity;
import java.util.LinkedList;

public class AnimalHelper
{
    private static final LinkedList<HerdableAnimal> animalHandlers;
    
    public static void registerFirst(final HerdableAnimal handler) {
        AnimalHelper.animalHandlers.addFirst(handler);
    }
    
    public static void register(final HerdableAnimal handler) {
        AnimalHelper.animalHandlers.add(handler);
    }
    
    @Nullable
    public static HerdableAnimal getHandler(final LivingEntity entity) {
        for (final HerdableAnimal herd : AnimalHelper.animalHandlers) {
            if (herd.handles(entity)) {
                return herd;
            }
        }
        return null;
    }
    
    static {
        animalHandlers = new LinkedList<HerdableAnimal>();
        register(new Squid());
        register(new GenericAnimal());
    }
    
    public static class GenericAnimal implements HerdableAnimal
    {
        @Override
        public boolean handles(@Nonnull final LivingEntity entity) {
            return entity instanceof AnimalEntity;
        }
        
        @Override
        public List<ItemStack> generateDrops(@Nonnull final LivingEntity entity, final World world, final Random rand, final float luck) {
            return EntityUtils.generateLoot(entity, rand, CommonProxy.DAMAGE_SOURCE_STELLAR, null);
        }
    }
    
    public static class Squid extends GenericAnimal
    {
        @Override
        public boolean handles(@Nonnull final LivingEntity entity) {
            return entity instanceof SquidEntity;
        }
    }
    
    public interface HerdableAnimal
    {
        boolean handles(@Nonnull final LivingEntity p0);
        
        List<ItemStack> generateDrops(@Nonnull final LivingEntity p0, final World p1, final Random p2, final float p3);
    }
}
