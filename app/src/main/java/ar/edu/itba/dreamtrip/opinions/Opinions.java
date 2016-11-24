package ar.edu.itba.dreamtrip.opinions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ar.edu.itba.dreamtrip.R;

public class Opinions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
