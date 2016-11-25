package ar.edu.itba.dreamtrip.opinions;

/**
 * Created by Pedro on 25/11/2016.
 */

public class ReviewModel {
    public String comment;
    public boolean recomends;
    public Integer rating;

    public ReviewModel(String comment, boolean recomends, Integer rating) {
        this.comment = comment;
        this.recomends = recomends;
        this.rating = rating;
    }
}
