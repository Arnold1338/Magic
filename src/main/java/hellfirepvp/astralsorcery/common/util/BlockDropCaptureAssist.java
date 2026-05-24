package hellfirepvp.astralsorcery.common.util;

import net.minecraft.world.level.entity.item.ItemEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraft.world.level.item.ItemStack;
import net.minecraft.core.NonNullList;
import java.util.Stack;

public class BlockDropCaptureAssist
{
    public static final BlockDropCaptureAssist INSTANCE;
    private static final Stack<NonNullList<ItemStack>> capturing;
    
    private BlockDropCaptureAssist() {
    }
    
    public void onDrop(final EntityJoinWorldEvent event) {
        if (event.getWorld() instanceof ServerLevel && event.getEntity() instanceof ItemEntity) {
            final ItemStack itemStack = ((ItemEntity)event.getEntity()).func_92059_d();
            if (!BlockDropCaptureAssist.capturing.isEmpty()) {
                event.setCanceled(true);
                if (!itemStack.isEmpty() && !BlockDropCaptureAssist.capturing.isEmpty()) {
                    BlockDropCaptureAssist.capturing.peek().add((Object)itemStack);
                }
                event.getEntity().func_70106_y();
            }
        }
    }
    
    public static void startCapturing() {
        BlockDropCaptureAssist.capturing.push((NonNullList<ItemStack>)NonNullList.func_191196_a());
    }
    
    public static NonNullList<ItemStack> getCapturedStacksAndStop() {
        return BlockDropCaptureAssist.capturing.pop();
    }
    
    static {
        INSTANCE = new BlockDropCaptureAssist();
        capturing = new Stack<NonNullList<ItemStack>>();
    }
}
