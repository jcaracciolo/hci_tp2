package ar.edu.itba.dreamtrip.common;

import android.content.Context;

import ar.edu.itba.dreamtrip.R;
import ar.edu.itba.dreamtrip.common.model.FlightStatus;

/**
 * Created by Julian Benitez on 11/27/2016.
 */

public enum LenguageType {
    ENGLISH, SPANISH;
    public static LenguageType parseLenguage(Context context, String string){

        if(context.getString(R.string.general_settings_lenguage_english).equals(string))
            return LenguageType.ENGLISH;
        if(context.getString(R.string.general_settings_lenguage_spanish).equals(string))
            return LenguageType.SPANISH;
        return null;
    }

    public static String toString(Context context, LenguageType lenguageType){
        switch (lenguageType){
            case ENGLISH:
                return context.getString(R.string.general_settings_lenguage_english);
            case SPANISH:
                return context.getString(R.string.general_settings_lenguage_spanish);
        }
        return null;
    }
}
