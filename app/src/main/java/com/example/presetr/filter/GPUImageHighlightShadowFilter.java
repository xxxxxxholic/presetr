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

import android.opengl.GLES20;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

/**
 * default:   highlight - 0, shadow - 0
 * range: highlight -1.0f ~ 1.0f   shadow  -0.5f, 0.5f
 */
public class GPUImageHighlightShadowFilter extends GPUImageFilter {
    public static final String HIGHLIGHT_SHADOW_FRAGMENT_SHADER = "" +
            "uniform sampler2D inputImageTexture;\n" +
            "varying highp vec2 textureCoordinate;\n" +

            "uniform lowp float shadows;\n" +
            "uniform lowp float highlights;\n" +

            "const mediump vec3 luminanceWeighting = vec3(0.3, 0.3, 0.3);\n" +

            "void main()\n" +
            "{\n" +
            "lowp vec4 source = texture2D(inputImageTexture, textureCoordinate);\n" +
            "mediump float luminance = dot(source.rgb, luminanceWeighting);\n" +

            "mediump float shadow;\n" +
            "if (shadows > 0.0) {\n" +
            "shadow = clamp((pow(luminance, 1.0/(shadows+1.0)) + (-0.76)*pow(luminance, 2.0/(shadows+1.0))) - luminance, 0.0, 1.0);\n" +
            "} else {\n" +
            "shadow = clamp((pow(luminance, 1.0-shadows) + (0.76)*pow(luminance, 2.0*(1.0-shadows))) - luminance, -1.0, 0.0);\n" +
            "}\n" +

            "mediump float highlight;\n" +
            "if (highlights < 0.0) {\n" +
            "highlight = clamp((1.0 - (pow(1.0-luminance, 1.0/(1.0-highlights)) + (-0.8)*pow(1.0-luminance, 2.0/(1.0-highlights)))) - luminance, -1.0, 0.0);\n" +
            "} else {\n" +
            "highlight = clamp((1.0 - pow(1.0-luminance, 1.0+highlights) + (-0.8)*pow(1.0-luminance, 2.0*(1.0+highlights))) - luminance, 0.0, 1.0);\n" +
            "}\n" +

            "lowp vec3 result = vec3(0.0, 0.0, 0.0) + ((luminance + shadow + highlight) - 0.0) * ((source.rgb - vec3(0.0, 0.0, 0.0))/(luminance - 0.0));\n" +
            "gl_FragColor = vec4(result.rgb, source.a);\n" +
            "}\n";

    private int shadowsLocation;
    public float shadows;
    private int highlightsLocation;
    public float highlights;

    public float mDefault_shadows = 0.0f;
    public float mDefault_highlights = 1.0f;

    public GPUImageHighlightShadowFilter() {
        this(0.0f, 1.0f);
    }

    public GPUImageHighlightShadowFilter(final float shadows, final float highlights) {
        super(NO_FILTER_VERTEX_SHADER, HIGHLIGHT_SHADOW_FRAGMENT_SHADER);
        this.highlights = highlights;
        this.shadows = shadows;
    }

    @Override
    public void onInit() {
        super.onInit();
        highlightsLocation = GLES20.glGetUniformLocation(getProgram(), "highlights");
        shadowsLocation = GLES20.glGetUniformLocation(getProgram(), "shadows");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setHighlights(highlights);
        setShadows(shadows);
    }

    public void setHighlights(final float highlights) {
        this.highlights = highlights;
        setFloat(highlightsLocation, this.highlights);
    }

    public void setShadows(final float shadows) {
        this.shadows = shadows;
        setFloat(shadowsLocation, this.shadows);
    }
}
