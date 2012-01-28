package com.mamlambo.fallingsnow;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.Float2;
import android.renderscript.Matrix4f;
import android.renderscript.Mesh;
import android.renderscript.ProgramFragment;
import android.renderscript.ProgramFragmentFixedFunction;
import android.renderscript.ProgramRaster;
import android.renderscript.ProgramRaster.CullMode;
import android.renderscript.ProgramStore;
import android.renderscript.ProgramVertex;
import android.renderscript.RenderScript;
import android.renderscript.RenderScriptGL;
import android.renderscript.ScriptC;
import android.util.Log;

public class SnowRS {
    private static final String DEBUG_TAG = "SnowRS";

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
    
    public void resize(int width, int height) {
        mWidth = width;
        mHeight = height;
        
        //TODO  redo points to cover new area
    }
    
    public void setOffset(int pixelOffsetX, int pixelOffsetY) {
        Log.d (DEBUG_TAG, "x offset = ["+pixelOffsetX+"], y offset ["+pixelOffsetY+"]");
        /*Float2 offset = mVpConsts.get_offset(0);
        offset.x = pixelOffsetX;
        offset.y = pixelOffsetY;
        mVpConsts.set_offset(0, offset, true);*/
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

        updateProjectionMatrices();
        createVertexShader(sm.getVertexAllocation(0).getType().getElement());
        
        ProgramFragment pfs = configurePointSprite();
        
        mRS.bindProgramStore(BLEND_ADD_DEPTH_NONE(mRS));
        
        ScriptC_snow script;
        script = new ScriptC_snow(mRS, getResources(), R.raw.snow);
        script.set_snowMesh(sm);
        script.set_gPFSnow(pfs);
        script.bind_snow(snow);
        script.invoke_initSnow();

        return script;
    }
    
    private ScriptField_VpConsts mVpConsts;

    void updateProjectionMatrices() {
        mVpConsts = new ScriptField_VpConsts(mRS, 1,
                                             Allocation.USAGE_SCRIPT |
                                             Allocation.USAGE_GRAPHICS_CONSTANTS);
                                             
        ScriptField_VpConsts.Item i = new ScriptField_VpConsts.Item();
        Matrix4f mvp = new Matrix4f();
        mvp.loadOrtho(0, mRS.getWidth(), mRS.getHeight(), 0, -1, 1);
        Float2 wo = new Float2(0, 0);
        i.MVP = mvp;
        i.offset = wo;
        mVpConsts.set(i, 0, true);
    }
    
    private void createVertexShader(Element input) {
        ProgramVertex.Builder sb = new ProgramVertex.Builder(mRS);
        String t =  "varying vec4 varColor;\n" +
                    "void main() {\n" +
                    "  vec4 pos = vec4(0.0, 0.0, 0.0, 1.0);\n" +
                    "  pos.xy = ATTRIB_position;\n" +
                    "  pos.x = pos.x + UNI_offset.x;\n" +
                    "  gl_Position = UNI_MVP * pos;\n" +
                    "  varColor = ATTRIB_color;\n" +
                    "  gl_PointSize = ATTRIB_size;\n" +
                    "}\n";
        sb.setShader(t);
        sb.addConstant(mVpConsts.getType());
        sb.addInput(input);
        ProgramVertex pvs = sb.create();
        
        pvs.bindConstants(mVpConsts.getAllocation(), 0);
        mRS.bindProgramVertex(pvs);
    }

    private ProgramFragment configurePointSprite() {
    	ProgramFragmentFixedFunction.Builder builder = new ProgramFragmentFixedFunction.Builder(mRS);
    	
        builder = new ProgramFragmentFixedFunction.Builder(mRS);
        builder.setPointSpriteTexCoordinateReplacement(true);
        builder.setTexture(ProgramFragmentFixedFunction.Builder.EnvMode.MODULATE,
                           ProgramFragmentFixedFunction.Builder.Format.RGBA, 0);
        builder.setVaryingColor(true);
        ProgramFragment pfs = builder.create();

        pfs.bindTexture(loadTextureARGB(R.drawable.snowflake), 0);
    	
        ProgramRaster.Builder b = new ProgramRaster.Builder(mRS);
        b.setPointSpriteEnabled(true);
        b.setCullMode(CullMode.NONE);
        ProgramRaster pr = b.create();
        mRS.bindProgramRaster(pr);
        
        
        return pfs;
    }
    
    
    private Allocation loadTextureARGB(int id) {
    	BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inScaled = false;
    	options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap b = BitmapFactory.decodeResource(mResources, id, options);
        final Allocation allocation = Allocation.createFromBitmap(mRS, b);
        return allocation;
    }
    
    
    ProgramStore BLEND_ADD_DEPTH_NONE(RenderScript rs) {
        ProgramStore.Builder builder = new ProgramStore.Builder(rs);

        builder.setBlendFunc(ProgramStore.BlendSrcFunc.ONE, ProgramStore.BlendDstFunc.ONE);
        builder.setDitherEnabled(false);
        builder.setDepthMaskEnabled(false);
        return builder.create();
    }
}
