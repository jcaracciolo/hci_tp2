package ar.edu.itba.dreamtrip.airlineInfo;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.AirlinesReviewDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.DependencyType;
import ar.edu.itba.dreamtrip.common.model.Airline;
import ar.edu.itba.dreamtrip.common.model.Review;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;
import ar.edu.itba.dreamtrip.common.tasks.ImageLoadTask;


public class LoadAirlineRatingTask extends AsyncTaskInformed<Object,Void, Float>{

    private Context context;
    String id;
    RatingBar ratingBar;

    public LoadAirlineRatingTask(Context context, String id, RatingBar ratingBar) {
        this.context = context;
        this.id = id;
        this.ratingBar = ratingBar;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new AirlinesReviewDependency(id));
        return dependencies;
    }

    @Override
    protected Float doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<Review> arr = dataHolder.getAirlineReviews(id);
        Float grade = 0.0f;
        for (Review r: arr) {
            grade += r.getOverall();
        }
        return grade / arr.size();
    }

    @Override
    protected void onPostExecute(Float grade) {
        ratingBar.setRating(grade/2);
    }

}
