package com.ericjp.zbar.util;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
/**
 * -----------------------------------------------------------------
 * Copyright (C) 2019, by sumpay, All rights reserved.
 * -----------------------------------------------------------------
 * desc: ted
 * Author: wangjp
 * Email: wangjp1@fosun.com
 * Version: Vx.x.x
 * Create: 2019-11-03 11:13
 */
public class QRCodeUtil {
    private static boolean debug;

    public static void setDebug(boolean debug) {
        QRCodeUtil.debug = debug;
    }

    public static boolean isDebug() {
        return debug;
    }

    public static void d(String msg) {
        d("QRCode", msg);
    }

    public static void printRect(String prefix, Rect rect) {
        d("QRCodeFocusArea", prefix + " centerX：" + rect.centerX() + " centerY：" + rect.centerY() + " width：" + rect.width() + " height：" + rect.height()
                + " rectHalfWidth：" + rect.width() / 2 + " rectHalfHeight：" + rect.height() / 2
                + " left：" + rect.left + " top：" + rect.top + " right：" + rect.right + " bottom：" + rect.bottom);
    }

    public static void d(String tag, String msg) {
        if (debug) {
            Log.d(tag, msg);
        }
    }

    public static void e(String msg) {
        if (debug) {
            Log.e("QRCode", msg);
        }
    }

    /**
     * 是否为竖屏
     */
    public static boolean isPortrait(Context context) {
        Point screenResolution = getScreenResolution(context);
        return screenResolution.y > screenResolution.x;
    }

    public static Point getScreenResolution(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenResolution = new Point();
        display.getSize(screenResolution);
        return screenResolution;
    }

    public static int getStatusBarHeight(Context context) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(new int[]{
                android.R.attr.windowFullscreen
        });
        boolean windowFullscreen = typedArray.getBoolean(0, false);
        typedArray.recycle();

        if (windowFullscreen) {
            return 0;
        }

        int height = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }

    public static int dp2px(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, context.getResources().getDisplayMetrics());
    }

    public static Bitmap adjustPhotoRotation(Bitmap inputBitmap, int orientationDegree) {
        if (inputBitmap == null) {
            return null;
        }

        Matrix matrix = new Matrix();
        matrix.setRotate(orientationDegree, (float) inputBitmap.getWidth() / 2, (float) inputBitmap.getHeight() / 2);
        float outputX, outputY;
        if (orientationDegree == 90) {
            outputX = inputBitmap.getHeight();
            outputY = 0;
        } else {
            outputX = inputBitmap.getHeight();
            outputY = inputBitmap.getWidth();
        }

        final float[] values = new float[9];
        matrix.getValues(values);
        float x1 = values[Matrix.MTRANS_X];
        float y1 = values[Matrix.MTRANS_Y];
        matrix.postTranslate(outputX - x1, outputY - y1);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getHeight(), inputBitmap.getWidth(), Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(outputBitmap);
        canvas.drawBitmap(inputBitmap, matrix, paint);
        return outputBitmap;
    }

    public static Bitmap makeTintBitmap(Bitmap inputBitmap, int tintColor) {
        if (inputBitmap == null) {
            return null;
        }

        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap.getWidth(), inputBitmap.getHeight(), inputBitmap.getConfig());
        Canvas canvas = new Canvas(outputBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(tintColor, PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(inputBitmap, 0, 0, paint);
        return outputBitmap;
    }

    /**
     * 计算对焦和测光区域
     *
     * @param coefficient        比率
     * @param originFocusCenterX 对焦中心点X
     * @param originFocusCenterY 对焦中心点Y
     * @param originFocusWidth   对焦宽度
     * @param originFocusHeight  对焦高度
     * @param previewViewWidth   预览宽度
     * @param previewViewHeight  预览高度
     */
    public static Rect calculateFocusMeteringArea(float coefficient,
                                                  float originFocusCenterX, float originFocusCenterY,
                                                  int originFocusWidth, int originFocusHeight,
                                                  int previewViewWidth, int previewViewHeight) {

        int halfFocusAreaWidth = (int) (originFocusWidth * coefficient / 2);
        int halfFocusAreaHeight = (int) (originFocusHeight * coefficient / 2);

        int centerX = (int) (originFocusCenterX / previewViewWidth * 2000 - 1000);
        int centerY = (int) (originFocusCenterY / previewViewHeight * 2000 - 1000);

        RectF rectF = new RectF(QRCodeUtil.clamp(centerX - halfFocusAreaWidth, -1000, 1000),
                QRCodeUtil.clamp(centerY - halfFocusAreaHeight, -1000, 1000),
                QRCodeUtil.clamp(centerX + halfFocusAreaWidth, -1000, 1000),
                QRCodeUtil.clamp(centerY + halfFocusAreaHeight, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top),
                Math.round(rectF.right), Math.round(rectF.bottom));
    }

    public static int clamp(int value, int min, int max) {
        return Math.min(Math.max(value, min), max);
    }

    /**
     * 计算手指间距
     */
    public static float calculateFingerSpacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    /**
     * 将本地图片文件转换成可解码二维码的 Bitmap。为了避免图片太大，这里对图片进行了压缩。感谢 https://github.com/devilsen 提的 PR
     *
     * @param picturePath 本地图片文件路径
     */
    public static Bitmap getDecodeAbleBitmap(String picturePath) {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(picturePath, options);
            int sampleSize = options.outHeight / 400;
            if (sampleSize <= 0) {
                sampleSize = 1;
            }
            options.inSampleSize = sampleSize;
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(picturePath, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取从相册返回的图片路径
     *
     * @param context
     * @param data
     * @return
     */
    public static String getImagePath(Context context, Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        //获取系統版本
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.KITKAT) {
            Log.d("uri=intent.getData :", "" + uri);
            if (DocumentsContract.isDocumentUri(context, uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                Log.d("getDocumentId(uri) :", "" + docId);
                Log.d("uri.getAuthority() :", "" + uri.getAuthority());
                if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                    String id = docId.split(":")[1];
                    String selection = MediaStore.Images.Media._ID + "=" + id;
                    imagePath = getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                    imagePath = getImagePath(context, contentUri, null);
                }

            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                imagePath = getImagePath(context, uri, null);
            }
        } else {
            imagePath = getImagePath(context, uri, null);
        }
        return imagePath;
    }

    /**
     * 通过uri和selection来获取真实的图片路径,从相册获取图片时要用
     */
    private static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
}