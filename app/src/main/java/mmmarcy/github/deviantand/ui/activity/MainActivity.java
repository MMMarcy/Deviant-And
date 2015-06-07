package mmmarcy.github.deviantand.ui.activity;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import mmmarcy.github.deviantand.R;

public class MainActivity extends AppCompatActivity {

    protected static final String TAG = MainActivity.class.getSimpleName();

    DrawerLayout mDrawerLayout = null;
    NavigationView mNavigationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_mainActivity);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view_mainActivity);
        mNavigationView.setNavigationItemSelectedListener(new MainActivityNavigationListener());
    }


    class MainActivityNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            Log.i(TAG, "Clieckd on " + menuItem.getTitle());
            mDrawerLayout.closeDrawer(mNavigationView);
            return true;
        }
    }

}
