package hellfirepvp.astralsorcery.common.constellation.mantle.effect;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.nbt.Tag;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import java.util.ArrayList;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraft.world.level.damagesource.DamageSource;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import java.util.Iterator;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import hellfirepvp.astralsorcery.common.entity.EntityFlare;
import net.minecraftforge.fml.LogicalSide;
import hellfirepvp.astralsorcery.common.auxiliary.charge.AlignmentChargeHandler;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import hellfirepvp.astralsorcery.common.lib.ConstellationsAS;
import hellfirepvp.astralsorcery.common.constellation.mantle.MantleEffect;

public class MantleEffectBootes extends MantleEffect
{
    public static BootesConfig CONFIG;
    
    public MantleEffectBootes() {
        super(ConstellationsAS.bootes);
    }
    
    @Override
    protected void attachEventListeners(final IEventBus bus) {
        super.attachEventListeners(bus);
        bus.addListener(EventPriority.LOW, (Consumer)this::onHurt);
        bus.addListener(EventPriority.LOW, (Consumer)this::onAttacked);
    }
    
    @Override
    protected void tickServer(final Player player) {
        super.tickServer(player);
        final ItemStack mantle = player.getItemBySlot(EquipmentSlot.CHEST);
        if (mantle.isEmpty() || !(mantle.getItem() instanceof ItemMantle)) {
            return;
        }
        final Level world = player.level();
        final List<EntityFlare> flares = this.gatherFlares(world, mantle);
        if (flares.size() < (int)MantleEffectBootes.CONFIG.maxFlareCount.get() && player.field_70173_aa % 80 == 0 && AlignmentChargeHandler.INSTANCE.hasCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectBootes.CONFIG.chargeCostPerFlare.get()) && MantleEffectBootes.rand.nextInt(4) == 0) {
            final EntityFlare flare = (EntityFlare)EntityTypesAS.FLARE.func_200721_a(player.level());
            flare.setPos(player.getX(), player.getY(), player.getZ());
            flare.setFollowingTarget((LivingEntity)player);
            if (world.addFreshEntity((Entity)flare)) {
                flares.add(flare);
                AlignmentChargeHandler.INSTANCE.drainCharge(player, LogicalSide.SERVER, (float)(int)MantleEffectBootes.CONFIG.chargeCostPerFlare.get(), false);
            }
        }
        for (final EntityFlare flare2 : flares) {
            if (flare2.getFollowingTarget() != null) {
                if (flare2.func_70638_az() == null) {
                    if (player.func_70032_d((Entity)flare2) < 12.0f) {
                        continue;
                    }
                }
                else if (player.func_70032_d((Entity)flare2) < 35.0f) {
                    continue;
                }
                flare2.func_70080_a(player.getX(), player.getY(), player.getZ(), 0.0f, 0.0f);
            }
        }
        this.setEntityIds(mantle, (List<Integer>)flares.stream().map((Function<? super Object, ?>)Entity::func_145782_y).collect((Collector<? super Object, ?, List<? super Object>>)Collectors.toList()));
    }
    
    @OnlyIn(Dist.CLIENT)
    @Override
    protected void tickClient(final Player player) {
        super.tickClient(player);
        this.playCapeSparkles(player, 0.15f);
    }
    
    private void onAttacked(final LivingAttackEvent event) {
        final LivingEntity attacked = event.getEntityLiving();
        final DamageSource src = event.getSource();
        if (!attacked.level().isClientSide() && src.getDirectEntity() instanceof LivingEntity) {
            final LivingEntity attacker = (LivingEntity)src.getDirectEntity();
            if (ItemMantle.getEffect(attacker, ConstellationsAS.bootes) != null && attacked.isAlive()) {
                if (attacked instanceof Player && !MiscUtils.canPlayerAttackServer(attacker, attacked)) {
                    return;
                }
                this.forEachFlare(attacker, flare -> flare.func_70624_b(attacked));
            }
        }
    }
    
    private void onHurt(final LivingHurtEvent event) {
        final LivingEntity hurt = event.getEntityLiving();
        if (!hurt.level().isClientSide() && ItemMantle.getEffect(hurt, ConstellationsAS.bootes) != null) {
            final Entity source = event.getSource().getDirectEntity();
            if (source instanceof LivingEntity) {
                this.forEachFlare(hurt, flare -> flare.func_70624_b((LivingEntity)source));
            }
        }
    }
    
    protected void forEachFlare(final LivingEntity owner, final Consumer<EntityFlare> fn) {
        final ItemStack mantle = owner.getItemBySlot(EquipmentSlot.CHEST);
        if (mantle.isEmpty() || !(mantle.getItem() instanceof ItemMantle)) {
            return;
        }
        this.gatherFlares(owner.level(), mantle).forEach(fn);
    }
    
    protected List<EntityFlare> gatherFlares(final Level world, final ItemStack mantleStack) {
        final List<EntityFlare> flares = new ArrayList<EntityFlare>();
        for (final int flareId : this.getEntityIds(mantleStack)) {
            final Entity e = world.getEntityById(flareId);
            if (e instanceof EntityFlare && e.isAlive()) {
                flares.add((EntityFlare)e);
            }
        }
        return flares;
    }
    
    protected void setEntityIds(final ItemStack mantleStack, final List<Integer> ids) {
        final ListTag list = new ListTag();
        ids.forEach(i -> list.add((Object)IntTag.func_229692_a_((int)i)));
        NBTHelper.getPersistentData(mantleStack).put("flareIds", (Tag)list);
    }
    
    protected List<Integer> getEntityIds(final ItemStack mantleStack) {
        final List<Integer> ids = new ArrayList<Integer>();
        final ListTag nbtIds = NBTHelper.getPersistentData(mantleStack).getList("flareIds", 3);
        for (int i = 0; i < nbtIds.size(); ++i) {
            ids.add(nbtIds.func_186858_c(i));
        }
        return ids;
    }
    
    @Override
    protected boolean usesTickMethods() {
        return true;
    }
    
    @Override
    public Config getConfig() {
        return MantleEffectBootes.CONFIG;
    }
    
    static {
        MantleEffectBootes.CONFIG = new BootesConfig();
    }
    
    public static class BootesConfig extends Config
    {
        private final int defaultMaxFlareCount = 3;
        private final int defaultChargeCostPerFlare = 400;
        public ForgeConfigSpec.IntValue maxFlareCount;
        public ForgeConfigSpec.IntValue chargeCostPerFlare;
        
        public BootesConfig() {
            super("bootes");
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            super.createEntries(cfgBuilder);
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Defines the maximum flare count the mantle can summon and keep following the wearer.").translation(this.translationKey("maxFlareCount"));
            final String s = "maxFlareCount";
            this.getClass();
            this.maxFlareCount = translation.defineInRange(s, 3, 0, 6);
            final ForgeConfigSpec.Builder translation2 = cfgBuilder.comment("Set the amount alignment charge consumed per created flare").translation(this.translationKey("chargeCostPerFlare"));
            final String s2 = "chargeCostPerFlare";
            this.getClass();
            this.chargeCostPerFlare = translation2.defineInRange(s2, 400, 0, 1000);
        }
    }
}
