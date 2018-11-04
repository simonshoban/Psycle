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
    private ArrayList<String> listOfNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            FileInputStream fis = new FileInputStream(this.getFilesDir() + "locations");
            ObjectInputStream ois = new ObjectInputStream(fis);
            listOfNames = (ArrayList) ois.readObject();
            ois.close();
            fis.close();

            for (String location : listOfNames) {
                System.out.println(location);
            }

        } catch (IOException ioe){
            // If serialized file does not exist or cannot be read, get data from web
            listOfNames = new ArrayList<>();
            new JSONLoader(this, listOfNames).execute();
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
        private WeakReference<ArrayList<String>> listOfNames;
        private String[] fileUrlArray = {
                "http://opendata.newwestcity.ca/downloads/drinking-fountains/DRINKING_FOUNTAINS.json",
                "http://opendata.newwestcity.ca/downloads/accessible-public-washrooms/WASHROOMS.json",
                "http://opendata.newwestcity.ca/downloads/significant-buildings-hospitals/SIGNIFICANT_BLDG_HOSPITALS.json",
                "http://opendata.newwestcity.ca/downloads/care-homes/CARE_HOMES.json",
                "http://opendata.newwestcity.ca/downloads/health/HEALTH_MENTAL_HEALTH_AND_ADDICTIONS_SERVICES.json"
        };
        private String[] fileLocationArray = {
                "fountains.json",
                "washrooms,json",
                "hospitals.json",
                "careHomes.json",
                "mentalHealthAndAddictions.json"
        };

        JSONLoader(Context context, ArrayList<String> listOfNames) {
            this.context = new WeakReference<>(context);
            this.listOfNames = new WeakReference<>(listOfNames);
        }

        private void loadJsonData() {
            JSONHandler jsonHandler = new JSONHandler(context.get());

            if (fileUrlArray.length != fileLocationArray.length) {
                throw new IllegalStateException("Unequal number of URLs and file locations");
            }

            for (int index = 0; index < fileUrlArray.length; index++) {
                String jsonBlob = jsonHandler.getJsonDataFromWeb(fileUrlArray[index]);

                jsonHandler.getNamesFromJsonString(jsonBlob, listOfNames.get());

                try {
                    FileOutputStream fos = new FileOutputStream(context.get().getFilesDir() + "locations");
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

            for (String name : listOfNames.get()) {
                System.out.println(name);
            }
        }
    }
}
