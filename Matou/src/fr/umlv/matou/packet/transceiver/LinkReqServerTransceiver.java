package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetInt;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetLong;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedBytes;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedUTF8String;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.writeAll;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.LinkReqServerPacket;

public class LinkReqServerTransceiver implements PacketTransceiver<LinkReqServerPacket> {

	@Override
	public LinkReqServerPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		String dstPseudo = readAndGetSizedUTF8String(sc, rBuff);
		int port = readAndGetInt(sc, rBuff);
		long token = readAndGetLong(sc, rBuff);
		String srcPseudo = readAndGetSizedUTF8String(sc, rBuff);
		InetAddress ipAddress = InetAddress.getByAddress(readAndGetSizedBytes(sc, rBuff));
		return new LinkReqServerPacket(dstPseudo, port, token, srcPseudo, ipAddress);
	}

	@Override
	public void send(SocketChannel sc, LinkReqServerPacket packet) throws IOException {
		ByteBuffer bbDstPseudo = UTF8.encode(packet.getDstPseudo());
		int dstPseudoSize = bbDstPseudo.flip().remaining();
		ByteBuffer bbSrcPseudo = UTF8.encode(packet.getSrcPseudo());
		int srcPseudoSize = bbSrcPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(3*Integer.BYTES + dstPseudoSize + Long.BYTES + srcPseudoSize + packet.getSrcIp().getAddress().length);
		bb.putInt(packet.getType().opCode()).putInt(dstPseudoSize).put(bbDstPseudo).putInt(packet.getPort()).putLong(packet.getToken()).putInt(srcPseudoSize).put(bbSrcPseudo).putInt(packet.getSrcIp().getAddress().length).put(packet.getSrcIp().getAddress());
		bb.flip();
		writeAll(sc, bb);
	}

}
