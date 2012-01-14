package com.mamlambo.selfcam;

import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SimpleSelfCamActivity extends Activity implements PictureCallback {
	private final static String DEBUG_TAG = "SimpleSelfCamActivity";
	private Camera mCam;
	private CameraPreview camPreview;
	private int cameraId = 0;
	private FrameLayout preview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// do we have a camera?
		if (!getPackageManager()
				.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Toast.makeText(this, "No camera feature on this device",
					Toast.LENGTH_LONG).show();
		} else {

			cameraId = findFirstFrontFacingCamera();

			preview = (FrameLayout) findViewById(R.id.camPreview);
			preview.removeAllViews();

			startCameraInLayout(preview, cameraId);

			Button takePic = (Button) findViewById(R.id.capture);
			takePic.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					mCam.takePicture(null, null, SimpleSelfCamActivity.this);

				}
			});
		}
	}

	private int findFirstFrontFacingCamera() {
		int foundId = -1;
		// find the first front facing camera
		int numCams = Camera.getNumberOfCameras();
		for (int camId = 0; camId < numCams; camId++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(camId, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				Log.d(DEBUG_TAG, "Found front facing camera");
				foundId = camId;
				break;
			}
		}
		return foundId;
	}

	private void startCameraInLayout(FrameLayout layout, int cameraId) {
		mCam = Camera.open(cameraId);

		camPreview = new CameraPreview(this, mCam);
		layout.addView(camPreview);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCam == null && preview != null) {
			preview.removeAllViews();
			startCameraInLayout(preview, cameraId);
		}
	}

	@Override
	protected void onPause() {
		if (mCam != null) {
			mCam.release();
			mCam = null;
		}
		super.onPause();
	}

	public class CameraPreview extends SurfaceView implements
			SurfaceHolder.Callback {
		private SurfaceHolder mHolder;
		private Camera mCamera;

		public CameraPreview(Context context, Camera camera) {
			super(context);
			mCamera = camera;
			mHolder = getHolder();
			mHolder.addCallback(this);
			mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		public void surfaceCreated(SurfaceHolder holder) {
			try {
				mCamera.setPreviewDisplay(holder);
				mCamera.startPreview();
			} catch (Exception error) {
				Log.d(DEBUG_TAG,
						"Error starting preview: " + error.getMessage());
			}
		}

		public void surfaceDestroyed(SurfaceHolder holder) {
		}

		public void surfaceChanged(SurfaceHolder holder, int format, int w,
				int h) {
			if (mHolder.getSurface() == null) {
				// preview surface does not exist
				return;
			}

			// can't make changes when preview is active
			try {
				mCamera.stopPreview();
			} catch (Exception e) {

			}

			
			try {
				// set rotation to match device orientation
				setCameraDisplayOrientationAndSize();
				
				// start up the preview
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();

			} catch (Exception error) {
				Log.d(DEBUG_TAG, "Error starting preview: " + error.getMessage());
			}
		}

		public void setCameraDisplayOrientationAndSize() {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(cameraId, info);
			int rotation = getWindowManager().getDefaultDisplay().getRotation();
			int degrees = rotation * 90;

			/*
			 * // the above is just a shorter way of doing this, but could break
			 * if the values change switch (rotation) { case Surface.ROTATION_0:
			 * degrees = 0; break; case Surface.ROTATION_90: degrees = 90;
			 * break; case Surface.ROTATION_180: degrees = 180; break; case
			 * Surface.ROTATION_270: degrees = 270; break; }
			 */

			int result;
			if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
				result = (info.orientation + degrees) % 360;
				result = (360 - result) % 360;
			} else {
				result = (info.orientation - degrees + 360) % 360;
			}
			mCamera.setDisplayOrientation(result);

			Camera.Size previewSize = mCam.getParameters().getPreviewSize();
			if (result == 90 || result == 270) {
				// swap - the physical camera itself doesn't rotate ;)
				mHolder.setFixedSize(previewSize.height, previewSize.width);
			} else {
				mHolder.setFixedSize(previewSize.width, previewSize.height);

			}
		}

	}

	public void onPictureTaken(byte[] data, Camera camera) {
		File pictureFileDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"SimpleSelfCam");// new
		// File("cap.jpg");

		if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

			Log.d(DEBUG_TAG, "Can't create directory to save image");
			Toast.makeText(this, "Can't make path to save pic.",
					Toast.LENGTH_LONG).show();
			return;

		}

		String filename = pictureFileDir.getPath() + File.separator
				+ "latest_mug.jpg";
		File pictureFile = new File(filename);

		try {
			FileOutputStream fos = new FileOutputStream(pictureFile);
			fos.write(data);
			fos.close();
			Toast.makeText(this, "Image saved as latest_mug.jpg",
					Toast.LENGTH_LONG).show();
		} catch (Exception error) {
			Log.d(DEBUG_TAG, "File not saved: " + error.getMessage());
			Toast.makeText(this, "Can't save image.", Toast.LENGTH_LONG).show();
		}
	}
}