package org.cs7cs3.team7.journeysharing.di.module.module;

import org.cs7cs3.team7.journeysharing.OnDemandJourneyFragment;
import org.cs7cs3.team7.journeysharing.ProfileFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract ProfileFragment contributeProfileFragment();

    @ContributesAndroidInjector
    abstract OnDemandJourneyFragment contributeOnDemandJourneyFragment();
}
