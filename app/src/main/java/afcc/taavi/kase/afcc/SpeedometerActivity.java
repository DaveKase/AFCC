package afcc.taavi.kase.afcc;

import android.os.Bundle;

/**
 * Created by Taavi Kase
 *
 * Speedometer activity
 */
public class SpeedometerActivity extends BaseActivity {
    /**
     * Called when Activity is first created
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speedometer);
    }
}
