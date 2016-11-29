package ar.edu.itba.dreamtrip;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.ToggleButton;

import java.util.Locale;

import ar.edu.itba.dreamtrip.common.API.SettingsManager;
import ar.edu.itba.dreamtrip.common.LenguageType;
import ar.edu.itba.dreamtrip.common.LocaleHelper;
import ar.edu.itba.dreamtrip.main.BaseActivity;

public class Settings extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Spinner flight_spinner = (Spinner) findViewById(R.id.flight_refresh_time);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.refresh_time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final SettingsManager settingsManager = SettingsManager.getInstance(getBaseContext());
        flight_spinner.setAdapter(adapter);
        flight_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                     @Override
                                                     public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                         String time = ((String)adapterView.getItemAtPosition(i)).split(" ")[0];
                                                         settingsManager.setFlightStateCheckInterval(Integer.parseInt(time));
                                                     }

                                                     @Override
                                                     public void onNothingSelected(AdapterView<?> adapterView) {

                                                     }
                                                 });


        Spinner deals_spinner = (Spinner) findViewById(R.id.deals_refresh_time);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.refresh_time, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deals_spinner.setAdapter(adapter2);
        deals_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String time = ((String)adapterView.getItemAtPosition(i)).split(" ")[0];
//                settingsManager.interva(Integer.parseInt(time));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ToggleButton btn = (ToggleButton) findViewById(R.id.enable_notifications);
        if (btn.isChecked()) {
            SettingsManager.getInstance(getBaseContext()).setDealsNotifications(true);
            SettingsManager.getInstance(getBaseContext()).setFlightNotifications(true);
        } else {
            SettingsManager.getInstance(getBaseContext()).setDealsNotifications(false);
            SettingsManager.getInstance(getBaseContext()).setFlightNotifications(false);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
