package ca.kelownakangaroos.psycle;

import android.content.Context;
import android.util.Log;
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

public class JSONHandler {
    private static final String TAG = "JSONHandler";
    private Context context;

    JSONHandler(Context context) {
        this.context = context;
    }

    public void saveJsonDataFromWeb(String fileUrl, String fileName) {
        saveJsonDataToLocal(getJsonDataFromWeb(fileUrl), fileName);
    }

    private String getJsonDataFromWeb(String fileUrl) {
        String response = null;
        try {
            URL url = new URL(fileUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

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

    private void saveJsonDataToLocal(String jsonData, String fileName) {
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
