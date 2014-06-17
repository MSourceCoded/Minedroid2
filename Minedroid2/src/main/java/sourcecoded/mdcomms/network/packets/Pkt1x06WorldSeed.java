package sourcecoded.mdcomms.network.packets;

import sourcecoded.mdcomms.network.SCSide;
import sourcecoded.mdcomms.network.SourceCommsPacketHandler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Pkt1x06WorldSeed implements ISourceCommsPacket {

	public long seed;

	public Pkt1x06WorldSeed() {}

	public Pkt1x06WorldSeed(long theSeed) {
        this.seed = theSeed;
	}
	
	@Override
	public void encode(DataOutputStream data) throws IOException {
        data.writeLong(seed);
	}

	@Override
    public void decode(DataInputStream data) throws IOException {
        seed = data.readLong();
    }

    @Override
    public void executeAfter(SCSide side) {
    }

}
