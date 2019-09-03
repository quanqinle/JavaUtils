/**
 * 
 */
package com.github.quanqinle.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @author quanql
 *
 */
public class OSUtil {

	/**
	 * 
	 */
	public OSUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * run system command line.
	 * @param command ,like the following:
	 * 		List<String> command = new ArrayList<String>();
	 * 		command.add("ffmpeg.exe");
	 * 		command.add("-i"); 
	 */
	public static void runCmd(List<String> command) {
//		command::forEach(System.out::print);
		System.out.println("command: " + command.toString());
		ProcessBuilder builder = new ProcessBuilder();
		builder.command(command);
		// 正常信息、错误信息合并输出
		builder.redirectErrorStream(true);
		Process process = null;
		try {
			// 开始执行命令
			process = builder.start();
			// 获取执行结果
			StringBuffer sbf = new StringBuffer();
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				sbf.append(line);
				sbf.append("\n");
				System.out.println(line);
			}
//			String result = sbf.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (process != null) {
				process.destroy();
			}
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
