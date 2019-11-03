package com.example.zbarapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnZBarScan, btnZXingScan, btnZXingGenerate, btnJump;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnZBarScan = findViewById(R.id.btn_zbar_scan);
        btnZXingScan = findViewById(R.id.btn_zxing_scan);
        btnZXingGenerate = findViewById(R.id.btn_zxing_generate);
        btnJump = findViewById(R.id.btn_jump);
        btnZBarScan.setOnClickListener(this);
        btnZXingScan.setOnClickListener(this);
        btnZXingGenerate.setOnClickListener(this);
        btnJump.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_zbar_scan:
                startActivityForResult(new Intent(MainActivity.this, ZBarScanActivity.class), 1000);
                break;
            case R.id.btn_zxing_scan:
                startActivityForResult(new Intent(MainActivity.this, ZXingScanActivity.class), 1000);
                break;
            case R.id.btn_zxing_generate:
                startActivityForResult(new Intent(MainActivity.this, BarGenerateActivity.class), 1000);
                break;
            case R.id.btn_jump:
                startActivityForResult(new Intent(MainActivity.this, CameraTestActivity.class), 1000);
                break;
            default:
                break;
        }
    }
}
