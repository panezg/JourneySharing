package org.cs7cs3.team7.journeysharing.di.module.module;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cs7cs3.team7.journeysharing.Constants;
import org.cs7cs3.team7.journeysharing.api.UserWebService;
import org.cs7cs3.team7.journeysharing.database.LocalDatabase;
import org.cs7cs3.team7.journeysharing.database.dao.UserDao;
import org.cs7cs3.team7.journeysharing.repositories.UserRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import androidx.room.Room;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    LocalDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                LocalDatabase.class, "LocalDatabase.db")
                //Found a way to use Room, Retrofit, and best practices without invoking DB on main thread
                //.allowMainThreadQueries()
                .build();
    }

    @Provides
    @Singleton
    UserDao provideUserDao(LocalDatabase database) {
        return database.userDao();
    }

    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    UserRepository provideUserRepository(UserWebService webservice, UserDao userDao, Executor executor) {
        return new UserRepository(webservice, userDao, executor);
    }

    // --- NETWORK INJECTION ---

    private static String BASE_URL = Constants.HOST;

    @Provides
    Gson provideGson() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().addInterceptor(interceptor).build())
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    UserWebService provideApiWebservice(Retrofit restAdapter) {
        return restAdapter.create(UserWebService.class);
    }
}
