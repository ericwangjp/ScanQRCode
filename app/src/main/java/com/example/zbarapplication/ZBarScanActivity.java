package com.example.zbarapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ericjp.zbar.util.QRCodeUtil;
import com.ericjp.zbar.view.QRCodeView;
import com.ericjp.zbar.view.ZBarView;

public class ZBarScanActivity extends AppCompatActivity implements QRCodeView.Delegate, View.OnClickListener {

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 1001;
    private ZBarView mZBarView;
    private Button btnOpenFlashlight, btnCloseFlashlight, btnRecogniseGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        initData();
    }

    private void initData() {
        QRCodeUtil.setDebug(true);
        mZBarView = findViewById(R.id.zbarview);
        btnOpenFlashlight = findViewById(R.id.btn_open_flashlight);
        btnCloseFlashlight = findViewById(R.id.btn_close_flashlight);
        btnRecogniseGallery = findViewById(R.id.btn_recognise_gallery);
        mZBarView.setDelegate(this);
        btnOpenFlashlight.setOnClickListener(this);
        btnCloseFlashlight.setOnClickListener(this);
        btnRecogniseGallery.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//        mZBarView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT); // 打开前置摄像头开始预览，但是并未开始识别
        mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
    }

    @Override
    protected void onStop() {
        mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mZBarView.onDestroy(); // 销毁二维码扫描控件
        super.onDestroy();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        Log.d("扫描成功", "-->" + result);
        Toast.makeText(ZBarScanActivity.this, "扫描成功：" + result, Toast.LENGTH_SHORT).show();
        mZBarView.startSpot(); // 开始识别
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {
        // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
        String tipText = mZBarView.getScanBoxView().getTipText();
        String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                mZBarView.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                mZBarView.getScanBoxView().setTipText(tipText);
            }
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Toast.makeText(ZBarScanActivity.this, "无法打开摄像头，请检查是否有相关权限", Toast.LENGTH_SHORT).show();
        Log.e("error-->", "打开相机出错");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mZBarView.showScanRect();
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
//            相册选择照片返回
            if (data != null) {
                String picturePath = QRCodeUtil.getImagePath(ZBarScanActivity.this, data);
                mZBarView.decodeQRCode(picturePath);
            } else {
                Toast.makeText(ZBarScanActivity.this, "获取图片路径失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.start_preview:
//                mZBarView.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
//                break;
//            case R.id.stop_preview:
//                mZBarView.stopCamera(); // 关闭摄像头预览，并且隐藏扫描框
//                break;
//            case R.id.start_spot:
//                mZBarView.startSpot(); // 开始识别
//                break;
//            case R.id.stop_spot:
//                mZBarView.stopSpot(); // 停止识别
//                break;
//            case R.id.start_spot_showrect:
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.stop_spot_hiddenrect:
//                mZBarView.stopSpotAndHiddenRect(); // 停止识别，并且隐藏扫描框
//                break;
//            case R.id.show_scan_rect:
//                mZBarView.showScanRect(); // 显示扫描框
//                break;
//            case R.id.hidden_scan_rect:
//                mZBarView.hiddenScanRect(); // 隐藏扫描框
//                break;
//            case R.id.decode_scan_box_area:
//                mZBarView.getScanBoxView().setOnlyDecodeScanBoxArea(true); // 仅识别扫描框中的码
//                break;
//            case R.id.decode_full_screen_area:
//                mZBarView.getScanBoxView().setOnlyDecodeScanBoxArea(false); // 识别整个屏幕中的码
//                break;
            case R.id.btn_open_flashlight:
                mZBarView.openFlashlight(); // 打开闪光灯
                break;
            case R.id.btn_close_flashlight:
                mZBarView.closeFlashlight(); // 关闭闪光灯
                break;
//            case R.id.scan_one_dimension:
//                mZBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZBarView.setType(BarcodeType.ONE_DIMENSION, null); // 只识别一维条码
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_two_dimension:
//                mZBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZBarView.setType(BarcodeType.TWO_DIMENSION, null); // 只识别二维条码
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_qr_code:
//                mZBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZBarView.setType(BarcodeType.ONLY_QR_CODE, null); // 只识别 QR_CODE
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_code128:
//                mZBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZBarView.setType(BarcodeType.ONLY_CODE_128, null); // 只识别 CODE_128
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_ean13:
//                mZBarView.changeToScanBarcodeStyle(); // 切换成扫描条码样式
//                mZBarView.setType(BarcodeType.ONLY_EAN_13, null); // 只识别 EAN_13
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_high_frequency:
//                mZBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZBarView.setType(BarcodeType.HIGH_FREQUENCY, null); // 只识别高频率格式，包括 QR_CODE、ISBN13、UPC_A、EAN_13、CODE_128
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_all:
//                mZBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                mZBarView.setType(BarcodeType.ALL, null); // 识别所有类型的码
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
//            case R.id.scan_custom:
//                mZBarView.changeToScanQRCodeStyle(); // 切换成扫描二维码样式
//                List<BarcodeFormat> formatList = new ArrayList<>();
//                formatList.add(BarcodeFormat.QRCODE);
//                formatList.add(BarcodeFormat.ISBN13);
//                formatList.add(BarcodeFormat.UPCA);
//                formatList.add(BarcodeFormat.EAN13);
//                formatList.add(BarcodeFormat.CODE128);
//                mZBarView.setType(BarcodeType.CUSTOM, formatList); // 自定义识别的类型
//                mZBarView.startSpotAndShowRect(); // 显示扫描框，并开始识别
//                break;
            case R.id.btn_recognise_gallery:
//                /*
//                从相册选取二维码图片，这里为了方便演示，使用的是
//                https://github.com/bingoogolapple/BGAPhotoPicker-Android
//                这个库来从图库中选择二维码图片，这个库不是必须的，你也可以通过自己的方式从图库中选择图片
//                 */
//                Intent photoPickerIntent = new BGAPhotoPickerActivity.IntentBuilder(this)
//                        .cameraFileDir(null)
//                        .maxChooseCount(1)
//                        .selectedPhotos(null)
//                        .pauseOnScroll(false)
//                        .build();
//                startActivityForResult(photoPickerIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(pickIntent, REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY);
                break;
        }
    }
}
