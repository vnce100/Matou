package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetInt;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedBytes;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedUTF8String;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.DataFileClientPacket;

public class DataFileClientTransceiver implements PacketTransceiver<DataFileClientPacket> {

	@Override
	public DataFileClientPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		String srcPseudo = readAndGetSizedUTF8String(sc, rBuff);
		String filename = readAndGetSizedUTF8String(sc, rBuff);
		int fragmentation = readAndGetInt(sc, rBuff);
		byte[] data = readAndGetSizedBytes(sc, rBuff);
		return new DataFileClientPacket(srcPseudo, filename, fragmentation, data);
	}

	@Override
	public void send(SocketChannel sc, DataFileClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getSrcPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bbFilename = UTF8.encode(packet.getFilename());
		int filenameSize = bbFilename.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(5 * Integer.BYTES + pseudoSize + filenameSize + packet.getContent().length);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo).putInt(filenameSize).put(bbFilename).putInt(packet.getFragmentation()).putInt(packet.getContent().length).put(packet.getContent());
		bb.flip();
		PacketTransceiver.writeAll(sc, bb);
	}
	
}
