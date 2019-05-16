package ru.bmstu.atmospherepollution.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Pollution {
    // error errorSubscript if parsing failed
    // null otherwise
    private String errorSubscript;
    // date and time corresponding to returned data
    private String time;
    // location for which data is provided
    private Location location;
    // specified toxic gas information
    private ArrayList<PollutionData> data;
    // toxin
    private Toxin toxin;

    public static Pollution parseFromJsonString(String jsonString) {
        Pollution pollution = new Pollution();
        try {
            JSONObject root = new JSONObject(jsonString);

            pollution.setTime(root.getString("time"));

            JSONObject location = root.getJSONObject("location");
            Location loc = new Location();

            loc.setLatitude(location.getDouble("latitude"));
            loc.setLongitude(location.getDouble("longitude"));
            pollution.setLocation(loc);

            JSONArray dataArray = root.getJSONArray("data");
            ArrayList<PollutionData> dataList = new ArrayList<>();
            for (int i = 0; i < dataArray.length(); i++) {
                JSONObject dataItem = dataArray.getJSONObject(i);
                PollutionData pollutionData = new PollutionData();
                pollutionData.setPrecision(dataItem.getDouble("precision"));
                pollutionData.setPressure(dataItem.getDouble("pressure"));
                pollutionData.setValue(dataItem.getDouble("value"));
                dataList.add(pollutionData);
            }
            pollution.setData(dataList);

            Log.i("pollution", "data " + pollution.getData().size());
        } catch (JSONException e) {
            e.printStackTrace();
            pollution.setErrorSubscript(e.getMessage());
            Log.i("pollution", "errorSubscript " + pollution.getErrorSubscript());
        }
        return pollution;
    }

    public void setToxin(Toxin toxin) {
        this.toxin = toxin;
        data.forEach(elem -> elem.setToxin(toxin));
    }
}


