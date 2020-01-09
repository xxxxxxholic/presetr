precision highp float;

varying highp vec2 textureCoordinate;
varying highp vec2 textureCoordinate2;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform float opacity;

 vec3 colorburn(vec4 Cb, vec4 Cs) {
     float CbCsAlphaProduct = Cb.a * Cs.a;

     float ra;
     if (Cs.r == 0.0) {
         ra = 0.0;
     } else {
         ra = CbCsAlphaProduct - min(CbCsAlphaProduct,
                                     (CbCsAlphaProduct * Cs.a - Cb.r * Cs.a * Cs.a) / Cs.r);
     }

     float rg;
     if (Cs.g == 0.0) {
         rg = 0.0;
     } else {
         rg = CbCsAlphaProduct - min(CbCsAlphaProduct,
                                     (CbCsAlphaProduct * Cs.a - Cb.g * Cs.a * Cs.a) / Cs.g);
     }

     float rb;
     if (Cs.b == 0.0) {
         rb = 0.0;
     } else {
         rb = CbCsAlphaProduct - min(CbCsAlphaProduct,
                                     (CbCsAlphaProduct * Cs.a - Cb.b * Cs.a * Cs.a) / Cs.b);
     }

     return (1.0 - Cs.a) * Cb.rgb + (1.0 - Cb.a) * Cs.rgb + vec3(ra, rg, rb);
 }

void main()
{
    vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);
    vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2) * opacity;

    float alpha = textureColor.a + textureColor2.a - textureColor.a * textureColor2.a;
    vec3 color = colorburn(textureColor, textureColor2);
    gl_FragColor = vec4(color, alpha);
}