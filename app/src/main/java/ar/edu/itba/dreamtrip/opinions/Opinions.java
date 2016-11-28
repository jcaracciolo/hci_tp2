package ar.edu.itba.dreamtrip.opinions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.main.BaseActivity;
import ar.edu.itba.dreamtrip.opinions.sendOpinions.SendOpinions;

public class Opinions extends BaseActivity {
    public final static String FROM_AIRLINE_INFO_KEY = "airline_id_to_opinion";
    public final static String SEND_AIRLINE_ID_GIVE_OPINIONS = "airline_id_to_give_opinion";

    String airlineID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView list = (ListView) findViewById(R.id.comment_list);
        View header = getLayoutInflater().inflate(R.layout.layout_review_detail, null);
        list.addHeaderView(header);

        airlineID = getIntent().getExtras().getString(FROM_AIRLINE_INFO_KEY);

        RatingsView ratingsView = fillRatingsView();

        DataHolder dataHolder = DataHolder.getInstance(this);
        dataHolder.waitForIt(new LoadAirlineNameTask(getBaseContext(), airlineID, (TextView)findViewById(R.id.opinions_title)));
        dataHolder.waitForIt(new LoadAllAirlineRatingTask(this, airlineID, ratingsView));

        findViewById(R.id.give_opinion_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), SendOpinions.class);
                intent.putExtra(SEND_AIRLINE_ID_GIVE_OPINIONS, airlineID);
                startActivity(intent);
            }
        });
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
