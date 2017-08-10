package muscular.man.tools.learning.easyjapaneselessions.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;

import com.startapp.android.publish.StartAppSDK;

import muscular.man.tools.learning.easyjapaneselessions.R;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "209582531", true);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
