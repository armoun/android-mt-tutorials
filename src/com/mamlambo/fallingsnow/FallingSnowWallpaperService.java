package com.mamlambo.fallingsnow;

import android.os.Bundle;
import android.renderscript.RenderScript;
import android.renderscript.RenderScriptGL;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

public class FallingSnowWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new FallingSnowWallpaperEngine();
    }
    
    private class FallingSnowWallpaperEngine extends Engine {
        private RenderScriptGL mRenderScriptGL;
        private SnowRS mSnowRS;

        @Override
        public Bundle onCommand(String action, int x, int y, int z,
                Bundle extras, boolean resultRequested) {
            return super.onCommand(action, x, y, z, extras, resultRequested);
        }

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            //surfaceHolder.setSizeFromLayout();
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            if (mSnowRS != null)  {
                mSnowRS.stop();
                mSnowRS = null;
            }
            if (mRenderScriptGL != null) {
                mRenderScriptGL.destroy();
                mRenderScriptGL = null;
            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset,
                float xOffsetStep, float yOffsetStep, int xPixelOffset,
                int yPixelOffset) {
            super.onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep,
                    xPixelOffset, yPixelOffset);
            if (mSnowRS != null) {
                //mSnowRS.setOffset(xPixelOffset, yPixelOffset);
            }
            
        }

        @Override
        public void onSurfaceChanged(SurfaceHolder holder, int format,
                int width, int height) {
            super.onSurfaceChanged(holder, format, width, height);
            
            
            if (mRenderScriptGL != null) {
                mRenderScriptGL.setSurface(holder, width, height);
            }
            if (mSnowRS == null) {
                mSnowRS = new SnowRS(width, height);
                mSnowRS.init(mRenderScriptGL, getResources(), isPreview());
                mSnowRS.start();
            } else {
                //mSnowRS.resize(width, height);
            }
        }

        @Override
        public void onSurfaceCreated(SurfaceHolder holder) {
            super.onSurfaceCreated(holder);
            
            RenderScriptGL.SurfaceConfig surfaceConfig = new RenderScriptGL.SurfaceConfig();
            mRenderScriptGL = new RenderScriptGL(FallingSnowWallpaperService.this, surfaceConfig);
            
            // use low for wallpapers
            mRenderScriptGL.setPriority(RenderScript.Priority.LOW);
        }

        @Override
        public void onSurfaceDestroyed(SurfaceHolder holder) {
            super.onSurfaceDestroyed(holder);
            if (mSnowRS != null)  {
                mSnowRS.stop();
                mSnowRS = null;
            }
            if (mRenderScriptGL != null) {
                mRenderScriptGL.destroy();
                mRenderScriptGL = null;
            }
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if (mSnowRS == null) {
                if (visible) {
                    mSnowRS.start();
                } else {
                    mSnowRS.stop();
                }
            }
        }
        
        
    }

}
