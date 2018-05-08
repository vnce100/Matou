package fr.umlv.matou.packets;

public class DataFileClientPacket implements Packet {
	private final String srcPseudo;
	private final String filename;
	private final int fragmentation;
	private final byte[] content;
	
	public DataFileClientPacket(String srcPseudo, String filename, int fragmentation, byte[] content) {
		this.srcPseudo = srcPseudo;
		this.filename = filename;
		this.fragmentation = fragmentation;
		this.content = content;
	}

	public String getSrcPseudo() {
		return srcPseudo;
	}

	public String getFilename() {
		return filename;
	}

	public int getFragmentation() {
		return fragmentation;
	}

	public byte[] getContent() {
		return content;
	}

	@Override
	public PacketType getType() {
		return PacketType.DATA_FILE_CLIENT;
	}
}
