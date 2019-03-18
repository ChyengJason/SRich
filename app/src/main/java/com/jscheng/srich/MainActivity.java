package com.jscheng.srich;

import android.os.Bundle;

import com.jscheng.annotations.Route;

@Route("main")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
