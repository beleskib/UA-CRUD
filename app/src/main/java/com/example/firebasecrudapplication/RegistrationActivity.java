package com.example.firebasecrudapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity {

    private TextInputEditText userNameEdt, pwEdt, cnfPwEdt;
    private Button regBtn;
    private ProgressBar loadingPB;
    private FirebaseAuth mAuth;
    private TextView loginTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        userNameEdt = (TextInputEditText) findViewById(R.id.idEditUserName);
        pwEdt = (TextInputEditText) findViewById(R.id.idEditPassword);
        cnfPwEdt = (TextInputEditText) findViewById(R.id.idEditConfirmPassword);
        regBtn = (Button) findViewById(R.id.idBtnRegister);
        loadingPB = (ProgressBar) findViewById(R.id.idPBloading);
        loginTV = (TextView) findViewById(R.id.txtViewLogin);
        mAuth = FirebaseAuth.getInstance();

        loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);

                String userName = userNameEdt.getText().toString();
                String pwd = pwEdt.getText().toString();
                String cnfPwd = cnfPwEdt.getText().toString();

                if (!pwd.equals(cnfPwd)) {
                    Toast.makeText(RegistrationActivity.this, "Please check both having same password..", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(userName) && TextUtils.isEmpty(pwd) && TextUtils.isEmpty(cnfPwd)) {

                    Toast.makeText(RegistrationActivity.this, "Please enter your credentials..", Toast.LENGTH_SHORT).show();
                } else {

                    mAuth.createUserWithEmailAndPassword(userName, pwd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "User Registered..", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
                            } else {

                                loadingPB.setVisibility(View.GONE);
                                Toast.makeText(RegistrationActivity.this, "Fail to register user..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}