package com.adfinancegh.aedmoneylending;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class exit extends AppCompatActivity {
    private Button mainMenuBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exit);
        mainMenuBtn = (Button)findViewById(R.id.mainMenuBtn);
    }

    public void homeBtnClicked(View v){
        Intent i = new Intent(this,statLogin.class);
        startActivity(i);
    }
}
