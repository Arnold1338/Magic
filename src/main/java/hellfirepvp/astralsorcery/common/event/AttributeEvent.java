package hellfirepvp.astralsorcery.common.event;

import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeTypeHelper;
import net.minecraft.world.entity.ai.attributes.Attribute;
import javax.annotation.Nullable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.common.perk.type.PerkAttributeType;
import net.minecraft.world.entity.player.Player;

public class AttributeEvent
{
    public static double postProcessModded(final Player player, final PerkAttributeType type, final double value) {
        final PostProcessModded ev = new PostProcessModded(value, type, player);
        MinecraftForge.EVENT_BUS.post((Event)ev);
        return ev.getValue();
    }
    
    public static float postProcessModded(final Player player, final PerkAttributeType type, final float value) {
        return (float)postProcessModded(player, type, (double)value);
    }
    
    public static double postProcessModded(final Player player, final ResourceLocation key, final double value) {
        final PerkAttributeType pType = (PerkAttributeType)RegistriesAS.REGISTRY_PERK_ATTRIBUTE_TYPES.getValue(key);
        if (pType == null) {
            return value;
        }
        return postProcessModded(player, pType, value);
    }
    
    public static float postProcessModded(final Player player, final ResourceLocation key, final float value) {
        return (float)postProcessModded(player, key, (double)value);
    }
    
    public static double postProcessVanilla(final double value, final AttributeInstance attribute) {
        final PostProcessVanilla event = new PostProcessVanilla(attribute, value);
        MinecraftForge.EVENT_BUS.post((Event)event);
        return event.getAttribute().func_111109_a(event.getValue());
    }
    
    @Nullable
    private static LivingEntity getEntity(final AttributeMap map) {
        if (map instanceof EntityModifierManager) {
            return ((EntityModifierManager)map).getLivingEntity();
        }
        return null;
    }
    
    public static void setEntity(final AttributeMap map, final LivingEntity entity) {
        if (map instanceof EntityModifierManager) {
            ((EntityModifierManager)map).setLivingEntity(entity);
        }
    }
    
    public static class PostProcessVanilla extends Event
    {
        private final AttributeInstance instance;
        private final double originalValue;
        private double value;
        
        public PostProcessVanilla(final AttributeInstance instance, final double value) {
            this.instance = instance;
            this.originalValue = value;
            this.value = value;
        }
        
        public double getOriginalValue() {
            return this.originalValue;
        }
        
        public double getValue() {
            return this.value;
        }
        
        public void setValue(final double value) {
            this.value = value;
        }
        
        public AttributeInstance getInstance() {
            return this.instance;
        }
        
        public Attribute getAttribute() {
            return this.instance.func_111123_a();
        }
        
        @Nullable
        public PerkAttributeType resolveAttributeType() {
            return PerkAttributeTypeHelper.findVanillaType(this.getAttribute());
        }
    }
    
    public static class PostProcessModded extends Event
    {
        private final Player player;
        private final PerkAttributeType type;
        private final double originalValue;
        private double value;
        
        public PostProcessModded(final double value, final PerkAttributeType type, final Player player) {
            this.player = player;
            this.type = type;
            this.originalValue = value;
            this.value = value;
        }
        
        public double getOriginalValue() {
            return this.originalValue;
        }
        
        public double getValue() {
            return this.value;
        }
        
        public void setValue(final double value) {
            this.value = value;
        }
        
        public PerkAttributeType getType() {
            return this.type;
        }
        
        public Player getPlayer() {
            return this.player;
        }
    }
    
    public interface EntityModifierManager
    {
        @Nullable
        LivingEntity getLivingEntity();
        
        void setLivingEntity(final LivingEntity p0);
    }
}
