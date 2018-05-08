package fr.umlv.matou.packets;

public class DataMsgServerPacket implements Packet {
	private final String srcPseudo;
	private final String message;
	
	public DataMsgServerPacket(String srcPseudo, String message) {
		this.srcPseudo = srcPseudo;
		this.message = message;
	}

	public String getSrcPseudo() {
		return srcPseudo;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public PacketType getType() {
		return PacketType.DATA_MSG_SERVER;
	}
}
