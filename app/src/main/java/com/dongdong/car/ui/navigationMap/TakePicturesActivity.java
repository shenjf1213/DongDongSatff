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

public class TakePicturesActivity extends BaseActivity implements View.OnClickListener {

    private String id, ordersId;
    private ImageView takePicturesBack; // 返回
    private ImageView takePicturesIv1, takePicturesIv2, takePicturesIv3, takePicturesIv4, takePicturesIv5, takePicturesIv6, takePicturesIv7, takePicturesIv8;
    private Button takePicturesBtn; // 确定上传

    public static String SDPATH = Environment.getExternalStorageDirectory() + "/hsyfm/recordimg/";
    public static File destDir = new File(SDPATH);
    private File imageFile; // 保存图片的文件
    private Bitmap bitmap; // 导入临时图片
    private String imagePath = ""; // 选择图片路径
    private String imageString; // 上传图片后的信息
    private String viewNumber = "";
    private List<String> beforeImageList = new ArrayList<>();

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
                case GlobalConsts.BEFORE_CLEANING_UPLOAD_PICTURE_HANDLER:
                    UploadImageRequest upImageRequest = (UploadImageRequest) msg.obj;
                    hideDialogByProgress();
                    imageString = upImageRequest.getErrmsg();
                    if (TextUtils.equals("上传成功", imageString)) {
                        imagePath = GlobalConsts.DONG_DONG_IMAGE_URL + upImageRequest.getFileName();
                        if (TextUtils.equals("1", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv1);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("2", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv2);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("3", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv3);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("4", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv4);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("5", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv5);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("6", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv6);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("7", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv7);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        } else if (TextUtils.equals("8", viewNumber)) {
                            Glide.with(TakePicturesActivity.this).load(imagePath).into(takePicturesIv8);
                            beforeImageList.add(upImageRequest.getFileName());
                            viewNumber = "";
                        }
                    }
                    break;

                case GlobalConsts.SAVE_BEFORE_CLEANING_PICTURE_HANDLER:
                    BeforePictureRequest beforePictureRequest = (BeforePictureRequest) msg.obj;
                    hideDialogByProgress();
                    String beforePicState = beforePictureRequest.getIsSucess();
                    if (TextUtils.equals("true", beforePicState)) {
                        dialog = new DialogByOneButton(TakePicturesActivity.this, "提示", "清洗前车辆图片上传成功", "确定");
                        dialog.show();
                        dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                            @Override
                            public void doPositive() {
                                dialog.dismiss();
                                beforeImageList.clear();
                                Intent intent = new Intent(TakePicturesActivity.this, OrderOnProgressActivity.class);
                                intent.putExtra("ORDER_ID", ordersId);
                                startActivity(intent);
                                TakePicturesActivity.this.finish();
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pictures_layout);

        dialogByProgress = new DialogByProgress(TakePicturesActivity.this);
        dialogByProgress.getWindow().setBackgroundDrawableResource(R.color.transparent);

        Intent intent = getIntent();
        ordersId = intent.getStringExtra("ORDER_ID");

        imageFile = new File(destDir, getPhotoFileName());

        SharedPreferences spf = TakePicturesActivity.this.getSharedPreferences("user_info", 0);
        id = spf.getString("uid", "");

        initView();
        initListener();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        takePicturesBack = (ImageView) findViewById(R.id.take_pictures_back);
        takePicturesIv1 = (ImageView) findViewById(R.id.take_pictures_iv1);
        takePicturesIv2 = (ImageView) findViewById(R.id.take_pictures_iv2);
        takePicturesIv3 = (ImageView) findViewById(R.id.take_pictures_iv3);
        takePicturesIv4 = (ImageView) findViewById(R.id.take_pictures_iv4);
        takePicturesIv5 = (ImageView) findViewById(R.id.take_pictures_iv5);
        takePicturesIv6 = (ImageView) findViewById(R.id.take_pictures_iv6);
        takePicturesIv7 = (ImageView) findViewById(R.id.take_pictures_iv7);
        takePicturesIv8 = (ImageView) findViewById(R.id.take_pictures_iv8);
        takePicturesBtn = (Button) findViewById(R.id.take_pictures_btn);
    }

    /**
     * 监听
     */
    private void initListener() {
        takePicturesBack.setOnClickListener(this);
        takePicturesIv1.setOnClickListener(this);
        takePicturesIv2.setOnClickListener(this);
        takePicturesIv3.setOnClickListener(this);
        takePicturesIv4.setOnClickListener(this);
        takePicturesIv5.setOnClickListener(this);
        takePicturesIv6.setOnClickListener(this);
        takePicturesIv7.setOnClickListener(this);
        takePicturesIv8.setOnClickListener(this);
        takePicturesBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_pictures_back:
                TakePicturesActivity.this.finish();
                break;

            case R.id.take_pictures_iv1:
                showCameraPW();
                viewNumber = "1";
                break;

            case R.id.take_pictures_iv2:
                showCameraPW();
                viewNumber = "2";
                break;

            case R.id.take_pictures_iv3:
                showCameraPW();
                viewNumber = "3";
                break;

            case R.id.take_pictures_iv4:
                showCameraPW();
                viewNumber = "4";
                break;

            case R.id.take_pictures_iv5:
                showCameraPW();
                viewNumber = "5";
                break;

            case R.id.take_pictures_iv6:
                showCameraPW();
                viewNumber = "6";
                break;

            case R.id.take_pictures_iv7:
                showCameraPW();
                viewNumber = "7";
                break;

            case R.id.take_pictures_iv8:
                showCameraPW();
                viewNumber = "8";
                break;

            case R.id.real_location_camera: // 拍照
                if (!IsCameraCanUse.isSDCard(TakePicturesActivity.this)) break;
                if (IsCameraCanUse.isCameraCanUse() == true) {
                    takePhone(); // 拍照获取
                    destroyPopupWindow();
                } else {
                    Toast.makeText(TakePicturesActivity.this, "请设置摄像头拍摄权限！", Toast.LENGTH_SHORT).show();
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

            case R.id.take_pictures_btn: // 下一步
                if (beforeImageList.size() < 8) {
                    dialog = new DialogByOneButton(TakePicturesActivity.this, "提示", "为了保护双方利益，请补全照片，谢谢！", "确定");
                    dialog.show();
                    dialog.setClicklistener(new DialogByOneButton.ClickListenerInterface() {
                        @Override
                        public void doPositive() {
                            dialog.dismiss();
                        }
                    });
                    return;
                } else {
                    Log.d("test", "照片数据-------------------" + beforeImageList.toString());
                    dialogByProgress.show();
                    FormBody.Builder builder = new FormBody.Builder();
                    builder.add("staffId", id);
                    builder.add("orderId", ordersId);
                    builder.add("frontLeft", beforeImageList.get(0));
                    builder.add("frontLeftBehind", beforeImageList.get(1));
                    builder.add("frontLeftHead", beforeImageList.get(2));
                    builder.add("frontLeftPicture", beforeImageList.get(3));
                    builder.add("frontRight", beforeImageList.get(4));
                    builder.add("frontRightBehind", beforeImageList.get(5));
                    builder.add("frontRightHead", beforeImageList.get(6));
                    builder.add("frontRightTail", beforeImageList.get(7));
                    FormBody body = builder.build();
                    Request request = new Request.Builder().url(GlobalConsts.SAVE_BEFORE_CLEANING_PICTURE_URL).post(body).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String bodyString = response.body().string();
                            Log.d("test", "清洗前图片保存-------------" + bodyString.toString());
                            Gson beforePicGson = new Gson();
                            BeforePictureRequest beforePicRequest = beforePicGson.fromJson(bodyString, BeforePictureRequest.class);
                            Message beforePicMsg = Message.obtain();
                            beforePicMsg.what = GlobalConsts.SAVE_BEFORE_CLEANING_PICTURE_HANDLER;
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
        View popupView = View.inflate(TakePicturesActivity.this, R.layout.real_location_pw_layout, null);
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
        Request request = new Request.Builder().url(GlobalConsts.BEFORE_CLEANING_UPLOAD_PICTURE_URL).post(multipartBody).build();
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
                uploadMsg.what = GlobalConsts.BEFORE_CLEANING_UPLOAD_PICTURE_HANDLER;
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
                Toast.makeText(TakePicturesActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == GlobalConsts.MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                choosePhoto(); // 访问相册
            } else {
                Toast.makeText(TakePicturesActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
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

    /**
     * TODO 页面不可见时保存数据
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (ordersId != null && !TextUtils.isEmpty(ordersId)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        RequestBody requestBody = new FormBody.Builder().add("orderId", ordersId).add("pageIndex", "1").add("orderState", "4").build();
                        Request request = new Request.Builder().url(GlobalConsts.SAVE_PAGE_UNEXPECTED_EXIT_URL).post(requestBody).build();
                        okHttpClient.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
