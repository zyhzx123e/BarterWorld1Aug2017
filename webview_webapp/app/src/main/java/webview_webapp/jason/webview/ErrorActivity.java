package webview_webapp.jason.webview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ErrorActivity extends AppCompatActivity {


    TextView txtError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        if(getIntent().getExtras().getString("MSG")!=null){
            txtError=(TextView)findViewById(R.id.textView);
            txtError.setText(getIntent().getExtras().getString("MSG"));
        }

    }

    public  void btn_retry_click(View v){

        Redirect();

    }

    public  void btn_exit_click(View v){

        finish();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Redirect();
    }
    
    
    private void Redirect(){
        if(Common.connectionAvailable(ErrorActivity.this)){
            Intent home = new Intent(ErrorActivity.this,MainActivity.class);
            startActivity(home);
            finish();
        }else {
            Toast.makeText(this, R.string.ErrorActivity_connection_str, Toast.LENGTH_SHORT).show();
            
        }
        
    }
    
    
    
}
