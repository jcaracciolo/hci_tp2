package ar.edu.itba.dreamtrip.opinions;

import android.widget.ListView;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.Collection;

import ar.edu.itba.dreamtrip.common.model.Review;

/**
 * Created by Pedro on 25/11/2016.
 */

public class RatingsModel {
    public Float kindness = 0.0f;
    public Float comfort = 0.0f;
    public Float food = 0.0f;
    public Float miles = 0.0f;
    public Float punctuality = 0.0f;
    public Float quality_price = 0.0f;
    public Float overall = 0.0f;
    public ListView commentsList;
    public ArrayList<ReviewModel> reviews = new ArrayList<>();


    public void resetRatings() {
        kindness = 0.0f;
        comfort = 0.0f;
        food = 0.0f;
        miles = 0.0f;
        punctuality = 0.0f;
        quality_price = 0.0f;
        overall = 0.0f;
    }

    public void addReview(ReviewModel review) {
        reviews.add(review);
    }

    public void calculateFromReviews(Collection<Review> reviews) {
        resetRatings();
        for(Review r: reviews) {
            kindness += r.getFriendliness();
            comfort += r.getConfort();
            food += r.getFood();
            miles += r.getMilageProgram();
            punctuality += r.getPunctuality();
            quality_price += r.getQualityPriceRatio();
            overall += r.getQualityPriceRatio();
        }
        kindness /= reviews.size();
        comfort /= reviews.size();
        food /= reviews.size();
        miles /= reviews.size();
        punctuality /= reviews.size();
        quality_price /= reviews.size();
        overall /= reviews.size();
    }
}
