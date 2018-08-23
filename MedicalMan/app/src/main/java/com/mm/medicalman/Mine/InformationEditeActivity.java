package com.mm.medicalman.Mine;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mm.medicalman.Api.NpApi;
import com.mm.medicalman.Bean.BeanTool;
import com.mm.medicalman.Bean.UpPicSingleTool;
import com.mm.medicalman.Bean.UserInfoBean;
import com.mm.medicalman.R;
import com.mm.medicalman.Tool.FrescoUtils;
import com.mm.medicalman.Tool.HeadImageChangeTool;
import com.mm.medicalman.Tool.MoudleUtils;
import com.mm.medicalman.Tool.RetrofitUtils;
import com.mm.medicalman.Tool.SPUtils;
import com.mm.medicalman.Tool.SavaDataLocalUtils;
import com.mm.medicalman.Tool.ToastUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mm.medicalman.Mine.MineFragment.userInforInterface;
import static com.mm.medicalman.Tool.HeadImageChangeTool.CAPTURE_IMAGE;
import static com.mm.medicalman.Tool.HeadImageChangeTool.CROP_IMAGE;
import static com.mm.medicalman.Tool.HeadImageChangeTool.JPG;
import static com.mm.medicalman.Tool.HeadImageChangeTool.btnclick;
import static com.mm.medicalman.Tool.HeadImageChangeTool.btnclickxc;
import static com.mm.medicalman.Tool.HeadImageChangeTool.hss_cache;
import static com.mm.medicalman.Tool.HeadImageChangeTool.initToseeDongTai;
import static com.mm.medicalman.Tool.HeadImageChangeTool.saveBitmapFile;
import static com.mm.medicalman.Tool.HeadImageChangeTool.zoomBitmap;

/**
 * Created by NiPing and Airr Wang on 2017/10/23.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class InformationEditeActivity extends AppCompatActivity {
    @BindView(R.id.tv_sex)
    TextView tv_sex;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_sigin)
    EditText et_sigin;
    @BindView(R.id.mine_userpic)
    SimpleDraweeView mine_userpic;
    @BindView(R.id.user_info_edite_pic)
    LinearLayout user_info_edite_pic;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    private Uri imageUri;
    private File imageFile;
    private String nickname, sex, slogan;
    private String url;//返回的图片的全名，上传时也上传图片全名，然后由后台陈进行处理

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_edite);
        ButterKnife.bind(this);
        initToolbar();
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<UserInfoBean> callBack = restApi.userInfo(token, user_id);
        callBack.enqueue(new Callback<UserInfoBean>() {
            @Override
            public void onResponse(Call<UserInfoBean> call, Response<UserInfoBean> response) {
                UserInfoBean bean = response.body();
                if (bean == null) {
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        initTaskSuccess(bean);
                        initToEditDataPost();
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(InformationEditeActivity.this, bean.getErr_var());
                        ToastUtils.showShort(InformationEditeActivity.this, bean.getMessage());
                        break;
                }
            }

            @Override
            public void onFailure(Call<UserInfoBean> call, Throwable t) {
                MoudleUtils.toChekWifi(InformationEditeActivity.this);
            }
        });
    }

    private void initToEditDataPost() {
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nickname = et_name.getText().toString().trim();
                slogan = et_sigin.getText().toString().trim();
                sex = tv_sex.getText().toString().trim();
                switch (sex) {
                    case "女":
                        sex = "2";
                        break;
                    case "男":
                        sex = "1";
                        break;
                    default:
                        sex = "3";
                        break;
                }
                if (url == null || url.equals("") || nickname.equals("") || slogan.equals("") || sex.equals("")) {
                    ToastUtils.showShort(InformationEditeActivity.this, "请填写全部信息");
                    return;
                }
                if (MoudleUtils.containsEmoji(nickname) || MoudleUtils.containsEmoji(slogan)) {
                    ToastUtils.showShort(InformationEditeActivity.this, "不支持输入Emoji表情符号~");
                    return;
                }
                initTaskDataPost();
            }
        });
    }

    private void initTaskDataPost() {
        String user_id = (String) SPUtils.get("user_id", "");
        final String token = (String) SPUtils.get("token", "");
        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<BeanTool> callBack = restApi.edit(token, user_id, nickname, sex, url, slogan);
        callBack.enqueue(new Callback<BeanTool>() {
            @Override
            public void onResponse(Call<BeanTool> call, Response<BeanTool> response) {
                BeanTool bean = response.body();
                if (bean == null) {
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        SavaDataLocalUtils.saveDataString(InformationEditeActivity.this, "informationPicNp", url);
                        SavaDataLocalUtils.saveDataString(InformationEditeActivity.this, "informationNicknameNp", nickname);
                        SavaDataLocalUtils.saveDataString(InformationEditeActivity.this, "informationSloganNp", slogan);
                        SavaDataLocalUtils.saveDataString(InformationEditeActivity.this, "informationSexNp", sex);
                        if (userInforInterface != null) {
                            userInforInterface.userInfor();
                        }
                        finish();
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(InformationEditeActivity.this, bean.getErr_var());
                        ToastUtils.showShort(InformationEditeActivity.this, bean.getMessage());
                        break;
                }
            }

            @Override
            public void onFailure(Call<BeanTool> call, Throwable t) {
                MoudleUtils.toChekWifi(InformationEditeActivity.this);
            }
        });
    }

    private void initTaskSuccess(UserInfoBean bean) {
        UserInfoBean.BeanData beanData = bean.getData();
        if (beanData == null)
            return;
        url = beanData.getPic();
        FrescoUtils.setImage(mine_userpic, beanData.getPic());
        MoudleUtils.editTextViewSetText(et_name, beanData.getNickname());
        MoudleUtils.editTextViewSetText(et_sigin, beanData.getSlogan());
        switch (beanData.getSex()) {//性别,:1男2女3未定义
            case "3":
                MoudleUtils.textViewSetText(tv_sex, "未定义");
                break;
            case "2":
                MoudleUtils.textViewSetText(tv_sex, "女");
                break;
            case "1":
                MoudleUtils.textViewSetText(tv_sex, "男");
                break;
        }
    }

    /**
     * 页面标题
     */
    private void initToolbar() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_title.setText("个人资料");
        tv_right.setText("保存");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);//显示返回键，有时需要不带返回键
            actionBar.setDisplayShowTitleEnabled(false);//不显示toolbar自带标题
        }

    }

    @OnClick(R.id.user_info_edite_pic)
    void userInfoEditePic() {
        changeUserImage(this);
    }

    @OnClick(R.id.tv_sex)
    void tv_sex() {
        changeUserSex(this);
    }

    private void changeUserImage(final Context context) {
        final CharSequence[] items = {"相册", "相机", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("更换图片");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String filePathUserInfo = "";
                filePathUserInfo = hss_cache
                        + "/"
                        + System.currentTimeMillis()
                        + JPG;
                imageFile = new File(filePathUserInfo);
                imageUri = Uri.fromFile(imageFile);
                switch (item) {
                    case 0:
                        btnclickxc(imageUri, imageFile, context, hss_cache);
                        break;
                    case 1:
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                btnclick(imageUri, imageFile, context, hss_cache);
                            }
                        });
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == InformationEditeActivity.RESULT_OK) {
            if (imageFile == null) {
                ToastUtils.showShortNotInternet(this, "图片跑丢了，再来一次吧");
                return;
            }
            if (!imageFile.exists()) {
                ToastUtils.showShortNotInternet(this, "图片跑丢了，再来一次吧");
                return;
            }
            switch (requestCode) {
                case HeadImageChangeTool.SELECT_IMAGE:
                    initSelectImage(data);
                    break;
                case CAPTURE_IMAGE:
                    initCaptureImage();
                    break;
                case CROP_IMAGE:
                    initCropImage();
                    break;
                default:
                    break;
            }
        } else {
            initImgLose();
        }
    }

    private void initSelectImage(Intent data) {
        try {
            imageUri = Uri.fromFile(imageFile);
            if (imageUri != null) {
                cropImage(data.getData());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initCropImage() {
        try {
            if (imageFile == null) {
                ToastUtils.showShortNotInternet(this, "图片跑丢了，再来一次吧");
                return;
            }
            Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            if (bmp == null) {
                ToastUtils.showShortNotInternet(this, "图片跑丢了，再来一次吧");
                return;
            }
            bmp = zoomBitmap(bmp, 160, 160);
            if (bmp != null) {
                File file = saveBitmapFile(bmp, imageFile);
                if (file == null) {
                    ToastUtils.showShortNotInternet(this, "图片跑丢了，再来一次吧");
                }
                iniTask(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void iniTask(File file) {
        String user_id = (String) SPUtils.get("user_id", "");
        String token = (String) SPUtils.get("token", "");
        RequestBody useridBody = RequestBody.create(MediaType.parse("text/plain"), user_id);
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), token);
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);

        Map<String, RequestBody> map = new HashMap<>();
        map.put("user_id", useridBody);
        map.put("token", tokenBody);
        map.put("image\"; filename=\"" + file.getName(), fileBody);

        NpApi restApi = RetrofitUtils.retrofit.create(NpApi.class);
        Call<UpPicSingleTool> callBack = restApi.uploadMemberIcon(map);
        callBack.enqueue(new Callback<UpPicSingleTool>() {
            @Override
            public void onResponse(Call<UpPicSingleTool> call, Response<UpPicSingleTool> response) {
                UpPicSingleTool bean = response.body();
                if (bean == null) {
                    return;
                }
                switch (bean.getStatus()) {
                    case "1":
                        UpPicSingleTool.BeanData beanData = bean.getData();
                        if (beanData == null) return;
                        url = beanData.getUrl();
                        FrescoUtils.setImage(mine_userpic, url);
                        break;
                    case "0":
                        ToastUtils.initLoginAgain(InformationEditeActivity.this, bean.getErr_var());
                        ToastUtils.showShort(InformationEditeActivity.this, bean.getMessage());
                        break;
                }
            }

            @Override
            public void onFailure(Call<UpPicSingleTool> call, Throwable t) {
                MoudleUtils.toChekWifi(InformationEditeActivity.this);
            }
        });
    }

    private void initCaptureImage() {
        try {
            initToseeDongTai(this, imageFile);//通知相册扫描
            imageUri = Uri.fromFile(imageFile);
            if (imageUri != null) {
                cropImage(imageUri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initImgLose() {
        ToastUtils.showShortNotInternet(this, "图片跑丢了，再来一次吧");
        try {
            if (imageFile == null) return;
            if (imageFile.exists()) {
                imageFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cropImage(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, CROP_IMAGE);
    }

    private void changeUserSex(final Context context) {
        final CharSequence[] items = {"男", "女"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        MoudleUtils.textViewSetText(tv_sex, items[0].toString());
                        break;
                    case 1:
                        MoudleUtils.textViewSetText(tv_sex, items[1].toString());
                        break;
                    default:
                        break;
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.setCanceledOnTouchOutside(true);
        alert.setCancelable(true);
        alert.show();
    }
}
