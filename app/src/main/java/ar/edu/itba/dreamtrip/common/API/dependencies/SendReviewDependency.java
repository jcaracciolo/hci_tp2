package ar.edu.itba.dreamtrip.common.API.dependencies;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SendReviewDependency that = (SendReviewDependency) o;
        return that == this;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
