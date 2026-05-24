package hellfirepvp.astralsorcery.common.constellation.mantle;

import net.minecraftforge.common.ForgeConfigSpec;
import hellfirepvp.astralsorcery.common.data.config.base.ConfigEntry;
import java.util.EnumSet;
import net.minecraft.world.level.item.ItemStack;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.world.level.entity.EquipmentSlot;
import net.minecraft.nbt.CompoundTag;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.entity.LivingEntity;
import hellfirepvp.astralsorcery.common.item.armor.ItemMantle;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.event.TickEvent;
import javax.annotation.Nonnull;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import java.awt.Color;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.world.level.entity.player.Player;
import java.util.function.Consumer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.common.MinecraftForge;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.common.constellation.IWeakConstellation;
import hellfirepvp.astralsorcery.common.event.PlayerAffectionFlags;
import java.util.Random;
import hellfirepvp.observerlib.common.util.tick.ITickHandler;
import net.minecraftforge.registries.ForgeRegistryEntry;

public abstract class MantleEffect extends ForgeRegistryEntry<MantleEffect> implements ITickHandler
{
    protected static final Random rand;
    private final PlayerAffectionFlags.AffectionFlag playerAffectionFlag;
    private final IWeakConstellation constellation;
    
    public MantleEffect(final IWeakConstellation constellation) {
        this.constellation = constellation;
        this.setRegistryName(this.constellation.getRegistryName());
        this.playerAffectionFlag = new PlayerAffectionFlags.NoOpAffectionFlag(AstralSorcery.key("mantle_effect_" + constellation.getSimpleName()));
        this.attachEventListeners(MinecraftForge.EVENT_BUS);
        this.attachTickHandlers(AstralSorcery.getProxy().getTickManager()::register);
    }
    
    public final IWeakConstellation getAssociatedConstellation() {
        return this.constellation;
    }
    
    public abstract Config getConfig();
    
    public final PlayerAffectionFlags.AffectionFlag getPlayerAffectionFlag() {
        return this.playerAffectionFlag;
    }
    
    protected void attachEventListeners(final IEventBus bus) {
    }
    
    protected void attachTickHandlers(final Consumer<ITickHandler> registrar) {
        if (this.usesTickMethods()) {
            registrar.accept((ITickHandler)this);
        }
    }
    
    protected void tickServer(final Player player) {
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void tickClient(final Player player) {
    }
    
    protected boolean usesTickMethods() {
        return false;
    }
    
    @OnlyIn(Dist.CLIENT)
    protected void playCapeSparkles(final Player player, float chance) {
        if (player == Minecraft.func_71410_x().field_71439_g && Minecraft.func_71410_x().field_71474_y.func_243230_g().func_243192_a()) {
            chance *= 0.1f;
        }
        if (MantleEffect.rand.nextFloat() < chance) {
            final Color c = this.getAssociatedConstellation().getConstellationColor();
            if (c != null) {
                final float width = player.func_213311_cf() * 0.8f;
                final double x = player.func_226277_ct_() + MantleEffect.rand.nextFloat() * width * (MantleEffect.rand.nextBoolean() ? 1 : -1);
                final double y = player.func_226278_cu_() + MantleEffect.rand.nextFloat() * (player.func_213302_cg() / 3.0f);
                final double z = player.func_226281_cx_() + MantleEffect.rand.nextFloat() * width * (MantleEffect.rand.nextBoolean() ? 1 : -1);
                final Vector3 pos = new Vector3(x, y, z);
                final FXFacingParticle fx = this.spawnFacingParticle(player, pos).color(VFXColorFunction.constant(c)).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.4f + MantleEffect.rand.nextFloat() * 0.4f).setMaxAge(20 + MantleEffect.rand.nextInt(10));
                if (MantleEffect.rand.nextInt(3) == 0) {
                    fx.color(VFXColorFunction.constant(this.getAssociatedConstellation().getTierRenderColor()));
                }
                if (MantleEffect.rand.nextFloat() > 0.35f) {
                    this.spawnFacingParticle(player, pos).color(VFXColorFunction.WHITE).alpha(VFXAlphaFunction.FADE_OUT).setScaleMultiplier(0.2f + MantleEffect.rand.nextFloat() * 0.2f).setMaxAge(10 + MantleEffect.rand.nextInt(10));
                }
            }
        }
    }
    
    @Nonnull
    @OnlyIn(Dist.CLIENT)
    protected FXFacingParticle spawnFacingParticle(final Player player, final Vector3 at) {
        return EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).setOwner(player.getUUID()).spawn(at);
    }
    
    public final void tick(final TickEvent.Type type, final Object... context) {
        if (!(boolean)this.getConfig().enabled.get()) {
            return;
        }
        final Player pl = (Player)context[0];
        final LogicalSide side = (LogicalSide)context[1];
        final boolean hasMantle = ItemMantle.getEffect((LivingEntity)pl, this.getAssociatedConstellation()) != null;
        if (!hasMantle) {
            return;
        }
        if (side.isServer()) {
            if (!(pl instanceof ServerPlayer) || MiscUtils.isPlayerFakeMP((ServerPlayer)pl)) {
                return;
            }
            PlayerAffectionFlags.markPlayerAffected(pl, this.playerAffectionFlag);
            this.tickServer(pl);
        }
        else {
            this.tickClient(pl);
        }
    }
    
    @Nonnull
    protected CompoundTag getData(final LivingEntity entity) {
        if (entity == null) {
            return new CompoundTag();
        }
        final ItemStack stack = entity.getItemBySlot(EquipmentSlot.CHEST);
        if (stack.isEmpty() || !(stack.getItem() instanceof ItemMantle)) {
            return new CompoundTag();
        }
        return NBTHelper.getPersistentData(stack);
    }
    
    public EnumSet<TickEvent.Type> getHandledTypes() {
        return EnumSet.of(TickEvent.Type.PLAYER);
    }
    
    public boolean canFire(final TickEvent.Phase phase) {
        return phase == TickEvent.Phase.END;
    }
    
    public String getName() {
        return this.getClass().getName();
    }
    
    static {
        rand = new Random();
    }
    
    public static class Config extends ConfigEntry
    {
        private final boolean defaultEnabled = true;
        public ForgeConfigSpec.BooleanValue enabled;
        
        public Config(final String constellationName) {
            super(String.format("constellation.mantle.%s", constellationName));
        }
        
        @Override
        public void createEntries(final ForgeConfigSpec.Builder cfgBuilder) {
            final ForgeConfigSpec.Builder translation = cfgBuilder.comment("Set this to false to disable this mantle effect").translation(this.translationKey("enabled"));
            final String s = "enabled";
            this.getClass();
            this.enabled = translation.define(s, true);
        }
    }
}
