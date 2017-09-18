package com.dongdong.car.ui.navigationMap;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;
import com.dongdong.car.entity.completeOrder.CompleteOrderRequest;
import com.dongdong.car.entity.saveBeforePicture.BeforePictureRequest;
import com.dongdong.car.entity.uploadImage.UploadImageRequest;
import com.dongdong.car.util.DialogByOneButton;
import com.dongdong.car.util.DialogByProgress;
import com.dongdong.car.util.GlobalConsts;
import com.dongdong.car.util.ImageUtils;
import com.dongdong.car.util.IsCameraCanUse;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 沈 on 2017/6/15.
 */

public class TakePicturesByAfterActivity extends BaseActivity implements View.OnClickListener {

    private String id, orderId;
    private ImageView afterPictureBack; // 返回
    private ImageView afterP1, afterP2, afterP3, afterP4, afterP5, afterP6, afterP7, afterP8;
    private Button afterPictureBtn; // 确定上传

    public static String SDPATH = Environment.getExternalStorageDirectory() + "/hsyfm/recordimg/";
    public static File destDir = new File(SDPATH);
    private File imageFile; // 保存图片的文件
    private Bitmap bitmap; // 导入临时图片
    private String imagePath = ""; // 选择图片路径
    private String imageString; // 上传图片后的信息
    private String viewNumber = "";
    private List<String> afterImageList = new ArrayList<>();

    private boolean isShowingPW;
    private PopupWindow displayPW; // 拍照选择的弹窗布局
    private Button realLocationCamera, realLocationAlbum, realLocationCancel; // 拍照选择的弹窗布局控件

    private DialogByProgress dialogByProgress;
    private DialogByOneButton dialog;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobalConsts.AFTER_CLEANING_UPLOAD_PICTURE_HANDLER: // 上传图片
                    UploadImageRequest upImageRequest = (UploadImageRequest) msg.obj;
                    hideDialogByProgress();
                    imageString = upImageRequest.getErrmsg();
                    if (TextUtils.equals("上传成功", imageString)) {
                        imagePath = GlobalConsts.DONG_DONG_IMAGE_URL + upImageRequest.getFileName();
                        if (TextUtils.equals("1", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP1);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("2", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP2);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("3", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP3);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("4", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP4);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("5", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP5);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("6", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP6);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("7", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP7);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("8", viewNumber)) {
                            Glide.with(TakePicturesByAfterActivity.this).load(imagePath).into(afterP8);
                            afterImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        }
                    }
                    break;

                case GlobalConsts.SAVE_AFTER_CLEANING_PICTURE_HANDLER: // 洗后照片保存
                    BeforePictureRequest beforePictureRequest = (BeforePictureRequest) msg.obj;
                    hideDialogByProgress();
                    String beforePicState = beforePictureRequest.getIsSucess();
                    if (TextUtils.equals("true", beforePicState)) {
                        dialog = new DialogByOneButton(TakePicturesByAfterActivity.this, "提示", "清洗后车辆图片上传成功，点击确定完成此订单", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                                afterImageList.clear();
                                CompleteOrderByPost();
                            }
                        });
                    }
                    break;

                case GlobalConsts.COMPLETE_ORDER_HANDLER: // 结束订单
                    CompleteOrderRequest completeOrderRequest = (CompleteOrderRequest) msg.obj;
                    String completeOrderState = completeOrderRequest.getIsSucess();
                    if (TextUtils.equals("true", completeOrderState)) {
                        TakePicturesByAfterActivity.this.finish();
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pictures_after_layout);

        dialogByProgress = new DialogByProgress(TakePicturesByAfterActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        imageFile = new File(destDir, getPhotoFileName());

        Intent intent = getIntent();
        orderId = intent.getStringExtra("ORDER_ID");

        SharedPreferences spf = TakePicturesByAfterActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        afterPictureBack = (ImageView) findViewById(R.id.take_pictures_after_back);
        afterP1 = (ImageView) findViewById(R.id.after_p1);
        afterP2 = (ImageView) findViewById(R.id.after_p2);
        afterP3 = (ImageView) findViewById(R.id.after_p3);
        afterP4 = (ImageView) findViewById(R.id.after_p4);
        afterP5 = (ImageView) findViewById(R.id.after_p5);
        afterP6 = (ImageView) findViewById(R.id.after_p6);
        afterP7 = (ImageView) findViewById(R.id.after_p7);
        afterP8 = (ImageView) findViewById(R.id.after_p8);
        afterPictureBtn = (Button) findViewById(R.id.take_pictures_after_btn);
    }

    /**
     * 监听
     */
    private void initListener() {
        afterPictureBack.setOnClickListener(this);
        afterP1.setOnClickListener(this);
        afterP2.setOnClickListener(this);
        afterP3.setOnClickListener(this);
        afterP4.setOnClickListener(this);
        afterP5.setOnClickListener(this);
        afterP6.setOnClickListener(this);
        afterP7.setOnClickListener(this);
        afterP8.setOnClickListener(this);
        afterPictureBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_pictures_after_back:
                TakePicturesByAfterActivity.this.finish();
                break;

            case R.id.after_p1:
                showCameraPW();
                viewNumber = "1";
                break;

            case R.id.after_p2:
                showCameraPW();
                viewNumber = "2";
                break;

            case R.id.after_p3:
                showCameraPW();
                viewNumber = "3";
                break;

            case R.id.after_p4:
                showCameraPW();
                viewNumber = "4";
                break;

            case R.id.after_p5:
                showCameraPW();
                viewNumber = "5";
                break;

            case R.id.after_p6:
                showCameraPW();
                viewNumber = "6";
                break;

            case R.id.after_p7:
                showCameraPW();
                viewNumber = "7";
                break;

            case R.id.after_p8:
                showCameraPW();
                viewNumber = "8";
                break;

            case R.id.real_location_camera: // 拍照
                if (!IsCameraCanUse.isSDCard(TakePicturesByAfterActivity.this)) break;
                if (IsCameraCanUse.isCameraCanUse() == true) {
                    takePhone(); // 拍照获取
                    destroyPopupWindow();
                } else {
                    Toast.makeText(TakePicturesByAfterActivity.this, "请设置摄像头拍摄权限！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.real_location_album: // 从相册获取照片
                choosePhone(); // 相册获取
                destroyPopupWindow();
                break;

            case R.id.real_location_cancel: // 拍照或相册选择弹出框的取消按钮
                if (displayPW != null && displayPW.isShowing()) {
                    displayPW.dismiss();
                }
                break;

            case R.id.take_pictures_after_btn: // 洗后照片保存
                if (afterImageList.size() < 8) {
                    dialog = new DialogByOneButton(TakePicturesByAfterActivity.this, "提示", "为了保护双方利益，请补全照片，谢谢！", "确定");
                    dialog.show();
                    dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                        @Override
                        public void doPositive() {
                            dialog.dismiss();
                        }
                    });
                    return;
                } else {
                    dialogByProgress.show();
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("staffId", id);
                    builder.add("orderId", orderId);
                    builder.add("bihindLeft", afterImageList.get(0));
                    builder.add("bihindLeftBehind", afterImageList.get(1));
                    builder.add("bihindLeftHead", afterImageList.get(2));
                    builder.add("bihindLeftPicture", afterImageList.get(3));
                    builder.add("bihindRight", afterImageList.get(4));
                    builder.add("bihindRightBehind", afterImageList.get(5));
                    builder.add("bihindRightHead", afterImageList.get(6));
                    builder.add("bihindRightTail", afterImageList.get(7));
                    FormBody body = builder.build();
                    Request request = new Request.Builder().url(GlobalConsts.SAVE_AFTER_CLEANING_PICTURE_URL).post(body).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            Log.d("test", "清洗后图片保存-------------" + bodyString.toString());
                            Gson beforePicGson = new Gson();
                            BeforePictureRequest beforePicRequest = beforePicGson.fromJson(bodyString, BeforePictureRequest.class);
                            Message beforePicMsg = Message.obtain();
                            beforePicMsg.what = GlobalConsts.SAVE_AFTER_CLEANING_PICTURE_HANDLER;
                            beforePicMsg.obj = beforePicRequest;
                            handler.sendMessage(beforePicMsg);
                        }
                    });
                }
                break;
        }
    }

    /**
     * 弹出照片选择的弹窗
     */
    private void showCameraPW() {
        View popupView = View.inflate(TakePicturesByAfterActivity.this, R.layout.real_location_pw_layout, null);
        displayPW = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        realLocationCamera = (Button) popupView.findViewById(R.id.real_location_camera);
        realLocationAlbum = (Button) popupView.findViewById(R.id.real_location_album);
        realLocationCancel = (Button) popupView.findViewById(R.id.real_location_cancel);

        realLocationCamera.setOnClickListener(this);
        realLocationAlbum.setOnClickListener(this);
        realLocationCancel.setOnClickListener(this);

        displayPW.setFocusable(true);
        displayPW.setOutsideTouchable(true);
        displayPW.setAnimationStyle(android.R.style.Animation_InputMethod);
        displayPW.showAtLocation(popupView, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 设置拍照后的照片格式
     *
     * @return
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh：mm：ss.SSS");
        return sdf.format(date) + ".jpg";
    }

    /**
     * 检查拍照权限
     */
    private void takePhone() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalConsts.MY_PERMISSIONS_REQUEST_CALL_PHONE2);
        } else {
            takePhoto();
        }
    }

    /**
     * 使用拍照设置头像
     */
    private void takePhoto() {
        imageFile = new File(destDir, getPhotoFileName());
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        startActivityForResult(intent, GlobalConsts.TAKE_PICTURES_UPLOAD);
    }

    /**
     * 检查访问相册权限
     */
    public void choosePhone() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, GlobalConsts.MY_PERMISSIONS_REQUEST_CALL_PHONE);
        } else {
            choosePhoto();
        }
    }

    /**
     * 从相册选取图片
     */
    public void choosePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, GlobalConsts.ALBUM_UPLOAD);
    }

    /**
     * 获取图片路径 响应startActivityForResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GlobalConsts.TAKE_PICTURES_UPLOAD: // 拍照选择照片
                if (resultCode == RESULT_OK) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(imageFile));
                        saveCropPic(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case GlobalConsts.ALBUM_UPLOAD: // 相册选择照片
                if (resultCode == RESULT_OK && data != null) {
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));
                        saveCropPic(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    /**
     * 把裁剪后的图片保存到sdcard上并上传至服务器通过adapter适配后显示
     */
    private void saveCropPic(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fis = null;
        Bitmap bm = ImageUtils.imageZoom(bmp); // 点击图片显示效果
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        try {
            fis = new FileOutputStream(imageFile);
            fis.write(baos.toByteArray());
            /**
             * 将图片文件上传服务
             */
            upImgFile(imageFile);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != baos) {
                    baos.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片上传至服务器并显示
     */
    private void upImgFile(File imageFile) {
        dialogByProgress.show();
        String BOUNDARY = UUID.randomUUID().toString();
        RequestBody body = RequestBody.create(MediaType.parse("application/octet-stream"), imageFile);
        MultipartBody multipartBody = new MultipartBody.Builder(BOUNDARY).setType(MultipartBody.FORM).addFormDataPart("", imageFile.getName(), body).build();
        Request request = new Request.Builder().url(GlobalConsts.AFTER_CLEANING_UPLOAD_PICTURE_URL).post(multipartBody).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "上传图片至服务器---" + bodyString.toString());
                Gson uploadImageGson = new Gson();
                UploadImageRequest uploadImageRequest = uploadImageGson.fromJson(bodyString, UploadImageRequest.class);
                Message uploadMsg = Message.obtain();
                uploadMsg.what = GlobalConsts.AFTER_CLEANING_UPLOAD_PICTURE_HANDLER;
                uploadMsg.obj = uploadImageRequest;
                handler.sendMessage(uploadMsg);
            }
        });
    }

    /**
     * 6.0版本手机权限用户拒绝后接受回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GlobalConsts.MY_PERMISSIONS_REQUEST_CALL_PHONE2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto(); // 拍摄
            } else {
                Toast.makeText(TakePicturesByAfterActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == GlobalConsts.MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto(); // 访问相册
            } else {
                Toast.makeText(TakePicturesByAfterActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 提交完成订单
     */
    private void CompleteOrderByPost() {
        Request request = new Request.Builder().url(GlobalConsts.DONG_DONG_URL + "api/Staff/FinshStaffOrder?orderId=" + orderId + "&staffId=" + id).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bodyString = response.body().string();
                Log.d("test", "提交完成订单----------" + bodyString.toString());
                Gson completeOrderGson = new Gson();
                CompleteOrderRequest completeOrderRequest = completeOrderGson.fromJson(bodyString, CompleteOrderRequest.class);
                Message completeOrderMsg = Message.obtain();
                completeOrderMsg.what = GlobalConsts.COMPLETE_ORDER_HANDLER;
                completeOrderMsg.obj = completeOrderRequest;
                handler.sendMessage(completeOrderMsg);
            }
        });
    }

    /**
     * 页面不可见时保存数据
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (orderId != null && !TextUtils.isEmpty(orderId)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RequestBody requestBody = new FormBody.Builder().add("orderId", orderId).add("pageIndex", "3").add("orderState", "5").build();
                        Request request = new Request.Builder().url(GlobalConsts.SAVE_PAGE_UNEXPECTED_EXIT_URL).post(requestBody).build();
                        okHttpClient.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * 销毁弹窗
     */
    private void destroyPopupWindow() {
        if (displayPW != null && displayPW.isShowing()) {
            displayPW.dismiss();
        }
        isShowingPW = true;
    }

    private void hideDialogByProgress() {
        if (dialogByProgress != null && dialogByProgress.isShowing()) {
            dialogByProgress.dismiss();
        }
    }
}
