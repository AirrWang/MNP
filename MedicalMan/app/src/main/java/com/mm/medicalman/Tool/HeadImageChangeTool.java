package com.mm.medicalman.Tool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.mm.medicalman.MmApplication;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * /**
 * Created by NiPing and AirrWang on 2017/10/24.
 * Copyright  © 2017年 Hyperspace Technology(Beijing)Co.,Ltd. All rights reserved
 */

public class HeadImageChangeTool {
    public static final int CAPTURE_IMAGE = 0x01;
    public static final int SELECT_IMAGE = 0x02;
    public static final int CROP_IMAGE = 0x03;
    public static String JPG = ".jpg";
    //自定义磁盘缓存目录
    public static String hss_save = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mm/mm_img_save";
    public static String hss_cache = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Mm/mm_img_cache";


    /**
     * 点击按钮后调用相机拍照
     */

    public static void btnclick(Uri imageUri, File imageFile, Context context, String wenJianJia) {
        // 如果初始化文件成功，则调用相机
        if (initImageFile(imageFile, wenJianJia)) {
            //下面用第一种方式获取图片，还可以调用startTakePhoto2()方法获取图片
            startTakePhoto(imageUri, imageFile, context);
            //startTakePhoto2();
        } else {
            ToastUtils.showShortNotInternet(context, "初始化文件失败，无法调用相机拍照！");

        }
    }

    /**
     * 点击照片后选择系统相册
     *
     * @param imageUri
     * @param imageFile
     * @param context
     * @param wenJianJia
     */

    public static void btnclickxc(Uri imageUri, File imageFile, Context context, String wenJianJia) {
        // 如果初始化文件成功，则调用相机
        if (initImageFile(imageFile, wenJianJia)) {
            selectImage(imageUri, imageFile, context);
        } else {
            ToastUtils.showShortNotInternet(context, "初始化文件失败，无法调用相机拍照！");
        }
    }

    /**
     * 调用系统相册
     *
     * @param imageUri
     * @param imageFile
     * @param context
     */
    public static void selectImage(Uri imageUri, File imageFile, Context context) {

        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity) (context)).startActivityForResult(intent, SELECT_IMAGE);
    }

    /**
     * 判断是否有SD卡
     *
     * @return 有SD卡返回true，否则false
     */
    private static boolean hasSDCard() {
        // 获取外部存储的状态
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // 有SD卡
            return true;
        }
        return false;
    }

    /**
     * 初始化存储图片的文件
     *
     * @param imageFile
     * @return 初始化成功返回true，否则false
     */
    public static boolean initImageFile(File imageFile, String wenJianJia) {
        // 有SD卡时才初始化文件
        if (hasSDCard()) {
            if (initToCacheFiles(wenJianJia)) {
                if (!imageFile.exists()) {// 如果文件不存在，就创建文件
                    try {
                        imageFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 是否生成了次文件夹
     *
     * @param wenJianJia
     * @return
     */
    private static boolean initToCacheFiles(String wenJianJia) {
        File cacheLocation = new File(wenJianJia);
        if (!cacheLocation.exists()) {
            cacheLocation.mkdirs();
        }
        return true;
    }

    /**
     * 跳转到系统相机拍照
     * // 尽可能调用系统相机
     */
    private static void startTakePhoto(Uri imageUri, File imageFile, Context context) {
        try {
            Intent intent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            // 设置拍照后保存的图片存储在文件中
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            // 启动activity并获取返回数据
            ((Activity) (context)).startActivityForResult(intent, CAPTURE_IMAGE);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * 保存网络图片到本地
     *
     * @param imageFile
     * @param context
     * @param picStr
     */

    public static void savePic(File imageFile, Context context, String picStr, String wenJianJia) {
        // 如果初始化文件成功，则调用相机
        if (initImageFile(imageFile, wenJianJia)) {
            savePicture(picStr, context, imageFile);
        } else {
            ToastUtils.showShortNotInternet(context, "初始化文件失败，保存失败");
        }
    }

    /**
     * 保存图片
     *
     * @param context
     * @param picUrl
     */
    public static void savePicture(String picUrl, Context context, File picDir) {

        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(Uri.parse(picUrl)), null);
        File cacheFile = getCachedImageOnDisk(cacheKey);
        if (cacheFile == null) {
            downLoadImage(Uri.parse(picUrl), picDir, context);
            return;
        } else {
            copyTo(context, cacheFile, picDir);
        }
    }

    public static File getCachedImageOnDisk(CacheKey cacheKey) {
        File localFile = null;
        if (cacheKey != null) {
            if (ImagePipelineFactory.getInstance().getMainDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getMainDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            } else if (ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().hasKey(cacheKey)) {
                BinaryResource resource = ImagePipelineFactory.getInstance().getSmallImageDiskStorageCache().getResource(cacheKey);
                localFile = ((FileBinaryResource) resource).getFile();
            }
        }
        return localFile;
    }


    /**
     * 复制文件
     *
     * @param src 源文件
     * @param dir 目标文件
     * @return
     */
    public static boolean copyTo(Context context, File src, File dir) {

        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(src);
            in = fi.getChannel();//得到对应的文件通道
            fo = new FileOutputStream(dir);
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
            initTosee(context, dir);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            ToastUtils.showShortNotInternet(context, "保存图片失败啦");
            return false;
        } finally {
            try {

                if (fi != null) {
                    fi.close();
                }

                if (in != null) {
                    in.close();
                }

                if (fo != null) {
                    fo.close();
                }

                if (out != null) {
                    out.close();
                }

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

    }

    private static void initTosee(Context context, File file) {
        if (file != null) {
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                MmApplication.getInstance().sendBroadcast(intent);
                ToastUtils.showShortNotInternet(context, "保存成功！路径：" + file.getAbsolutePath());
            } else {
                ToastUtils.showShortNotInternet(context, "保存图片失败啦,无法下载图片");
            }
        } else {
            ToastUtils.showShortNotInternet(context, "保存图片失败啦,无法下载图片");
        }

    }


    public static void downLoadImage(Uri uri, final File file, final Context context) {
        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        DataSource<CloseableReference<CloseableImage>>
                dataSource = imagePipeline.fetchDecodedImage(imageRequest, context);
        dataSource.subscribe(new BaseBitmapDataSubscriber() {
            @Override
            public void onNewResultImpl(Bitmap bitmap) {
                if (bitmap == null) {
                    ToastUtils.showShort(context, "保存图片失败啦,无法下载图片");
                    return;
                }
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    initTosee(context, file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                ToastUtils.showShort(context, "保存图片失败啦,无法下载图片");
            }
        }, CallerThreadExecutor.getInstance());
    }

    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);// 利用矩阵进行缩放不会造成内存溢出
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        //        LogUtils.i(newbmp + "");
        return newbmp;
    }

    /***
     * 拍摄图片后通知手机系统相册进行扫描
     *
     * @param file
     */
    public static void initToseeDongTai(Context context, File file) {
        if (file != null) {
            if (file.exists()) {
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                MmApplication.getInstance().sendBroadcast(intent);
                //                ToastUtils.showShort("保存成功！路径：" + file.getAbsolutePath());
            } else {
                ToastUtils.showShortNotInternet(context, "由于手机权限原因，图片拍摄失败啦");
            }
        } else {
            ToastUtils.showShortNotInternet(context, "由于手机权限原因，图片拍摄失败啦");
        }

    }

    /**
     * 把Base64转换成Bitmap
     *
     * @param iconBase64
     * @return
     */
    public static Bitmap getBitmapNew(String iconBase64) {
        byte[] bitmapArray;
        bitmapArray = Base64.decode(iconBase64, 0);
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /**
     * 图片上传转Base64码
     *
     * @param bitmap
     * @return
     */
    public static String Bitmap2Base64(Bitmap bitmap, int n) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int option = 100;
        if (bitmap != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
            //            Log.i(TAG, "baos.toByteArray().length/1024_1:：" + baos.toByteArray().length / 1024);
            while (baos.toByteArray().length / 1024 > 100 && option > 6) {
                baos.reset();
                option -= 6;
                bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
            }
            //            Log.i(TAG, "baos.toByteArray().length/1024_2：" + baos.toByteArray().length / 1024);
            //            Log.i(TAG, "option：" + option);
            byte[] bytes = baos.toByteArray();

            return Base64.encodeToString(bytes, Base64.CRLF);
        } else {
            return null;
        }
    }


    public static File saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
            return file;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return file;
    }
}
