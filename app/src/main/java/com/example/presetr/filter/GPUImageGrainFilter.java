package com.example.presetr.filter;

import android.opengl.GLES20;

import com.example.presetr.R;
import com.example.presetr.util.GlUtil;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * default: 0
 * range: 0.0f ~ 20.0f
 */
public class GPUImageGrainFilter extends GPUImageFilter {

    //0.0~20.0, default 0.0
    public static final String GRAIN_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " \n" +
            " uniform highp float grain;\n" +
            " \n" +
            " void main() {\n" +
            "     highp vec2 uv = textureCoordinate;\n" +
            "     \n" +
            "     highp vec4 color = texture2D(inputImageTexture,fract(uv));\n" +
            "     \n" +
            "     highp float x = (uv.x + 4.0) * (uv.y + 4.0) * 10.0;\n" +
            "     highp vec4 grain = vec4(mod((mod(x, 13.0) + 1.0) * (mod(x, 123.0) + 1.0), 0.01) - 0.005) * grain;\n" +
            "     \n" +
            "     gl_FragColor = color + grain;\n" +
            " }";

    //0.0~1.0, default 0.0
    public static final String FRAGMENT = GlUtil.getStringFromRaw(R.raw.filter_grain_fs, false);

    private int grainLocation;
    private float grain;
    private int imageWidthPos;
    private int imageHeightPos;

    public float mDefault = 0.0f;

    public GPUImageGrainFilter() {
        this(0.0f);
    }

    public GPUImageGrainFilter(float grain) {
        super(NO_FILTER_VERTEX_SHADER, GRAIN_FRAGMENT_SHADER);
        this.grain = grain;
    }

    @Override
    public void onOutputSizeChanged(int width, int height) {
        super.onOutputSizeChanged(width, height);
//        setFloat(imageWidthPos, width);
//        setFloat(imageHeightPos, height);
    }

    @Override
    public void onInit() {
        super.onInit();
        grainLocation = GLES20.glGetUniformLocation(getProgram(), "grain");
//        imageHeightPos = GLES20.glGetUniformLocation(getProgram(), "imageH");
//        imageWidthPos = GLES20.glGetUniformLocation(getProgram(), "imageW");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setGrain(grain);
    }

    public void setGrain(final float grain) {
        this.grain = grain;
        setFloat(grainLocation, this.grain);
    }

    public float getGrain() {
        return grain;
    }
}
