package mmmarcy.github.deviantand.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import mmmarcy.github.deviantand.R;
import mmmarcy.github.deviantand.net.DeviantHTTPClient;
import mmmarcy.github.deviantand.net.oauth.Token;


public class LoginActivity extends Activity {

    public static final String LOGIN_PREFS = "login_prefs";
    public static final String OAUTH_TOKEN = "token";
    private static final String TAG = LoginActivity.class.getName();
    protected Optional<DeviantHTTPClient> deviantHTTPClient = Optional.absent();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Optional<Token> oauthToken = isTokenPresent();
        bootStrapHTTPClient(oauthToken);
        if (oauthToken.isPresent()) {
            startMainActivity();
        } else {
            setupLoginUI();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (isAfterRedirect(intent)) {
            Log.i(TAG, "User accepted the application, now requesting the access token");
            new GetAccessTokenTask().execute(intent.getData().getQueryParameter("code"));
        }
    }


    /**
     * Checks if the intent that started the activity
     * is obtained from the redirect in the oauth
     * handshake
     *
     * @param intent The intentet that started the activity
     * @return If the intent is the redirect
     */
    protected boolean isAfterRedirect(Intent intent) {
        //TODO: add the check for the domain/path
        return intent != null &&
                intent.getAction() != null &&
                intent.getAction().equals(Intent.ACTION_VIEW) &&
                intent.getData() != null &&
                intent.getData().getScheme().equals("deviantand");
    }


    /**
     * Set up the DeviantHTTPClient accordingly
     *
     * @param oauthToken
     */
    protected void bootStrapHTTPClient(Optional<Token> oauthToken) {
        if (!deviantHTTPClient.isPresent()) {
            deviantHTTPClient = Optional.of(
                    DeviantHTTPClient.getInstance(getApplicationContext())
            );
        }
        if (oauthToken.isPresent()) {
            deviantHTTPClient.get().setOauthToken(oauthToken);
        }
    }

    /**
     * Method that starts the activity that follows after
     * the login step
     */
    protected void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Method that loads the UI components needed for performing
     * the login
     */
    protected void setupLoginUI() {
        setContentView(R.layout.activity_login);
        Button button = (Button) findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetUserAuthorizationTask().execute();
            }
        });
    }

    /**
     * If there is already a oauth token saved in the preferences
     * we can skip the login activity
     *
     * @return If the oauth token is already set
     */
    protected Optional<Token> isTokenPresent() {
        SharedPreferences prefs = getSharedPreferences(LOGIN_PREFS, MODE_PRIVATE);
        Optional<Token> tokenOptional = Optional.absent();
        try {
            tokenOptional = Optional.fromNullable(
                    new Gson().fromJson(prefs.getString(OAUTH_TOKEN, ""), Token.class)
            );
        } catch (JsonSyntaxException e) {
            Log.d(TAG, "No Json serialization of the token");
        }
        return tokenOptional;
    }


    /**
     * Async task that opens the browser for ask the authorization of the user
     */
    class GetUserAuthorizationTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            if (deviantHTTPClient.isPresent())
                return deviantHTTPClient.get().requestAuthorizationToken().get();
            else
                throw new IllegalStateException("DeviantHTTPClient is not present");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(s));
            startActivity(i);
        }
    }

    /**
     * Class that gets the Bearer Token.
     */
    class GetAccessTokenTask extends AsyncTask<String, Void, Optional<Token>> {

        @Override
        protected Optional<Token> doInBackground(String... params) {
            return deviantHTTPClient.get().requestUserAccessToken(params[0]);
        }

        @Override
        protected void onPostExecute(Optional<Token> tokenOptional) {
            super.onPostExecute(tokenOptional);
            if (tokenOptional.isPresent()) {
                getSharedPreferences(LOGIN_PREFS, Context.MODE_PRIVATE).edit()
                        .putString(OAUTH_TOKEN, new Gson().toJson(tokenOptional.get(), Token.class))
                        .commit();
                startMainActivity();
            } else {
                Log.wtf(TAG, "An error occurred while getting the access token");
            }
        }
    }
}
