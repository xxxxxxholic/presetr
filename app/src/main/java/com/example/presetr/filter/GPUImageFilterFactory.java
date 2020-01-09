package com.example.presetr.filter;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter;

public class GPUImageFilterFactory {
    private static GPUImageBrightnessFilter brightnessFilter = new GPUImageBrightnessFilter();//亮度
    private static GPUImageContrastFilter contrastFilter = new GPUImageContrastFilter();//对比度
    private static GPUImageSaturationFilter saturationFilter = new GPUImageSaturationFilter();//饱和度
    private static GPUImageWhiteBalanceFilter whiteBalanceFilter = new GPUImageWhiteBalanceFilter();//色温
    private static GPUImageSharpenFilter sharpenFilter = new GPUImageSharpenFilter();//锐化
    private static GPUImageVignetteFilter vignetteFilter = new GPUImageVignetteFilter();//暗角
    private static GPUImageGrainFilter grainFilter = new GPUImageGrainFilter();//噪点
    private static GPUImageHighlightShadowFilter shadowFilter = new GPUImageHighlightShadowFilter();//阴影
    private static GPUImageHighlightShadowFilter highlightShadowFilter = new GPUImageHighlightShadowFilter();//高光
    private static GPUImageHueFilter hueFilter = new GPUImageHueFilter();//色调

    public static GPUImageFilter getFilter(int type){
        switch (type){
            case 0: return brightnessFilter;
            case 1: return contrastFilter;
            case 2: return saturationFilter;
            case 3: return whiteBalanceFilter;
            case 4: return sharpenFilter;
            case 5: return vignetteFilter;
            case 6: return grainFilter;
            case 7: return shadowFilter;
            case 8: return highlightShadowFilter;
            case 9: return hueFilter;
            default: throw new IllegalStateException("Unexpected value: " + type);
        }
    }
}
