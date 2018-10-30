package ca.kelownakangaroos.psycle;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setButtonOnClickListeners();
        new JSONLoader(this).execute();
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

        JSONLoader(Context context) {
            this.context = new WeakReference<>(context);
        }

        private void loadJsonData() {
            JSONHandler jsonHandler = new JSONHandler(context.get());

            jsonHandler.saveJsonDataFromWeb(
                    "http://opendata.newwestcity.ca/downloads/drinking-fountains/DRINKING_FOUNTAINS.json",
                    "fountain.json");

            Log.d("JSON", jsonHandler.getJsonDataFromLocal("fountain.json"));
        }

        @Override
        protected Void doInBackground(Void... params) {
            loadJsonData();
            return null;
        }
    }
}
