package fr.umlv.matou.packets;

public class DeconAckServerPacket implements Packet {
private final String pseudo;
	
	public DeconAckServerPacket(String pseudo) {
		this.pseudo = pseudo;
	}

	public String getPseudo() {
		return pseudo;
	}
	
	@Override
	public PacketType getType() {
		return PacketType.DECON_ACK_SERVER;
	}
}
