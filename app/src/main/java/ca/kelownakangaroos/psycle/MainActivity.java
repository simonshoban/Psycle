package ca.kelownakangaroos.psycle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static String MAP_TYPE_INTENT_KEY = "places";
    private static String[] fileUrlArray = {
            "http://opendata.newwestcity.ca/downloads/drinking-fountains/DRINKING_FOUNTAINS.json",
            "http://opendata.newwestcity.ca/downloads/accessible-public-washrooms/WASHROOMS.json",
            "http://opendata.newwestcity.ca/downloads/significant-buildings-hospitals/SIGNIFICANT_BLDG_HOSPITALS.json",
            "http://opendata.newwestcity.ca/downloads/care-homes/CARE_HOMES.json",
            "http://opendata.newwestcity.ca/downloads/health/HEALTH_MENTAL_HEALTH_AND_ADDICTIONS_SERVICES.json"
    };

    private ArrayList<ArrayList<Location>> listOfLocations;
    private String serializedFileLocation;

    /**
     * Loads location data first by checking for a local serialized file, if that file
     * doesn't exist then it grabs the data from the internet. Sets button onclick listeners.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        serializedFileLocation = this.getFilesDir() + "locationObjects";

        try {
            listOfLocations = ArrayListUtils.deserializeDoubleArrayList(serializedFileLocation);
            ArrayListUtils.printDoubleArrayList(listOfLocations);

        } catch (IOException ioe){
            // If serialized file does not exist or cannot be read, get data from web
            updateData();
        } catch (ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            return;
        }

        setButtonOnClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();

        ((TextView) findViewById(R.id.btn_label_washroom)).setText(getResources().getString(R.string.btn_washroom));
        ((TextView) findViewById(R.id.btn_label_water_fountain)).setText(getResources().getString(R.string.btn_water_fountain));
        ((TextView) findViewById(R.id.btn_label_addiction_clinic)).setText(getResources().getString(R.string.btn_addiction_clinic));
        ((TextView) findViewById(R.id.btn_label_hospital)).setText(getResources().getString(R.string.btn_hospital));
        ((TextView) findViewById(R.id.btn_label_care_home)).setText(getResources().getString(R.string.btn_care_home));

        setButtonOnClickListeners();
    }

    private void updateData() {
        listOfLocations = new ArrayList<>();
        new JSONLoader(this, listOfLocations, serializedFileLocation).execute();
    }

    /**
     * Sets button onclick listeners to start the MapsActivity with the relevant data.
     */
    private void setButtonOnClickListeners() {
        OnClickListener listener = new OnClickListener() {
            @Override
            public void onClick(View viewElement) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                intent.putExtra("Title", viewElement.getTag().toString());

                switch (viewElement.getId()) {
                    case R.id.btn_water_fountain:
                        intent.putExtra(MAP_TYPE_INTENT_KEY, listOfLocations.get(0));
                        ((TextView) findViewById(R.id.btn_label_water_fountain)).setText(R.string.map_opening_message);
                        break;
                    case R.id.btn_washroom:
                        intent.putExtra(MAP_TYPE_INTENT_KEY, listOfLocations.get(1));
                        ((TextView) findViewById(R.id.btn_label_washroom)).setText(R.string.map_opening_message);
                        break;
                    case R.id.btn_hospital:
                        intent.putExtra(MAP_TYPE_INTENT_KEY, listOfLocations.get(2));
                        ((TextView) findViewById(R.id.btn_label_hospital)).setText(R.string.map_opening_message);
                        break;
                    case R.id.btn_care_home:
                        intent.putExtra(MAP_TYPE_INTENT_KEY, listOfLocations.get(3));
                        ((TextView) findViewById(R.id.btn_label_care_home)).setText(R.string.map_opening_message);
                        break;
                    case R.id.btn_addiction_clinic:
                        intent.putExtra(MAP_TYPE_INTENT_KEY, listOfLocations.get(4));
                        ((TextView) findViewById(R.id.btn_label_addiction_clinic)).setText(R.string.map_opening_message);
                        break;
                    default:
                        //do nothing
                        break;
                }

                startActivity(intent);
            }
        };

        findViewById(R.id.btn_washroom).setOnClickListener(listener);
        findViewById(R.id.btn_water_fountain).setOnClickListener(listener);
        findViewById(R.id.btn_hospital).setOnClickListener(listener);
        findViewById(R.id.btn_care_home).setOnClickListener(listener);
        findViewById(R.id.btn_addiction_clinic).setOnClickListener(listener);
        findViewById(R.id.btn_update).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                updateData();
            }
        });
    }

    static class JSONLoader extends AsyncTask<Void, Void, Void> {
        private WeakReference<Context> context;
        private WeakReference<ArrayList<ArrayList<Location>>> listOfNames;
        private WeakReference<String> serializedFileLocation;

        JSONLoader(Context context, ArrayList<ArrayList<Location>> listOfNames, String serializedFileLocation) {
            this.context = new WeakReference<>(context);
            this.listOfNames = new WeakReference<>(listOfNames);
            this.serializedFileLocation = new WeakReference<>(serializedFileLocation);
        }

        private void loadJsonData() {
            JSONHandler jsonHandler = new JSONHandler(context.get());

            for (int index = 0; index < fileUrlArray.length; index++) {
                listOfNames.get().add(new ArrayList<Location>());
                String jsonBlob = jsonHandler.getJsonDataFromWeb(fileUrlArray[index]);

                jsonHandler.getNamesFromJsonString(jsonBlob, listOfNames.get().get(index));

                try {
                    ArrayListUtils.serializeDoubleArrayList(listOfNames.get(), serializedFileLocation.get());
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

            ArrayListUtils.printDoubleArrayList(listOfNames.get());
            Toast.makeText(context.get(), R.string.update_confirmation_text, Toast.LENGTH_LONG).show();
        }
    }
}
