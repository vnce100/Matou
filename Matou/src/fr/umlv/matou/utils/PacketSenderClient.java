package fr.umlv.matou.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import fr.umlv.matou.packets.ConReqClientPacket;
import fr.umlv.matou.packets.DataFileClientPacket;
import fr.umlv.matou.packets.DataMsgClientPacket;
import fr.umlv.matou.packets.DeconReqClientPacket;
import fr.umlv.matou.packets.LinkReqClientPacket;
import fr.umlv.matou.packets.LinkRespClientPacket;

/**
 * TO RECOMMENT
 * This interface provides method to create ByteBuffer ready to send.
 * There is one method for each Matou packet type. ByteBuffer is formed as described in the Matou RFC.
 * Each method returns the ByteBuffer in read mode !!!
 * @author v.vivier
 *
 */
public interface PacketSenderClient {
	final static Charset UTF8 = Charset.forName("utf-8");
	
	/**
	 * Create and return a REG REQ ByteBuffer.
	 * @param pseudo
	 * @return
	 * @throws IOException 
	 */
	public static void sendConReqClient(SocketChannel sc, ConReqClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(2*Integer.BYTES + pseudoSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo);
		bb.flip();
		writeAll(sc, bb, bb.remaining());
	}
	
	/**
	 * 
	 * @param sc
	 * @param packet
	 * @throws IOException
	 */
	public static void sendLinkReqClient(SocketChannel sc, LinkReqClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getDstPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(3*Integer.BYTES + pseudoSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo).putInt(packet.getPort());
		bb.flip();
		writeAll(sc, bb, bb.remaining());
	}
	
	/**
	 * 
	 * @param sc
	 * @param packet
	 * @throws IOException
	 */
	public static void sendLinkRespClient(SocketChannel sc, LinkRespClientPacket packet) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES + Long.BYTES);
		bb.putInt(packet.getType().opCode()).putLong(packet.getToken());
		bb.flip();
		writeAll(sc, bb, bb.remaining());
	}
	
	/**
	 * 
	 * @param sc
	 * @param packet
	 * @throws IOException
	 */
	public static void sendDataMsgClient(SocketChannel sc, DataMsgClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getSrcPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bbMessage = UTF8.encode(packet.getMessage());
		int messageSize = bbMessage.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(3*Integer.BYTES + pseudoSize + messageSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo).putInt(messageSize).put(bbMessage);
		bb.flip();
		writeAll(sc, bb, bb.remaining());
	}
	
	/**
	 * 
	 * @param sc
	 * @param packet
	 * @throws IOException
	 */
	public static void sendDataFileClient(SocketChannel sc, DataFileClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getSrcPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bbFilename = UTF8.encode(packet.getFilename());
		int filenameSize = bbFilename.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(5*Integer.BYTES + pseudoSize + filenameSize + packet.getContent().length);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo).putInt(filenameSize).put(bbFilename).putInt(packet.getFragmentation()).putInt(packet.getContent().length).put(packet.getContent());
		bb.flip();
		writeAll(sc, bb, bb.remaining());
	}
	
	/**
	 * 
	 * @param sc
	 * @param packet
	 * @throws IOException
	 */
	public static void sendDeconReqClient(SocketChannel sc, DeconReqClientPacket packet) throws IOException {
		ByteBuffer bbPseudo = UTF8.encode(packet.getPseudo());
		int pseudoSize = bbPseudo.flip().remaining();
		ByteBuffer bb = ByteBuffer.allocate(2*Integer.BYTES + pseudoSize);
		bb.putInt(packet.getType().opCode()).putInt(pseudoSize).put(bbPseudo);
		bb.flip();
		writeAll(sc, bb, bb.remaining());
	}
	
	private static void writeAll(SocketChannel sc, ByteBuffer bb, int n) throws IOException {
		while(bb.hasRemaining()) {
			if(sc.write(bb) == -1) {
				throw new IllegalStateException("readInt(): Unexpected closed channel");
			}
		}
	}
}
