package com.zby.chapter2.bio.processer.impl;

import java.net.Socket;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.zby.chapter2.bio.processer.CommandProcesser;

public class JavaScriptCommandProcesser implements CommandProcesser {
	public static final String JS_PREFIX = "js:";
	public static final String BAD_SCRIPT = "Bad Script";
	private ScriptEngine scriptEngine;

	public JavaScriptCommandProcesser() {
		scriptEngine = new ScriptEngineManager().getEngineByName("JS");
	}

	@Override
	public boolean support(String command) {
		if (null == command) {
			return false;
		}
		if (command.startsWith(JS_PREFIX)) {
			return true;
		}
		return false;
	}

	@Override
	public String process(Socket socket, String command) {
		String response = null;
		try {
			Object result = scriptEngine.eval(command);
			if (null != result) {
				response = result.toString();
			} else {
				response = OK_RESPONSE;
			}
		} catch (ScriptException e) {
			response = BAD_SCRIPT;
		}
		return response;
	}

	@Override
	public String processOption() {
		return JS_PREFIX + "JS脚本";
	}

}
