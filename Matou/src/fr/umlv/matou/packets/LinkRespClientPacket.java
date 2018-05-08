package fr.umlv.matou.packets;

public class LinkRespClientPacket implements Packet {
	private final long token;

	public LinkRespClientPacket(long token) {
		this.token = token;
	}

	@Override
	public PacketType getType() {
		return PacketType.LINK_RESP_CLIENT;
	}

	public long getToken() {
		return token;
	}
}
