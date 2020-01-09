precision highp float;
varying highp vec2 textureCoordinate;
uniform sampler2D inputImageTexture;
uniform lowp vec3 color;

void main() {
    gl_FragColor = vec4(color,1.0);
}