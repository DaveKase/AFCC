package afcc.taavi.kase.afcc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

/**
 * Created by Taavi Kase
 */
public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";

    /**
     * Called when Activity is first created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        makeToast("Just for testing");
        Log.e("MAIN", "TEST");
    }

    /**
     * Creates an options menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Called when an item is selected from options menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * Called when a button is pressed in activity
     */
    public void onClick(View clickedButton) {
        Intent intent = null;

        switch(clickedButton.getId()) {
            case R.id.fuelConsumptionButton:
                intent = new Intent(this, AverageFuelConsumptionActivity.class);
                break;
            case R.id.speedButton:
                //intent = new Intent(this, AverageFuelConsumptionActivity.class);
                break;
            case R.id.speedCalculatorButton:
                //intent = new Intent(this, AverageFuelConsumptionActivity.class);
                break;
        }

        try {
            startActivity(intent);
        }catch(NullPointerException e) {
            Log.e(TAG, "Error with starting activity, have you declared an intent");
        }
    }
}