package hellfirepvp.astralsorcery.common.tile;

import hellfirepvp.astralsorcery.client.effect.EntityComplexFX;
import net.minecraft.Util;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.nbt.Tag;
import net.minecraft.util.Tuple;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import hellfirepvp.astralsorcery.common.lib.StructureTypesAS;
import hellfirepvp.astralsorcery.common.structure.types.StructureType;
import net.minecraft.util.Mth;
import hellfirepvp.astralsorcery.common.network.play.server.PktPlayEffect;
import java.util.Iterator;
import java.util.UUID;
import java.util.Collection;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import net.minecraft.world.entity.player.Player;
import javax.annotation.Nullable;
import hellfirepvp.astralsorcery.client.effect.function.VFXAlphaFunction;
import hellfirepvp.astralsorcery.common.lib.ColorsAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXFacingParticle;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import java.awt.Color;
import hellfirepvp.astralsorcery.common.util.ColorUtils;
import hellfirepvp.astralsorcery.client.event.effect.GatewayUIRenderHandler;
import net.minecraft.client.options.PointOfView;
import hellfirepvp.astralsorcery.client.effect.function.RefreshFunction;
import hellfirepvp.astralsorcery.client.effect.function.VFXColorFunction;
import hellfirepvp.astralsorcery.client.effect.handler.EffectHelper;
import hellfirepvp.astralsorcery.client.lib.EffectTemplatesAS;
import hellfirepvp.astralsorcery.client.effect.vfx.FXColorEffectSphere;
import net.minecraft.world.entity.Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import hellfirepvp.astralsorcery.client.effect.EntityVisualFX;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import hellfirepvp.observerlib.common.data.WorldCacheDomain;
import hellfirepvp.astralsorcery.common.lib.DataAS;
import hellfirepvp.astralsorcery.common.data.world.GatewayCache;
import java.util.HashMap;
import net.minecraft.world.level.block.entity.BlockEntityType;
import hellfirepvp.astralsorcery.common.lib.TileEntityTypesAS;
import java.util.Map;
import hellfirepvp.astralsorcery.common.util.PlayerReference;
import net.minecraft.world.item.DyeColor;
import net.minecraft.network.chat.Component;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.auxiliary.link.LinkableTileEntity;
import hellfirepvp.astralsorcery.common.tile.base.TileOwned;
import net.minecraft.util.INameable;
import hellfirepvp.astralsorcery.common.tile.base.TileEntityTick;

public class TileCelestialGateway extends TileEntityTick implements INameable, TileOwned, LinkableTileEntity
{
    private static final BlockPos[] OFFSETS_ALLOWED_PREVIEW;
    private boolean networkRegistered;
    private Component displayText;
    private DyeColor color;
    private boolean locked;
    private PlayerReference owner;
    private final Map<Integer, PlayerReference> allowedUsers;
    private Object clientGatewaySphereEffect;
    
    public TileCelestialGateway() {
        super(TileEntityTypesAS.GATEWAY);
        this.networkRegistered = false;
        this.displayText = null;
        this.color = null;
        this.locked = false;
        this.owner = null;
        this.allowedUsers = new HashMap<Integer, PlayerReference>();
        this.clientGatewaySphereEffect = null;
    }
    
    @Override
    public void func_73660_a() {
        super.func_73660_a();
        if (this.level.level()) {
            this.playEffects();
        }
        else {
            final boolean complete = this.hasMultiblock() & this.doesSeeSky();
            if (complete) {
                if (!this.networkRegistered) {
                    final GatewayCache cache = (GatewayCache)DataAS.DOMAIN_AS.getData(this.level, (WorldCacheDomain.SaveKey)DataAS.KEY_GATEWAY_CACHE);
                    if (cache.offerPosition(this.level, this.getBlockState())) {
                        cache.updateGatewayNode(this.getBlockState(), node -> {
                            node.setDisplayName(this.displayText);
                            node.setColor(this.color);
                            return;
                        });
                        this.updateAccessInformation();
                        this.networkRegistered = true;
                        this.markForUpdate();
                    }
                }
            }
            else if (this.networkRegistered) {
                ((GatewayCache)DataAS.DOMAIN_AS.getData(this.level, (WorldCacheDomain.SaveKey)DataAS.KEY_GATEWAY_CACHE)).removePosition(this.level, this.getBlockState());
                this.networkRegistered = false;
                this.markForUpdate();
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playEffects() {
        this.setupGatewaySphere();
        this.playGatewayParticles();
    }
    
    @OnlyIn(Dist.CLIENT)
    private void setupGatewaySphere() {
        if (!this.hasMultiblock() || !this.doesSeeSky()) {
            if (this.clientGatewaySphereEffect != null) {
                ((EntityVisualFX)this.clientGatewaySphereEffect).requestRemoval();
                this.clientGatewaySphereEffect = null;
            }
            return;
        }
        final Vector3 at = new Vector3(this).add(0.5, 1.7, 0.5);
        final double distance = Vector3.atEntityCorner((Entity)Minecraft.getInstance().player).distance(at);
        if (this.clientGatewaySphereEffect == null) {
            this.clientGatewaySphereEffect = EffectHelper.of(EffectTemplatesAS.COLOR_SPHERE).spawn(at).setupSphere(Vector3.RotAxis.Y_AXIS, 6.0f).setRemoveIfInvisible(true).setAlphaFadeDistance(4.0).setAlphaMultiplier(1.0f).color(VFXColorFunction.BLACK).refresh(RefreshFunction.tileExistsAnd(this, (te, fx) -> te.doesSeeSky() && te.hasMultiblock()));
        }
        else if (((EntityVisualFX)this.clientGatewaySphereEffect).isRemoved() && distance < 5.0) {
            EffectHelper.refresh(this.clientGatewaySphereEffect, EffectTemplatesAS.COLOR_SPHERE);
        }
        if (distance < 5.5) {
            Minecraft.getInstance().options.func_243229_a(PointOfView.FIRST_PERSON);
        }
        if (distance < 2.5) {
            GatewayUIRenderHandler.getInstance().getOrCreateUI(this.getLevel(), this.getBlockState(), at);
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    private void playGatewayParticles() {
        if (!this.hasMultiblock() || !this.doesSeeSky()) {
            return;
        }
        final Color gatewayColor = ColorUtils.flareColorFromDye(this.getColor().orElse(DyeColor.YELLOW));
        for (int i = 0; i < 3; ++i) {
            final Vector3 offset = new Vector3(this).add(-2.0, 0.05, -2.0);
            if (TileCelestialGateway.rand.nextBoolean()) {
                offset.add((float)(5 * (TileCelestialGateway.rand.nextBoolean() ? 1 : 0)), 0.0f, TileCelestialGateway.rand.nextFloat() * 5.0f);
            }
            else {
                offset.add(TileCelestialGateway.rand.nextFloat() * 5.0f, 0.0f, (float)(5 * (TileCelestialGateway.rand.nextBoolean() ? 1 : 0)));
            }
            final Color c = MiscUtils.eitherOf(TileCelestialGateway.rand, new Color[] { Color.WHITE, gatewayColor, gatewayColor.brighter() });
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setGravityStrength(-1.0E-4f).color(VFXColorFunction.constant(c)).setScaleMultiplier(0.25f + TileCelestialGateway.rand.nextFloat() * 0.15f).setMaxAge(30 + TileCelestialGateway.rand.nextInt(30));
        }
        for (int i = 0; i < 2; ++i) {
            final Vector3 offset = new Vector3();
            MiscUtils.applyRandomOffset(offset, TileCelestialGateway.rand, 3.0f);
            offset.add(new Vector3(this)).add(0.5, 0.0, 0.5).setY(this.getBlockState().getY() + 0.05);
            final Color c = MiscUtils.eitherOf(TileCelestialGateway.rand, new Color[] { Color.WHITE, gatewayColor, gatewayColor.brighter() });
            EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(offset).setGravityStrength(-4.0E-5f).color(VFXColorFunction.constant(c)).setScaleMultiplier(0.15f + TileCelestialGateway.rand.nextFloat() * 0.1f).setMaxAge(15 + TileCelestialGateway.rand.nextInt(10));
        }
        if (this.isLocked() && this.getOwner() != null) {
            final Vector3 center = new Vector3(this).add(0.5, 0.2, 0.5);
            for (int j = 0; j < TileCelestialGateway.rand.nextInt(5) + 2; ++j) {
                final Vector3 pos = MiscUtils.getRandomCirclePosition(center, Vector3.RotAxis.Y_AXIS, 1.7);
                MiscUtils.applyRandomOffset(pos, TileCelestialGateway.rand, 0.05f);
                final Color c2 = MiscUtils.eitherOf(TileCelestialGateway.rand, new Color[] { Color.WHITE, ColorsAS.EFFECT_BLUE_LIGHT, ColorsAS.EFFECT_BLUE_DARK });
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).color(VFXColorFunction.constant(c2)).setScaleMultiplier(0.25f + TileCelestialGateway.rand.nextFloat() * 0.15f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(15 + TileCelestialGateway.rand.nextInt(10));
            }
            for (int j = 0; j < TileCelestialGateway.rand.nextInt(3) + 1; ++j) {
                final Vector3 pos = MiscUtils.getRandomCirclePosition(center, Vector3.RotAxis.Y_AXIS, 1.1).addY(0.3);
                MiscUtils.applyRandomOffset(pos, TileCelestialGateway.rand, 0.05f);
                final Color c2 = MiscUtils.eitherOf(TileCelestialGateway.rand, new Color[] { Color.WHITE, ColorsAS.EFFECT_BLUE_LIGHT, ColorsAS.EFFECT_BLUE_DARK });
                EffectHelper.of(EffectTemplatesAS.GENERIC_PARTICLE).spawn(pos).color(VFXColorFunction.constant(c2)).setScaleMultiplier(0.25f + TileCelestialGateway.rand.nextFloat() * 0.15f).alpha(VFXAlphaFunction.FADE_OUT).setMaxAge(15 + TileCelestialGateway.rand.nextInt(10));
            }
        }
    }
    
    public boolean isLocked() {
        return this.locked;
    }
    
    public boolean lock() {
        if (this.isLocked() || this.getOwner() == null) {
            return false;
        }
        this.locked = true;
        this.updateAccessInformation();
        return true;
    }
    
    public boolean unlock() {
        if (!this.isLocked()) {
            return false;
        }
        this.locked = false;
        this.updateAccessInformation();
        return true;
    }
    
    @Nullable
    public PlayerReference getOwner() {
        return this.owner;
    }
    
    @Nullable
    public PlayerReference setOwner(@Nullable final PlayerReference player) {
        final PlayerReference prevOwner = this.owner;
        this.owner = player;
        this.updateAccessInformation();
        return prevOwner;
    }
    
    public boolean canAddAllowedUser(final Player otherUser) {
        return this.canAddAllowedUser(PlayerReference.of(otherUser));
    }
    
    public boolean canAddAllowedUser(final PlayerReference otherUser) {
        return this.getOwner() != null && this.allowedUsers.size() < TileCelestialGateway.OFFSETS_ALLOWED_PREVIEW.length && !this.allowedUsers.containsValue(otherUser);
    }
    
    public boolean addAllowedUser(final Player otherUser) {
        return this.addAllowedUser(PlayerReference.of(otherUser));
    }
    
    public boolean addAllowedUser(final PlayerReference otherUser) {
        if (!this.canAddAllowedUser(otherUser)) {
            return false;
        }
        final List<Integer> availableIndices = new ArrayList<Integer>();
        for (int i = 0; i < TileCelestialGateway.OFFSETS_ALLOWED_PREVIEW.length; ++i) {
            if (!this.allowedUsers.containsKey(i)) {
                availableIndices.add(i);
            }
        }
        if (availableIndices.isEmpty()) {
            return false;
        }
        Collections.shuffle(availableIndices);
        this.allowedUsers.put(MiscUtils.getRandomEntry(availableIndices, TileCelestialGateway.rand), otherUser);
        this.updateAccessInformation();
        return true;
    }
    
    @Nullable
    public PlayerReference removeAllowedUser(final UUID otherUser) {
        if (this.allowedUsers.isEmpty()) {
            return null;
        }
        for (final Map.Entry<Integer, PlayerReference> entry : this.allowedUsers.entrySet()) {
            if (entry.getValue().getPlayerUUID().equals(otherUser)) {
                this.allowedUsers.remove(entry.getKey());
                this.updateAccessInformation();
                return entry.getValue();
            }
        }
        return null;
    }
    
    public Map<Integer, PlayerReference> getAllowedUsers() {
        if (this.getOwner() == null) {
            return Collections.emptyMap();
        }
        return Collections.unmodifiableMap((Map<? extends Integer, ? extends PlayerReference>)this.allowedUsers);
    }
    
    private void updateAccessInformation() {
        ((GatewayCache)DataAS.DOMAIN_AS.getData(this.level, (WorldCacheDomain.SaveKey)DataAS.KEY_GATEWAY_CACHE)).updateGatewayNode(this.getBlockState(), node -> {
            node.setLocked(this.isLocked());
            node.setOwner(this.getOwner());
            node.setAllowedUsers(this.allowedUsers);
            return;
        });
        this.markForUpdate();
    }
    
    @OnlyIn(Dist.CLIENT)
    public static void playAccessRevokeEffect(final PktPlayEffect pktPlayEffect) {
    }
    
    public static BlockPos getAllowedUserOffset(final int index) {
        return TileCelestialGateway.OFFSETS_ALLOWED_PREVIEW[Mth.getDescriptionId(index, 0, TileCelestialGateway.OFFSETS_ALLOWED_PREVIEW.length - 1)];
    }
    
    public void setDisplayText(@Nullable final Component displayText) {
        this.displayText = displayText;
    }
    
    public void setColor(@Nullable final DyeColor color) {
        this.color = color;
    }
    
    @Nullable
    @Override
    public StructureType getRequiredStructureType() {
        return StructureTypesAS.PTYPE_CELESTIAL_GATEWAY;
    }
    
    @Override
    public boolean seesSkyInNoSkyWorlds() {
        return true;
    }
    
    public Component func_200200_C_() {
        return (Component)((this.displayText != null) ? this.displayText : Component.translatable("block.astralsorcery.celestial_gateway"));
    }
    
    public boolean func_145818_k_() {
        return this.displayText != null;
    }
    
    @Nullable
    public Component func_200201_e() {
        return this.func_200200_C_();
    }
    
    public Optional<DyeColor> getColor() {
        return Optional.ofNullable(this.color);
    }
    
    @Override
    public void readCustomNBT(final CompoundTag compound) {
        super.readCustomNBT(compound);
        this.networkRegistered = compound.getBoolean("networkRegistered");
        this.displayText = (Component)(compound.contains("displayText") ? Component.Serializer.func_240643_a_(compound.getString("displayText")) : null);
        this.color = (compound.contains("color") ? NBTHelper.readEnum(compound, "color", DyeColor.class) : null);
        this.locked = compound.getBoolean("locked");
        this.owner = NBTHelper.readOptional(compound, "owningPlayer", PlayerReference::deserialize);
        this.allowedUsers.clear();
        NBTHelper.readList(compound, "allowedUsers", 10, nbt -> {
            final CompoundTag tag = (CompoundTag)nbt;
            return new Tuple((Object)tag.getInt("index"), (Object)PlayerReference.deserialize(tag.func_74775_l("player")));
        }).forEach(tpl -> {
            final PlayerReference playerReference = this.allowedUsers.put((Integer)tpl.getA(), (PlayerReference)tpl.getB());
        });
    }
    
    @Override
    public void writeCustomNBT(final CompoundTag compound) {
        super.writeCustomNBT(compound);
        compound.putBoolean("networkRegistered", this.networkRegistered);
        if (this.displayText != null) {
            compound.putString("displayText", Component.Serializer.func_150696_a(this.displayText));
        }
        if (this.color != null) {
            NBTHelper.writeEnum(compound, "color", this.color);
        }
        compound.putBoolean("locked", this.locked);
        NBTHelper.writeOptional(compound, "owningPlayer", this.owner, (tag, playerRef) -> playerRef.writeToNBT(tag));
        NBTHelper.writeList(compound, "allowedUsers", (Collection<Map.Entry<Integer, PlayerReference>>)this.allowedUsers.entrySet(), entry -> {
            final CompoundTag tag2 = new CompoundTag();
            tag2.putInt("index", (int)entry.getKey());
            tag2.put("player", (Tag)entry.getValue().serialize());
            return tag2;
        });
    }
    
    public void onBlockLinkCreate(final Player player, final BlockPos other) {
    }
    
    public void onEntityLinkCreate(final Player player, final LivingEntity linked) {
        if (linked instanceof Player && this.addAllowedUser((Player)linked)) {
            final Component accessGrantedMessage = Component.translatable("astralsorcery.misc.link.gateway.link").withStyle(ChatFormatting.GREEN);
            player.sendSystemMessage(accessGrantedMessage);
            linked.sendSystemMessage(accessGrantedMessage);
        }
    }
    
    public boolean tryLinkBlock(final Player player, final BlockPos other) {
        return false;
    }
    
    public boolean tryLinkEntity(final Player player, final LivingEntity other) {
        return other instanceof Player && this.canAddAllowedUser((Player)other);
    }
    
    public boolean tryUnlink(final Player player, final BlockPos other) {
        return false;
    }
    
    public List<BlockPos> getLinkedPositions() {
        return Collections.emptyList();
    }
    
    public boolean onSelect(final Player player) {
        return false;
    }
    
    static {
        OFFSETS_ALLOWED_PREVIEW = new BlockPos[] { new BlockPos(-3, 0, -2), new BlockPos(-3, 0, -1), new BlockPos(-3, 0, 0), new BlockPos(-3, 0, 1), new BlockPos(-3, 0, 2), new BlockPos(-2, 0, 3), new BlockPos(-1, 0, 3), new BlockPos(0, 0, 3), new BlockPos(1, 0, 3), new BlockPos(2, 0, 3), new BlockPos(3, 0, 2), new BlockPos(3, 0, 1), new BlockPos(3, 0, 0), new BlockPos(3, 0, -1), new BlockPos(3, 0, -2), new BlockPos(2, 0, -3), new BlockPos(1, 0, -3), new BlockPos(0, 0, -3), new BlockPos(-1, 0, -3), new BlockPos(-2, 0, -3) };
    }
}
