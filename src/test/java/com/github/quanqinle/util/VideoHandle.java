/**
 * 
 */
package com.github.quanqinle.util;

import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.config.RedirectConfig.redirectConfig;
//import io.restassured.config.RestAssuredConfig;
/**
 * 视频转换。加字幕转成.mp4
 * 
 * @author quanql
 *
 */
public class VideoHandle {

    /**
     * 
     */
    public VideoHandle() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args) {
        LogUtil.info("start:" + DateTimeUtil.getCurrentDateTime());

//        videosWalk();
        Response response = RestAssured.given()
                .config(RestAssured.config().redirect(redirectConfig().followRedirects(false)))
                .post("/data");
        LogUtil.info("end  :" + DateTimeUtil.getCurrentDateTime());
    }

    private static void convertVideoWithSubtitle(Path sourceVideoPath, Path subtitlePath, Path targetVideoPath) {
        Path newSubtile = Paths.get(DateTimeUtil.getCurrentDateTime() + ".srt");

        try {
            Files.createDirectories(targetVideoPath.getParent());
            Files.copy(subtitlePath, newSubtile);
        } catch (Exception e) {
            LogUtil.info("fail:" + 1);
        }

        List<String> command = new ArrayList<String>();
        command.add("ffmpeg.exe");
        command.add("-i");
        command.add(sourceVideoPath.toString());
        command.add("-vf");
        command.add(String.format("subtitles=\"%s\"", newSubtile.getFileName()));
        command.add("-y");
        command.add(String.format("\"%s\"", targetVideoPath.toString()));

        OSUtil.runCmd(command);

        try {
            Files.deleteIfExists(newSubtile);
        } catch (Exception e) {
            LogUtil.info("fail:" + 2);
        }
    }

    private static void convertVideoNoSubtitle(Path sourceVideoPath, Path targetVideoPath) {
        try {
            Files.createDirectories(targetVideoPath.getParent());
        } catch (Exception e) {
            LogUtil.info("fail to create dir");
        }
        List<String> command = new ArrayList<String>();
        command.add("ffmpeg.exe");
        command.add("-i");
        command.add(sourceVideoPath.toString());
        command.add("-y");
        command.add(String.format("\"%s\"", targetVideoPath.toString()));

        OSUtil.runCmd(command);
    }

    /**
     * 遍历所有视频
     */
    private static void videosWalk() {
        Path sourcePath = Paths.get("D:", "tmp", "video-youtube", "wowenglish");
        Path targetPath = Paths.get("D:", "tmp", "video-youtube", "wowenglish-changed");

        try (Stream<Path> walk = Files.walk(sourcePath)) {
            List<Path> filePaths = walk.filter(Files::isRegularFile).collect(Collectors.toList());
            // filePaths.forEach(System.out::println);
            for (Path path : filePaths) {
                Path parentPath = path.getParent().getFileName();
                String filename = path.getFileName().toString();

                if (filename.toLowerCase().endsWith(".mp4")) {
                    LogUtil.info("[mp4] " + path);
                    Path newVideoPath = targetPath.resolve(parentPath).resolve(filename);
                    if (Files.exists(newVideoPath)) {
                        continue;
                    }
                    try {
                        Files.createDirectories(newVideoPath.getParent());
                    } catch (Exception e) {
                        LogUtil.info("fail to create dir");
                    }
                    Files.copy(path, newVideoPath);

                } else if (filename.toLowerCase().endsWith(".mkv")) {
                    LogUtil.info("[mkv] " + path);
                    String newVideoName = filename.replace(".mkv", ".mp4");
                    Path newVideoPath = targetPath.resolve(parentPath).resolve(newVideoName);
                    if (Files.exists(newVideoPath)) {
                        continue;
                    }
                    convertVideoNoSubtitle(path, newVideoPath);

                } else if (filename.toLowerCase().endsWith(".webm")) {
                    LogUtil.info("[webm] " + path);

                    String newVideoName = filename.replace(".webm", ".mp4");
                    Path newVideoPath = targetPath.resolve(parentPath).resolve(newVideoName);
                    Path srt1Subtitle = path.getParent().resolve(filename.replace(".webm", ".en.srt"));
                    Path srt2Subtitle = path.getParent().resolve(filename.replace(".webm", ".en-GB.srt"));
                    Path vttSubtitle = path.getParent().resolve(filename.replace(".webm", ".vtt"));
                    if (Files.exists(newVideoPath)) {
                        continue;
                    }
                    if (filePaths.contains(srt1Subtitle)) {
                        convertVideoWithSubtitle(path, srt1Subtitle, newVideoPath);
                    } else if (filePaths.contains(srt2Subtitle)) {
                        convertVideoWithSubtitle(path, srt2Subtitle, newVideoPath);
                    } else if (filePaths.contains(vttSubtitle)) {
                        convertVideoWithSubtitle(path, vttSubtitle, newVideoPath);
                    } else {
                        LogUtil.info("no subtitle :" + path);
                        convertVideoNoSubtitle(path, newVideoPath);
                    }
                } else {
                    LogUtil.info("[??] " + path);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
