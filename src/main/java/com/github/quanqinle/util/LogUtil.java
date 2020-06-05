package com.github.quanqinle.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具 <br>
 * 建议：slf4j已经够简单了，你就尽量自己管理log吧，别用这个类了。
 * 
 * @author 权芹乐
 *
 */
public class LogUtil {

    /**
     * error和debug是否进行源代码定位，ture表示输出源代码所在类以及代码行
     */
    private static boolean showLocSrc = true;

    /**
     * 该类的名称，用于识别该类的堆栈
     */
    private static final String thisClassName = LogUtil.class.getName();

    public static final Logger logger = LoggerFactory.getLogger(LogUtil.class);

    private LogUtil() {
    }

    public static void main(String[] args) {

        System.out.println("test start");
        LogUtil.info("调试信息info");
        LogUtil.warn("调试信息warn");
        LogUtil.debug("调试信息debug");
        LogUtil.error("调试信息error");
        System.out.println("test end");
    }

    /**
     * 给日志消息加上堆栈信息
     *
     * @param message
     *            日志消息
     */
    private static String extendMsgWithStackTrace(String message) {
        if (showLocSrc) {
            message = message + " : " + getStackMsg();
        }
        return message;
    }

    /**
     * 根据堆栈信息得到源代码行信息
     * <p>
     * 原理：本工具类的堆栈下一行即为源代码的最原始堆栈。
     *
     * @return 调用输出日志的代码所在的类.方法.代码行的相关信息
     *         <p>
     *         如：com.MyClass 类里的 fun()方法调用了Logs.debug("test");
     *         <p>
     *         则堆栈信息为: com.MyClass.fun(MyClass.java 代码行号)
     */
    private static String getStackMsg() {
        StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        if (ste == null) {
            return null;
        }

        boolean srcFlag = false;
        for (StackTraceElement s : ste) {
            // 如果上一行堆栈代码是本类的堆栈，则该行代码则为源代码的最原始堆栈。
            if (srcFlag) {
                return s == null ? "" : s.toString();
            }
            // 定位本类的堆栈
            if (thisClassName.equals(s.getClassName())) {
                srcFlag = true;
            }
        }

        return null;
    }

    /**
     * 输出info信息
     *
     * @param message
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * 输出debug信息。如果debug disable，则不输出日志
     *
     * @param message
     */
    public static void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(extendMsgWithStackTrace(message));
        }
    }

    /**
     * 输出warn信息
     *
     * @param message
     */
    public static void warn(String message) {
        logger.warn(message);
    }

    /**
     * 输出error信息
     *
     * @param message
     */
    public static void error(String message) {
        logger.error(extendMsgWithStackTrace(message));
    }

    /**
     * 获取当前文件名和行号，用于打印log
     *
     * @return [filename:linenumber]
     * @author 权芹乐 2014-12-2
     */
    public static String getFileAndLine() {
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        return "[" + ste.getFileName() + ":" + ste.getLineNumber() + "]";
    }
}
