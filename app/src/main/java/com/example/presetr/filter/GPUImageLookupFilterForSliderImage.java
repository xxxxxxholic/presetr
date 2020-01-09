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

import jp.co.cyberagent.android.gpuimage.filter.GPUImageTwoInputFilter;

/**
 * 八宫格滤镜
 */
public class GPUImageLookupFilterForSliderImage extends GPUImageTwoInputFilter {

    public static final String LOOKUP_FRAGMENT_SHADER =
            "varying highp vec2 textureCoordinate;\n" +
            " varying highp vec2 textureCoordinate2; // TODO: This is not used\n" +
            " \n" +
            " uniform sampler2D inputImageTexture;\n" +
            " uniform sampler2D inputImageTexture2; // lookup texture\n" +
            " \n" +
            " uniform lowp float intensity;\n" +
            "uniform lowp int backOld;"+
            "uniform highp float divide_x;"+
            " \n" +
            " void main()\n" +
            " {\n" +
            "     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            // TODO 因为美术那边对于某些滤镜实在没有办法解决断层的问题，经测试后，
            //  对那些有问题的滤镜使用先，先降低0.2的曝光度再使用滤镜就不会有问题
//            "     textureColor = vec4(textureColor.rgb * pow(2.0, -0.2), textureColor.w);\n" +
            "if(backOld == 1){" +
            "   gl_FragColor = textureColor;" +
            "   return;" +
            "}"+
            "     \n" +
            "     highp float blueColor = textureColor.b * 63.0;\n" +
            "     \n" +
            "     highp vec2 quad1;\n" +
            "     quad1.y = floor(floor(blueColor) / 8.0);\n" +
            "     quad1.x = floor(blueColor) - (quad1.y * 8.0);\n" +
            "     \n" +
            "     highp vec2 quad2;\n" +
            "     quad2.y = floor(ceil(blueColor) / 8.0);\n" +
            "     quad2.x = ceil(blueColor) - (quad2.y * 8.0);\n" +
            "     \n" +
            "     highp vec2 texPos1;\n" +
            "     texPos1.x = (quad1.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
            "     texPos1.y = (quad1.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
            "     \n" +
            "     highp vec2 texPos2;\n" +
            "     texPos2.x = (quad2.x * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.r);\n" +
            "     texPos2.y = (quad2.y * 0.125) + 0.5/512.0 + ((0.125 - 1.0/512.0) * textureColor.g);\n" +
            "     \n" +
            "     lowp vec4 newColor1 = texture2D(inputImageTexture2, texPos1);\n" +
            "     lowp vec4 newColor2 = texture2D(inputImageTexture2, texPos2);\n" +
            "     \n" +
            "     lowp vec4 newColor = mix(newColor1, newColor2, fract(blueColor));\n" +
            "       if(textureCoordinate.x<divide_x){" +
            "        gl_FragColor = textureColor;"+
            "       }else{"+
            "           gl_FragColor = mix(textureColor, vec4(newColor.rgb, textureColor.w), intensity);\n" +
            "       }"+
//            "     gl_FragColor = texture2D(inputImageTexture2, textureCoordinate);\n" +
            " }";
    //cg add
    private boolean notNeedDraw;

    private int backOldLocation;
    public int backOld;
    private int divide_xLocation;
    public float divide_x;
    private int intensityLocation;
    public float intensity;

    public GPUImageLookupFilterForSliderImage() {
        this(1.0f);
    }

    public GPUImageLookupFilterForSliderImage(final float intensity) {
        super(LOOKUP_FRAGMENT_SHADER);
        this.intensity = intensity;

        this.notNeedDraw = false;
    }


    @Override
    public void onInit() {
        super.onInit();
        intensityLocation = GLES20.glGetUniformLocation(getProgram(), "intensity");
        backOldLocation  = GLES20.glGetUniformLocation(getProgram(),"backOld");
        divide_xLocation = GLES20.glGetUniformLocation(getProgram(),"divide_x");
    }

    @Override
    public void onInitialized() {
        super.onInitialized();
        setIntensity(intensity);
        setBackOld(backOld);
        setDivide_x(divide_x);
    }


    public void setIntensityValue(final float intensity) {
        this.intensity = intensity;
    }

    public void setBackOld(final int backOld) {
        this.backOld = backOld;
        setInteger(backOldLocation,this.backOld);
    }
    public void setDivide_x(final float divide_x){
        this.divide_x = divide_x;
        setFloat(divide_xLocation,this.divide_x);
    }

    public void setIntensity(final float intensity) {
        this.intensity = intensity;
        setFloat(intensityLocation, this.intensity);
    }
}
