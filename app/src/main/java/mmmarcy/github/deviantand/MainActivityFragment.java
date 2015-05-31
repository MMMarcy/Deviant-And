package mmmarcy.github.deviantand;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.common.base.Optional;

import mmmarcy.github.deviantand.net.DeviantHTTPClient;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private static final String TAG = MainActivityFragment.class.getName();

    protected Optional<DeviantHTTPClient> deviantHTTPClient = Optional.absent();


    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!deviantHTTPClient.isPresent()) {
            deviantHTTPClient = Optional.of(
                    DeviantHTTPClient.getInstance(getActivity().getApplicationContext())
            );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button button = (Button) view.findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetUserAuthorizationTask().execute();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getActivity().getIntent();
        if (intent != null &&
                intent.getAction() != null &&
                intent.getAction().equals(Intent.ACTION_VIEW) &&
                intent.getData() != null &&
                intent.getData().getScheme().equals("deviantand")
            //TODO: add the check for the domain/path
                ) {
            Log.i(TAG, "User accepted the application, now requesting the access token");
            new GetAccessTokenTask().execute(intent.getData().getQueryParameter("code"));
        }
    }

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

    class GetAccessTokenTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            return deviantHTTPClient.get().requestUserAccessToken(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean isSuccessful) {
            super.onPostExecute(isSuccessful);
            if (isSuccessful) {
                //TODO: start activity after login
            } else {
                Log.wtf(TAG, "An error occurred while getting the access token");
            }
        }
    }


}
