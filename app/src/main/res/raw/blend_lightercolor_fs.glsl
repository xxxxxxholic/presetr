precision mediump float;
precision mediump int;

varying vec2 textureCoordinate;
varying vec2 textureCoordinate2;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform float opacity;

vec3 blend(vec4 base, vec4 overlay) {
 vec3 baseRGB = base.rgb / (base.a + step(base.a, 0.0));
 vec3 overlayRGB = overlay.rgb / (overlay.a + step(overlay.a, 0.0));
 float baseSum = baseRGB.r * 0.299 + baseRGB.g * 0.587 + baseRGB.b * 0.114;
 float overlaySum = overlayRGB.r * 0.299 + overlayRGB.g * 0.587 + overlayRGB.b * 0.114;
 vec3 color = overlaySum > baseSum ? overlayRGB : baseRGB;
 return (1.0 - overlay.a) * base.rgb + (1.0 - base.a) * overlay.rgb + base.a * overlay.a * color;
}

void main() {
 vec4 texture1Color = texture2D(inputImageTexture, textureCoordinate);
 
 vec4 outputColor = texture1Color;
 vec4 texture2Color = texture2D(inputImageTexture2, textureCoordinate2) * opacity;
 float flag1 = float(textureCoordinate2.x >= 0.0 && textureCoordinate2.x <= 1.0 && textureCoordinate2.y >= 0.0 && textureCoordinate2.y <= 1.0);
 texture2Color = mix(vec4(0.0), texture2Color, flag1);
 
 float alpha = texture1Color.a + texture2Color.a - texture1Color.a * texture2Color.a;
 vec3 color = blend(texture1Color, texture2Color);
 outputColor = vec4(color, alpha);

 gl_FragColor = outputColor;
}