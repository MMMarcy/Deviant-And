package mmmarcy.github.deviantand.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.base.Optional;

import mmmarcy.github.deviantand.app.AndroidAndApplication;
import mmmarcy.github.deviantand.net.oauth.AuthenticationCodeHandler;
import mmmarcy.github.deviantand.net.oauth.Token;

/**
 * Created by marcello on 20/05/15.
 */
public class DeviantHTTPClient {

    protected static final String TAG = DeviantHTTPClient.class.getSimpleName();

    protected static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    protected static final JsonFactory JSON_FACTORY = new GsonFactory();
    protected static final HttpRequestFactory requestFactory =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });

    protected static DeviantHTTPClient instance = null;
    protected AuthenticationCodeHandler authHandler;
    protected Token token;

    protected DeviantHTTPClient(String clientId, String clientSecret) {
        this.authHandler = buildAuthHandler(clientId, clientSecret);
    }

    /**
     * Singleton method for obtaining the DeviantHttpClient
     *
     * @return The unique instance of this class
     */
    public static synchronized DeviantHTTPClient getInstance(Context context) {
        if (instance == null) {
            SharedPreferences prefs = context.getSharedPreferences(AndroidAndApplication.OAUTH_PREFS, Context.MODE_PRIVATE);
            String clientSecret = prefs.getString(AndroidAndApplication.OAUTH_SECRET, "");
            String clientId = prefs.getString(AndroidAndApplication.OAUTH_ID, "");
            Log.i(TAG, "CREDENTIALS: "+clientId + " " + clientSecret);
            instance = new DeviantHTTPClient(clientId, clientSecret);
        }
        return instance;
    }

    /**
     * Method that build the Oauth Handler
     *
     * @param clientId     The id generated when signing the application on Deviant Art
     * @param clientSecret The app secret generated when signing the application on Deviant Art
     * @return The instance of the Auth Handler
     */
    protected AuthenticationCodeHandler buildAuthHandler(String clientId, String clientSecret) {
        return AuthenticationCodeHandler.newBuilder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRequestFactory(requestFactory)
                .getInstance();
    }


    public Optional<String> requestAuthorizationToken() {
        return authHandler.requestAuthorizationToken();
    }

    public Boolean requestUserAccessToken(String authCode) {
        return authHandler.requestUserAccessToken(authCode);
    }

}
