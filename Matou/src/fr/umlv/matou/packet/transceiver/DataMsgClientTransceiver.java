package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAndGetSizedUTF8String;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import fr.umlv.matou.packets.DataMsgClientPacket;

public class DataMsgClientTransceiver implements PacketTransceiver<DataMsgClientPacket> {

	@Override
	public DataMsgClientPacket read(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		String pseudo = readAndGetSizedUTF8String(sc, rBuff);
		String message = readAndGetSizedUTF8String(sc, rBuff);
		return new DataMsgClientPacket(pseudo, message);
	}

	@Override
	public void send(SocketChannel sc, DataMsgClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getSrcPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bbMessage = UTF8.encode(packet.getMessage());
		int messageSize = bbMessage.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(3 * Integer.BYTES + pseudoSize + messageSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo).putInt(messageSize).put(bbMessage);
		bb.flip();
		PacketTransceiver.writeAll(sc, bb);
	}	
}
