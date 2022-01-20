package com.developerdepository.scout.User;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developerdepository.scout.R;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class WeatherActivity extends AppCompatActivity {
private  static final String API_KEY ="bb93b338055f73edcb743d4d348999e2";
// ImageView iconWeather;
TextView tVtemp , tVcity,etCityName;
String city = "Hofuf";
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.weatherlayout);

  etCityName = findViewById(R.id.CityName);
  tVtemp = findViewById(R.id.tVtemp);
  tVcity = findViewById(R.id.tVcity);
  loadWeatherByCityName(city);
 }

 private void loadWeatherByCityName(String city) {
  Ion.with(this)
          .load("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid="+ API_KEY )
          .asJsonObject()
          .setCallback(new FutureCallback<JsonObject>() {
           @Override
           public void onCompleted(Exception e, JsonObject result) {
            // do stuff with the result or error
               if (e != null){
                   e.printStackTrace();
                   Toast.makeText(WeatherActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
               }
               else
               {
                   JsonObject  main = result.get("main").getAsJsonObject();
                   double temp = main.get("temp").getAsDouble();
                   tVtemp.setText(temp+"C");

                   JsonObject sys = result.get("main").getAsJsonObject();
                   String country = sys.get("country").getAsString();

                   tVcity.setText(city+","+country);
               }
            Log.d("result",result.toString());
           }
          });
 }
}
