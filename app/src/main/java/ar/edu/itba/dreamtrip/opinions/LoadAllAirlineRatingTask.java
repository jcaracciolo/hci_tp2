package ar.edu.itba.dreamtrip.opinions;

import android.content.Context;
import android.widget.ListView;

import java.util.Collection;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.AirlinesReviewDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.model.Review;
import ar.edu.itba.dreamtrip.common.tasks.AsyncTaskInformed;



public class LoadAllAirlineRatingTask extends AsyncTaskInformed<Object,Void, RatingsModel>{

    private Context context;
    String id;
    RatingsView view;

    public LoadAllAirlineRatingTask(Context context, String id, RatingsView view) {
        this.context = context;
        this.id = id;
        this.view = view;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new AirlinesReviewDependency(id));
        return dependencies;
    }

    @Override
    protected RatingsModel doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        Collection<Review> arr = dataHolder.getAirlineReviews(id);

        RatingsModel model = new RatingsModel();
        for(Review r: arr) {
            model.kindness += r.getFriendliness();
            model.comfort += r.getConfort();
            model.food += r.getFood();
            model.miles += r.getMilageProgram();
            model.punctuality += r.getPunctuality();
            model.quality_price += r.getQualityPriceRatio();
            model.overall += r.getQualityPriceRatio();
            String a = r.getComment();
            model.addReview(new ReviewModel(r.getComment(), r.getRecommended(), r.getOverall()));
        }

        model.kindness /= arr.size();
        model.comfort /= arr.size();
        model.food /= arr.size();
        model.miles /= arr.size();
        model.punctuality /= arr.size();
        model.quality_price /= arr.size();
        model.overall /= arr.size();

        return model;
    }

    @Override
    protected void onPostExecute(RatingsModel model) {
        view.comfort.setRating(model.comfort / 2);
        view.food.setRating(model.food / 2);
        view.kindness.setRating(model.kindness / 2);
        view.miles.setRating(model.miles / 2);
        view.overall.setRating(model.overall / 2);
        view.punctuality.setRating(model.punctuality / 2);
        view.quality_price.setRating(model.quality_price / 2);

        view.commentList.setAdapter(new ReviewsAdapter(context, model.reviews));
    }

}
