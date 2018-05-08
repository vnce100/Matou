package fr.umlv.matou.packets;

import java.net.InetAddress;

public class LinkReqServerPacket implements Packet {
	private final String dstPseudo;
	private final int port;
	private final long token;
	private final String srcPseudo;
	private final InetAddress srcIp;

	public LinkReqServerPacket(String dstPseudo, int port, long token, String srcPseudo, InetAddress srcIp) {
		this.dstPseudo = dstPseudo;
		this.port = port;
		this.token = token;
		this.srcPseudo = srcPseudo;
		this.srcIp = srcIp;
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

	public String getSrcPseudo() {
		return srcPseudo;
	}

	public InetAddress getSrcIp() {
		return srcIp;
	}
	
	@Override
	public PacketType getType() {
		return PacketType.LINK_REQ_SERVER;
	}
}
