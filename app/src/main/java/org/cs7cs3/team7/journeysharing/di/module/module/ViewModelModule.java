package org.cs7cs3.team7.journeysharing.di.module.module;


import org.cs7cs3.team7.journeysharing.MainViewModel;
import org.cs7cs3.team7.journeysharing.di.module.key.ViewModelKey;
import org.cs7cs3.team7.journeysharing.view_models.FactoryViewModel;
import org.cs7cs3.team7.journeysharing.view_models.ProfileViewModel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel.class)
    abstract ViewModel bindMainViewModel(MainViewModel viewModel);
    //abstract ViewModel bindUserProfileViewModel(MainViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel.class)
    abstract ViewModel bindProfileViewModel(ProfileViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
