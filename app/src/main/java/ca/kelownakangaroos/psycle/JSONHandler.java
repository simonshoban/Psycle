package ca.kelownakangaroos.psycle;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class JSONHandler {
    private static final String TAG = "JSONHandler";
    private Context context;

    JSONHandler(Context context) {
        this.context = context;
    }

    public void getNamesFromJsonString(String jsonString, ArrayList<Location> listOfLocations) {
        try {
            JSONArray features = new JSONObject(jsonString).getJSONArray("features");
            for (int index = 0; index < features.length(); index++) {
                listOfLocations.add(getLocationFromJson(features.getJSONObject(index)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getNameFromJson(JSONObject featuresObject) {
        String name;

        try {
            name = featuresObject.getJSONObject("properties").getString("Name");
            return name;
        } catch (JSONException e) {
            try {
                name = featuresObject.getJSONObject("properties").getString("BLDGNAM");
                return name;
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }
    }

    private Location getLocationFromJson(JSONObject featuresObject) {
        String name = "";
        double latitude = 0;
        double longitude = 0;

        try {
            latitude = featuresObject.getJSONObject("properties").getDouble("Y");
            longitude = featuresObject.getJSONObject("properties").getDouble("X");

            name = featuresObject.getJSONObject("properties").getString("Name");
        } catch (JSONException e) {
            try {
                name = featuresObject.getJSONObject("properties").getString("BLDGNAM");
            } catch (JSONException e1) {
                e1.printStackTrace();
                return null;
            }
        }

        return new Location(latitude, longitude, name);
    }

    @Deprecated
    public void saveJsonDataFromWeb(String fileUrl, String fileName) {
        saveJsonDataToLocal(getJsonDataFromWeb(fileUrl), fileName);
    }

    public String getJsonDataFromWeb(String fileUrl) {
        String response = null;
        try {
            URL url = new URL(fileUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

            Log.d(TAG, "Web request");

        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    @Deprecated
    public String getJsonDataFromLocal(String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }

            br.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Deprecated
    public void saveJsonDataToLocal(String jsonData, String fileName) {
        try {
            File file = new File(context.getFilesDir(), fileName);
            Writer output = new BufferedWriter(new FileWriter(file));

            output.write(jsonData);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, e.getLocalizedMessage());
        }
    }
}
