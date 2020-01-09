/*
 * Copyright (C) 2018 CyberAgent, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.presetr.filter;

import android.graphics.PointF;
import android.opengl.GLES20;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * 该滤镜调节的是Start值
 * centerPoint: x - 0.5, y - 0.5
 * color: 0,0,0(可自定义)
 * vignetteEnd: 0.8
 * defaultStart：0.7
 * VignetteStartRange：0.25f ~ 0.7f
 */
public class GPUImageVignetteFilter extends GPUImageFilter {
    public static final String VIGNETTING_FRAGMENT_SHADER = "" +
            " uniform sampler2D inputImageTexture;\n" +
            " varying highp vec2 textureCoordinate;\n" +
            " \n" +
            " uniform lowp vec2 vignetteCenter;\n" +
            " uniform lowp vec3 vignetteColor;\n" +
            " uniform highp float vignetteStart;\n" +
            " uniform highp float vignetteEnd;\n" +
            " \n" +
            " void main()\n" +
            " {\n" +
            "     /*\n" +
            "     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "     lowp float d = distance(textureCoordinate, vec2(0.5,0.5));\n" +
            "     rgb *= (1.0 - smoothstep(vignetteStart, vignetteEnd, d));\n" +
            "     gl_FragColor = vec4(vec3(rgb),1.0);\n" +
            "      */\n" +
            "     \n" +
            "     lowp vec3 rgb = texture2D(inputImageTexture, textureCoordinate).rgb;\n" +
            "     lowp float d = distance(textureCoordinate, vec2(vignetteCenter.x, vignetteCenter.y));\n" +
            "     lowp float percent = smoothstep(vignetteStart, vignetteEnd, d);\n" +
            "     gl_FragColor = vec4(mix(rgb.x, vignetteColor.x, percent), mix(rgb.y, vignetteColor.y, percent), mix(rgb.z, vignetteColor.z, percent), 1.0);\n" +
            " }";

    private int vignetteCenterLocation;
    private PointF vignetteCenter;
    private int vignetteColorLocation;
    private float[] vignetteColor;
    private int vignetteStartLocation;
    private float vignetteStart;
    private int vignetteEndLocation;
    private float vignetteEnd;

    public float mDefault = 0.7f;
    public GPUImageVignetteFilter() {
        this(new PointF(), new float[]{0.0f, 0.0f, 0.0f}, 0.7f, 0.8f);
    }

    public GPUImageVignetteFilter(final PointF vignetteCenter, final float[] vignetteColor, final float vignetteStart, final float vignetteEnd) {
        super(NO_FILTER_VERTEX_SHADER, VIGNETTING_FRAGMENT_SHADER);
        this.vignetteCenter = vignetteCenter;
        this.vignetteColor = vignetteColor;
        this.vignetteStart = vignetteStart;
        this.vignetteEnd = vignetteEnd;

    }

    @Override
    public void onInit() {
        super.onInit();
        vignetteCenterLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteCenter");
        vignetteColorLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteColor");
        vignetteStartLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteStart");
        vignetteEndLocation = GLES20.glGetUniformLocation(getProgram(), "vignetteEnd");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setVignetteCenter(vignetteCenter);
        setVignetteColor(vignetteColor);
        setVignetteStart(vignetteStart);
        setVignetteEnd(vignetteEnd);
    }

    public void setVignetteCenter(final PointF vignetteCenter) {
        this.vignetteCenter = vignetteCenter;
        setPoint(vignetteCenterLocation, this.vignetteCenter);
    }

    public void setVignetteColor(final float[] vignetteColor) {
        this.vignetteColor = vignetteColor;
        setFloatVec3(vignetteColorLocation, this.vignetteColor);
    }

    public void setVignetteStart(final float vignetteStart) {
        this.vignetteStart = vignetteStart;
        setFloat(vignetteStartLocation, this.vignetteStart);
    }

    public void setVignetteEnd(final float vignetteEnd) {
        this.vignetteEnd = vignetteEnd;
        setFloat(vignetteEndLocation, this.vignetteEnd);
    }

    public float getVignetteStart() {
        return vignetteStart;
    }
}
