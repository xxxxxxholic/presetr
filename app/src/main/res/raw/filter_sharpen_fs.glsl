precision highp float;
 
 varying highp vec2 textureCoordinate;
 uniform sampler2D inputImageTexture;

 uniform float sharpness;
 uniform float radius;
 uniform float imageWidthFactor;
 uniform float imageHeightFactor;
 
 highp vec3 blurSample(highp vec2 uv, highp vec2 xoff, highp vec2 yoff) {
     highp vec3 v11 = texture2D(inputImageTexture, uv + xoff).rgb;
     highp vec3 v12 = texture2D(inputImageTexture, uv + yoff).rgb;
     highp vec3 v21 = texture2D(inputImageTexture, uv - xoff).rgb;
     highp vec3 v22 = texture2D(inputImageTexture, uv - yoff).rgb;
     return (v11 + v12 + v21 + v22 + 2.0 * texture2D(inputImageTexture,uv).rgb) * 0.166667;
 }

 highp vec3 edgeStrength(highp vec2 uv) {
     float spread = radius;
     highp vec2 offset = vec2(1.0) / vec2(imageWidthFactor, imageHeightFactor);
     highp vec2 up    = vec2(0.0, offset.y) * spread;
     highp vec2 right = vec2(offset.x, 0.0) * spread;
     const float frad =  3.0;
     highp vec3 v11 = blurSample(uv + up - right,     right, up);
     highp vec3 v12 = blurSample(uv + up,             right, up);
     highp vec3 v13 = blurSample(uv + up + right,     right, up);
     
     highp vec3 v21 = blurSample(uv - right,         right, up);
     highp vec3 v22 = blurSample(uv,                 right, up);
     highp vec3 v23 = blurSample(uv + right,         right, up);
     
     highp vec3 v31 = blurSample(uv - up - right,     right, up);
     highp vec3 v32 = blurSample(uv - up,             right, up);
     highp vec3 v33 = blurSample(uv - up + right,     right, up);
     
     highp vec3 laplacian_of_g = v11 * 0.0 + v12 *  1.0 + v13 * 0.0
                         + v21 * 1.0 + v22 * -4.0 + v23 * 1.0
                         + v31 * 0.0 + v32 *  1.0 + v33 * 0.0;
          laplacian_of_g = laplacian_of_g * 1.0;
     return laplacian_of_g.xyz;
 }
 void main() {
    highp vec4 color = texture2D(inputImageTexture,textureCoordinate);
    gl_FragColor = vec4(color.xyz - edgeStrength(textureCoordinate) * sharpness, 1.0);
 }
