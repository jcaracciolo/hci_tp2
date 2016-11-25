package ar.edu.itba.dreamtrip.airlineInfo;

import android.content.Intent;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.City;
import ar.edu.itba.dreamtrip.common.tasks.ImageLoadTask;
import ar.edu.itba.dreamtrip.common.tasks.LoadAirlinesTask;
import ar.edu.itba.dreamtrip.common.tasks.LoadCitiesTask;
import ar.edu.itba.dreamtrip.main.BaseActivity;
import ar.edu.itba.dreamtrip.main.SetupActivity;
import ar.edu.itba.dreamtrip.opinions.Opinions;

import static android.R.attr.data;

public class AirlineInfo extends BaseActivity {

    public final static String RESULT_ID_KEY = "main_activity.go_to_info_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_airline_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String id = intent.getStringExtra(RESULT_ID_KEY);

        ImageView img = (ImageView)findViewById(R.id.airline_logo);
        TextView text = (TextView)findViewById(R.id.airline_name);
        TextView desc = (TextView)findViewById(R.id.airline_description);

        DataHolder dataHolder = DataHolder.getInstance(this);
        dataHolder.waitForIt(new LoadAirlineInfoTask(this, img, text, desc, id));
    }

    public void seeOpinions(View v) {
        Intent intent = new Intent(this, Opinions.class);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
