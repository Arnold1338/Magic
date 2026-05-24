package hellfirepvp.astralsorcery.common.network.channel;

import net.minecraftforge.fml.network.simple.SimpleChannel;

public class BufferedReplyChannel extends SimpleSendChannel
{
    public BufferedReplyChannel(final SimpleChannel channel) {
        super(channel);
    }
}
