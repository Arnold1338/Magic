package hellfirepvp.astralsorcery.common.lib;

import hellfirepvp.astralsorcery.common.entity.technical.EntityGrapplingHook;
import hellfirepvp.astralsorcery.common.entity.technical.EntityObservatoryHelper;
import hellfirepvp.astralsorcery.common.entity.item.EntityStarmetal;
import hellfirepvp.astralsorcery.common.entity.item.EntityDazzlingGem;
import hellfirepvp.astralsorcery.common.entity.item.EntityCrystal;
import hellfirepvp.astralsorcery.common.entity.item.EntityItemExplosionResistant;
import hellfirepvp.astralsorcery.common.entity.item.EntityItemHighlighted;
import hellfirepvp.astralsorcery.common.entity.EntityShootingStar;
import hellfirepvp.astralsorcery.common.entity.EntitySpectralTool;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import hellfirepvp.astralsorcery.common.entity.EntityIlluminationSpark;
import hellfirepvp.astralsorcery.common.entity.EntityNocturnalSpark;
import net.minecraft.world.level.entity.EntityType;

public class EntityTypesAS
{
    public static EntityType<EntityNocturnalSpark> NOCTURNAL_SPARK;
    public static EntityType<EntityIlluminationSpark> ILLUMINATION_SPARK;
    public static EntityType<EntityFlare> FLARE;
    public static EntityType<EntitySpectralTool> SPECTRAL_TOOL;
    public static EntityType<EntityShootingStar> SHOOTING_STAR;
    public static EntityType<EntityItemHighlighted> ITEM_HIGHLIGHT;
    public static EntityType<EntityItemExplosionResistant> ITEM_EXPLOSION_RESISTANT;
    public static EntityType<EntityCrystal> ITEM_CRYSTAL;
    public static EntityType<EntityDazzlingGem> ITEM_DAZZLING_GEM;
    public static EntityType<EntityStarmetal> ITEM_STARMETAL_INGOT;
    public static EntityType<EntityObservatoryHelper> OBSERVATORY_HELPER;
    public static EntityType<EntityGrapplingHook> GRAPPLING_HOOK;
    
    private EntityTypesAS() {
    }
}
