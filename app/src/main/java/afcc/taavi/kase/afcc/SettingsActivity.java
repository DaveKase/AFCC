package afcc.taavi.kase.afcc;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        versionNumber();
    }

    public void save(View clickedButton) {
        makeToast("Save clicked");
    }

    private void versionNumber() {
        String version = "";
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