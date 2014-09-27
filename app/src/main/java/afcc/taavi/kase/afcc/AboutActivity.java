package afcc.taavi.kase.afcc;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Taavi Kase on 25.09.2014.
 *
 * Average fuel consumption activity
 */
public class AboutActivity extends Activity {
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }
}