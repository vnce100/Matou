package fr.umlv.matou.packets;

public enum PacketType {
	CON_REQ_CLIENT(2),
	LINK_REQ_CLIENT(3),
	LINK_RESP_CLIENT(4),
	DATA_MSG_CLIENT(5),
	DATA_FILE_CLIENT(6),
	DECON_REQ_CLIENT(7),
	CON_RESP_SERVER(102),
	LINK_REQ_SERVER(103),
	DATA_MSG_SERVER(105),
	DECON_ACK_SERVER(107);
	
	private int opCode;
	
	private PacketType(int opCode) {
		this.opCode = opCode;
	}
	
	public int opCode() {
		return opCode;
	}
}
