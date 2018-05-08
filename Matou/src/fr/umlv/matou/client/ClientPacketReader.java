package fr.umlv.matou.client;

import static fr.umlv.matou.packets.PacketType.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import fr.umlv.matou.exceptions.MalformedPacketException;
import fr.umlv.matou.packets.ConRespServerPacket;
import fr.umlv.matou.packets.LinkRespClientPacket;

/**
 * TO FINISH
 * This interface provides method to read packet and return the corresponding packet object.
 * There is one method for each Matou packet type. ByteBuffer is formed as described in the Matou RFC.
 * ByteBuffer MUST have enough remaining space to write the response.
 * Methods may block if there is no timeout when read on sockets.
 * @author v.vivier
 */
public interface ClientPacketReader {
	final static Charset UTF8 = Charset.forName("utf-8");

	
	
	/**
	 * 
	 * @param sc
	 * @param rBuff
	 * @return
	 * @throws IOException
	 */
	public static ConRespServerPacket readConRespServer(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		readInt(sc, rBuff);
		rBuff.flip();
		int pseudoSize;
		if((pseudoSize = rBuff.getInt()) < 0) throw new MalformedPacketException();
		if(rBuff.remaining() < pseudoSize) {
			rBuff.compact();
			readAll(sc, rBuff, pseudoSize);
			rBuff.flip();
		}
		String pseudo = UTF8.decode(rBuff).toString();
		if(rBuff.remaining() < Integer.BYTES) {
			rBuff.compact();
			readAll(sc, rBuff, pseudoSize);
			rBuff.flip();
		}
		return new ConRespServerPacket(pseudo, rBuff.getInt());
	}
	
	/**
	 * 
	 * @param sc
	 * @param rBuff
	 * @return
	 * @throws IOException
	 */
	public static LinkRespClientPacket readLinkRespClient(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		rBuff.clear();
		readLong(sc, rBuff);
		rBuff.flip();
		return new LinkRespClientPacket(rBuff.getLong());
	}
}
