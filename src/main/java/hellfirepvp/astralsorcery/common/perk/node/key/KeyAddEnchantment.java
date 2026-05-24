package hellfirepvp.astralsorcery.common.perk.node.key;

import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.util.JSONUtils;
import com.google.gson.JsonObject;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.perk.AbstractPerk;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.event.DynamicEnchantmentEvent;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantmentType;
import net.minecraft.world.item.enchantment.Enchantment;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.LogicalSide;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.enchantment.dynamic.DynamicEnchantment;
import java.util.List;
import hellfirepvp.astralsorcery.common.perk.node.KeyPerk;

public class KeyAddEnchantment extends KeyPerk
{
    private final List<DynamicEnchantment> enchantments;
    
    public KeyAddEnchantment(final ResourceLocation name, final float x, final float y) {
        super(name, x, y);
        this.enchantments = Lists.newArrayList();
    }
    
    public void attachListeners(final LogicalSide side, final IEventBus bus) {
        super.attachListeners(side, bus);
        if (side.isServer()) {
            bus.addListener((Consumer)this::onEnchantmentAddServer);
        }
        else {
            bus.addListener((Consumer)this::onEnchantmentAddClient);
        }
    }
    
    public KeyAddEnchantment addEnchantment(final Enchantment ench, final int level) {
        return this.addEnchantment(DynamicEnchantmentType.ADD_TO_SPECIFIC, ench, level);
    }
    
    public KeyAddEnchantment addEnchantment(final DynamicEnchantmentType type, final Enchantment ench, final int level) {
        this.enchantments.add(new DynamicEnchantment(type, ench, level));
        return this;
    }
    
    public KeyAddEnchantment addAllEnchantmentIncrease(final int level) {
        this.enchantments.add(new DynamicEnchantment(DynamicEnchantmentType.ADD_TO_EXISTING_ALL, level));
        return this;
    }
    
    private void onEnchantmentAddClient(final DynamicEnchantmentEvent.Add event) {
        final Player player = event.getResolvedPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        if (side.isClient()) {
            this.addEnchantments(player, side, event);
        }
    }
    
    private void onEnchantmentAddServer(final DynamicEnchantmentEvent.Add event) {
        final Player player = event.getResolvedPlayer();
        final LogicalSide side = this.getSide((Entity)player);
        if (side.isServer()) {
            this.addEnchantments(player, side, event);
        }
    }
    
    private void addEnchantments(final Player player, final LogicalSide side, final DynamicEnchantmentEvent.Add event) {
        final PlayerProgress prog = ResearchHelper.getProgress(player, side);
        if (prog.getPerkData().hasPerkEffect(this)) {
            final List<DynamicEnchantment> listedEnchantments = event.getEnchantmentsToApply();
            for (final DynamicEnchantment ench : this.enchantments) {
                final DynamicEnchantment added = MiscUtils.iterativeSearch(listedEnchantments, e -> {
                    if (e.getEnchantment() == null) {
                        if (ench.getEnchantment() != null) {
                            return 0 != 0;
                        }
                    }
                    else if (!e.getEnchantment().equals(ench.getEnchantment())) {
                        return 0 != 0;
                    }
                    final boolean b;
                    if (e.getType().equals(ench.getType())) {
                        return b;
                    }
                    return b;
                });
                if (added != null) {
                    added.setLevelAddition(added.getLevelAddition() + ench.getLevelAddition());
                }
                else {
                    listedEnchantments.add(ench.copy());
                }
            }
        }
    }
    
    @Override
    public void deserializeData(final JsonObject perkData) {
        super.deserializeData(perkData);
        this.enchantments.clear();
        if (perkData.has("enchantments")) {
            final JsonArray array = JSONUtils.func_151214_t(perkData, "enchantments");
            for (int i = 0; i < array.size(); ++i) {
                final JsonObject serializedEnchantment = JSONUtils.func_151210_l(array.get(i), "enchantments[%s]");
                final String typeKey = JSONUtils.func_151200_h(serializedEnchantment, "type");
                DynamicEnchantmentType type;
                try {
                    type = DynamicEnchantmentType.valueOf(typeKey);
                }
                catch (final Exception exc) {
                    throw new IllegalArgumentException("Unknown dynamic enchantment type: " + typeKey);
                }
                final int level = JSONUtils.func_151203_m(serializedEnchantment, "level");
                if (type.isEnchantmentSpecific()) {
                    final String enchantmentKey = JSONUtils.func_151200_h(serializedEnchantment, "enchantment");
                    final Enchantment ench = (Enchantment)ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(enchantmentKey));
                    if (ench == null) {
                        throw new IllegalArgumentException("Unknown Enchantment: " + enchantmentKey);
                    }
                    this.addEnchantment(type, ench, level);
                }
                else {
                    this.addAllEnchantmentIncrease(level);
                }
            }
        }
    }
    
    @Override
    public void serializeData(final JsonObject perkData) {
        super.serializeData(perkData);
        if (!this.enchantments.isEmpty()) {
            final JsonArray array = new JsonArray();
            for (final DynamicEnchantment enchantment : this.enchantments) {
                final JsonObject serializedEnchantment = new JsonObject();
                serializedEnchantment.addProperty("type", enchantment.getType().name());
                if (enchantment.getEnchantment() != null) {
                    serializedEnchantment.addProperty("enchantment", enchantment.getEnchantment().getRegistryName().toString());
                }
                serializedEnchantment.addProperty("level", (Number)enchantment.getLevelAddition());
                array.add((JsonElement)serializedEnchantment);
            }
            perkData.add("enchantments", (JsonElement)array);
        }
    }
}
