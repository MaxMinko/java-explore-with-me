package ru.practicum;

import org.apache.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.practicum.dto.PostStaticDto;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class StatClient {

    private final HttpClient httpClient;

    private final String statServerUrl;


    @Autowired
    public StatClient(@Value("${client.url}") String statServerUrl) {
        this.httpClient = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
        this.statServerUrl = statServerUrl;
    }

    public void hit(String uri) throws IOException, InterruptedException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        PostStaticDto postStaticDto = new PostStaticDto();
        InetAddress ip = InetAddress.getLocalHost();
        postStaticDto.setApp("ewm-main-service");
        postStaticDto.setUri(uri);
        postStaticDto.setIp(ip.getHostAddress());
        postStaticDto.setTimestamp(LocalDateTime.now().format(formatter));
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(statServerUrl + "/hit")).header(HttpHeaders.ACCEPT, "application/json").headers(HttpHeaders.CONTENT_TYPE, "application/json").POST(HttpRequest.BodyPublishers.ofString("{\"app\":\"" + postStaticDto.getApp() + "\"," + "\"uri\":\"" + postStaticDto.getUri() + "\"," + "\"ip\":\" " + postStaticDto.getIp() + "\"," + "\"timestamp\":\"" + postStaticDto.getTimestamp() + "\"}")).build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

    }

    public Integer stat(String queryString) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(statServerUrl + "/stats" + queryString)).header(HttpHeaders.ACCEPT, "application/json").build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        String[] str = response.body().split(":");
        String[] s = str[3].split("}]");
        return Integer.valueOf(s[0]);
    }

    public Map<Integer, Integer> stats(String queryString) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(statServerUrl + "/stats" + queryString)).header(HttpHeaders.ACCEPT, "application/json").build();
        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        String[] str1 = response.body().split(",");

        Map<Integer, Integer> map = new HashMap<>();
        List<Integer> eventsId = new ArrayList<>();
        List<Integer> hits = new ArrayList<>();

        for (int i = 1; i < str1.length; i = i + 3) {
            String[] str = str1[i].split(":");
            String[] str2 = str[1].split("s/");
            String[] str3 = str2[1].split("\"");
            String str4 = str3[0];
            eventsId.add(Integer.valueOf(str4));
        }
        for (int k = 2; k < str1.length; k = k + 3) {
            String[] str = str1[k].split(":");
            String[] str2 = str[1].split("}");
            String s = str2[0];
            hits.add(Integer.valueOf(s));
        }
        for (int j = 0; j < eventsId.size(); j++) {
            map.put(eventsId.get(j), hits.get(j));
        }
        return map;
    }

}
