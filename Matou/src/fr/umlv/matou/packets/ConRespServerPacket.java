package fr.umlv.matou.packets;

import java.util.Objects;

public class ConRespServerPacket implements Packet {
	private final String pseudo;
	private final int flag;
	
	public ConRespServerPacket(String pseudo, int flag) {
		this.pseudo = Objects.requireNonNull(pseudo);
		this.flag = flag;
	}
	
	public String getPseudo() {
		return pseudo;
	}
	
	public int getFlag() {
		return flag;
	}
	
	@Override
	public PacketType getType() {
		return PacketType.CON_RESP_SERVER;
	}
}
