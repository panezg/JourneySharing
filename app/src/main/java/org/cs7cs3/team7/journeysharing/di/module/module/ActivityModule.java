package org.cs7cs3.team7.journeysharing.di.module.module;

import org.cs7cs3.team7.journeysharing.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract MainActivity contributeMainActivity();
}
