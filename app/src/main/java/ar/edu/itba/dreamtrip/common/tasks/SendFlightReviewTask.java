package ar.edu.itba.dreamtrip.common.tasks;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.API.DataHolder;
import ar.edu.itba.dreamtrip.common.API.dependencies.Dependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.FlightDependency;
import ar.edu.itba.dreamtrip.common.API.dependencies.SendReviewDependency;
import ar.edu.itba.dreamtrip.common.model.Deal;
import ar.edu.itba.dreamtrip.common.model.Flight;
import ar.edu.itba.dreamtrip.common.model.Review;

/**
 * Created by Julian Benitez on 11/27/2016.
 */

public class SendFlightReviewTask extends AsyncTaskInformed<Object,Void,ArrayList<String>> {
    private Context context;
    private Review review;
    private Boolean reviewRecievedCorrectly;
    public SendFlightReviewTask(Context context, Review review) {
        this.context = context;
        this.review = review;
    }

    @Override
    public HashSet<Dependency> getDependencies() {
        HashSet<Dependency> dependencies = new HashSet<>();
        dependencies.add(new SendReviewDependency(review));
        return dependencies;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... params) {
        DataHolder dataHolder = (DataHolder) params[0];
        ArrayList<String> strings = new ArrayList<>();
        reviewRecievedCorrectly = dataHolder.lastReviewValid();

        return strings;
    }

    @Override
    protected void onPostExecute(ArrayList<String> result) {
        if (!reviewRecievedCorrectly) {
            Toast.makeText(context, context.getResources().getString(R.string.error_opinion_send), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.ok_opinion_send), Toast.LENGTH_SHORT).show();
        }
    }

    public void toast(String str) {
        Toast.makeText(context, str,
                Toast.LENGTH_SHORT).show();
    }
}
