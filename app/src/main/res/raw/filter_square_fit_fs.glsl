precision highp float;
varying highp vec2 textureCoordinate;
varying highp vec2 textureCoordinate2;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform highp vec4 rect;
uniform lowp float blur;

void main() {
    highp vec4 texture1Color;
    if (blur > 0.0) {
        texture1Color = texture2D(inputImageTexture2, textureCoordinate2);
        //texture1Color = texture2D(inputImageTexture2, vec2(0.5,0.5));
    } else {
        texture1Color = texture2D(inputImageTexture2, vec2(0.5,0.5));
    }

    highp vec4 outputColor = texture1Color;
    highp vec2 lt_point = rect.xy;
    highp vec2 rb_point = rect.zw;
    if (textureCoordinate.x >= lt_point.x && textureCoordinate.y <= lt_point.y && textureCoordinate.x <= rb_point.x && textureCoordinate.y >= rb_point.y) {
        highp vec2 tex2Coord = vec2((textureCoordinate - lt_point) / (rb_point - lt_point));
        outputColor = texture2D(inputImageTexture, tex2Coord);
    }
    //if (all(lessThanEqual(lt_point, textureCoordinate)), all(lessThanEqual(textureCoordinate, rb_point)))) {
    //    highp vec2 tex2Coord = vec2((textureCoordinate - lt_point) / (rb_point - lt_point));
    //    outputColor = texture2D(inputImageTexture, tex2Coord);
    //}
    gl_FragColor = outputColor;
    //gl_FragColor = gl_FragColor * 0.0 + texture1Color;
}