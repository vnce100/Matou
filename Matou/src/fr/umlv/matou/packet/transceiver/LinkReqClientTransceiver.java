package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.LinkReqClientPacket;

public class LinkReqClientTransceiver implements PacketTransceiver<LinkReqClientPacket> {

	@Override
	public LinkReqClientPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		String pseudo = readAndGetSizedUTF8String(sc, rBuff);
		int port = readAndGetInt(sc, rBuff);
		long token = readAndGetLong(sc, rBuff);
		return new LinkReqClientPacket(pseudo, port, token);
	}

	@Override
	public void send(SocketChannel sc, LinkReqClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getDstPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(3*Integer.BYTES + pseudoSize + Long.BYTES);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo).putInt(packet.getPort()).putLong(packet.getToken());
		bb.flip();
		writeAll(sc, bb);
	}	
}
