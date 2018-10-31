package com.spd.pk30dome.menu.view;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.spd.pk30dome.R;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

/**
 * @author xuyan 单独页面申请权限
 */
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        permission();
    }


    /**
     * 权限申请
     */
    private void permission() {
        AndPermission.with(FirstActivity.this)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.BLUETOOTH
                        , Manifest.permission.BLUETOOTH_ADMIN
                        , Manifest.permission.VIBRATE
                        , Manifest.permission.CAMERA)
                .callback(listener)
                .rationale((requestCode, rationale) -> AndPermission.rationaleDialog(FirstActivity.this, rationale).show()).start();
    }

    PermissionListener listener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            startActivity(new Intent(FirstActivity.this, MenuActivity.class));
            finish();
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
            // 用户否勾选了不再提示并且拒绝了权限，那么提示用户到设置中授权。
            if (AndPermission.hasAlwaysDeniedPermission(FirstActivity.this, deniedPermissions)) {
                AndPermission.defaultSettingDialog(FirstActivity.this, 300).show();
                finish();
            }

        }
    };

}
