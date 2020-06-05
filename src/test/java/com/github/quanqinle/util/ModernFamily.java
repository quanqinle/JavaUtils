package com.github.quanqinle.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * 把txt文件中的《摩登家族》台词转换成html格式
 */
public class ModernFamily {

    public static void main(String[] args) throws IOException {
        LogUtil.info("start...");
        Path baseP = Paths.get("C:", "Users", "quanql", "Desktop", "ModernFamily");
        Path srcPath = baseP.resolve("txt-base");
        Path drtPath = baseP.resolve("txt-fixed");

        // 查找以 .txt 结尾的所有 Path
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**/*.{txt}");

        Files.walk(srcPath).filter(Files::isRegularFile).filter(matcher::matches).forEach(f -> {
            try {
                in2out(f, drtPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        LogUtil.info("end...");
    }

    public static void in2out(Path inPath, Path outPath) throws IOException {
        Map<String, List<String>> seasonMap = new HashMap<String, List<String>>();
        List<String> lines = new ArrayList<String>();
        Stack<String> preChapterName = new Stack<String>(); // 栈
        Files.readAllLines(inPath).stream().filter(line -> !line.contains("----")).forEach(line -> {
            line = line.trim();
            if (!line.isBlank()) {
                if (RegexUtil.find(line, "^S\\d\\dE\\d\\d$")) {
                    // save the previous chapter
                    if (preChapterName.size() == 1) {
                        String chpName = preChapterName.pop();
                        List<String> copy = new ArrayList<String>(lines);
                        seasonMap.put(chpName, copy);
                        lines.clear();
                    } else if (!preChapterName.empty()) {
                        LogUtil.error("something wrong while doing pre-chapter : " + line);
                    }
                    preChapterName.push(line);
                } else if (containsHanScript(line)) {
                    lines.add("<zh>" + line + "</zh>");
                } else {
                    lines.add("<en>" + line + "</en>");
                }
            }
        });
        // save the last chapter
        if (preChapterName.size() == 1) {
            String chpName = preChapterName.pop();
            seasonMap.put(chpName, lines);
        } else {
            LogUtil.error("something wrong when doing last chapter");
        }

        for (String key : seasonMap.keySet()) {
            String chapterName = key;
            List<String> chpLines = new ArrayList<>();

            String topPart = new String("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n"
                    + "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\"\r\n"
                    + "  \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">\r\n"
                    + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n" + "<head>\r\n" + "<title>FORMAT1</title>\r\n"
                    + "<link href=\"../Styles/stylesheet.css\" type=\"text/css\" rel=\"stylesheet\"/>\r\n"
                    + "</head>\r\n" + "<body>\r\n" + "<h1 class=\"chptitle\">FORMAT1</h1>").replace("FORMAT1",
                            chapterName);
            String bottomPart = "<div class=\"mbppagebreak\"></div></body></html>";
            chpLines.add(topPart);
            chpLines.addAll(seasonMap.get(key));
            chpLines.add(bottomPart);
            Files.write(outPath.resolve("MF-" + chapterName + ".xhtml"), chpLines);
        }
    }

    /**
     * tested if contain Chinese character?
     * 
     * @param s
     * @return true, contain; false, not.
     */
    public static boolean containsHanScript(String s) {
        return s.codePoints()
                .anyMatch(codepoint -> Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN);
    }

}
