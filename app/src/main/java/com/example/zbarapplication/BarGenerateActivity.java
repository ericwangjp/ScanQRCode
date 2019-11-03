package com.example.zbarapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ericjp.zbar.util.QRCodeUtil;
import com.ericjp.zbar.zxing.QRCodeDecoder;
import com.ericjp.zbar.zxing.QRCodeEncoder;

public class BarGenerateActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edtQrContent, edtBarContent;
    private Button btnGenerateQrCode, btnGenerateLogoQrCode, btnGenerateCode, btnGenerateCodeNum, btnRecogniseQrCode, btnRecogniseCode,
            btnRecogniseCodeWithLogo, btnRecogniseCodeWithNum;
    private ImageView imgQrCode, imgLogoQrCode, imgCode, imgCodeNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_generate);
        edtQrContent = findViewById(R.id.edt_input_qr);
        edtBarContent = findViewById(R.id.edt_input_num);
        btnGenerateQrCode = findViewById(R.id.btn_generate_qr_code);
        btnGenerateLogoQrCode = findViewById(R.id.btn_generate_logo_qr_code);
        imgQrCode = findViewById(R.id.img_qr_code);
        imgLogoQrCode = findViewById(R.id.img_logo_qr_code);
        btnGenerateCode = findViewById(R.id.btn_generate_code);
        btnGenerateCodeNum = findViewById(R.id.btn_generate_code_num);
        imgCode = findViewById(R.id.img_code);
        imgCodeNum = findViewById(R.id.img_code_num);
        btnRecogniseQrCode = findViewById(R.id.btn_recognise_qr_code);
        btnRecogniseCode = findViewById(R.id.btn_recognise_code);
        btnRecogniseCodeWithLogo = findViewById(R.id.btn_recognise_code_with_logo);
        btnRecogniseCodeWithNum = findViewById(R.id.btn_recognise_code_with_num);
        initData();
    }

    private void initData() {
        btnGenerateQrCode.setOnClickListener(this);
        btnGenerateLogoQrCode.setOnClickListener(this);
        btnGenerateCode.setOnClickListener(this);
        btnGenerateCodeNum.setOnClickListener(this);
        btnRecogniseQrCode.setOnClickListener(this);
        btnRecogniseCode.setOnClickListener(this);
        btnRecogniseCodeWithLogo.setOnClickListener(this);
        btnRecogniseCodeWithNum.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_generate_qr_code:
//                生成二维码
                final String qrContent = edtQrContent.getText().toString();
                if (TextUtils.isEmpty(qrContent)) {
                    Toast.makeText(BarGenerateActivity.this, "请输入二维码内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        return QRCodeEncoder.syncEncodeQRCode(qrContent, QRCodeUtil.dp2px(BarGenerateActivity.this, 150), Color.parseColor("#ff0000"));
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            imgQrCode.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(BarGenerateActivity.this, "生成二维码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.id.btn_generate_logo_qr_code:
//生成带logo的二维码
                final String qrLogoContent = edtQrContent.getText().toString();
                if (TextUtils.isEmpty(qrLogoContent)) {
                    Toast.makeText(BarGenerateActivity.this, "请输入二维码内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        Bitmap logoBitmap = BitmapFactory.decodeResource(BarGenerateActivity.this.getResources(), R.mipmap.ic_launcher);
                        return QRCodeEncoder.syncEncodeQRCode(qrLogoContent, QRCodeUtil.dp2px(BarGenerateActivity.this, 150), Color.BLACK, Color.WHITE,
                                logoBitmap);
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            imgLogoQrCode.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(BarGenerateActivity.this, "生成带logo二维码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.id.btn_generate_code:
                final String barContent = edtBarContent.getText().toString();
                if (TextUtils.isEmpty(barContent)) {
                    Toast.makeText(BarGenerateActivity.this, "请输入条形码内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        int width = QRCodeUtil.dp2px(BarGenerateActivity.this, 150);
                        int height = QRCodeUtil.dp2px(BarGenerateActivity.this, 70);
                        return QRCodeEncoder.syncEncodeBarcode(barContent, width, height, 0);
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            imgCode.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(BarGenerateActivity.this, "生成底部不带文字条形码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.id.btn_generate_code_num:
                final String barNumContent = edtBarContent.getText().toString();
                if (TextUtils.isEmpty(barNumContent)) {
                    Toast.makeText(BarGenerateActivity.this, "请输入条形码内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                new AsyncTask<Void, Void, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(Void... params) {
                        int width = QRCodeUtil.dp2px(BarGenerateActivity.this, 150);
                        int height = QRCodeUtil.dp2px(BarGenerateActivity.this, 70);
                        int textSize = QRCodeUtil.sp2px(BarGenerateActivity.this, 14);
                        return QRCodeEncoder.syncEncodeBarcode(barNumContent, width, height, textSize);
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        if (bitmap != null) {
                            imgCodeNum.setImageBitmap(bitmap);
                        } else {
                            Toast.makeText(BarGenerateActivity.this, "生成底部带文字条形码失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }.execute();
                break;
            case R.id.btn_recognise_qr_code:
                imgQrCode.setDrawingCacheEnabled(true);
                Bitmap qrCodeDrawingCache = imgQrCode.getDrawingCache();
                decode(qrCodeDrawingCache);
                break;
            case R.id.btn_recognise_code:
                imgCode.setDrawingCacheEnabled(true);
                Bitmap barCode = imgCode.getDrawingCache();
                decode(barCode);
                break;
            case R.id.btn_recognise_code_with_logo:
                imgLogoQrCode.setDrawingCacheEnabled(true);
                Bitmap qrCodeLogo = imgLogoQrCode.getDrawingCache();
                decode(qrCodeLogo);
                break;
            case R.id.btn_recognise_code_with_num:
                imgCodeNum.setDrawingCacheEnabled(true);
                Bitmap barCodeLogo = imgCodeNum.getDrawingCache();
                decode(barCodeLogo);
                break;
            default:
                break;
        }
    }

    private void decode(final Bitmap bitmap) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(BarGenerateActivity.this, "识别失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BarGenerateActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
