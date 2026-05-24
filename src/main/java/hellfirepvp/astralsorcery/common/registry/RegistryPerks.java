package hellfirepvp.astralsorcery.common.registry;

import hellfirepvp.astralsorcery.common.perk.node.root.RootVicio;
import hellfirepvp.astralsorcery.common.perk.node.root.RootEvorsio;
import hellfirepvp.astralsorcery.common.perk.node.root.RootDiscidia;
import hellfirepvp.astralsorcery.common.perk.node.root.RootArmara;
import hellfirepvp.astralsorcery.common.perk.node.root.RootAevitas;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyVoidTrash;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyStoneEnrichment;
import hellfirepvp.astralsorcery.common.perk.node.key.KeySpawnLights;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyRampage;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyProjectileProximity;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyProjectileDistance;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyNoArmor;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyMending;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyLightningArc;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyLastBreath;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyGrowables;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyDisarm;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyDamageEffects;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyDamageArmor;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyCullingAttack;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyCheatDeath;
import hellfirepvp.astralsorcery.common.perk.node.key.KeyBleed;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeDodge;
import hellfirepvp.astralsorcery.common.perk.type.AttributeTypeMiningSize;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import java.util.function.Consumer;

public class RegistryPerks
{
    private RegistryPerks() {
    }
    
    public static void initConfig(final Consumer<ConfigEntry> registrar) {
        registrar.accept(AttributeTypeMiningSize.CONFIG);
        registrar.accept(AttributeTypeDodge.CONFIG);
        registrar.accept(KeyBleed.CONFIG);
        registrar.accept(KeyCheatDeath.CONFIG);
        registrar.accept(KeyCullingAttack.CONFIG);
        registrar.accept(KeyDamageArmor.CONFIG);
        registrar.accept(KeyDamageEffects.CONFIG);
        registrar.accept(KeyDisarm.CONFIG);
        registrar.accept(KeyGrowables.CONFIG);
        registrar.accept(KeyLastBreath.CONFIG);
        registrar.accept(KeyLightningArc.CONFIG);
        registrar.accept(KeyMending.CONFIG);
        registrar.accept(KeyNoArmor.CONFIG);
        registrar.accept(KeyProjectileDistance.CONFIG);
        registrar.accept(KeyProjectileProximity.CONFIG);
        registrar.accept(KeyRampage.CONFIG);
        registrar.accept(KeySpawnLights.CONFIG);
        registrar.accept(KeyStoneEnrichment.CONFIG);
        registrar.accept(KeyVoidTrash.CONFIG);
        registrar.accept(RootAevitas.CONFIG);
        registrar.accept(RootArmara.CONFIG);
        registrar.accept(RootDiscidia.CONFIG);
        registrar.accept(RootEvorsio.CONFIG);
        registrar.accept(RootVicio.CONFIG);
    }
}
