package com.zby.chapter2.bio.processer;

import java.net.Socket;

public interface CommandProcesser {

	String BAD_REQUEST = "bad request";
	String OK_RESPONSE = "ok";

	boolean support(String command);

	String process(Socket socket, String command);

	String processOption();
}
