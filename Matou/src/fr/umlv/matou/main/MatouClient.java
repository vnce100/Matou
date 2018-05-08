package fr.umlv.matou.main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Logger;

import fr.umlv.matou.User;
import fr.umlv.matou.exceptions.MalformedPseudoException;
import fr.umlv.matou.packet.transceiver.PacketTransceiver;
import fr.umlv.matou.packets.ConReqClientPacket;
import fr.umlv.matou.packets.ConRespServerPacket;
import fr.umlv.matou.packets.Packet;
import fr.umlv.matou.packets.PacketType;
import fr.umlv.matou.utils.MatouScanner;

public class MatouClient {
	private final static Logger logger = Logger.getLogger(MatouClient.class.getName());
	private final static int TIMEOUT = 3000;

	private final InetSocketAddress server;
	private final ByteBuffer rBuff = ByteBuffer.allocateDirect(4096);
	private boolean connected;
	private HashMap<Integer, PacketTransceiver<? extends Packet>> readers;
	private SocketChannel sc;
	private User user;

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			usage();
			return;
		}
		InetSocketAddress server = new InetSocketAddress(args[0], Integer.valueOf(args[1]));
		MatouClient client = new MatouClient(server);
		client.launch();
	}

	private static void usage() {
		System.out.println("usage : MatouClient [server] [port]");
	}

	/**
	 * 
	 * @param server
	 */
	public MatouClient(InetSocketAddress server) {
		this.server = Objects.requireNonNull(server);
	}

	/**
	 * 
	 */
	public void launch() {
		logger.info("launch(): Trying to reach server");
		try (SocketChannel sc = SocketChannel.open(server)) {
			logger.info("launch(): Server reached");
			this.sc = sc;
			sc.socket().setSoTimeout(TIMEOUT);
			readers = initReaders();
			/* ########### MAIN LOOP ########### */
			while (true) {
				while (user == null)
					getUser();
				connect();
				if (connected == false)
					break;
				while (connected) {
					String commandLine = MatouScanner.scanLine(System.in);
					String[] tokens = commandLine.trim().split(" ", 2);
					switch (tokens[0]) {
					case "/l":
						link(tokens[1]);
						break;
					default:
						// Sauf si default = c'est un message pour tous
						System.out.println("Unknown command " + tokens[0]);
					}
				}
			}
			logger.info("Client disconnected");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Demande le pseudo à l'utilisateur et créé un objet User correspondant
	 */
	private void getUser() {
		System.out.print("Pseudo: ");
		String pseudo = MatouScanner.scanLine(System.in);
		try {
			this.user = new User(pseudo);
		} catch (MalformedPseudoException e) {
			System.out.println("Malformed pseudo");
		}
	}

	/**
	 * Essaye de se connecter auprès du serveur
	 * 
	 * @throws IOException
	 */
	private void connect() throws IOException {
		logger.info("Trying to connect as " + user.getPseudo());
		ClientPacketSender.sendConReqClient(sc, new ConReqClientPacket(user.getPseudo()));
		ConRespServerPacket response = ClientPacketReader.readConRespServer(sc, rBuff);
		if (response.getFlag() != 0 || !response.getPseudo().equals(user.getPseudo())) {
			logger.info("Connection refused");
			return;
		}
		logger.info("Connection accepted");
		connected = true;
	}

	/**
	 * 
	 * @param argsLine
	 */
	private void link(String argsLine) {
		String[] args = parseArgs(argsLine);
		if (args.length != 1)
			System.out.println("usage(): /l [destPseudo]");
		logger.info("Trying to link " + args[0]);
		// TODO continue
	}

	/**
	 * 
	 * @param args
	 * @return
	 */
	private static String[] parseArgs(String args) {
		return args.trim().split(" ");
	}

	private HashMap<Integer, PacketTransceiver<? extends Packet>> initReaders() throws IOException {
		HashMap<Integer, PacketTransceiver<? extends Packet>> hm = new HashMap<>();
		hm.put(PacketType.CON_REQ_CLIENT.opCode(), new ConReqClientReader());
		hm.put(PacketType.DATA_FILE_CLIENT.opCode(), new DataFileCLientReader());
		hm.put(PacketType.DATA_MSG_CLIENT.opCode(), new DataMsgClientReader());
		hm.put(PacketType.DATA_MSG_SERVER.opCode(), new DataMsgServerReader());
		hm.put(PacketType.DECON_ACK_SERVER.opCode(), new DeconAckServerReader());
		hm.put(PacketType.DECON_REQ_CLIENT.opCode(), new DeconReqClientReader());
		hm.put(PacketType.LINK_REQ_CLIENT.opCode(), new LinkReqClientrReader());
		hm.put(PacketType.LINK_REQ_SERVER.opCode(), new LinkReqServerReader());
		hm.put(PacketType.LINK_RESP_CLIENT.opCode(), new LinkRespClientReader());
		return hm;
	}
}