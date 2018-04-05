package com.example.osw.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class WeatherForecast extends Activity {
    public ProgressBar pb;
    public ImageView weatherView;
    public TextView currentTempView;
    public TextView maxTempView;
    public TextView minTempView;
    public TextView windView;

    public static final String urlString = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        weatherView = (ImageView) findViewById(R.id.weather);
        currentTempView = (TextView) findViewById(R.id.currentTemp);
        maxTempView = (TextView) findViewById(R.id.maxTemp);
        minTempView = (TextView) findViewById(R.id.minTemp);
        windView = (TextView) findViewById(R.id.wind);
        pb = (ProgressBar) findViewById(R.id.progress);



        new ForecastQuery().execute(urlString);
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{

        String currentTemp;
        String minTemp;
        String maxTemp;
        String weatherIcon;
        String wind;
        Bitmap weather;

        @Override
        protected String doInBackground(String ...args){
            URL url;
            InputStream in;

            try {
                url = new URL(args[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();

                in = conn.getInputStream();

                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                //parser.nextTag();

                while (parser.next() != XmlPullParser.END_DOCUMENT) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    String name = parser.getName();

                    if (name.equals("temperature")) {
                        currentTemp = parser.getAttributeValue(null, "value");
                        publishProgress(25);
                        minTemp = parser.getAttributeValue(null, "min");
                        publishProgress(50);
                        maxTemp = parser.getAttributeValue(null, "max");
                        publishProgress(75);

                    }

                    if (name.equals("speed")){
                        wind = parser.getAttributeValue(null, "value");
                    }


                    if (name.equals("weather")){
                        weatherIcon = parser.getAttributeValue(null, "icon");
                        String fileName = weatherIcon + ".png";
                        File file = getBaseContext().getFileStreamPath(fileName);

                        if (file.exists()){
                            Log.i("WeatherForeCast", "Loading existing file");
                            FileInputStream input = null;
                            try {
                                input = openFileInput(fileName);
                            }
                            catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            weather = BitmapFactory.decodeStream(input);

                        }

                        else{
                            Log.i("WeatherForecast", "Downloading file");
                            int responseCode = conn.getResponseCode();
                            String iconUrlString = "http://openweathermap.org/img/w/" + weatherIcon + ".png";
                            HttpURLConnection connection = (HttpURLConnection) new URL(iconUrlString).openConnection();
                            connection.connect();

                            if (responseCode==200){
                                weather = BitmapFactory.decodeStream(connection.getInputStream());
                                FileOutputStream outputStream = openFileOutput( weatherIcon + ".png", Context.MODE_PRIVATE);
                                weather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                            }

                            connection.disconnect();
                        }

                        publishProgress(80);

                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            return "done";

    }

    @Override
    protected void onProgressUpdate(Integer ...values){
        pb.setProgress(values[0]);
        pb.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result){
        weatherView.setImageBitmap(weather);
        currentTempView.setText("Current Temperature: " + currentTemp);
        maxTempView.setText("Max: " + maxTemp);
        minTempView.setText("Min: " + minTemp);
        windView.setText("Wind speed: " + wind);

        pb.setVisibility(View.INVISIBLE);
    }

}


}
