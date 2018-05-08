package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetInt;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedUTF8String;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.ConRespServerPacket;

public class ConRespServerTransceiver implements PacketTransceiver<ConRespServerPacket> {

	@Override
	public ConRespServerPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		String pseudo = readAndGetSizedUTF8String(sc, rBuff);
		int flag = readAndGetInt(sc, rBuff);
		return new ConRespServerPacket(pseudo, flag);
	}

	@Override
	public void send(SocketChannel sc, ConRespServerPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(3 * Integer.BYTES + pseudoSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo).putInt(packet.getFlag());
		bb.flip();
		PacketTransceiver.writeAll(sc, bb);
	}
}
