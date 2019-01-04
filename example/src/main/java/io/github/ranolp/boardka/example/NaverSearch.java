package io.github.ranolp.boardka.example;

import io.github.ranolp.boardka.api.BoardkaManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public final class NaverSearch {
    private static Pattern pattern = Pattern.compile(
            "<em class=\"num\">1</em>\\s*<span class=\"title\">([\\w가-힣ㄱ-ㅎㅏ-ㅣ\\s]+)</span>");
    private static volatile String best = "결과 없음";
    private static Timer timer = new Timer("Naver Search Parser", true);
    private static TimerTask task = new TimerTask() {
        @Override
        public void run() {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(
                        "https://datalab.naver.com/keyword/realtimeList.naver").openConnection();
                connection.addRequestProperty("User-Agent", "Gecko/20100101 Firefox/60.0");
                try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String source = reader.lines().collect(Collectors.joining("\n"));
                    Matcher matcher = pattern.matcher(source);
                    if (matcher.find()) {
                        best = matcher.group(1);
                    }
                    BoardkaManager.renderAllSidebars();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    private NaverSearch() {
        throw new UnsupportedOperationException("You cannot instantiate NaverSearch");
    }

    public static String getBest() {
        return best;
    }

    public static void schedule() {
        timer.schedule(task, 0, TimeUnit.MINUTES.toMillis(1));
    }
}
