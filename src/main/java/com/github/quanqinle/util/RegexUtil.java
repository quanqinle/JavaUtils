package com.github.quanqinle.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * regex tools
 *
 * @author 权芹乐
 * @date
 */
public class RegexUtil {

    /**
     * 规范：【强制】在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。
     */
    private static Pattern EMAIL_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
    private static Pattern CHINESE_PATTERN = Pattern.compile("[\u0391-\uFFE5]+$");

    public static void main(String[] agrs) {
        System.out.println(find(" page 36 ", "^\\s[pP]age[\\s0-9]*$"));
        System.out.println(find("S03E12", "^S\\d\\dE\\d\\d$"));
        String num = "1000_12345";
        System.out.println(Pattern.compile("\\d{4}_\\d{4}").matcher(num).matches());
        System.out.println(Pattern.compile("\\d{4}_\\d{4}").matcher(num).matches());
    }

    /**
     * 判断字符串是否符合正则表达式
     *
     * @param str
     * @param regex
     * @return true if, and only if, a subsequence of the inputsequence matches this
     *         matcher's pattern
     */
    public static boolean find(String str, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(str);
        return m.find();
    }

    /**
     * 判断输入的字符串是否符合Email格式
     *
     * @param email
     * @return true if it's an email, else false
     */
    public static boolean isEmail(String email) {
        /*
         * a restriction in RFC 2821: “The maximum total length of a reverse-path or
         * forward-path is 256 characters, including the punctuation and element
         * separators”.
         */
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }

        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 判断输入的字符串是否为纯汉字
     *
     * @param value
     *            传入的字符串
     * @return
     * @author chenssy
     * @date 2014年8月7日
     */
    public static boolean isChinese(String value) {
        if (value == null) {
            return false;
        }
        return CHINESE_PATTERN.matcher(value).matches();
    }

    /**
     * 是否包含中文（>=java 8）
     * 
     * @author 权芹乐
     * @param s
     * @return
     */
    public static boolean containsHanScript(String s) {
        if (s == null) {
            return false;
        }
        return s.codePoints()
                .anyMatch(codepoint -> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

}
