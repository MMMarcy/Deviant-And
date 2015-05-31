package mmmarcy.github.deviantand.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import mmmarcy.github.deviantand.BuildConfig;

/**
 * Created by marcello on 31/05/15.
 */
public class AndroidAndApplication extends Application {

    protected final static String TAG = AndroidAndApplication.class.getSimpleName();

    public static final String OAUTH_PREFS = "oauth2";
    public static final String OAUTH_ID = "CLIENT_ID";
    public static final String OAUTH_SECRET = "CLIENT_SECRET";
    protected static AndroidAndApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        SharedPreferences.Editor editor = getSharedPreferences(OAUTH_PREFS, MODE_PRIVATE).edit();
        Log.i(TAG, "Client id = "+BuildConfig.CLIENT_ID);
        editor.putString(OAUTH_ID, BuildConfig.CLIENT_ID);
        editor.putString(OAUTH_SECRET, BuildConfig.CLIENT_SECRET);
        editor.commit();

    }


}
