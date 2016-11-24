package ar.edu.itba.dreamtrip.common.tasks;

import android.os.AsyncTask;

import ar.edu.itba.dreamtrip.common.interfaces.KnowsItsDependencies;

/**
 * Created by Julian Benitez on 11/15/2016.
 */

public abstract class AsyncTaskInformed<R,T,K> extends AsyncTask<R,T,K> implements KnowsItsDependencies {

}
