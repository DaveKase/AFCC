package afcc.taavi.kase.afcc.activity;

import android.util.Log;

import junit.framework.TestCase;

/**
 * Created by Taavi Kase on 10.07.2015 16:57.
 */
@SuppressWarnings("ALL")
public class AverageFuelConsumptionActivityTest extends TestCase {
    private static final String TAG = "AfccActivityTest";

    public void testCalculate() throws Exception {
        double distance = -0.1;
        double fuel = -0.1;
        double value = (fuel * 100) / distance;
        String result = String.valueOf(value);
        Log.d(TAG, "result = " + result);
    }
}