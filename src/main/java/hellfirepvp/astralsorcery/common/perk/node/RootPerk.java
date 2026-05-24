package hellfirepvp.astralsorcery.common.perk.node;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import javax.annotation.Nonnull;
import net.minecraft.world.level.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreeConstellation;
import hellfirepvp.astralsorcery.common.perk.tree.PerkTreePoint;
import java.util.HashMap;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.util.DiminishingMultiplier;
import java.util.UUID;
import java.util.Map;
import hellfirepvp.astralsorcery.common.constellation.IMajorConstellation;
import hellfirepvp.astralsorcery.common.perk.modifier.AttributeModifierPerk;

public abstract class RootPerk extends AttributeModifierPerk
{
    private final IMajorConstellation constellation;
    private final Map<UUID, DiminishingMultiplier> dimReturns;
    private final Config config;
    
    public RootPerk(final ResourceLocation name, final Config rootConfig, final IMajorConstellation constellation, final float x, final float y) {
        super(name, x, y);
        this.dimReturns = new HashMap<UUID, DiminishingMultiplier>();
        this.constellation = constellation;
        this.config = rootConfig;
        this.setCategory(RootPerk.CATEGORY_ROOT);
    }
    
    @Override
    protected PerkTreePoint<? extends RootPerk> initPerkTreePoint() {
        return new PerkTreeConstellation<RootPerk>((RootPerk)this, this.getOffset(), (IConstellation)this.constellation, 50);
    }
    
    public IMajorConstellation getConstellation() {
        return this.constellation;
    }
    
    @Override
    public void clearCaches(final LogicalSide side) {
        super.clearCaches(side);
        if (side.isServer()) {
            this.dimReturns.clear();
        }
    }
    
    protected double getExpMultiplier() {
        return (double)this.config.expMultiplier.get();
    }
    
    protected float getDiminishingReturns(final Player player) {
        final UUID playerUUID = player.getUUID();
        return this.dimReturns.computeIfAbsent(playerUUID, uuid -> this.createMultiplier()).getMultiplier();
    }
    
    @Nonnull
    protected abstract DiminishingMultiplier createMultiplier();
    
    public static class Config extends ConfigEntry
    {
        private ForgeConfigSpec.DoubleValue expMultiplier;
        
        public Config(final String section) {
            super(section);
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            this.expMultiplier = cfgBuilder.comment("Defines the general exp multiplier for this root perk. Can be used for balancing in a pack environment.").translation(this.translationKey("expMultiplier")).defineInRange("expMultiplier", 1.0, 0.10000000149011612, 20.0);
        }
    }
}
