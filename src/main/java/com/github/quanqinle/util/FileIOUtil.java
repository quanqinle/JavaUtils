package com.github.quanqinle.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2017/06/22
 *     desc  : 文件读写相关工具类
 * </pre>
 */
public final class FileIOUtil {

	private FileIOUtil() {
		throw new UnsupportedOperationException("u can't instantiate me...");
	}

	private static final String LINE_SEP = System.getProperty("line.separator");

	private static int sBufferSize = 8192;

	/**
	 * 设置缓冲区尺寸
	 *
	 * @param bufferSize
	 *          缓冲区大小
	 */
	public static void setBufferSize(final int bufferSize) {
		sBufferSize = bufferSize;
	}
	
	/**
	 * 将输入流写入文件
	 *
	 * @param file
	 *          文件
	 * @param is
	 *          输入流
	 * @param append
	 *          是否追加在文件末
	 * @return {@code true}: 写入成功<br>
	 *         {@code false}: 写入失败
	 */
	public static boolean writeFileFromIS(final File file, final InputStream is, final boolean append) {
		if (!createOrExistsFile(file) || is == null) {
			return false;
		}
		OutputStream os = null;
		try {
			os = new BufferedOutputStream(new FileOutputStream(file, append));
			byte data[] = new byte[sBufferSize];
			int len;
			while ((len = is.read(data, 0, sBufferSize)) != -1) {
				os.write(data, 0, len);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			CloseUtils.closeIO(is, os);
		}
	}

	/**
	 * 将字节数组写入文件
	 *
	 * @param file
	 *          文件
	 * @param bytes
	 *          字节数组
	 * @param append
	 *          是否追加在文件末
	 * @return {@code true}: 写入成功<br>
	 *         {@code false}: 写入失败
	 */
	public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes, final boolean append) {
		if (bytes == null || !createOrExistsFile(file))
			return false;
		BufferedOutputStream bos = null;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file, append));
			bos.write(bytes);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			CloseUtils.closeIO(bos);
		}
	}

	/**
	 * 将字节数组写入文件
	 *
	 * @param file
	 *          文件
	 * @param bytes
	 *          字节数组
	 * @param append
	 *          是否追加在文件末
	 * @param isForce
	 *          是否写入文件
	 * @return {@code true}: 写入成功<br>
	 *         {@code false}: 写入失败
	 */
	@SuppressWarnings("resource")
	public static boolean writeFileFromBytesByChannel(final File file, final byte[] bytes, final boolean append,
	    final boolean isForce) {
		if (bytes == null)
			return false;
		FileChannel fc = null;
		try {
			fc = new FileOutputStream(file, append).getChannel();
			fc.position(fc.size());
			fc.write(ByteBuffer.wrap(bytes));
			if (isForce)
				fc.force(true);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			CloseUtils.closeIO(fc);
		}
	}

	/**
	 * 将字节数组写入文件
	 *
	 * @param file
	 *          文件
	 * @param bytes
	 *          字节数组
	 * @param append
	 *          是否追加在文件末
	 * @param isForce
	 *          是否写入文件
	 * @return {@code true}: 写入成功<br>
	 *         {@code false}: 写入失败
	 */
	@SuppressWarnings("resource")
	public static boolean writeFileFromBytesByMap(final File file, final byte[] bytes, final boolean append,
	    final boolean isForce) {
		if (bytes == null || !createOrExistsFile(file))
			return false;
		FileChannel fc = null;
		try {
			fc = new FileOutputStream(file, append).getChannel();
			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, fc.size(), bytes.length);
			mbb.put(bytes);
			if (isForce)
				mbb.force();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			CloseUtils.closeIO(fc);
		}
	}

	/**
	 * 将字符串写入文件
	 *
	 * @param file
	 *          文件
	 * @param content
	 *          写入内容
	 * @param append
	 *          是否追加在文件末
	 * @return {@code true}: 写入成功<br>
	 *         {@code false}: 写入失败
	 */
	public static boolean writeFileFromString(final File file, final String content, final boolean append) {
		if (file == null || content == null)
			return false;
		if (!createOrExistsFile(file))
			return false;
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, append));
			bw.write(content);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			CloseUtils.closeIO(bw);
		}
	}

	/**
	 * 读取文件到字符串链表中
	 *
	 * @param file
	 *          文件
	 * @return 字符串链表中
	 */
	public static List<String> readFile2List(final File file) {
		return readFile2List(file, 0, 0x7FFFFFFF, null);
	}

	/**
	 * 读取文件到字符串链表中
	 *
	 * @param file
	 *          文件
	 * @param charsetName
	 *          编码格式。默认值null
	 * @return 字符串链表中
	 */
	public static List<String> readFile2List(final File file, final String charsetName) {
		return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
	}

	/**
	 * 读取文件到字符串链表中
	 *
	 * @param file
	 *          文件
	 * @param st
	 *          需要读取的开始行数
	 * @param end
	 *          需要读取的结束行数
	 * @param charsetName
	 *          编码格式。默认值null
	 * @return 字符串链表中
	 */
	public static List<String> readFile2List(final File file, final int st, final int end, final String charsetName) {
		if (!isFileExists(file))
			return null;
		if (st > end)
			return null;
		BufferedReader reader = null;
		try {
			String line;
			int curLine = 1;
			List<String> list = new ArrayList<>();
			if (isSpace(charsetName)) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
			}
			while ((line = reader.readLine()) != null) {
				if (curLine > end)
					break;
				if (st <= curLine && curLine <= end)
					list.add(line);
				++curLine;
			}
			return list;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			CloseUtils.closeIO(reader);
		}
	}

	/**
	 * 读取文件到字符串中
	 *
	 * @param file
	 *          文件
	 * @param charsetName
	 *          编码格式。默认值null
	 * @return 字符串
	 */
	public static String readFile2String(final File file, final String charsetName) {
		if (!isFileExists(file))
			return null;
		BufferedReader reader = null;
		try {
			StringBuilder sb = new StringBuilder();
			if (isSpace(charsetName)) {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			} else {
				reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
			}
			String line;
			if ((line = reader.readLine()) != null) {
				sb.append(line);
				while ((line = reader.readLine()) != null) {
					sb.append(LINE_SEP).append(line);
				}
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			CloseUtils.closeIO(reader);
		}
	}

	/**
	 * 读取文件到字节数组中
	 *
	 * @param file
	 *          文件
	 * @return 字符数组
	 */
	public static byte[] readFile2BytesByStream(final File file) {
		if (!isFileExists(file))
			return null;
		FileInputStream fis = null;
		ByteArrayOutputStream os = null;
		try {
			fis = new FileInputStream(file);
			os = new ByteArrayOutputStream();
			byte[] b = new byte[sBufferSize];
			int len;
			while ((len = fis.read(b, 0, sBufferSize)) != -1) {
				os.write(b, 0, len);
			}
			return os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			CloseUtils.closeIO(fis, os);
		}
	}
	/**
	 * 读取文件到字节数组中
	 *
	 * @param file
	 *          文件
	 * @return 字符数组
	 */
	@SuppressWarnings("resource")
	public static byte[] readFile2BytesByChannel(final File file) {
		if (!isFileExists(file))
			return null;
		FileChannel fc = null;
		try {
			fc = new RandomAccessFile(file, "r").getChannel();
			ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());
			while (true) {
				if (!((fc.read(byteBuffer)) > 0))
					break;
			}
			return byteBuffer.array();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			CloseUtils.closeIO(fc);
		}
	}

	/**
	 * 读取文件到字节数组中
	 *
	 * @param file
	 *          文件
	 * @return 字符数组
	 */
	@SuppressWarnings("resource")
	public static byte[] readFile2BytesByMap(final File file) {
		if (!isFileExists(file))
			return null;
		FileChannel fc = null;
		try {
			fc = new RandomAccessFile(file, "r").getChannel();
			int size = (int) fc.size();
			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size).load();
			byte[] result = new byte[size];
			mbb.get(result, 0, size);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			CloseUtils.closeIO(fc);
		}
	}

	private static boolean createOrExistsFile(final File file) {
		if (file == null)
			return false;
		if (file.exists())
			return file.isFile();
		if (!createOrExistsDir(file.getParentFile()))
			return false;
		try {
			return file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static boolean createOrExistsDir(final File file) {
		return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
	}

	private static boolean isFileExists(final File file) {
		return file != null && file.exists();
	}

	private static boolean isSpace(final String s) {
		if (s == null)
			return true;
		for (int i = 0, len = s.length(); i < len; ++i) {
			if (!Character.isWhitespace(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}
}