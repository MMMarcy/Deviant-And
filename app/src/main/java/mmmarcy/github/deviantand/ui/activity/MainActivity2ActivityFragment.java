package mmmarcy.github.deviantand.ui.activity;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mmmarcy.github.deviantand.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivity2ActivityFragment extends Fragment {

    public MainActivity2ActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_activity2, container, false);
    }
}