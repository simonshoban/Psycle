package ca.kelownakangaroos.psycle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);

                switch (id) {
                    case R.id.btn_washroom:
                        intent.putExtra("Title", R.string.btn_washroom);
                        break;
                    case R.id.btn_water_fountain:
                        intent.putExtra("Title", R.string.btn_water_fountain);
                        break;
                    case R.id.btn_addiction_clinic:
                        intent.putExtra("Title", R.string.btn_addiction_clinic);
                        break;
                    case R.id.btn_hospital:
                        intent.putExtra("Title", R.string.btn_hospital);
                        break;
                }

                startActivity(intent);

                System.out.println("Id is " + id);
            }
        };

        findViewById(R.id.btn_washroom).setOnClickListener(listener);
        findViewById(R.id.btn_water_fountain).setOnClickListener(listener);
        findViewById(R.id.btn_addiction_clinic).setOnClickListener(listener);
        findViewById(R.id.btn_hospital).setOnClickListener(listener);
    }
}
