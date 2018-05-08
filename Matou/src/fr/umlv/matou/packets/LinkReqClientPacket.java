package fr.umlv.matou.packets;

public class LinkReqClientPacket implements Packet {
	private final String dstPseudo;
	private final int port;
	private final long token;

	public LinkReqClientPacket(String dstPseudo, int port, long token) {
		this.dstPseudo = dstPseudo;
		this.port = port;
		this.token = token;
	}

	public String getDstPseudo() {
		return dstPseudo;
	}

	public int getPort() {
		return port;
	}

	public long getToken() {
		return token;
	}

	@Override
	public PacketType getType() {
		return PacketType.LINK_REQ_CLIENT;
	}
}
