package webview_webapp.jason.webview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread welcomeThread = new Thread() {
            Intent i = new Intent(getApplicationContext(),
                    MainActivity.class);
            @Override
            public void run() {
                try {
                    super.run();

                    sleep(3500);  //Delay of 3.5 seconds
                } catch (Exception e) {

                } finally {


                    startActivity(i);
                    finish();
                }
            }
        };
        welcomeThread.start();

    }
}
