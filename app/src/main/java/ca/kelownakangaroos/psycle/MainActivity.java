package ca.kelownakangaroos.psycle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ArrayList<String>> listOfNames;

    private String serializedFileLocation;

    private static String[] fileUrlArray = {
            "http://opendata.newwestcity.ca/downloads/drinking-fountains/DRINKING_FOUNTAINS.json",
            "http://opendata.newwestcity.ca/downloads/accessible-public-washrooms/WASHROOMS.json",
            "http://opendata.newwestcity.ca/downloads/significant-buildings-hospitals/SIGNIFICANT_BLDG_HOSPITALS.json",
            "http://opendata.newwestcity.ca/downloads/care-homes/CARE_HOMES.json",
            "http://opendata.newwestcity.ca/downloads/health/HEALTH_MENTAL_HEALTH_AND_ADDICTIONS_SERVICES.json"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serializedFileLocation = this.getFilesDir() + "locationObjects";

        try {
            FileInputStream fis = new FileInputStream(serializedFileLocation);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listOfNames = (ArrayList<ArrayList<String>>) ois.readObject();
            ois.close();
            fis.close();

            for (ArrayList<String> temp_List : listOfNames) {
                for (String location : temp_List) {
                    System.out.println(location);
                }
            }

        } catch (IOException ioe){
            // If serialized file does not exist or cannot be read, get data from web
            listOfNames = new ArrayList<>();
            new JSONLoader(this, listOfNames, serializedFileLocation).execute();
        } catch (ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        setButtonOnClickListeners();
    }

    private void setButtonOnClickListeners() {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View viewElement) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                Button button = (Button) viewElement;

                intent.putExtra("Title", button.getText());

                startActivity(intent);
            }
        };

        findViewById(R.id.btn_washroom).setOnClickListener(listener);
        findViewById(R.id.btn_water_fountain).setOnClickListener(listener);
        findViewById(R.id.btn_addiction_clinic).setOnClickListener(listener);
        findViewById(R.id.btn_hospital).setOnClickListener(listener);
    }

    static class JSONLoader extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> context;
        private WeakReference<ArrayList<ArrayList<String>>> listOfNames;
        private WeakReference<String> serializedFileLocation;

        JSONLoader(Context context, ArrayList<ArrayList<String>> listOfNames, String serializedFileLocation) {
            this.context = new WeakReference<>(context);
            this.listOfNames = new WeakReference<>(listOfNames);
            this.serializedFileLocation = new WeakReference<>(serializedFileLocation);
        }

        private void loadJsonData() {
            JSONHandler jsonHandler = new JSONHandler(context.get());

            for (int index = 0; index < fileUrlArray.length; index++) {
                listOfNames.get().add(new ArrayList<String>());
                String jsonBlob = jsonHandler.getJsonDataFromWeb(fileUrlArray[index]);

                jsonHandler.getNamesFromJsonString(jsonBlob, listOfNames.get().get(index));

                try {
                    FileOutputStream fos = new FileOutputStream(serializedFileLocation.get());
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(listOfNames.get());
                    oos.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadJsonData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            for (ArrayList<String> temp_List : listOfNames.get()) {
                for (String name : temp_List) {
                    System.out.println(name);
                }
            }
        }
    }
}
