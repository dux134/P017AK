package in.dux.p017ak;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    private Firebase reference1;
    private ProgressDialog progressDialog;
    private TextView email,name,message;
    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.feedbackToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://p017ak-779c2.firebaseio.com/Feedbacks/");


        email = findViewById(R.id.feedbackEmail);
        name = findViewById(R.id.feedbackName);
        message = findViewById(R.id.feedbackMessage);
        submit = findViewById(R.id.feedbackButton);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),"Email cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(name.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),"Name cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(message.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(getApplicationContext(),"Message cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog = new ProgressDialog(Feedback.this);
                progressDialog.setMessage("Loading...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                Date currentTime = Calendar.getInstance().getTime();
                Map<String, String> map = new HashMap<String, String>();
                map.put("Email", email.getText().toString());
                map.put("Name", name.getText().toString());
                map.put("Message", message.getText().toString());
                map.put("Date", currentTime.toString());
                reference1.push().setValue(map);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                },1000);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
