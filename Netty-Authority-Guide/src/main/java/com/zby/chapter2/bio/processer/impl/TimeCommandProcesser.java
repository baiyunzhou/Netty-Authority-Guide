package com.zby.chapter2.bio.processer.impl;

import java.net.Socket;
import java.util.Date;

import com.zby.chapter2.bio.processer.CommandProcesser;

public class TimeCommandProcesser implements CommandProcesser {
	public static final String QUERY_TIME = "query time";

	@Override
	public boolean support(String command) {
		if (QUERY_TIME.equalsIgnoreCase(command)) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String process(Socket socket, String command) {
		return new Date().toLocaleString();
	}

	@Override
	public String processOption() {
		return QUERY_TIME;
	}

}
