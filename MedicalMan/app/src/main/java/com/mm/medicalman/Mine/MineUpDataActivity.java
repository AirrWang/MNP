package com.mm.medicalman.Mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.mm.medicalman.R;
import com.mm.medicalman.Tool.UpdateAppManager;


/**
 * Created by NiPing and AirrWang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */

public class MineUpDataActivity extends Activity implements View.OnClickListener {

    private TextView mUpdata;
    private Button mUpdataNow;
    private String url;
    private String isupdate;//强制更新,2个值,是:1,否:2
    private String verdec;
    private String vername;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView(R.layout.activity_mine_updata);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.5);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * 0.66);    //宽度设置为屏幕的0.8
        //p.alpha = 1.0f;      //设置本身透明度
        //p.dimAmount = 0.8f;      //设置黑暗度

        getWindow().setAttributes(p);
        initIntent();
        initId();

    }

    private void initIntent() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        isupdate = intent.getStringExtra("isupdate");
        verdec = intent.getStringExtra("verdec");
        vername = intent.getStringExtra("vername");
        name = intent.getStringExtra("name");
        if (!isupdate.equals("") && isupdate != null) {
            if (name.equals("main")) {
                if (isupdate.equals("2")) {//强制更新,2个值,是:1,否:2
                } else {
                    //强制更新
                    this.setFinishOnTouchOutside(false);
                }
            }
        }
    }


    private void initId() {
        TextView updata_version = (TextView) findViewById(R.id.mine_updata_version);
        updata_version.setText(vername+"版本");
        mUpdata = (TextView) findViewById(R.id.mine_updata_info);
        mUpdata.setText("医护人才已更新到" + vername + "版本");
        TextView mine_updata_detail = (TextView) findViewById(R.id.mine_updata_detail);
        mine_updata_detail.setText(verdec);
        mUpdataNow = (Button) findViewById(R.id.mine_updata_now);
        mUpdataNow.setOnClickListener(this);
    }

    /**
     * 锁定登录返回键，使其失效
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (!isupdate.equals("") && isupdate != null) {
                if (name.equals("main")) {
                    if (isupdate.equals("2")) {
                        finish();
                    } else {
                        //强制更新
                        return false;
                    }
                } else if (name.equals("set")) {
                    finish();
                }
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        UpdateAppManager updateAppManager = new UpdateAppManager(this);
        updateAppManager.showDownloadDialog(url, isupdate, name);
    }
}
