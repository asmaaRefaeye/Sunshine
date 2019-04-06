package com.example.sunshine;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.sunshine.Utilities.NetworkUtils;
import com.example.sunshine.Utilities.OpenWeatherJsonUtils;
import com.example.sunshine.data.SunshinePreferences;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView mWeatherData ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intiat();
    }

    private void loadWeatherData() {
        String location = SunshinePreferences.getPreferredWeatherLocation(this);
        new FetchWeatherTask().execute(location);
    }


    public class FetchWeatherTask extends AsyncTask<String, Void, String[]> {

        @Override
        protected String[] doInBackground(String... params) {

            /* If there's no zip code, there's nothing to look up. */
            if (params.length == 0) {
                return null;
            }

            String location = params[0];
            URL weatherRequestUrl = NetworkUtils.buildUrl(location);

            try {
                String jsonWeatherResponse = NetworkUtils
                        .getResponseFromHttpUrl(weatherRequestUrl);

                String[] simpleJsonWeatherData = OpenWeatherJsonUtils
                        .getSimpleWeatherStringsFromJson(MainActivity.this, jsonWeatherResponse);

                return simpleJsonWeatherData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] weatherData) {
            if (weatherData != null) {
                /*
                 * Iterate through the array and append the Strings to the TextView. The reason why we add
                 * the "\n\n\n" after the String is to give visual separation between each String in the
                 * TextView. Later, we'll learn about a better way to display lists of data.
                 */
                for (String weatherString : weatherData) {
                    mWeatherData.append((weatherString) + "\n\n\n");
                }
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.forcast, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            mWeatherData.setText("");
            loadWeatherData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


 public void   intiat(){
     mWeatherData=(TextView)findViewById(R.id.tv_weather_data);

     String[] dummyWeatherData = {
             "Today, May 17 - Clear - 17°C / 15°C",
             "Tomorrow - Cloudy - 19°C / 15°C",
             "Thursday - Rainy- 30°C / 11°C",
             "Friday - Thunderstorms - 21°C / 9°C",
             "Saturday - Thunderstorms - 16°C / 7°C",
             "Sunday - Rainy - 16°C / 8°C",
             "Monday - Partly Cloudy - 15°C / 10°C",
             "Tue, May 24 - Meatballs - 16°C / 18°C",
             "Wed, May 25 - Cloudy - 19°C / 15°C",
             "Thu, May 26 - Stormy - 30°C / 11°C",
             "Fri, May 27 - Hurricane - 21°C / 9°C",
             "Sat, May 28 - Meteors - 16°C / 7°C",
             "Sun, May 29 - Apocalypse - 16°C / 8°C",
             "Mon, May 30 - Post Apocalypse - 15°C / 10°C",
     };

     for (String dummyweatherData : dummyWeatherData){
         mWeatherData.append(dummyWeatherData+"\n\n\n");
     }
    }
}
