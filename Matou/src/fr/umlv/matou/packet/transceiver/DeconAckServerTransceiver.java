package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedUTF8String;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.DeconAckServerPacket;

public class DeconAckServerTransceiver implements PacketTransceiver<DeconAckServerPacket> {

	@Override
	public DeconAckServerPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		String pseudo = readAndGetSizedUTF8String(sc, rBuff);
		return new DeconAckServerPacket(pseudo);
	}

	@Override
	public void send(SocketChannel sc, DeconAckServerPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(2 * Integer.BYTES + pseudoSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo);
		bb.flip();
		PacketTransceiver.writeAll(sc, bb);
	}	
}
