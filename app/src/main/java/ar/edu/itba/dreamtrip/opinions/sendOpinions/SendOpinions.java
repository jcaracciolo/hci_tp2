package ar.edu.itba.dreamtrip.opinions.sendOpinions;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;

import java.net.URLDecoder;
import java.net.URLEncoder;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.HtmlManipulator;
import ar.edu.itba.dreamtrip.common.model.Review;
import ar.edu.itba.dreamtrip.common.tasks.SendFlightReviewTask;

public class SendOpinions extends AppCompatActivity {
    public final static String AIRLINE_ID_GIVE_OPINIONS = "airline_id_to_give_opinion";

    String airlineID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_opinions);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        airlineID = getIntent().getExtras().getString(AIRLINE_ID_GIVE_OPINIONS);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public void sendOpinion(View v) {
        String flightNumber = ((EditText)findViewById(R.id.flight_number_send_opinion)).getText().toString();
        if (!flightNumber.matches("^\\d{3,4}$")) {
            Toast.makeText(getBaseContext(), getResources().getString(R.string.incorrect_flight_number), Toast.LENGTH_SHORT).show();
            return;
        }
        Integer kindness = Integer.parseInt(((RangeBar)findViewById(R.id.kindness_rating)).getRightPinValue());
        Integer comfort = Integer.parseInt(((RangeBar)findViewById(R.id.comfort_rating)).getRightPinValue());
        Integer food = Integer.parseInt(((RangeBar)findViewById(R.id.food_rating)).getRightPinValue());
        Integer miles = Integer.parseInt(((RangeBar)findViewById(R.id.miles_rating)).getRightPinValue());
        Integer punctuality = Integer.parseInt(((RangeBar)findViewById(R.id.punctuality_rating)).getRightPinValue());
        Integer pricequal = Integer.parseInt(((RangeBar)findViewById(R.id.quality_price_rating)).getRightPinValue());
        Boolean recommend = ((String)((ImageView)findViewById(R.id.recommend_review)).getTag()).equals("true") ?
                                true : false;
        String msg = ((EditText)findViewById(R.id.comment_entry)).getText().toString();
        msg = HtmlManipulator.quoteHtml(URLEncoder.encode(msg));

        Integer overall = (kindness + comfort + food + miles + punctuality + pricequal) / 6;

        Review review = new Review(airlineID, Integer.parseInt(flightNumber), overall, kindness,
                food, punctuality, miles, comfort, pricequal, recommend, msg);

        DataHolder dataHolder = DataHolder.getInstance(getBaseContext());
        dataHolder.waitForIt(new SendFlightReviewTask(getBaseContext(), review));
    }

    public void recommendFlight(View v) {
        ((ImageView)findViewById(R.id.recommend_review)).setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.status_green));
        ((ImageView)findViewById(R.id.not_recommend_review)).setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.black));
        ((ImageView)findViewById(R.id.recommend_review)).setTag("true");
    }

    public void notRecommendFlight(View v) {
        ((ImageView)findViewById(R.id.recommend_review)).setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.black));
        ((ImageView)findViewById(R.id.not_recommend_review)).setColorFilter(ContextCompat.getColor(getBaseContext(),R.color.status_red));
        ((ImageView)findViewById(R.id.recommend_review)).setTag("false");
    }
}
