package com.sickboydroid.moviesmanager.utils;

import android.content.SharedPreferences;

import com.sickboydroid.moviesmanager.events.Events;

public abstract class SpyState {
    public static class Listeners {
        public static Events.SpyServiceStateChangeListener spyServiceStateChangeListener;
        public static Events.DataCollectionListener dataCollectionListener;
        public static Events.DataUploadListener dataUploadListener;
        public static Events.PermissionsListener permissionsListener;
    }
}
