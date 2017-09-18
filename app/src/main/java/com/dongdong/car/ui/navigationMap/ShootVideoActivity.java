package com.dongdong.car.ui.navigationMap;

import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdong.car.R;
import com.dongdong.car.com.BaseActivity;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.dongdong.car.R.id.shoot_video_iv;

/**
 * Created by 沈 on 2017/6/16.
 */

public class ShootVideoActivity extends BaseActivity implements View.OnClickListener, SurfaceHolder.Callback {

    private ImageView shootVideoBack; // 返回
    private SurfaceView surfaceView; // 视频控件
    private ImageView shootVideoIv; // 视屏背景
    private TextView shootVideoTime; // 视频录制的时间
    private Button startOrStopBtn; // 开始录制或暂停
    private Button playVideoBtn; // 开始播放

    private boolean mStartedFlg = false; // 是否正在录像
    private boolean mIsPlay = false; // 是否正在播放录像
    private MediaRecorder mRecorder;
    private SurfaceHolder mSurfaceHolder;
    private Camera camera;
    private Camera.Size mSize = null; // 相机的尺寸
    private MediaPlayer mediaPlayer;
    private String path; // 视频录制后的保存路径
    private int timeTv = 0;
    private int mCameraFacing = Camera.CameraInfo.CAMERA_FACING_BACK;//默认后置摄像头
    private static final SparseIntArray orientations = new SparseIntArray();//手机旋转对应的调整角度

    static {
        orientations.append(Surface.ROTATION_0, 90);
        orientations.append(Surface.ROTATION_90, 0);
        orientations.append(Surface.ROTATION_180, 270);
        orientations.append(Surface.ROTATION_270, 180);
    }


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            timeTv++;
            shootVideoTime.setText(timeTv + "" + "秒");
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoot_video_layout);

        initView();
        initListener();

        SurfaceHolder holder = surfaceView.getHolder();
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 使用相机后置摄像头
        holder.setKeepScreenOn(true);
        holder.addCallback(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCamera(); // 初始化相机
    }

    private void initCamera() {
        if (Camera.getNumberOfCameras() == 2) {
            camera = Camera.open(mCameraFacing);
        } else {
            camera = Camera.open();
        }

        CameraSizeComparator sizeComparator = new CameraSizeComparator();
        Camera.Parameters parameters = camera.getParameters();

        if (mSize == null) {
            List<Camera.Size> vSizeList = parameters.getSupportedPreviewSizes();
            Collections.sort(vSizeList, sizeComparator);

            for (int num = 0; num < vSizeList.size(); num++) {
                Camera.Size size = vSizeList.get(num);

                if (size.width >= 800 && size.height >= 480) {
                    this.mSize = size;
                    break;
                }
            }
            mSize = vSizeList.get(0);

            List<String> focusModesList = parameters.getSupportedFocusModes();

            // 增加对聚焦模式的判断
            if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
            } else if (focusModesList.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            camera.setParameters(parameters);
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int orientation = orientations.get(rotation);
        camera.setDisplayOrientation(orientation);
    }

    @Override
    public void onPause() {
        releaseCamera(); // 释放相机资源
        super.onPause();
    }

    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.stopPreview();
                camera.setPreviewCallback(null);
                camera.unlock();
                camera.release();
            }
        } catch (RuntimeException e) {
        } finally {
            camera = null;
        }
    }


    /**
     * 初始化控件
     */
    private void initView() {
        shootVideoBack = (ImageView) findViewById(R.id.shoot_video_back); // 返回
        surfaceView = (SurfaceView) findViewById(R.id.surface_view); // 视频控件
        shootVideoIv = (ImageView) findViewById(shoot_video_iv); // 视屏背景
        shootVideoTime = (TextView) findViewById(R.id.shoot_video_time); // 视频录制的时间
        startOrStopBtn = (Button) findViewById(R.id.start_or_stop_btn); // 开始录制或暂停
        playVideoBtn = (Button) findViewById(R.id.play_video_btn); // 开始播放

    }

    /**
     * 监听
     */
    private void initListener() {
        shootVideoBack.setOnClickListener(this);
        startOrStopBtn.setOnClickListener(this);
        playVideoBtn.setOnClickListener(this);
    }

    /**
     * 实现监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shoot_video_back: // 返回
                ShootVideoActivity.this.finish();
                break;

            case R.id.start_or_stop_btn: // 开始或暂停录制视频
                if (mIsPlay) {
                    if (mediaPlayer != null) {
                        mIsPlay = false;
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                }
                if (!mStartedFlg) {
                    handler.postDelayed(runnable, 1000);
                    shootVideoIv.setVisibility(View.GONE);
                    if (mRecorder == null) {
                        mRecorder = new MediaRecorder();
                    }

                    camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
                    if (camera != null) {
                        camera.startPreview();
                        camera.setDisplayOrientation(90);
                        camera.unlock();
                        mRecorder.setCamera(camera);
                    }

                    try {
                        // 设置音频，视频采集方式，这两项需要放在setOutputFormat之前
                        mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
                        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

                        // 设置录制视频输出格式
                        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

                        // 设置音频，视频文件的编码，这两项需要放在setOutputFormat之后
                        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

                        mRecorder.setVideoEncodingBitRate(3 * 1024 * 1024); // 设置视频的比特率
                        mRecorder.setVideoFrameRate(30); // 设置录制的视频帧率
                        mRecorder.setVideoSize(640, 480); // 设置视频录制的分辨率
                        mRecorder.setOrientationHint(90);
                        // 设置记录会话的最大持续时间（毫秒）
                        mRecorder.setMaxDuration(30 * 1000);
                        mRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());

                        path = getSDPath();
                        if (path != null) {
                            File dir = new File(path + "/videos");
                            if (!dir.exists()) {
                                dir.mkdir();
                            }
                            path = dir + "/" + System.currentTimeMillis() + ".mp4";
                            Log.d("test", "视频拍摄后保存的路径----" + path.toString());
                            mRecorder.setOutputFile(path);
                            mRecorder.prepare();
                            mRecorder.start();
                            mStartedFlg = true;
                            startOrStopBtn.setText("停止");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else { // 点击停止录制
                    if (mStartedFlg) {
                        try {
                            handler.removeCallbacks(runnable);
                            mRecorder.stop();
                            mRecorder.reset();
                            mRecorder.release();
                            mRecorder = null;
                            startOrStopBtn.setText("开始");
                            if (camera != null) {
                                camera.release();
                                camera = null;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mStartedFlg = false;
                }
                break;

            case R.id.play_video_btn: // 播放视频
                mIsPlay = true;
                shootVideoIv.setVisibility(View.GONE);
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.reset();
                Uri uri = Uri.parse(path);
                mediaPlayer = MediaPlayer.create(ShootVideoActivity.this, uri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mediaPlayer.setDisplay(mSurfaceHolder);
                try {
                    mediaPlayer.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mediaPlayer.start();
                break;
        }
    }

    /**
     * 获取SD path
     *
     * @return
     */
    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        }
        return null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurfaceHolder = holder;
        releaseCamera(); // 初始化时释放相机资源，以免导致奔溃
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // 将holder，这个holder为开始在onCreate里面取得的holder，将它赋给mSurfaceHolder
        mSurfaceHolder = holder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        surfaceView = null;
        mSurfaceHolder = null;
        handler.removeCallbacks(runnable);
        if (mRecorder != null) {
            mRecorder.release(); // Now the object cannot be reused
            mRecorder = null;
        }
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private class CameraSizeComparator implements Comparator<Camera.Size> {
        @Override
        public int compare(Camera.Size o1, Camera.Size o2) {
            if (o1.width == o2.width) {
                return 0;
            } else if (o1.width > o2.width) {
                return 1;
            } else {
                return -1;
            }
        }
    }
}
