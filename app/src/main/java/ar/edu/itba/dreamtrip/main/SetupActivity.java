package ar.edu.itba.dreamtrip.main;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;

import ar.edu.itba.dreamtrip.R;

/**
 * Created by juanfra on 22/11/16.
 */

public class SetupActivity {

    public static void setup(BaseActivity activity, int layout, DrawerLayout drawer){
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(layout, null, false);
        drawer.addView(contentView, 0);
    }
}
