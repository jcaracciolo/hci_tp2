package ar.edu.itba.dreamtrip.Deals;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.main.BaseActivity;

/**
 * Created by juanfra on 23/11/16.
 */

public class DealsFromActivity extends BaseActivity {
    GridView grid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_deals_from);

        grid= (GridView) findViewById(R.id.gridview);

        final DataHolder dataholder = DataHolder.getInstance(getBaseContext());

        dataholder.waitForIt(new PopulateDeals(getBaseContext(), grid,"BUE"));

    }



}
