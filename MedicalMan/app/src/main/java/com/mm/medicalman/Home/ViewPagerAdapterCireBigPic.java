package com.mm.medicalman.Home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Animatable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.imagepipeline.image.ImageInfo;
import com.mm.medicalman.Tool.HeadImageChangeTool;

import java.io.File;
import java.util.List;

import me.relex.photodraweeview.OnPhotoTapListener;
import me.relex.photodraweeview.PhotoDraweeView;

import static com.mm.medicalman.Tool.HeadImageChangeTool.hss_save;

/**
 * Created by NiPing and Airr Wang
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved.
 */
public class ViewPagerAdapterCireBigPic extends PagerAdapter {
    String data;
    Context context;
    public ViewPagerAdapterCireBigPic(String data, Context context) {
        // TODO Auto-generated constructor stub
        this.data = data;
        this.context = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        // TODO Auto-generated method stub
        final PhotoDraweeView photoDraweeView = new PhotoDraweeView(container.getContext());
        PipelineDraweeControllerBuilder controller = Fresco.newDraweeControllerBuilder();

        controller.setUri(data);

        controller.setOldController(photoDraweeView.getController());
        controller.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (imageInfo == null) {
                    return;
                }
                photoDraweeView.update(imageInfo.getWidth(), imageInfo.getHeight());
            }
        });
        photoDraweeView.setController(controller.build());

        try {
            container.addView(photoDraweeView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                ((Activity) (context)).finish();
            }
        });
        photoDraweeView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                initToSavePic(data);
                return true;
            }
        });

        return photoDraweeView;

    }

    private void initToSavePic(final String pic) {
        final CharSequence[] items = {"保存", "取消"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        initSave(pic);
                        break;
                    default:
                        break;
                }
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void initSave(String picStr) {
        String filePath = hss_save
                + "/"
                + System.currentTimeMillis()
                + ".jpg";
        File imageFile = new File(filePath);
        HeadImageChangeTool.savePic(imageFile,context,picStr,hss_save);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


}
