precision highp float;

varying highp vec2 textureCoordinate;
varying highp vec2 textureCoordinate2;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform float opacity;

void main()
{
    vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);
    vec4 textureColor2 = texture2D(inputImageTexture2, textureCoordinate2) * opacity;

    vec4 whiteColor = vec4(1.0);
    gl_FragColor = whiteColor - ((whiteColor - textureColor2) * (whiteColor - textureColor));
}