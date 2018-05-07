package fr.umlv.matou;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class MatouClient {
	
	public static void main(String[] args) {
		if(args.length != 2) {
			usage();
			return;
		}
		
		InetSocketAddress server = new InetSocketAddress(args[0], Integer.valueOf(args[1]));
		System.out.print("Trying to reach server... ");
		try(SocketChannel sc = SocketChannel.open(server)) {
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void usage() {
		System.out.println("usage : MatouClient [server] [port]");
	}
}
