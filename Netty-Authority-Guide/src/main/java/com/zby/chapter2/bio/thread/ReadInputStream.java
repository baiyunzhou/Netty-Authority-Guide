package com.zby.chapter2.bio.thread;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

public class ReadInputStream implements Runnable {
	private InputStream inputStream;

	public ReadInputStream(InputStream inputStream) {
		Objects.requireNonNull(inputStream, "inputStream must not be null");
		this.inputStream = inputStream;
	}

	@Override
	public void run() {
		System.out.println("开启read thread");
		BufferedReader bufferedReader = null;
		try {
			while (true) {
				bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String read = bufferedReader.readLine();
				if (null == read || read.isEmpty()) {
					continue;
				}
				System.out.println(">" + read);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("正在关闭bufferedReader");
				bufferedReader.close();
			} catch (Exception e) {
				System.err.println("关闭bufferedReader出错");
				e.printStackTrace();
			}
		}

	}

}
