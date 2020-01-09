precision highp float;

 varying highp vec2 textureCoordinate;
 uniform sampler2D inputImageTexture;

 uniform lowp float intensity;
 uniform lowp float saturation;
 uniform lowp float brightness;

 uniform lowp float imageW;
 uniform lowp float imageH;

 float normpdf(float x, float sigma) {
     return 0.39894*exp(-0.5*x*x/(sigma*sigma))/sigma;
 }

 vec4 blurColor(sampler2D image, vec2 uv) {
    vec3 c = texture2D(image, uv).rgb;
    const int mSize = 11;
    const int kSize = (mSize-1)/2;
    float kernel[mSize];
    vec3 final_colour = vec3(0.0);

    float sigma = 7.0;
    float Z = 0.0;
    for (int j = 0; j <= kSize; ++j) {
        kernel[kSize+j] = kernel[kSize-j] = normpdf(float(j), sigma);
    }
    for (int j = 0; j < mSize; ++j) {
        Z += kernel[j];
    }

    for (int i=-kSize; i <= kSize; ++i)
    {
        for (int j=-kSize; j <= kSize; ++j)
        {
            vec2 uv1 = uv + vec2(float(i),float(j)) / vec2(imageW,imageH);
            final_colour += kernel[kSize+j] * kernel[kSize+i] * texture2D(image, uv1).rgb;
        }
    }
    return vec4(final_colour/(Z*Z), 1.0);
 }


 float eachcomponent(float Cb, float Cs) {
     if (Cs <= 0.5) {
         return Cb - (1.0 - 2.0 * Cs) * Cb * (1.0 - Cb);
     } else {
         float zeus = 0.0;
         if (Cb <= 0.25) {
             zeus = ((16.0 * Cb - 12.0) * Cb + 4.0) * Cb;
         } else {
             zeus = sqrt(Cb);
         }
         return Cb + (2.0 * Cs - 1.0) * (zeus - Cb);
     }
 }

 highp vec3 blend(highp vec4 base, highp vec4 overlay) {
     highp vec3 baseRGB = base.rgb / (base.a + step(base.a, 0.0));
     highp vec3 overlayRGB = overlay.rgb / (overlay.a + step(overlay.a, 0.0));
     float r = eachcomponent(baseRGB.r, overlayRGB.r);
     float g = eachcomponent(baseRGB.g, overlayRGB.g);
     float b = eachcomponent(baseRGB.b, overlayRGB.b);
     highp vec3 color = clamp(vec3(r, g, b), 0.0, 1.0);
     return (1.0 - overlay.a) * base.rgb + (1.0 - base.a) * overlay.rgb + base.a * overlay.a * color;
 }

// highp vec3 blend(highp vec4 base, highp vec4 overlay) {
//     float ra;
//     if (2.0 * overlay.r < overlay.a) {
//         ra = 2.0 * overlay.r * base.r + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);
//     } else {
//         ra = overlay.a * base.a - 2.0 * (base.a - base.r) * (overlay.a - overlay.r) + overlay.r * (1.0 - base.a) + base.r * (1.0 - overlay.a);
//     }
//
//     float ga;
//     if (2.0 * overlay.g < overlay.a) {
//         ga = 2.0 * overlay.g * base.g + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);
//     } else {
//         ga = overlay.a * base.a - 2.0 * (base.a - base.g) * (overlay.a - overlay.g) + overlay.g * (1.0 - base.a) + base.g * (1.0 - overlay.a);
//     }
//
//     float ba;
//     if (2.0 * overlay.b < overlay.a) {
//         ba = 2.0 * overlay.b * base.b + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);
//     } else {
//         ba = overlay.a * base.a - 2.0 * (base.a - base.b) * (overlay.a - overlay.b) + overlay.b * (1.0 - base.a) + base.b * (1.0 - overlay.a);
//     }
//
//     return vec3(ra, ga, ba);
// }

 vec4 adjustColor(vec4 color) {
    const mediump vec3 luminanceWeighting = vec3(0.2125, 0.7154, 0.0721);
    lowp float luminance = dot(color.rgb, luminanceWeighting);
    lowp vec3 greyScaleColor = vec3(luminance);
    color = vec4(mix(greyScaleColor, color.rgb, saturation), color.a);
    color = vec4((color.rgb + vec3(brightness)), color.a) * intensity;
    return color;
 }

 void main() {
    highp vec4 texture1Color = texture2D(inputImageTexture, textureCoordinate);
    highp vec4 texture2Color = blurColor(inputImageTexture, textureCoordinate);
    texture2Color = adjustColor(texture2Color);

    float alpha = texture1Color.a + texture2Color.a - texture1Color.a * texture2Color.a;
    gl_FragColor = vec4(blend(texture1Color, texture2Color), alpha);
 }