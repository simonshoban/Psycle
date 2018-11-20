package ca.kelownakangaroos.psycle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ArrayList<Location>> listOfLocations;

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
            listOfLocations = ArrayListUtils.deserializeDoubleArrayList(serializedFileLocation);
            ArrayListUtils.printDoubleArrayList(listOfLocations);

        } catch (IOException ioe){
            // If serialized file does not exist or cannot be read, get data from web
            listOfLocations = new ArrayList<>();
            new JSONLoader(this, listOfLocations, serializedFileLocation).execute();
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

                switch (viewElement.getId()) {
                    case R.id.btn_water_fountain:
                        intent.putExtra("places", listOfLocations.get(0));
                        break;
                    case R.id.btn_washroom:
                        intent.putExtra("places", listOfLocations.get(1));
                        break;
                    case R.id.btn_hospital:
                        intent.putExtra("places", listOfLocations.get(2));
                        break;
                    case R.id.btn_care_home:
                        intent.putExtra("places", listOfLocations.get(3));
                        break;
                    case R.id.btn_addiction_clinic:
                        intent.putExtra("places", listOfLocations.get(4));
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
        }
    }
}
