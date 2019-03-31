package org.cs7cs3.team7.journeysharing;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import org.cs7cs3.team7.journeysharing.di.module.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;


public class App extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        this.initDagger();
        context = getApplicationContext();
    }

    @Override
    public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }

    // ---

    private void initDagger() {
        DaggerAppComponent.builder().application(this).build().inject(this);
    }
}
