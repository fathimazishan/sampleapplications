package com.motion9studios.weatherservice.jsonweather;

import java.io.IOException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterOutputStream;

import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.motion9studios.weatherservice.utils.Utils;

import org.json.JSONObject;
import org.json.JSONTokener;

import static android.util.JsonToken.END_DOCUMENT;

/**
 * Parses the Json weather data returned from the Weather Services API
 * and returns a List of JsonWeather objects that contain this data.
 */
public class WeatherJSONParser {
    /**
     * Used for logging purposes.
     */
    private final String TAG =
        this.getClass().getCanonicalName();

    /**
     * Parse the @a inputStream and convert it into a List of JsonWeather
     * objects.
     */

    public List<JsonWeather> parseJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            return parseWeatherObject(reader);

        }
            finally {
                reader.close();
            }
        }

    public List parseWeatherObject(JsonReader reader) throws IOException {
        List<JsonWeather> jsonWeathers = new ArrayList();

       // reader.beginObject();
        //while (reader.hasNext()) {
            jsonWeathers.add(parseWeather(reader));
        //}
        //reader.endObject();
        return jsonWeathers;
    }

    public JsonWeather parseWeather(JsonReader reader) throws IOException {
        Sys sys =null;
        Main main = null;
        Wind wind = null;
        Coord coord = null;
        Weather weather =null;
        Clouds clouds = null;

        JsonWeather jsonWeather = new JsonWeather();


        try {
            // start reading the stream
            reader.beginObject();

            if(!reader.peek().toString().equals("STRING")){
                // read coord object
                if (reader.nextName().equals("coord")) {
                    reader.beginObject();
                    coord = parseCoord(reader);
                    if (coord != null) {
                        jsonWeather.setCoord(coord);
                    }
                }


                // read sys object
                if (reader.nextName().equals("sys")) {
                    reader.beginObject();
                    sys = parseSys(reader);
                    if (sys != null) {
                        jsonWeather.setSys(sys);

                    }
                }


                // read weather array
                if (reader.nextName().equals("weather")) {
                    reader.beginArray();
                    reader.beginObject();
                    weather = parseWeatherArray(reader);
                    if (weather != null) {
                        jsonWeather.setWeather(weather);
                    }
                }


                //read base property
                if (reader.nextName().equals("base")) {

                    String base = reader.nextString();
                    if (base != null) {
                        jsonWeather.setBase(base);
                    }
                }

                //read main object
                if (reader.nextName().equals("main")) {
                    reader.beginObject();
                    main = parseMain(reader);
                    if (main != null) {
                        jsonWeather.setMain(main);
                    }

                }


                //read wind object
                if (reader.nextName().equals("wind")) {
                    reader.beginObject();
                    wind = parseWind(reader);
                    if (wind != null) {
                        jsonWeather.setWind(wind);
                    }

                }


                //read clouds object
                if (reader.nextName().equals("clouds")) {
                    reader.beginObject();
                    clouds = parseClouds(reader);
                    if (clouds != null) {
                        jsonWeather.setClouds(clouds);
                    }

                }


                if (reader.nextName().equals("dt")) {
                    jsonWeather.setDt(reader.nextLong());
                    Log.d("Token value at error",reader.peek().toString());
                }

                if (reader.nextName().equals("id")) {
                    jsonWeather.setId(reader.nextLong());
                    Log.d("Token value at error",reader.peek().toString());
                }

                if (reader.nextName().equals("name")) {
                    jsonWeather.setName(reader.nextString());
                    Log.d("Name of city", jsonWeather.getName());
                }

                if (reader.nextName().equals("cod")) {
                    jsonWeather.setCod(reader.nextLong());
                }


                reader.endObject();



            }


        }catch(Exception e){
            Log.d("Json Token",reader.peek().toString());
            e.printStackTrace();
        }
        return jsonWeather;
    }


    private Clouds parseClouds(JsonReader reader) throws IOException{
        Clouds clouds = new Clouds();
        while(reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("all")) {
                clouds.setAll(reader.nextLong());
            }
            else {
                reader.skipValue();
            }


        }
        reader.endObject();

        return clouds;
    }


    private Weather parseWeatherArray(JsonReader reader) throws IOException{

        Weather weather = new Weather();
        while(reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("id")) {
                weather.setId(reader.nextLong());
            } else if (name.equals("main")) {
                weather.setMain(reader.nextString());
            } else if (name.equals("description")) {
                weather.setDescription(reader.nextString());
            } else if (name.equals("icon")) {
                weather.setIcon(reader.nextString());
            }
            else {
                reader.skipValue();
            }


        }
        reader.endObject();
        reader.endArray();
        return weather;
    }

    private Coord parseCoord(JsonReader reader) throws IOException{

        Coord coord = new Coord();

        while(reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("lon")) {
                coord.setLon(reader.nextDouble());
            } else if (name.equals("lat")) {
                coord.setLat(reader.nextDouble());
            }
           else {
                reader.skipValue();
            }


        }



        reader.endObject();
        return coord;
    }



    private Wind parseWind(JsonReader reader) throws IOException {
        Wind wind = new Wind();
        while(reader.hasNext()){

            String name = reader.nextName();
            if (name.equals("speed")) {

                wind.setSpeed(Utils.round(reader.nextDouble(),2));
            }
            else if (name.equals("deg")) {

                wind.setSpeed(reader.nextDouble());
            }
            else{
                reader.skipValue();
            }

        }

        reader.endObject();
        return wind;
    }
    private Main parseMain(JsonReader reader) throws IOException {
        Main main = new Main();

        while(reader.hasNext()){

            String name = reader.nextName();
            if (name.equals("temp")) {


                main.setTemp(Utils.round((reader.nextDouble() - 273.15),2));
            }
            else if (name.equals("temp_min")) {

                main.setTempMin(reader.nextDouble());
            }
            else if (name.equals("temp_max")) {

                main.setTempMax(reader.nextDouble());
            }
            else if (name.equals("pressure")) {

                main.setPressure(reader.nextDouble());
            }
            else if (name.equals("sea_level")) {

                main.setSeaLevel(reader.nextDouble());
            }
            else if (name.equals("grnd_level")) {

                main.setGrndLevel(reader.nextDouble());
            }
            else if (name.equals("humidity")) {

                main.setHumidity(reader.nextLong());
            }


            else{
                reader.skipValue();
            }

        }

        reader.endObject();
        return main;
    }

    private Sys parseSys(JsonReader reader) throws IOException{
        Sys sys = new Sys();
        while(reader.hasNext()){
            String name = reader.nextName();
            if (name.equals("message")) {
                sys.setMessage(reader.nextDouble());
            } else if (name.equals("country")) {
                sys.setCountry(reader.nextString());

            }
            else if (name.equals("sunrise")) {
                sys.setSunrise(reader.nextLong());
            }
            else if (name.equals("sunset")) {
                sys.setSunset(reader.nextLong());
            }
            else {
                reader.skipValue();
            }


        }

        reader.endObject();

        return sys;
    }






}
