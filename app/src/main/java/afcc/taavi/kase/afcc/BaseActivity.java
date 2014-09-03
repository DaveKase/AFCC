package afcc.taavi.kase.afcc;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Taavi on 3.09.2014.
 */
public class BaseActivity extends Activity {
       public static void makeToast(String text) {
           Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
       }
}