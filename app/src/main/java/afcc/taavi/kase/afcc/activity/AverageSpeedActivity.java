package afcc.taavi.kase.afcc.activity;

import android.os.Bundle;

import afcc.taavi.kase.afcc.R;

/**
 * Created by Taavi Kase on 24.09.2014.
 *
 * Average speed calculator activity
 */
public class AverageSpeedActivity extends BaseActivity {

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