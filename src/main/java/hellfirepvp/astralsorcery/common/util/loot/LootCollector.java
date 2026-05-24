package hellfirepvp.astralsorcery.common.util.loot;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.level.item.ItemStack;
import java.util.function.Consumer;

public class LootCollector implements Consumer<ItemStack>
{
    private final Consumer<ItemStack> chaining;
    private List<ItemStack> collectedOutput;
    
    public LootCollector(final Consumer<ItemStack> chaining) {
        this.collectedOutput = new ArrayList<ItemStack>();
        this.chaining = chaining;
    }
    
    @Override
    public void accept(final ItemStack stack) {
        this.collectedOutput.add(stack);
    }
    
    public List<ItemStack> getCollectedOutput() {
        return Collections.unmodifiableList((List<? extends ItemStack>)this.collectedOutput);
    }
    
    public void setCollectedOutput(final List<ItemStack> collectedOutput) {
        this.collectedOutput = collectedOutput;
    }
    
    public void flush() {
        this.getCollectedOutput().forEach(this.chaining);
    }
}
