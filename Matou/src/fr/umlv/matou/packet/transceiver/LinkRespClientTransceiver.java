package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetLong;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.writeAll;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.LinkRespClientPacket;

public class LinkRespClientTransceiver implements PacketTransceiver<LinkRespClientPacket> {

	@Override
	public LinkRespClientPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		long token = readAndGetLong(sc, rBuff);
		return new LinkRespClientPacket(token);
	}

	@Override
	public void send(SocketChannel sc, LinkRespClientPacket packet) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES + Long.BYTES);
		bb.putInt(packet.getType().opCode()).putLong(packet.getToken());
		bb.flip();
		writeAll(sc, bb);
	}	
}
