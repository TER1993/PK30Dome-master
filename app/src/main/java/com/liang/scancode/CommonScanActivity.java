/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liang.scancode;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.liang.scancode.defineview.MyImageView;
import com.liang.scancode.utils.Constant;
import com.liang.scancode.zxing.ScanListener;
import com.liang.scancode.zxing.ScanManager;
import com.liang.scancode.zxing.decode.DecodeThread;
import com.liang.scancode.zxing.decode.Utils;
import com.spd.pk30dome.R;

import org.greenrobot.eventbus.EventBus;


/**
 * 二维码扫描使用
 */
public final class CommonScanActivity extends Activity implements ScanListener, View.OnClickListener {
    static final String TAG = CommonScanActivity.class.getSimpleName();
    SurfaceView scanPreview = null;
    View scanContainer;
    View scanCropView;
    ImageView scanLine;
    ScanManager scanManager;
    TextView iv_light;
    TextView qrcode_g_gallery;
    TextView qrcode_ic_back;
    final int PHOTOREQUESTCODE = 1111;
    private int scanMode;//扫描模型（条形，二维码，全部）
    private String codeResult;
    private SurfaceView capture_preview;
    private TextView common_title_TV_center;
    private RelativeLayout title_bar;
    private RelativeLayout top_mask;
    private Button service_register_rescan;
    private RelativeLayout bottom_mask;
    private ImageView left_mask;
    private ImageView right_mask;
    private ImageView capture_scan_line;
    private RelativeLayout capture_crop_view;
    private RelativeLayout capture_container;
    private ImageView authorize_return;
    private TextView tv_scan_result;
    private TextView scan_hint;
    private MyImageView scan_image;


    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_scan_code);
        scanMode = getIntent().getIntExtra(Constant.REQUEST_SCAN_MODE, Constant.REQUEST_SCAN_MODE_ALL_MODE);
        initView();
    }

    void initView() {

        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = findViewById(R.id.capture_container);
        scanCropView = findViewById(R.id.capture_crop_view);
        scanLine = (ImageView) findViewById(R.id.capture_scan_line);
        qrcode_g_gallery = (TextView) findViewById(R.id.qrcode_g_gallery);
        qrcode_g_gallery.setOnClickListener(this);
        qrcode_ic_back = (TextView) findViewById(R.id.qrcode_ic_back);
        qrcode_ic_back.setOnClickListener(this);
        iv_light = (TextView) findViewById(R.id.iv_light);
        iv_light.setOnClickListener(this);

        //构造出扫描管理器
        scanManager = new ScanManager(this, scanPreview, scanContainer, scanCropView, scanLine, scanMode, this);
        capture_preview = (SurfaceView) findViewById(R.id.capture_preview);
        capture_preview.setOnClickListener(this);
        common_title_TV_center = (TextView) findViewById(R.id.common_title_TV_center);
        common_title_TV_center.setOnClickListener(this);
        title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        title_bar.setOnClickListener(this);
        top_mask = (RelativeLayout) findViewById(R.id.top_mask);
        top_mask.setOnClickListener(this);
        service_register_rescan = (Button) findViewById(R.id.service_register_rescan);
        service_register_rescan.setOnClickListener(this);
        bottom_mask = (RelativeLayout) findViewById(R.id.bottom_mask);
        bottom_mask.setOnClickListener(this);
        left_mask = (ImageView) findViewById(R.id.left_mask);
        left_mask.setOnClickListener(this);
        right_mask = (ImageView) findViewById(R.id.right_mask);
        right_mask.setOnClickListener(this);
        capture_scan_line = (ImageView) findViewById(R.id.capture_scan_line);
        capture_scan_line.setOnClickListener(this);
        capture_crop_view = (RelativeLayout) findViewById(R.id.capture_crop_view);
        capture_crop_view.setOnClickListener(this);
        capture_container = (RelativeLayout) findViewById(R.id.capture_container);
        capture_container.setOnClickListener(this);
        authorize_return = (ImageView) findViewById(R.id.authorize_return);
        authorize_return.setOnClickListener(this);
        tv_scan_result = (TextView) findViewById(R.id.tv_scan_result);
        tv_scan_result.setOnClickListener(this);
        scan_hint = (TextView) findViewById(R.id.scan_hint);
        scan_hint.setOnClickListener(this);
        scan_image = (MyImageView) findViewById(R.id.scan_image);
        scan_image.setOnClickListener(this);
        service_register_rescan.setOnClickListener(this);
        authorize_return.setOnClickListener(this);

        switch (scanMode) {
            case DecodeThread.BARCODE_MODE:
                common_title_TV_center.setText(R.string.scan_barcode_title);
                scan_hint.setText(R.string.scan_barcode_hint);
                break;
            case DecodeThread.QRCODE_MODE:
                common_title_TV_center.setText(R.string.scan_qrcode_title);
                scan_hint.setText(R.string.scan_qrcode_hint);
                break;
            case DecodeThread.ALL_MODE:
                common_title_TV_center.setText(R.string.scan_allcode_title);
                scan_hint.setText(R.string.scan_allcode_hint);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scanManager.onResume();
        service_register_rescan.setVisibility(View.INVISIBLE);
        scan_image.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        scanManager.onPause();
    }

    /**
     *
     */
    @Override
    public void scanResult(Result rawResult, Bundle bundle) {
        //扫描成功后，扫描器不会再连续扫描，如需连续扫描，调用reScan()方法。
        //scanManager.reScan();
//		Toast.makeText(that, "result="+rawResult.getText(), Toast.LENGTH_LONG).show();

        if (!scanManager.isScanning()) { //如果当前不是在扫描状态
            //设置再次扫描按钮出现
            service_register_rescan.setVisibility(View.VISIBLE);
            scan_image.setVisibility(View.VISIBLE);
            Bitmap barcode = null;
            byte[] compressedBitmap = bundle.getByteArray(DecodeThread.BARCODE_BITMAP);
            if (compressedBitmap != null) {
                barcode = BitmapFactory.decodeByteArray(compressedBitmap, 0, compressedBitmap.length, null);
                barcode = barcode.copy(Bitmap.Config.ARGB_8888, true);
            }
            scan_image.setImageBitmap(barcode);
        }
        service_register_rescan.setVisibility(View.VISIBLE);
        scan_image.setVisibility(View.VISIBLE);
        tv_scan_result.setVisibility(View.VISIBLE);
        boolean cn = getApplicationContext().getResources().getConfiguration().locale.getCountry().equals("CN");
        if (cn) {
            tv_scan_result.setText("结果：" + rawResult.getText());
        } else {
            tv_scan_result.setText("Result：" + rawResult.getText());
        }

        codeResult = rawResult.getText();
    }

    void startScan() {
        if (service_register_rescan.getVisibility() == View.VISIBLE) {
            service_register_rescan.setVisibility(View.INVISIBLE);
            scan_image.setVisibility(View.GONE);
            scanManager.reScan();
        }
    }

    @Override
    public void scanError(Exception e) {
        Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();

        boolean cn = getApplicationContext().getResources().getConfiguration().locale.getCountry().equals("CN");
        //相机扫描出错时
        if (e.getMessage() != null && e.getMessage().startsWith("相机")) {
            if (cn) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Camera open error, please check if this permission is disabled!", Toast.LENGTH_LONG).show();
            }
            scanPreview.setVisibility(View.INVISIBLE);
        } else if (e.getMessage() != null && e.getMessage().startsWith("图片")) {
            if (cn) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "The picture is incorrect, or the picture is blurred!", Toast.LENGTH_LONG).show();
            }
        } else {
            if (cn) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void showPictures(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photo_path;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTOREQUESTCODE:
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(data.getData(), proj, null, null, null);
                    if (cursor.moveToFirst()) {
                        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        photo_path = cursor.getString(colum_index);
                        if (photo_path == null) {
                            photo_path = Utils.getPath(getApplicationContext(), data.getData());
                        }
                        scanManager.scanningImage(photo_path);
                    }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qrcode_g_gallery:
                showPictures(PHOTOREQUESTCODE);
                break;
            case R.id.iv_light:
                scanManager.switchLight();
                break;
            case R.id.qrcode_ic_back:
                EventBus.getDefault().post(new MsgEvent("codeResult", codeResult));
                CommonScanActivity.this.finish();
                break;
            case R.id.service_register_rescan://再次开启扫描
                startScan();
                break;
            case R.id.authorize_return:
                finish();
                break;
            default:
                break;
        }
    }

}