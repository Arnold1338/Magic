package hellfirepvp.astralsorcery.common.perk.node.key;

import net.minecraft.world.item.ItemStack;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import net.minecraft.world.level.block.Blocks;
import java.util.ArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import java.util.List;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyVoidTrash extends KeyPerk
{
    private static final float defaultOreChance = 0.02f;
    private static final List<Item> defaultTrashItems;
    public static final Config CONFIG;
    
    public KeyVoidTrash(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
    }
    
    static {
        defaultTrashItems = new ArrayList<Item>() {
            {
                this.add(Blocks.field_150346_d.func_199767_j());
                this.add(Blocks.field_150347_e.func_199767_j());
                this.add(Blocks.field_196656_g.func_199767_j());
                this.add(Blocks.field_196654_e.func_199767_j());
                this.add(Blocks.field_196650_c.func_199767_j());
                this.add(Blocks.field_150348_b.func_199767_j());
                this.add(Blocks.field_150351_n.func_199767_j());
            }
        };
        CONFIG = new Config("key.void_trash");
    }
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue oreChance;
        private ForgeConfigSpec.ConfigValue<List<String>> trashItems;
        
        private Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.trashItems = (ForgeConfigSpec.ConfigValue<List<String>>)cfgBuilder.comment("List items that should count as trash and should be voided.").translation(this.translationKey("trashItems")).define("trashItems", KeyVoidTrash.defaultTrashItems.stream().map(item -> item.getRegistryName().toString()).collect(Collectors.toList()));
            this.oreChance = cfgBuilder.comment("Chance that a voided drop will instead yield a random ore out of the configured ore table.").translation(this.translationKey("oreChance")).defineInRange("oreChance", 0.019999999552965164, 0.0, 1.0);
        }
        
        public boolean isTrash(final ItemStack stack) {
            final String key = stack.getItem().getRegistryName().toString();
            return ((List)this.trashItems.get()).contains(key);
        }
        
        public double getOreChance() {
            return (double)this.oreChance.get();
        }
    }
}
