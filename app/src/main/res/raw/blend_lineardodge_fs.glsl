precision mediump float;
precision mediump int;

varying vec2 textureCoordinate;
varying vec2 textureCoordinate2;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform float opacity;

vec3 blend(vec4 base, vec4 overlay) {
     return base.rgb + overlay.rgb;
}

void main() {
     vec4 texture1Color = texture2D(inputImageTexture, textureCoordinate);

	 vec4 texture2Color = texture2D(inputImageTexture2, textureCoordinate2) * opacity;
	 float flag1 = float(textureCoordinate2.x >= 0.0 && textureCoordinate2.x <= 1.0 && textureCoordinate2.y >= 0.0 && textureCoordinate2.y <= 1.0);
	 texture2Color = mix(vec4(0.0), texture2Color, flag1);

	 float alpha = texture1Color.a + texture2Color.a - texture1Color.a * texture2Color.a;
	 vec3 color = blend(texture1Color, texture2Color);
	 gl_FragColor = vec4(color, alpha);
}