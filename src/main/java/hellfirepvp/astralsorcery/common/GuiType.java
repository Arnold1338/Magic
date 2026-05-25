package hellfirepvp.astralsorcery.common;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import hellfirepvp.astralsorcery.client.screen.ScreenHandTelescope;
import hellfirepvp.astralsorcery.client.screen.ScreenTelescope;
import hellfirepvp.astralsorcery.common.tile.TileTelescope;
import hellfirepvp.astralsorcery.client.screen.ScreenRefractionTable;
import net.minecraft.world.level.BlockGetter;
import hellfirepvp.astralsorcery.common.util.MiscUtils;
import hellfirepvp.astralsorcery.common.tile.TileRefractionTable;
import hellfirepvp.astralsorcery.client.screen.journal.ScreenJournalProgression;
import hellfirepvp.astralsorcery.client.screen.ScreenConstellationPaper;
import net.minecraft.resources.ResourceLocation;
import hellfirepvp.astralsorcery.common.lib.RegistriesAS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import hellfirepvp.astralsorcery.common.util.nbt.NBTHelper;
import net.minecraft.core.BlockPos;
import hellfirepvp.astralsorcery.common.constellation.IConstellation;
import net.minecraft.nbt.CompoundTag;

public enum GuiType
{
    CONSTELLATION_PAPER, 
    TOME, 
    REFRACTION_TABLE, 
    TELESCOPE, 
    HAND_TELESCOPE;
    
    public CompoundTag serializeArguments(final Object[] data) {
        try {
            final CompoundTag nbt = new CompoundTag();
            switch (this) {
                case CONSTELLATION_PAPER: {
                    nbt.putString("cst", ((IConstellation)data[0]).getRegistryName().toString());
                    break;
                }
                case REFRACTION_TABLE:
                case TELESCOPE: {
                    NBTHelper.writeBlockPosToNBT((BlockPos)data[0], nbt);
                    break;
                }
            }
            return nbt;
        }
        catch (final Exception exc) {
            throw new IllegalArgumentException("Invalid Arguments for GuiType: " + this.name(), exc);
        }
    }
    
    @Nullable
    @OnlyIn(Dist.CLIENT)
    public Screen deserialize(final CompoundTag data) {
        final Level clWorld = (Level)Minecraft.getInstance().level;
        final Player clPlayer = (Player)Minecraft.getInstance().player;
        if (clWorld == null || clPlayer == null) {
            return null;
        }
        try {
            switch (this) {
                case CONSTELLATION_PAPER: {
                    return new ScreenConstellationPaper((IConstellation)RegistriesAS.REGISTRY_CONSTELLATIONS.getValue(new ResourceLocation(data.getString("cst"))));
                }
                case TOME: {
                    return ScreenJournalProgression.getOpenJournalInstance();
                }
                case REFRACTION_TABLE: {
                    final BlockPos at = NBTHelper.readBlockPosFromNBT(data);
                    final TileRefractionTable refractionTable = MiscUtils.getTileAt((IBlockReader)clWorld, at, TileRefractionTable.class, true);
                    if (refractionTable != null) {
                        return new ScreenRefractionTable(refractionTable);
                    }
                    return null;
                }
                case TELESCOPE: {
                    final BlockPos at = NBTHelper.readBlockPosFromNBT(data);
                    final TileTelescope telescope = MiscUtils.getTileAt((IBlockReader)clWorld, at, TileTelescope.class, true);
                    if (telescope != null) {
                        return new ScreenTelescope(telescope);
                    }
                    return null;
                }
                case HAND_TELESCOPE: {
                    return new ScreenHandTelescope();
                }
                default: {
                    throw new IllegalArgumentException("Unknown GuiType: " + this.name());
                }
            }
        }
        catch (final Exception exc) {
            throw new IllegalArgumentException("Invalid Arguments for GuiType: " + this.name(), exc);
        }
    }
}
