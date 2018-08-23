package com.mm.medicalman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Tool.AppConfig;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.SPUtils;


/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class SplashActivity extends AppCompatActivity {

    private View rootView;
    private SimpleDraweeView sdlash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = SplashActivity.this.getLayoutInflater().inflate(R.layout.activity_splash, null);
        setContentView(rootView);
        sdlash = (SimpleDraweeView) findViewById(R.id.wel);
//        FrescoUtils.initSetPlayImage(sdlash, AppConfig.res + R.drawable.splash);
        FrescoUtils.setImage(sdlash, AppConfig.res + R.drawable.bg);
        initSplash();
    }

    private void initSplash() {
        AlphaAnimation animation = new AlphaAnimation(0.5f, 1.0f);
        animation.setDuration(2500);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                initToMain();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        rootView.setAnimation(animation);
    }

    private void initToMain() {
        String token = "";
        token = (String) SPUtils.get("token", token);
        if (token.equals("")) {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 返回==home
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            MoudleUtils.onBackPressed(this);
            try {
                moveTaskToBack(false);
            } catch (Exception e) {
                return super.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
