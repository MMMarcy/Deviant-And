package mmmarcy.github.deviantand.net.oauth;


import android.util.Log;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.common.base.Optional;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by Marcello Steiner on 17/05/15.
 */
public class AuthenticationCodeHandler {

    protected static final String TAG = AuthenticationCodeHandler.class.getSimpleName();

    protected HttpRequestFactory requestFactory;

    protected String[] SCOPES = new String[]
            {
                    "basic",
                    "browse",
                    "collection",
                    "comment.post",
                    "feed",
                    "stash",
                    "user.manage"
            };

    protected Optional<String>
            CLIENT_ID = Optional.absent(),
            CLIENT_SECRET = Optional.absent(),
            ACCESS_URL = Optional.of("https://www.deviantart.com/oauth2/token"),
            AUTHORIZATION_URL = Optional.of("https://www.deviantart.com/oauth2/authorize"),
            REDIRECT_URI = Optional.of("deviantand://oauth2"),
            RESPONSE_TYPE = Optional.of("code");

    protected Optional<Token> accessToken = Optional.absent();


    protected AuthenticationCodeHandler() {
    }

    public static Builder newBuilder() {
        return new Builder();
    }


    /**
     * Method that build the HttpRequest necessary to get the User Authorization
     *
     * @return The HttpRequest to be executed
     */
    protected Optional<HttpRequest> buildAuthorizationRequest() {
        try {
            GenericUrl url = new GenericUrl(AUTHORIZATION_URL.get());
            url.set("response_type", RESPONSE_TYPE.get())
                    .set("client_id", CLIENT_ID.get())
                    .set("redirect_uri", REDIRECT_URI.get())
                    .set("scope", getSpaceSeparatedScopes())
                            //TODO: create function for random state;
                    .set("state", "something");
            HttpRequest request = requestFactory.buildGetRequest(url);
            request.setFollowRedirects(false);
            request.setThrowExceptionOnExecuteError(false);
            return Optional.fromNullable(request);
        } catch (IOException e) {
            Log.wtf(TAG, Arrays.toString(e.getStackTrace()));
        }
        return Optional.absent();
    }

    /**
     * Method that executes the Authorization Request
     *
     * @return URL to which the user must be redirected to accept the application
     */
    public Optional<String> requestAuthorizationToken() {
        HttpResponse response;
        try {
            response = buildAuthorizationRequest().get().execute();
            Log.i(TAG, response.parseAsString());
            return Optional.fromNullable(response.getHeaders().getLocation());
        } catch (IOException e) {
            Log.wtf(TAG, Arrays.toString(e.getStackTrace()));
        }
        return Optional.absent();
    }


    /**
     * Method that build the request needed to get the Bearer Token.
     *
     * @param code access code obtained after the user accepted the App.
     * @return The HttpRequest to be executed
     */
    protected Optional<HttpRequest> buildAccessTokenRequest(String code) {
        try {
            GenericUrl url = new GenericUrl(ACCESS_URL.get());
            url.set("client_id", CLIENT_ID.get())
                    .set("client_secret", CLIENT_SECRET.get())
                    .set("grant_type", "authorization_code")
                    .set("code", code)
                    .set("redirect_uri", REDIRECT_URI.get())
                            //TODO: create function for random state;
                    .set("state", "something");
            HttpRequest request = requestFactory.buildGetRequest(url);
            request.setFollowRedirects(false);
            request.setThrowExceptionOnExecuteError(false);
            return Optional.fromNullable(request);
        } catch (IOException e) {
            Log.wtf(TAG, Arrays.toString(e.getStackTrace()));
        }
        return Optional.absent();
    }


    /**
     * The method that executes the final step of the Oauth2 Handshake.
     *
     * @param code The code obtained when the user accepted the application
     * @return The instance that must be used to perform the requests to the Deviant Art APIs
     */
    public Optional<Token> requestUserAccessToken(String code) {
        HttpResponse response;
        try {
            response = buildAccessTokenRequest(code).get().execute();
            Log.i(TAG, response.getStatusMessage());
            if (response.isSuccessStatusCode()) {
                accessToken = Optional.of(response.parseAs(Token.class));
                return accessToken;
            } else {
                Log.wtf(TAG, response.parseAsString());
            }
        } catch (IOException e) {
            Log.wtf(TAG, Arrays.toString(e.getStackTrace()));
        }
        return Optional.absent();
    }

    /**
     * Utility method used to concat scopes
     *
     * @return Space separated list of scopes
     */
    protected String getSpaceSeparatedScopes() {
        StringBuilder builder = new StringBuilder();
        for (String s : SCOPES) {
            builder.append(s);
            builder.append(' ');
        }
        return builder
                .deleteCharAt(builder.length() - 1)
                .toString();

    }

    public void setAccessToken(Optional<Token> accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Builder class for the Auth Handler
     */
    public static class Builder {

        protected AuthenticationCodeHandler instance = new AuthenticationCodeHandler();

        public Builder setClientId(String CLIENT_ID) {
            if (!Optional.fromNullable(CLIENT_ID).isPresent())
                throw new IllegalArgumentException();
            instance.CLIENT_ID = Optional.of(CLIENT_ID);
            return this;
        }

        public Builder setClientSecret(String CLIENT_SECRET) {
            if (!Optional.fromNullable(CLIENT_SECRET).isPresent())
                throw new IllegalArgumentException();
            instance.CLIENT_SECRET = Optional.of(CLIENT_SECRET);
            return this;
        }

        public Builder setRequestFactory(HttpRequestFactory requestFactory) {
            if (!Optional.fromNullable(requestFactory).isPresent())
                throw new IllegalArgumentException();
            instance.requestFactory = requestFactory;
            return this;
        }

        public AuthenticationCodeHandler getInstance() {

            if (!areNecessaryElemPresent())
                throw new IllegalStateException("Missing parameter");

            return instance;
        }

        protected boolean areNecessaryElemPresent() {
            return instance.requestFactory != null &&
                    instance.CLIENT_ID.isPresent() &&
                    instance.CLIENT_SECRET.isPresent();
        }


    }

}
