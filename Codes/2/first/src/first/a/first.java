package first.a;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class first extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TextView tv = new TextView(this);
        tv.setText("你好我的朋友！");
        setContentView(tv);

    }
}



