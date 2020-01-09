precision mediump float;
precision mediump int;

varying vec2 textureCoordinate;
varying vec2 textureCoordinate2;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform float opacity;

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

vec3 blend(vec4 base, vec4 overlay) {
 vec3 baseRGB = base.rgb / (base.a + step(base.a, 0.0));
 vec3 overlayRGB = overlay.rgb / (overlay.a + step(overlay.a, 0.0));
 float r = eachcomponent(baseRGB.r, overlayRGB.r);
 float g = eachcomponent(baseRGB.g, overlayRGB.g);
 float b = eachcomponent(baseRGB.b, overlayRGB.b);
 vec3 color = clamp(vec3(r, g, b), 0.0, 1.0);
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