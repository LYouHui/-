package com.example.schoolpet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RegisterOrLogin extends AppCompatActivity {
    private Button register;
    private Button log_in;

    public RegisterOrLogin() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_or_login);

        register=(Button)findViewById(R.id.RegisterOnLogin_button_register);
        log_in=(Button)findViewById(R.id.RegisterOrLogin_button_Login);

        register.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(RegisterOrLogin.this,Register.class);
                startActivity(intent);
            }
        });

        log_in.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent=new Intent(RegisterOrLogin.this,Login.class);
                startActivity(intent);
            }
        });
    }
}
