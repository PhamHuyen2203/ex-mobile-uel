package com.example.k23411tapp;

import android.util.Log;

import com.example.models.ForecastItem;
import com.example.models.Province;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WeatherService {

    private static final String PROVINCE_URL = "https://thanhnien.vn/ajax-get-item-weather.htm";
    private static final String WEATHER_URL_PREFIX = "https://eth2.cnnd.vn/ajax/weatherinfo/";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36";

    public interface Callback<T> {
        void onSuccess(T result);
        void onFailure(Exception e);
    }

    public static void getProvinces(Callback<List<Province>> callback) {
        new Thread(() -> {
            try {
                String html = downloadUrl(PROVINCE_URL);
                if (html == null || html.isEmpty()) {
                    callback.onFailure(new Exception("Could not fetch province list"));
                    return;
                }

                List<Province> list = new ArrayList<>();
                // Updated regex for <li> structure: <li class="itemWeather" value="2347719" ...><div class="label">An Giang</div></li>
                Pattern pattern = Pattern.compile("<li[^>]*value=\"(\\d+)\"[^>]*>\\s*<div[^>]*>([^<]+)</div>\\s*</li>", Pattern.CASE_INSENSITIVE);
                Matcher matcher = pattern.matcher(html);
                while (matcher.find()) {
                    String id = matcher.group(1);
                    String name = matcher.group(2).trim();
                    list.add(new Province(id, name));
                }
                
                // Fallback for <option> if <li> fails
                if (list.isEmpty()) {
                    Pattern optionPattern = Pattern.compile("<option[^>]*value=\"(\\d+)\"[^>]*>([^<]+)</option>", Pattern.CASE_INSENSITIVE);
                    Matcher optionMatcher = optionPattern.matcher(html);
                    while (optionMatcher.find()) {
                        list.add(new Province(optionMatcher.group(1), optionMatcher.group(2).trim()));
                    }
                }

                callback.onSuccess(list);
            } catch (Exception e) {
                callback.onFailure(e);
            }
        }).start();
    }

    public static void getWeatherInfo(String provinceId, Callback<JSONObject> callback) {
        new Thread(() -> {
            try {
                String url = WEATHER_URL_PREFIX + provinceId + ".htm";
                String jsonStr = downloadUrl(url);
                if (jsonStr == null || jsonStr.isEmpty()) {
                    callback.onFailure(new Exception("Could not fetch weather info"));
                    return;
                }
                JSONObject root = new JSONObject(jsonStr);
                if (root.optBoolean("Success", false)) {
                    JSONObject data = root.optJSONObject("Data");
                    if (data != null && data.has("data")) {
                        callback.onSuccess(data.getJSONObject("data"));
                    } else {
                        callback.onFailure(new Exception("Invalid data format"));
                    }
                } else {
                    callback.onFailure(new Exception("API error"));
                }
            } catch (Exception e) {
                callback.onFailure(e);
            }
        }).start();
    }

    public static List<ForecastItem> parseForecast(JSONObject data) {
        List<ForecastItem> list = new ArrayList<>();
        try {
            JSONArray arr = null;
            String[] keys = {"forecast", "forecastweek", "forecastday", "week_forecast"};
            for (String key : keys) {
                if (data.has(key)) {
                    arr = data.optJSONArray(key);
                    if (arr != null) break;
                }
            }
            if (arr == null) return list;

            for (int i = 0; i < arr.length(); i++) {
                JSONObject item = arr.getJSONObject(i);
                String day = item.optString("day", item.optString("name", ""));
                String date = item.optString("date", "");
                int maxTemp = item.optInt("max_temp", item.optInt("max", item.optInt("high", 0)));
                int minTemp = item.optInt("min_temp", item.optInt("min", item.optInt("low", 0)));
                String status = item.optString("weather", item.optString("status", item.optString("description", "")));
                list.add(new ForecastItem(day, date, maxTemp, minTemp, status));
            }
        } catch (Exception e) {
            Log.e("WeatherService", "Error parsing forecast", e);
        }
        return list;
    }

    private static String downloadUrl(String urlString) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", USER_AGENT);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();
                }
            }
        } finally {
            if (conn != null) conn.disconnect();
        }
        return null;
    }
}
