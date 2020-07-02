package net.shybaieva.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tv, city;
    String result="", location;
    SearchView selectCity;
    final String [] temp = {""};
    ImageButton searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCity.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        location = selectCity.getQuery().toString();
                        setQuery();
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        if(newText!=location){
                            location=newText;
                            setQuery();
                        }
                        return true;
                    }
                });
            }
        });

    }


    private void init(){
        tv= findViewById(R.id.tv);
        city = findViewById(R.id.cityTV);
        selectCity = findViewById(R.id.selectCity);
        searchButton = findViewById(R.id.searchButton);
    }

    private void setQuery (){
        try{
            String url = "https://api.openweathermap.org/data/2.5/weather?q="+ "Moscow" +"&units=metric&appid=ed6c7b23e66e08f6ba0f15180280fb84&lang=ru";
            GetWeatherInfo weatherInfo = new GetWeatherInfo();
            temp [0] = weatherInfo.execute(url).get();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    class GetWeatherInfo extends AsyncTask <String, Void, String>{

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
                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject (result);
                String weatherInfo = jsonObject.getString("main");
                tv.setText(weatherInfo);

            }
            catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
