package com.jpmcosta.test.realmtestproject.activity;

import android.content.Intent;
import android.os.Bundle;

import com.jpmcosta.test.realmtestproject.R;
import com.jpmcosta.test.realmtestproject.service.CreateIntentService;

public class CreateActivity extends RealmActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent serviceIntent = new Intent(CreateActivity.this, CreateIntentService.class);
                startService(serviceIntent);

                finish();
            }
        }, 1000);
    }
}