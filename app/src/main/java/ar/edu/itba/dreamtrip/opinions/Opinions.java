package ar.edu.itba.dreamtrip.opinions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.main.BaseActivity;

public class Opinions extends BaseActivity {
    public final static String FROM_AIRLINE_INFO_KEY = "airline_id_to_opinion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView list = (ListView) findViewById(R.id.comment_list);
        View header = getLayoutInflater().inflate(R.layout.layout_review_detail, null);
        list.addHeaderView(header);

        String airlineID = getIntent().getExtras().getString(FROM_AIRLINE_INFO_KEY);

        RatingsView ratingsView = fillRatingsView();

        DataHolder dataHolder = DataHolder.getInstance(this);
        dataHolder.waitForIt(new LoadAllAirlineRatingTask(this, airlineID, ratingsView));

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private RatingsView fillRatingsView() {
        RatingBar kindness = (RatingBar) findViewById(R.id.kindness_rating);
        RatingBar comfort = (RatingBar) findViewById(R.id.comfort_rating);
        RatingBar food = (RatingBar) findViewById(R.id.food_rating);
        RatingBar miles = (RatingBar) findViewById(R.id.miles_rating);
        RatingBar punctuality = (RatingBar) findViewById(R.id.punctuality_rating);
        RatingBar quality_price = (RatingBar) findViewById(R.id.quality_price_rating);
        RatingBar overall = (RatingBar) findViewById(R.id.general_rating);
        ListView commentList = (ListView) findViewById(R.id.comment_list);

        return new RatingsView(kindness, comfort, food, miles, punctuality, quality_price, overall, commentList);
    }

}
