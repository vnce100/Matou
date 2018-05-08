package fr.umlv.matou.utils;

import static fr.umlv.matou.packets.PacketType.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import fr.umlv.matou.exceptions.MalformedPacketException;
import fr.umlv.matou.packets.ConRespServerPacket;

/**
 * TO FINISH
 * This interface provides method to read packet and return the corresponding packet object.
 * There is one method for each Matou packet type. ByteBuffer is formed as described in the Matou RFC.
 * ByteBuffer MUST have enough remaining space to write the response.
 * Methods may block if there is no timeout when read on sockets.
 * @author v.vivier
 */
public interface PacketReaderClient {
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
		if(rBuff.getInt() != CON_RESP_SERVER.opCode()) throw new MalformedPacketException();
		if(rBuff.remaining() < Integer.BYTES) {
			rBuff.compact();
			readInt(sc, rBuff);
			rBuff.flip();
		}
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
	 * Ensure buffer contain at least the size of an int or try to read it.
	 * @param sc
	 * @param bb
	 * @throws IOException
	 */
	private static void readInt(SocketChannel sc, ByteBuffer bb) throws IOException {
		while(bb.capacity() - bb.remaining() < Integer.BYTES) {
			if(sc.read(bb) == 1) {
				throw new IllegalStateException("readInt(): Unexpected closed channel");
			}
		}
	}
	
	/**
	 * Ensure buffer contain at least n bytes or try to read it.
	 * @param sc
	 * @param bb
	 * @param n
	 * @throws IOException
	 */
	private static void readAll(SocketChannel sc, ByteBuffer bb, int n) throws IOException {
		while(bb.capacity() - bb.remaining() < n) {
			if(sc.read(bb) == 1) {
				throw new IllegalStateException("readInt(): Unexpected closed channel");
			}
		}
	}
}
