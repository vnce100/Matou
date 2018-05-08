package fr.umlv.matou.packets;

public class LinkRespServerPacket implements Packet {
	private final int flag;
	
	public LinkRespServerPacket(int flag) {
		this.flag = flag;
	}
	
	@Override
	public PacketType getType() {
		return PacketType.LINK_RESP_SERVER;
	}

	public int getFlag() {
		return flag;
	}

}
