package org.cs7cs3.team7.journeysharing.di.module.component;

import android.app.Application;

import org.cs7cs3.team7.journeysharing.App;
import org.cs7cs3.team7.journeysharing.di.module.module.ActivityModule;
import org.cs7cs3.team7.journeysharing.di.module.module.AppModule;
import org.cs7cs3.team7.journeysharing.di.module.module.FragmentModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);
}
