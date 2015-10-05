package afcc.taavi.kase.afcc.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import afcc.taavi.kase.afcc.R;

/**
 * Created by Taavi Kase on 25.09.2014.
 *
 * Average fuel consumption activity
 */
public class AboutActivity extends BaseActivity {
    private static final String TAG = "AboutActivity";
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        try {
            //noinspection ConstantConditions
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            Log.e(TAG, "No actionbar");
        }

        versionName();
    }

    /**
     * Gets the app version name and sets it to versionNumberText TextView.
     */
    private void versionName() {
        String version;

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version = getResourceString(R.string.err_no_ver_name);
        }

        TextView versionNumberText = (TextView) findViewById(R.id.versionText);
        String text = getResourceString(R.string.version) + " " + version;
        versionNumberText.setText(text);
    }
}
