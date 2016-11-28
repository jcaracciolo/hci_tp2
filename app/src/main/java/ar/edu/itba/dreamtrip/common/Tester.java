package ar.edu.itba.dreamtrip.common;

import android.content.Context;

import ar.edu.itba.dreamtrip.common.API.SettingsManager;

/**
 * Created by Julian Benitez on 11/27/2016.
 */

public class Tester {

    public static void testNotificationSettings(Context context){

        SettingsManager s = SettingsManager.getInstance(context);

        System.out.println(s.getDealsNotifications());
        System.out.println(s.getFlightNotifications());
        s.setDealsNotifications(false);
        System.out.println(s.getDealsNotifications());
        System.out.println(s.getFlightNotifications());
        s.setFlightNotifications(false);
        System.out.println(s.getDealsNotifications());
        System.out.println(s.getFlightNotifications());
        s.setDealsNotifications(true);
        System.out.println(s.getDealsNotifications());
        System.out.println(s.getFlightNotifications());
        s.setDealsNotifications(true);
        System.out.println(s.getDealsNotifications());
        System.out.println(s.getFlightNotifications());
        System.out.println("test complete");
    }
    public static void testLenguageSettings(Context context){

        SettingsManager s = SettingsManager.getInstance(context);

        System.out.println(s.getSavedLanguage());
        System.out.println(s.setSavedLenguage(LenguageType.SPANISH));
        System.out.println(s.getSavedLanguage());
        System.out.println(s.getSavedLanguage());
        System.out.println(s.setSavedLenguage(LenguageType.ENGLISH));
        System.out.println(s.getSavedLanguage());
    }
}
