package com.example.k23411tapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.models.ForecastItem;

import org.json.JSONObject;

import java.util.List;
import java.util.Locale;

public class WeatherDetailActivity extends AppCompatActivity {

    TextView txtLocation, txtTemperature, txtStatus, txtHumidity, txtFeelsLike, txtWind, txtUVIndex;
    View cardForecast;
    LinearLayout layoutForecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_weather_detail);
        addViews();
        addEvents();

        String provinceId = getIntent().getStringExtra("PROVINCE_ID");
        String provinceName = getIntent().getStringExtra("PROVINCE_NAME");

        if (provinceName != null) {
            txtLocation.setText(provinceName);
        }

        if (provinceId != null) {
            loadWeatherInfo(provinceId);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void addViews() {
        txtLocation = findViewById(R.id.txtLocation);
        txtTemperature = findViewById(R.id.txtTemperature);
        txtStatus = findViewById(R.id.txtStatus);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtFeelsLike = findViewById(R.id.txtFeelsLike);
        txtWind = findViewById(R.id.txtWind);
        txtUVIndex = findViewById(R.id.txtUVIndex);
        cardForecast = findViewById(R.id.cardForecast);
        layoutForecast = findViewById(R.id.layoutForecast);
    }

    private void addEvents() {
        ImageView imgBackWeatherDetail = findViewById(R.id.imgBackWeatherDetail);
        imgBackWeatherDetail.setOnClickListener(v -> finish());
    }

    private void loadWeatherInfo(String provinceId) {
        WeatherService.getWeatherInfo(provinceId, new WeatherService.Callback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                try {
                    JSONObject dataInfo = data.getJSONObject("datainfo");
                    String location = dataInfo.optString("location", "");
                    int temp = dataInfo.optInt("temperature", 0);
                    String status = dataInfo.optString("status", "");
                    int feelsLike = dataInfo.optInt("feels_like", 0);
                    String humidity = dataInfo.optString("humidity", "--");

                    JSONObject windObj = dataInfo.optJSONObject("wind");
                    String wind = windObj != null
                            ? windObj.optString("index", "--") + " " + windObj.optString("unit", "")
                            : "--";

                    JSONObject uvObj = dataInfo.optJSONObject("UV_index");
                    String uv = uvObj != null ? uvObj.optString("index", "--") : "--";

                    List<ForecastItem> forecastList = WeatherService.parseForecast(data);

                    runOnUiThread(() -> {
                        if (!location.isEmpty()) txtLocation.setText(location);
                        txtTemperature.setText(String.format(Locale.getDefault(), "%d°C", temp));
                        txtStatus.setText(status);
                        txtHumidity.setText(humidity);
                        txtFeelsLike.setText(String.format(Locale.getDefault(), "%d°C", feelsLike));
                        txtWind.setText(wind);
                        txtUVIndex.setText(uv);

                        showForecast(forecastList);
                    });
                } catch (Exception e) {
                    onFailure(e);
                }
            }

            @Override
            public void onFailure(Exception e) {
                runOnUiThread(() -> Toast.makeText(WeatherDetailActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void showForecast(List<ForecastItem> list) {
        if (list == null || list.isEmpty()) {
            cardForecast.setVisibility(View.GONE);
            return;
        }

        cardForecast.setVisibility(View.VISIBLE);
        layoutForecast.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        for (int i = 0; i < list.size(); i++) {
            ForecastItem item = list.get(i);

            View row = inflater.inflate(R.layout.item_forecast, layoutForecast, false);
            TextView txtDay = row.findViewById(R.id.txtForecastDay);
            TextView txtDate = row.findViewById(R.id.txtForecastDate);
            TextView txtForecastStatus = row.findViewById(R.id.txtForecastStatus);
            TextView txtTemp = row.findViewById(R.id.txtForecastTemp);

            String dayText = item.getDay().isEmpty() ? "Ngày " + (i + 1) : item.getDay();
            txtDay.setText(dayText);
            txtDate.setText(item.getDate());
            txtForecastStatus.setText(item.getStatus());
            txtTemp.setText(String.format(Locale.getDefault(), "%d° ~ %d°", item.getMinTemp(), item.getMaxTemp()));

            layoutForecast.addView(row);

            if (i < list.size() - 1) {
                View divider = new View(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1);
                divider.setLayoutParams(params);
                divider.setBackgroundColor(ContextCompat.getColor(this, R.color.grey_white));
                layoutForecast.addView(divider);
            }
        }
    }
}
