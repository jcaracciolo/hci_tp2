package ar.edu.itba.dreamtrip.Deals;


import android.os.Bundle;
import android.support.v7.app.ActionBar;
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
    public final static String INTENT_TO_DEALS_FROM = "ar.itba.edu.dreamtrip.deals_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupView(R.layout.activity_deals_from);

        String id=(String)getIntent().getExtras().get(INTENT_TO_DEALS_FROM);
        grid= (GridView) findViewById(R.id.gridview);

        ActionBar bar = getSupportActionBar();

        bar.setTitle(bar.getTitle() + id );

        final DataHolder dataholder = DataHolder.getInstance(getBaseContext());

        dataholder.waitForIt(new PopulateDeals(getBaseContext(), grid,id));

    }



}
