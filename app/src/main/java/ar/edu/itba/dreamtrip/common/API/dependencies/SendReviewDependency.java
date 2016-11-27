package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Calendar;
import java.util.Date;

import ar.edu.itba.dreamtrip.common.model.Review;

/**
 * Created by Julian Benitez on 11/22/2016.
 */

public class SendReviewDependency extends Dependency {

    private Review review;

    public SendReviewDependency(Review review) {
        super(DependencyType.SEND_REVIEW);
        this.review = review;
    }

    public Review getReview() {
        return review;
    }
}
