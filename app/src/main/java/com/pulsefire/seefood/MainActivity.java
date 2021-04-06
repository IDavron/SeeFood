package com.pulsefire.seefood;


import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;

import android.content.Intent;
import android.hardware.Camera.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.ANN_MLP;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.pulsefire.seefood.recipes.InfoActivity;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;



public class MainActivity extends CameraActivity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

//New camera//
private CameraView mOpenCvCameraView;
    private List<Size> mResolutionList;
    private Menu mMenu;
    private boolean mCameraStarted = false;
    private boolean mMenuItemsCreated = false;
    private MenuItem[] mEffectMenuItems;
    private SubMenu mColorEffectsMenu;
    private MenuItem[] mResolutionMenuItems;
    private SubMenu mResolutionMenu;
    ANN_MLP ANN;
    private ProgressBar pgsBar;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                } break;
                default:
                {
                    super.onManagerConnected(status);
                } break;
            }
        }
    };

    public MainActivity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_main);

        mOpenCvCameraView = (CameraView) findViewById(R.id.camera);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        mOpenCvCameraView.setCvCameraViewListener(this);

        pgsBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCvCameraView);
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
        mCameraStarted = true;
        setupMenuItems();
        ANN = ANN_MLP.load("/storage/emulated/0/model.yml");
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        return inputFrame.rgba();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mMenu = menu;
        setupMenuItems();
        return true;
    }

    private void setupMenuItems() {
        if (mMenu == null || !mCameraStarted || mMenuItemsCreated) {
            Log.e(TAG, "mMenu: " + mMenu + " mCameraStarted: " + mCameraStarted + " mMenuCreated: " + mMenuItemsCreated);
            return;
        }
        List<String> effects = mOpenCvCameraView.getEffectList();

        if (effects == null) {
            Log.e(TAG, "Color effects are not supported by device!");
            return;
        }

        mColorEffectsMenu = mMenu.addSubMenu("Color Effect");
        mEffectMenuItems = new MenuItem[effects.size()];

        int idx = 0;
        ListIterator<String> effectItr = effects.listIterator();
        for (String effect: effects) {
            mEffectMenuItems[idx] = mColorEffectsMenu.add(1, idx, Menu.NONE, effect);
            idx++;
        }

        mResolutionMenu = mMenu.addSubMenu("Resolution");
        mResolutionList = mOpenCvCameraView.getResolutionList();
        mResolutionMenuItems = new MenuItem[mResolutionList.size()];

        idx = 0;
        for (Size resolution: mResolutionList) {
            mResolutionMenuItems[idx] = mResolutionMenu.add(2, idx, Menu.NONE,
                    Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString());
            idx++;
        }
        mMenuItemsCreated = true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);
        if (item.getGroupId() == 1)
        {
            mOpenCvCameraView.setEffect((String) item.getTitle());
            Toast.makeText(this, mOpenCvCameraView.getEffect(), Toast.LENGTH_SHORT).show();
        }
        else if (item.getGroupId() == 2)
        {
            int id = item.getItemId();
            Size resolution = mResolutionList.get(id);
            mOpenCvCameraView.setResolution(resolution);
            resolution = mOpenCvCameraView.getResolution();
            String caption = Integer.valueOf(resolution.width).toString() + "x" + Integer.valueOf(resolution.height).toString();
            Toast.makeText(this, caption, Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public void takePhoto(View v) {
        Log.i(TAG,"Taking photo");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String currentDateandTime = sdf.format(new Date());
        String fileName = Environment.getExternalStorageDirectory().getPath() +
                "/picture_" + currentDateandTime + ".jpg";
        mOpenCvCameraView.takePicture(fileName);
        pgsBar.setVisibility(v.VISIBLE);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mOpenCvCameraView.disableView();
                predictANN(fileName);
            }
        }, 1000);
    }

    public void predictANN(String filename){
        Mat imgBGR = Imgcodecs.imread(filename);

        Mat imgHSV = new Mat();
        Imgproc.cvtColor(imgBGR, imgHSV, Imgproc.COLOR_BGR2HSV);

        MatOfInt selectedChannels = new MatOfInt(0);
        Mat imgHist = new Mat();
        MatOfInt histSize = new MatOfInt(180);
        MatOfFloat ranges = new MatOfFloat(0f, 180f);

        Imgproc.calcHist(Arrays.asList(imgHSV), selectedChannels, new Mat(), imgHist, histSize, ranges);
        imgHist = imgHist.t();
        imgHist.convertTo(imgHist, CvType.CV_32F);

        Mat results = new Mat();
        ANN.predict(imgHist, results, 0);

        double response = results.get(0, 0)[0];
        int predictedClassIdx = (int) Math.round(response);

        String [] classesNames = {"cacao", "egg", "milk", "pasta"};
        String predictedLabel = classesNames[predictedClassIdx];

        Toast.makeText(getApplicationContext(), "Ingredient: " + predictedLabel, Toast.LENGTH_LONG).show();
        pgsBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, InfoActivity.class);
        intent.putExtra(InfoActivity.EXTRA, predictedLabel);
        startActivity(intent);
        mOpenCvCameraView.enableView();

    }
}
