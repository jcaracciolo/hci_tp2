package ar.edu.itba.dreamtrip.main;

import android.os.Bundle;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by juanfra on 28/11/16.
 */

public class Help extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.help);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
