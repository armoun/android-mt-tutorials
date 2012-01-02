package com.mamlambo.fallingsnow;

import android.renderscript.Mesh;
import android.renderscript.ProgramFragmentFixedFunction;
import android.renderscript.ScriptC;

import com.android.wallpaper.RenderScriptScene;


public class SnowRS extends RenderScriptScene {

    public static final int SNOW_FLAKES = 4000;
    private ScriptC_snow mScript;

    public SnowRS(int width, int height) {
        super(width, height);
    }


    @Override
    public ScriptC createScript() {
        ProgramFragmentFixedFunction.Builder pfb = new ProgramFragmentFixedFunction.Builder(
                getRS());
        pfb.setVaryingColor(true);
        getRS().bindProgramFragment(pfb.create());

        ScriptField_Snow snow = new ScriptField_Snow(mRS, SNOW_FLAKES);
        Mesh.AllocationBuilder smb = new Mesh.AllocationBuilder(mRS);
        smb.addVertexAllocation(snow.getAllocation());
        smb.addIndexSetType(Mesh.Primitive.POINT);
        Mesh sm = smb.create();

        mScript = new ScriptC_snow(mRS, getResources(), R.raw.snow);
        mScript.set_snowMesh(sm);
        mScript.bind_snow(snow);
        mScript.invoke_initSnow();
        return mScript;
    }


}
