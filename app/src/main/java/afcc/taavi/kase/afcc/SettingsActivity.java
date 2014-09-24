package afcc.taavi.kase.afcc;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Taavi Kase
 *
 * Settings activity
 */
public class SettingsActivity extends BaseActivity {
    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState May contain data supplied by saveInstanceState or null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        versionName();
    }

    /**
     * Called when Save button is clicked. Saves all data to database
     *
     * @param clickedButton SaveButton
     */
    public void save(View clickedButton) {
        makeToast("Save clicked");
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
            version = "Did not get a version name";
        }

        TextView versionNumberText = (TextView) findViewById(R.id.versionNumberText);
        versionNumberText.setText(version);
    }
}