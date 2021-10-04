package com.example.weatherapicallapp;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {
    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

    Context context;
    String cityID;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public interface VolleyResponseListener{
        void onError(String message);

        void onResponse(String cityID);
    }


    //    Define methods

    public void getcityID (String cityName, VolleyResponseListener volleyResponseListener) {

        String url = QUERY_FOR_CITY_ID + cityName;


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                cityID ="";
                try {
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                This works but did not return the id to the mainactivity

//                Toast.makeText(context, "City ID = " + cityID , Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Something Wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something Wrong");
            }

        });
        MySingleton.getInstance(context).addToRequestQueue(request);

//        return cityID;
    }


    public interface ForeCastByIDResponse{
        void onError(String message);

        void onResponse(WeatherReportModel weatherReportModel);
    }

        public void getCityForecastByID(String cityID, ForeCastByIDResponse foreCastByIDResponse){

            List<WeatherReportModel> report = new ArrayList<>();
            String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;

    //        get the json object
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                    try {
                        JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");

//                        get the first item on the list

                        WeatherReportModel first_day = new WeatherReportModel();


                        JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(0);
                        first_day.setId(first_day_from_api.getInt("id"));
                        first_day.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                        first_day.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                        first_day.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                        first_day.setCreated(first_day_from_api.getString("created"));
                        first_day.setApplicable_date(first_day_from_api.getString("applicable_date"));
                        first_day.setMin_temp(first_day_from_api.getLong("min_temp"));
                        first_day.setMax_temp(first_day_from_api.getLong("max_temp"));
                        first_day.setWind_speed(first_day_from_api.getLong("wind_speed"));
                        first_day.setWind_direction(first_day_from_api.getLong("wind_direction"));
                        first_day.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                        first_day.setHumidity(first_day_from_api.getInt("humidity"));
                        first_day.setVisibility(first_day_from_api.getLong("visibility"));
                        first_day.setPredictability(first_day_from_api.getInt("predictability"));

                        foreCastByIDResponse.onResponse(first_day);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//
                }

            });

    //        get the json property

            MySingleton.getInstance(context).addToRequestQueue(request);
    }

//    public List<WeatherReportModel> getCityForecastByName(String cityName){
//
//    }
}
