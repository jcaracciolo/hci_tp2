package ar.edu.itba.dreamtrip.opinions;

import android.widget.ListView;
import android.widget.RatingBar;

/**
 * Created by Pedro on 25/11/2016.
 */

public class RatingsView {
    public RatingBar kindness;
    public RatingBar comfort;
    public RatingBar food;
    public RatingBar miles;
    public RatingBar punctuality;
    public RatingBar quality_price;
    public RatingBar overall;
    public ListView commentList;

    public RatingsView(RatingBar kindness, RatingBar comfort, RatingBar food, RatingBar miles,
                       RatingBar punctuality, RatingBar quality_price, RatingBar overall,
                       ListView commentList) {
        this.kindness = kindness;
        this.comfort = comfort;
        this.food = food;
        this.miles = miles;
        this.punctuality = punctuality;
        this.quality_price = quality_price;
        this.overall = overall;
        this.commentList = commentList;
    }
}
