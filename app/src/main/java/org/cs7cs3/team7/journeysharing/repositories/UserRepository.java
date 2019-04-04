package org.cs7cs3.team7.journeysharing.repositories;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.JsonElement;

import org.cs7cs3.team7.journeysharing.App;
import org.cs7cs3.team7.journeysharing.Models.HTTPResponse;
import org.cs7cs3.team7.journeysharing.SimpleCallback;
import org.cs7cs3.team7.journeysharing.api.UserWebService;
import org.cs7cs3.team7.journeysharing.database.dao.UserDao;
import org.cs7cs3.team7.journeysharing.database.entity.User;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class UserRepository {
    private static int FRESH_TIMEOUT_IN_MINUTES = 1;
    private static String SHARED_PREFERENCES_FILE_KEY = "JourneySharing.SharedPreferences";
    private static String SHARED_PREFERENCES_USER_LOGIN = "JourneySharing.UserLogin";

    private final UserWebService webService;
    private final UserDao userDao;
    private final Executor executor;

    public static User user;

    @Inject
    public UserRepository(UserWebService webService, UserDao userDao, Executor executor) {
        this.webService = webService;
        this.userDao = userDao;
        this.executor = executor;
    }

    public String recoverUserLogin() {
        SharedPreferences sharedPref = App.context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, App.context.MODE_PRIVATE);
        return sharedPref.getString(SHARED_PREFERENCES_USER_LOGIN, null);
    }

    //This should be the only method that is allowed to run on main thread, so that it loads info before presenting screen
    public void getUser(MutableLiveData<User> mldUser, String login) {
        Log.d("JINCHI", "Calling web service");
        refreshUser(login); // try to refresh data if possible via API
        //this is an async call executed on a background thread. Via the postValue() method of the MutableLiveData object,
        // the background thread can use postValue() to ask the UIThread to update the reference value of the MutableLiveData object
        // something that setValue() cannot do, because it can be executed on a background thread
        executor.execute(() -> {
            mldUser.postValue(userDao.load(login)); // return a LiveData directly from the database.
        });
    }

    public User getUserSync(String login) {
        return userDao.loadSync(login);
    }

    private void refreshUser(final String login) {
        executor.execute(() -> {
            // Check if user was fetched recently
            boolean userExists = (userDao.hasUser(login, getMaxRefreshTime(new Date())) != null);

            // If user have to be updated
            if (!userExists) {
                //TODO: We don't have a webservice to recovering user information, thus, all of this is commented
                /*
                webService.getUser(login).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Log.e("JINCHI", "DATA REFRESHED FROM NETWORK");
                        executor.execute(() -> {
                            if (response.isSuccessful()) {
                                //HTTPResponse httpResponse = response.body();
                                //if (httpResponse.getStatus().equals("success")) {
                                Log.e("JINCHI", "Executing second part of save-11 refreshUser");
                                User user = response.body();
                                user.setLastRefresh(new Date());
                                userDao.save(user);
                                //}
                            }
                            else {
                                //TODO: We should throw exception if we were using cache correctly, but for this project we will just let it go
                                Log.e("JINCHI", "OnResponse handler of userRepository.refreshUser() but response at HTTP level was not successful");
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.e("JINCHI", "On Failure handler of userRepository.refreshUser()");
                    }
                });
                */
            }
        });
    }

    public void saveUser(MutableLiveData<User> mldUser, User user, SimpleCallback<Boolean> delegate) {
        executor.execute(() -> {
            webService.save(user).enqueue(new Callback<HTTPResponse>() {
                @Override
                public void onResponse(Call<HTTPResponse> call, Response<HTTPResponse> response) {
                    Log.e("JINCHI", "DATA REFRESHED FROM NETWORK");
                    //Toast.makeText(App.context, "Data refreshed from network !", Toast.LENGTH_LONG).show();
                    if (response.isSuccessful()) {
                        HTTPResponse httpResponse = response.body();
                        if (httpResponse.getStatus().equals("success")) {
                            Log.e("JINCHI", "Successful response");

                            //Saving to preferences
                            SharedPreferences sharedPref = App.context.getSharedPreferences(SHARED_PREFERENCES_FILE_KEY, App.context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString(SHARED_PREFERENCES_USER_LOGIN, user.getLogin());
                            editor.commit();

                            executor.execute(() -> {
                                JsonElement jsonElement = response.body().getData();
                                user.setId(jsonElement.getAsJsonObject().get("id").getAsInt());
                                user.setLastRefresh(new Date());
                                //saving to cache SQLite
                                Log.e("JINCHI", "Saving to database: " + user.toString());
                                userDao.save(user);
                                Log.e("JINCHI", "Trying to load: " + user.getLogin());
                                mldUser.postValue(userDao.load(user.getLogin()));
                            });
                            delegate.accept(true);
                        } else {
                            Log.e("JINCHI", "Failure response at API level response within HTTP Response");
                            delegate.accept(false);
                        }
                    } else {
                        Log.e("JINCHI", "Failure at HTTP Response level");
                        delegate.accept(false);
                    }
                }

                @Override
                public void onFailure(Call<HTTPResponse> call, Throwable t) {
                    Log.e("JINCHI", "FAILED");
                    delegate.accept(false);
                    //TODO: Notify the UI about the problem
                }
            });
        });
    }

    // ---

    private Date getMaxRefreshTime(Date currentDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MINUTE, -FRESH_TIMEOUT_IN_MINUTES);
        return cal.getTime();
    }
}
