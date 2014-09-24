package afcc.taavi.kase.afcc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Average speed calculator activity
 */
public class AverageSpeedActivity extends Activity {
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_speed);
    }
}