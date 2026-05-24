package hellfirepvp.astralsorcery.common.network.base;

import hellfirepvp.astralsorcery.common.network.login.client.PktLoginAcknowledge;
import hellfirepvp.astralsorcery.common.network.PacketChannel;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.IntSupplier;

public abstract class ASLoginPacket<T extends ASLoginPacket<T>> extends ASPacket<T> implements IntSupplier
{
    private int loginIndex;
    
    public int getLoginIndex() {
        return this.loginIndex;
    }
    
    @Override
    public int getAsInt() {
        return this.getLoginIndex();
    }
    
    public void setLoginIndex(final int loginIndex) {
        this.loginIndex = loginIndex;
    }
    
    protected final void acknowledge(final NetworkEvent.Context ctx) {
        PacketChannel.CHANNEL.reply(new PktLoginAcknowledge(), ctx);
    }
}
