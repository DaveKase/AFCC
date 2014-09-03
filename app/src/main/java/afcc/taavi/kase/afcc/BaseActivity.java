package afcc.taavi.kase.afcc;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Taavi Kase.
 */
public class BaseActivity extends Activity {
    public static final String ID ="id";
    /**
     * Creates toast message
     */
    public static void makeToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}