package com.zby.chapter2.bio.processer.impl;

import java.net.Socket;
import java.util.concurrent.ConcurrentMap;

import com.zby.chapter2.bio.info.ClientInfo;
import com.zby.chapter2.bio.processer.CommandProcesser;

public class ClientInfoCommandProcesser implements CommandProcesser {
	public static final String CLIENT_INFO_PRIFIX = "client:";
	public static final String HELP = "help";
	public static final String QUERY_MY_ADDR = "addr";
	public static final String QUERY_MY_NAME = "name";
	public static final String QUERY_ALL_CLIENT = "all";
	public static final String SET_MY_NAME = "set name ";
	private ConcurrentMap<Socket, ClientInfo> clientInfo;

	public ClientInfoCommandProcesser(ConcurrentMap<Socket, ClientInfo> clientInfo) {
		this.clientInfo = clientInfo;
	}

	@Override
	public boolean support(String command) {
		if (null == command) {
			return false;
		}
		if (command.startsWith(CLIENT_INFO_PRIFIX)) {
			return true;
		}
		return false;
	}

	@Override
	public String process(Socket socket, String command) {
		String response = BAD_REQUEST;
		if (null == socket) {
			return response;
		}
		command = command.substring(7);
		if (command.equalsIgnoreCase(HELP)) {
			response = QUERY_MY_ADDR + "|" + QUERY_MY_NAME + "|" + QUERY_ALL_CLIENT + "|" + SET_MY_NAME;
		}
		if (command.equalsIgnoreCase(QUERY_MY_ADDR)) {
			response = socket.getRemoteSocketAddress().toString();
		}
		if (command.equalsIgnoreCase(QUERY_MY_NAME)) {
			response = clientInfo.get(socket).getName();
		}
		if (command.equalsIgnoreCase(QUERY_ALL_CLIENT)) {
			response = clientInfo.values().toString();
		}
		if (command.startsWith(SET_MY_NAME)) {
			clientInfo.get(socket).setName(command.substring(9));
			response = OK_RESPONSE;
		}
		return response;
	}

	@Override
	public String processOption() {
		return CLIENT_INFO_PRIFIX + HELP;
	}

}
