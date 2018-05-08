package fr.umlv.matou.packets;

import java.util.Objects;

public class ConReqClientPacket implements Packet{
private final String pseudo;
	
	public ConReqClientPacket(String pseudo) {
		this.pseudo = Objects.requireNonNull(pseudo);
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
	@Override
	public PacketType getType() {
		return PacketType.CON_REQ_CLIENT;
	}
}
