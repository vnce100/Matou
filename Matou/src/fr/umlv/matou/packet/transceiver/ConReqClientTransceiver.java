package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedUTF8String;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.ConReqClientPacket;

public class ConReqClientTransceiver implements PacketTransceiver<ConReqClientPacket> {

	@Override
	public ConReqClientPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		String pseudo = readAndGetSizedUTF8String(sc, rBuff);
		return new ConReqClientPacket(pseudo);
	}

	@Override
	public void send(SocketChannel sc, ConReqClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(2 * Integer.BYTES + pseudoSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo);
		bb.flip();
		PacketTransceiver.writeAll(sc, bb);
	}
}
