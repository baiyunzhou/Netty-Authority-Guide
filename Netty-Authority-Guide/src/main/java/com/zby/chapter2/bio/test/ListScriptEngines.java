package com.zby.chapter2.bio.test;

import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;

/**
 * 
 * @author zby
 * @date 2018年11月2日
 * @Description 获取当前JDK支持的脚本引擎
 */
public class ListScriptEngines {
	public static void main(String args[]) {
		ScriptEngineManager manager = new ScriptEngineManager();

		List<ScriptEngineFactory> factories = manager.getEngineFactories();

		for (ScriptEngineFactory factory : factories) {
			System.out.printf(
					"Name: %s%n" + "Version: %s%n" + "Language name: %s%n" + "Language version: %s%n" + "Extensions: %s%n"
							+ "Mime types: %s%n" + "Names: %s%n",
					factory.getEngineName(), factory.getEngineVersion(), factory.getLanguageName(), factory.getLanguageVersion(),
					factory.getExtensions(), factory.getMimeTypes(), factory.getNames());
			ScriptEngine engine = factory.getScriptEngine();
			System.out.println(engine);
		}
	}
}