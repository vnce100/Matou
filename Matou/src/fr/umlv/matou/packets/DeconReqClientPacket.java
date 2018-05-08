package fr.umlv.matou.packets;

public class DeconReqClientPacket implements Packet {
	private final String pseudo;
	
	public DeconReqClientPacket(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPseudo() {
		return pseudo;
	}
	
	@Override
	public PacketType getType() {
		return PacketType.DECON_REQ_CLIENT;
	}
}
