package hellfirepvp.astralsorcery.common.item;

import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import hellfirepvp.astralsorcery.common.data.research.PlayerProgress;
import net.minecraft.commands.CommandSourceStack;
import hellfirepvp.astralsorcery.common.data.research.ResearchManager;
import java.util.Collection;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import net.minecraft.resources.ResourceLocation;
import java.util.ArrayList;
import net.minecraftforge.fml.LogicalSide;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.EntityType;
import hellfirepvp.astralsorcery.common.entity.item.EntityItemExplosionResistant;
import hellfirepvp.astralsorcery.common.lib.EntityTypesAS;
import net.minecraft.world.entity.Entity;
import hellfirepvp.astralsorcery.common.GuiType;
import hellfirepvp.astralsorcery.AstralSorcery;
import net.minecraft.sounds.SoundEvent;
import hellfirepvp.astralsorcery.common.util.sound.SoundHelper;
import hellfirepvp.astralsorcery.common.lib.SoundsAS;
import net.minecraft.world.level.InteractionResult;
import net.minecraft.world.level.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.player.Player;
import hellfirepvp.astralsorcery.common.data.research.ResearchHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.network.chat.Component;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.level.Level;
import java.util.Iterator;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import hellfirepvp.astralsorcery.common.constellation.ConstellationRegistry;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import hellfirepvp.astralsorcery.common.CommonProxy;
import hellfirepvp.astralsorcery.common.constellation.ConstellationBaseItem;
import hellfirepvp.astralsorcery.common.item.base.client.ItemDynamicColor;
import net.minecraft.world.item.Item;

public class ItemConstellationPaper extends Item implements ItemDynamicColor, ConstellationBaseItem
{
    public ItemConstellationPaper() {
        super(new Item.Properties().func_200917_a(1).hasModifier(CommonProxy.ITEM_GROUP_AS_PAPERS));
    }
    
    public void func_150895_a(final CreativeModeTab group, final NonNullList<ItemStack> items) {
        if (this.func_194125_a(group)) {
            items.add((Object)new ItemStack((ItemLike)this, 1));
            for (final IConstellation c : ConstellationRegistry.getAllConstellations()) {
                final ItemStack cPaper = new ItemStack((ItemLike)this, 1);
                this.setConstellation(cPaper, c);
                items.add((Object)cPaper);
            }
        }
    }
    
    @OnlyIn(Dist.CLIENT)
    public void func_77624_a(final ItemStack stack, @Nullable final Level world, final List<Component> toolTip, final TooltipFlag flag) {
        final IConstellation c = this.getConstellation(stack);
        if (c != null && c.canDiscover((Player)Minecraft.getInstance().player, ResearchHelper.getClientProgress())) {
            toolTip.add((Component)c.getConstellationName().toString()ChatFormatting.BLUE));
        }
        else {
            toolTip.add((Component)new Component("astralsorcery.misc.noinformation").toString()ChatFormatting.GRAY));
        }
    }
    
    public InteractionResult<ItemStack> func_77659_a(final Level world, final Player player, final Hand hand) {
        final ItemStack held = player.getItemInHand(hand);
        if (held.isEmpty()) {
            return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
        }
        if (world.level() && this.getConstellation(held) != null) {
            SoundHelper.playSoundClient(SoundsAS.GUI_JOURNAL_PAGE, 1.0f, 1.0f);
            AstralSorcery.getProxy().openGui(player, GuiType.CONSTELLATION_PAPER, this.getConstellation(held));
        }
        return (InteractionResult<ItemStack>)InteractionResult.func_226248_a_((Object)held);
    }
    
    public boolean hasCustomEntity(final ItemStack stack) {
        return true;
    }
    
    @Nullable
    public Entity createEntity(final Level world, final Entity location, final ItemStack itemstack) {
        final EntityItemExplosionResistant res = new EntityItemExplosionResistant(EntityTypesAS.ITEM_EXPLOSION_RESISTANT, world, location.getX(), location.getY(), location.getZ(), itemstack);
        res.func_70020_e(location.func_189511_e(new CompoundTag()));
        if (itemstack.getItem() instanceof ItemConstellationPaper) {
            final IConstellation cst = this.getConstellation(itemstack);
            if (cst != null) {
                res.applyColor(cst.getConstellationColor());
            }
        }
        if (location instanceof ItemEntity) {
            res.setReplacedEntity((ItemEntity)location);
        }
        return (Entity)res;
    }
    
    public void func_77663_a(final ItemStack stack, final Level world, final Entity entity, final int slot, final boolean isSelected) {
        if (world.isClientSide || !(entity instanceof Player)) {
            return;
        }
        IConstellation cst = this.getConstellation(stack);
        if (cst == null) {
            final PlayerProgress progress = ResearchHelper.getProgress((Player)entity, LogicalSide.SERVER);
            final List<IConstellation> constellations = new ArrayList<IConstellation>();
            for (final IConstellation c : ConstellationRegistry.getAllConstellations()) {
                if (c.canDiscover((Player)entity, progress)) {
                    constellations.add(c);
                }
            }
            for (final ResourceLocation strConstellation : progress.getKnownConstellations()) {
                final IConstellation c2 = ConstellationRegistry.getConstellation(strConstellation);
                if (c2 != null) {
                    constellations.remove(c2);
                }
            }
            for (final ResourceLocation strConstellation : progress.getSeenConstellations()) {
                final IConstellation c2 = ConstellationRegistry.getConstellation(strConstellation);
                if (c2 != null) {
                    constellations.remove(c2);
                }
            }
            final IConstellation constellation = MiscUtils.getRandomEntry(constellations, world.field_73012_v);
            if (constellation != null) {
                this.setConstellation(stack, constellation);
            }
        }
        cst = this.getConstellation(stack);
        if (cst != null) {
            final PlayerProgress progress = ResearchHelper.getProgress((Player)entity, LogicalSide.SERVER);
            boolean has = false;
            for (final ResourceLocation strConstellation : progress.getSeenConstellations()) {
                final IConstellation c2 = ConstellationRegistry.getConstellation(strConstellation);
                if (c2 != null && c2.equals(cst)) {
                    has = true;
                    break;
                }
            }
            if (!has && cst.canDiscover((Player)entity, progress) && ResearchManager.memorizeConstellation(cst, (Player)entity)) {
                ResearchHelper.sendConstellationMemorizationMessage((CommandSource)entity, progress, cst);
            }
        }
        super.func_77663_a(stack, world, entity, slot, isSelected);
    }
    
    @OnlyIn(Dist.CLIENT)
    public int getColor(final ItemStack stack, final int tintIndex) {
        if (tintIndex != 1) {
            return -1;
        }
        final IConstellation c = this.getConstellation(stack);
        if (c != null && ResearchHelper.getClientProgress().hasConstellationDiscovered(c)) {
            return 0xFF000000 | c.getConstellationColor().getRGB();
        }
        return -10921639;
    }
    
    @Nullable
    public IConstellation getConstellation(final ItemStack stack) {
        return IConstellation.readFromNBT(NBTHelper.getPersistentData(stack));
    }
    
    public boolean setConstellation(final ItemStack stack, @Nullable final IConstellation constellation) {
        constellation.writeToNBT(NBTHelper.getPersistentData(stack));
        return true;
    }
}
