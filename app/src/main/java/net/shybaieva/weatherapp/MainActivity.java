package net.shybaieva.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity   {

    TextView tv, city, weatherTV, windTV, humidityTV, pressureTV;
    String result="", location;
    SearchView selectCity;
    final String [] temp = {""};
    RadioButton london, newYork;
    final String apiKey = "5fd3af0f02fdf416de32f6bc192b4ed2";
    ImageView weatherImg, tempImg, windImg, humidityImg, pressureImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        init();

        london.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = "London";
                city.setText(location);
                setQuery();
            }
        });

        newYork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                location = "New York";
                city.setText(location);
                setQuery();
            }
        });

        selectCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                location = selectCity.getQuery().toString();
                city.setText(location);
                setQuery();
                selectCity.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }


    private void init(){
        city = findViewById(R.id.cityTV);
        selectCity = findViewById(R.id.selectCity);

        london = findViewById(R.id.london);
        newYork = findViewById(R.id.newYork);

        weatherImg = findViewById(R.id.weatherImg);
        weatherTV = findViewById(R.id.weatherText);

        tempImg = findViewById(R.id.tempImg);
        tv= findViewById(R.id.tv);

        windImg = findViewById(R.id.widImg);
        windTV = findViewById(R.id.windTV);

        humidityImg =  findViewById(R.id.humidityImg);
        humidityTV = findViewById(R.id.humidityTV);

        pressureImg = findViewById(R.id.pressureImg);
        pressureTV = findViewById(R.id.pressureTV);


    }

    private void setQuery (){
        try{
            String url = "https://api.openweathermap.org/data/2.5/weather?q="+ location +"&units=metric&appid="+ apiKey + "&lang=ru";
            GetWeatherInfo weatherInfo = new GetWeatherInfo();
            temp [0] = weatherInfo.execute(url).get();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    class GetWeatherInfo extends AsyncTask <String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            result="";
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while( (line = bufferedReader.readLine()) != null){
                    result = result + line;
                }
                Log.i("result1", result+"result1");
                return null;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject (result);
                //TODO weather and weather ico


                Double temp = jsonObject.getJSONObject("main").getDouble("temp");
                tv.setText(temp.toString() + "°C");
                tempImg.setVisibility(View.VISIBLE);

                Log.i("result", temp.toString());

                Double wind = jsonObject.getJSONObject("wind").getDouble("speed");
                windTV.setText(wind.toString()+ "m/s");
                windImg.setVisibility(View.VISIBLE);

                Double humidity = jsonObject.getJSONObject("main").getDouble("humidity");
                humidityTV.setText(humidity.toString());
                humidityImg.setVisibility(View.VISIBLE);

                Double pressure = jsonObject.getJSONObject("main").getDouble("pressure");
                pressureTV.setText(pressure.toString());
                pressureImg.setVisibility(View.VISIBLE);

               /* JSONArray jsonArray = new JSONArray(result);
                */

               String weatherData = jsonObject.getString("weather");
               Log.i("result", weatherData);
               JSONArray jsonArray = new JSONArray(weatherData);

                String weather="";

                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject tempArray = jsonArray.getJSONObject(i);
                    weather = tempArray.getString("main");
                }
                Log.i("result", weather);
                weatherTV.setText(weather);
                switch (weather){
                    case "Clouds":{
                        weatherImg.setImageResource(R.drawable.cloud);
                        weatherImg.setVisibility(View.VISIBLE);
                        break;
                    }
                }


            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
