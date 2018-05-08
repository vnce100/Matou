package fr.umlv.matou.packet.transceiver;

import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readAll;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readInt;
import static fr.umlv.matou.packet.transceiver.PacketTransceiver.readLong;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

import fr.umlv.matou.exceptions.MalformedPacketException;

/**
 * This interface provides methods to read or write Packet on a SocketChannel.
 * @author v.vivier
 *
 * @param <T> is the exact type of the Packet a class which implements this interface can treat.
 */
public interface PacketTransceiver<T> {
	final static Charset UTF8 = Charset.forName("utf-8");
	
	/**
	 * Read bytes coming on a SocketChannel as a Packet.
	 * OPCODE must already by read before calling read method.
	 * @param sc is the SocketChannel on which the method try to read.
	 * @param rBuff is the receiving buffer which must be already allocated by the caller. This buffer must be large enough to contain the packet.
	 * @return the packet read.
	 */
	public T read(SocketChannel sc, ByteBuffer rBuff) throws IOException;
	
	/**
	 * Read a packet and send it on the SocketChannel.
	 * @param sc is the SocketChannel on which the method try to write.
	 * @param packet is the packet to send.
	 * @throws IOException
	 */
	public void send(SocketChannel sc, T packet) throws IOException;
	
	/**
	 * Ensure buffer contain at least the size of an int or try to read it.
	 * @param sc
	 * @param bb
	 * @throws IOException
	 */
	public static void readInt(SocketChannel sc, ByteBuffer bb) throws IOException {
		while(bb.capacity() - bb.remaining() < Integer.BYTES) {
			if(sc.read(bb) == 1) {
				throw new IllegalStateException("readInt(): Unexpected closed channel");
			}
		}
	}
	
	/**
	 * 
	 * @param sc
	 * @param rBuff
	 * @return
	 * @throws IOException
	 */
	public static int readAndGetInt(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		if(rBuff.remaining() < Integer.BYTES) {
			rBuff.compact();
			readInt(sc, rBuff);
			rBuff.flip();
		}
		int value = rBuff.getInt();
		return value;
	}
	
	/**
	 * 
	 * @param sc
	 * @param bb
	 * @throws IOException
	 */
	public static void readLong(SocketChannel sc, ByteBuffer bb) throws IOException {
		while(bb.capacity() - bb.remaining() < Long.BYTES) {
			if(sc.read(bb) == 1) {
				throw new IllegalStateException("readInt(): Unexpected closed channel");
			}
		}
	}
	
	/**
	 * 
	 * @param sc
	 * @param rBuff
	 * @return
	 * @throws IOException
	 */
	public static long readAndGetLong(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		if(rBuff.remaining() < Long.BYTES) {
			rBuff.compact();
			readLong(sc, rBuff);
			rBuff.flip();
		}
		long value = rBuff.getLong();
		return value;
	}
	
	/**
	 * Ensure buffer contain at least n bytes or try to read it.
	 * @param sc
	 * @param bb
	 * @param n
	 * @throws IOException
	 */
	public static void readAll(SocketChannel sc, ByteBuffer bb, int n) throws IOException {
		while(bb.capacity() - bb.remaining() < n) {
			if(sc.read(bb) == 1) {
				throw new IllegalStateException("readInt(): Unexpected closed channel");
			}
		}
	}
	
	/**
	 * 
	 * @param sc
	 * @param bb
	 * @return
	 * @throws IOException
	 */
	public static byte[] readAndGetSizedBytes(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		if(rBuff.remaining() < Integer.BYTES) {
			rBuff.compact();
			readInt(sc, rBuff);
			rBuff.flip();
		}
		int bytesSize;
		if((bytesSize = rBuff.getInt()) < 0) throw new MalformedPacketException();
		if(rBuff.remaining() < bytesSize) {
			rBuff.compact();
			readAll(sc, rBuff, bytesSize);
			rBuff.flip();
		}
		byte[] bBytes = new byte[bytesSize];
		rBuff.get(bBytes);
		return bBytes;
	}
	
	/**
	 * 
	 * @param sc
	 * @param bb
	 * @throws IOException
	 */
	public static String readAndGetSizedUTF8String(SocketChannel sc, ByteBuffer rBuff) throws IOException {
		if(rBuff.remaining() < Integer.BYTES) {
			rBuff.compact();
			readInt(sc, rBuff);
			rBuff.flip();
		}
		int stringSize;
		if((stringSize = rBuff.getInt()) < 0) throw new MalformedPacketException();
		if(rBuff.remaining() < stringSize) {
			rBuff.compact();
			readAll(sc, rBuff, stringSize);
			rBuff.flip();
		}
		byte[] bString = new byte[stringSize];
		rBuff.get(bString);
		return UTF8.decode(ByteBuffer.wrap(bString)).toString();
	}
	
	/**
	 * 
	 * @param sc
	 * @param bb
	 * @param n
	 * @throws IOException
	 */
	public static void writeAll(SocketChannel sc, ByteBuffer bb) throws IOException {
		while(bb.hasRemaining()) {
			if(sc.write(bb) == -1) {
				throw new IllegalStateException("readInt(): Unexpected closed channel");
			}
		}
	}
	
	/**
	 * 
	 * @param sc
	 * @return
	 * @throws IOException
	 */
	public static int readOpCode(SocketChannel sc) throws IOException {
		ByteBuffer bb = ByteBuffer.allocate(Integer.BYTES);
		readInt(sc, bb);
		return bb.flip().getInt();
	}
}
