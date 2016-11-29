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

        Spinner language_spinner = (Spinner) findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.language_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language_spinner.setAdapter(adapter3);
        language_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String language = ((String)adapterView.getItemAtPosition(i)).split(" ")[0];
                String english = view.getResources().getString(R.string.english);
                String spanish = view.getResources().getString(R.string.spanish);

                Resources res = view.getContext().getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                android.content.res.Configuration conf = res.getConfiguration();

                if (language.equals(english)) {
                    settingsManager.setSavedLenguage(LenguageType.ENGLISH);
                    LocaleHelper.setLocale(getBaseContext(), "en");
                    conf.locale=new Locale("en");
                    refreshView();
                } else if (language.equals(spanish)) {
                    settingsManager.setSavedLenguage(LenguageType.SPANISH);
                    LocaleHelper.setLocale(getBaseContext(), "es");
                    conf.locale=new Locale("es");
                    refreshView();
                } else {
                }
                Locale.setDefault(conf.locale);
                res.updateConfiguration(conf, dm);


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

            public void refreshView(){
                Intent intent = new Intent(getBaseContext(), Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                finish();
                startActivity(intent);
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
}
