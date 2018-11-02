package com.zby.chapter2.bio.thread;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Scanner;

public class WriteOutputStream implements Runnable {
	private PrintWriter printWriter;
	private Scanner scanner = new Scanner(System.in);

	public WriteOutputStream(OutputStream outputStream) {
		Objects.requireNonNull(outputStream, "outputStream must not be null");
		this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);
	}

	@Override
	public void run() {
		System.out.println("开启write thread");
		try {
			while (true) {
				String line = scanner.nextLine();
				if (null == line || line.isEmpty()) {
					continue;
				}
				printWriter.println(line);
				System.out.println("发送请求数据：" + line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				System.out.println("正在关闭printWriter");
				printWriter.close();
			} catch (Exception e) {
				System.err.println("关闭printWriter出错");
				e.printStackTrace();
			}
			try {
				System.out.println("正在关闭scanner");
				scanner.close();
			} catch (Exception e) {
				System.err.println("关闭scanner出错");
				e.printStackTrace();
			}
		}

	}

}
