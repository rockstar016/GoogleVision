package recog.com.imagerecognition;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    EditText password, confirm_password, email;
    Button signup;
    FirebaseAuth mAuth;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        password = (EditText)findViewById(R.id.sign_password);
        confirm_password = (EditText)findViewById(R.id.sign_confirm);
        email = (EditText)findViewById(R.id.sign_email);
        signup = (Button)findViewById(R.id.bt_register);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnSignupButton();
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }
    public void showDialog(){
        dialog = ProgressDialog.show(this, "Sign up","");

    }
    public void hideDialog(){
        if(dialog.isShowing())
            dialog.dismiss();
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void setOnSignupButton(){
        if(!validAllFields()) return;
        showDialog();
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        hideDialog();
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }else{
                            GotoLogin();
                        }

                    }

                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        hideDialog();
                        e.printStackTrace();
                    }
                });
    }
    private void GotoLogin(){
        Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }
    private boolean validAllFields(){
        if(email.getText().toString().isEmpty()){
            email.setError("Fill out this field");
            return false;
        }
        else if(isValidEmail(email.getText().toString()) == false){
            email.setError("input valid email address");
            return false;
        }

        else if(password.getText().toString().isEmpty()) {
            password.setError("Fill out this field");
            return false;
        }
        else if(confirm_password.getText().toString().isEmpty()){
            confirm_password.setError("Fill out this field");
            return false;
        }
        else if(password.getText().toString().equals(confirm_password.getText().toString()) == false){
            password.setError("doesn't match");
            confirm_password.setError("doesn't match");
            return false;
        }
        else if(password.getText().toString().length() < 6){
            password.setError("At least 6 characters");
            confirm_password.setError("At least 6 characters");
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            Intent i = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        }
        return true;
    }
}
