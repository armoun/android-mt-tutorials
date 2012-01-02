package com.mamlambo.fallingsnow;

import android.content.res.Resources;
import android.renderscript.Mesh;
import android.renderscript.RenderScriptGL;
import android.renderscript.ScriptC;

public class SnowRS {

    public static final int SNOW_FLAKES = 4000;
    private ScriptC_snow mScript;

    protected int mWidth;
    protected int mHeight;
    protected boolean mPreview;
    protected Resources mResources;
    protected RenderScriptGL mRS;
    
    public SnowRS(int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    public void stop() {
        mRS.bindRootScript(null);
    }

    public void start() {
        mRS.bindRootScript(mScript);
    }
    
    public void init(RenderScriptGL rs, Resources res, boolean isPreview) {
        mRS = rs;
        mResources = res;
        mPreview = isPreview;
        mScript = (ScriptC_snow) createScript();
    }
    
    public RenderScriptGL getRS() {
        return mRS;
    }
    
    public Resources getResources() {
        return mResources;
    }
    
    public ScriptC createScript() {
        ScriptField_Snow snow = new ScriptField_Snow(mRS, SNOW_FLAKES);
        Mesh.AllocationBuilder smb = new Mesh.AllocationBuilder(mRS);
        smb.addVertexAllocation(snow.getAllocation());
        smb.addIndexSetType(Mesh.Primitive.POINT);
        Mesh sm = smb.create();

        ScriptC_snow script;
        script = new ScriptC_snow(mRS, getResources(), R.raw.snow);
        script.set_snowMesh(sm);
        script.bind_snow(snow);
        script.invoke_initSnow();
        return script;
    }


}
