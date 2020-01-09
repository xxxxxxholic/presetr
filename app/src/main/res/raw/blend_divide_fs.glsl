precision highp float;
precision mediump int;

varying highp vec2 textureCoordinate;
varying highp vec2 textureCoordinate2;

uniform sampler2D inputImageTexture;
uniform sampler2D inputImageTexture2;

uniform float opacity;

 vec3 blend(vec4 base, vec4 overlay) {
     float odin = 1.0;
     if (base.a > 0.0) {
         odin = overlay.a / base.a;
     }

     float ra = 1.0 - step(base.r, 0.0);
     if (overlay.r > 0.0) {
         ra = base.r / overlay.r * odin;
     }

     float rg = 1.0 - step(base.g, 0.0);
     if (overlay.g > 0.0) {
         rg = base.g / overlay.g * odin;
     }

     float rb = 1.0 - step(base.b, 0.0);
     if (overlay.b > 0.0) {
         rb = base.b / overlay.b * odin;
     }

     vec3 color = clamp(vec3(ra, rg, rb), 0.0, 1.0);

     return (1.0 - overlay.a) * base.rgb + (1.0 - base.a) * overlay.rgb + base.a * overlay.a * color;
 }

void main()
{
    vec4 base = texture2D(inputImageTexture, textureCoordinate);
    vec4 overlay = texture2D(inputImageTexture2, textureCoordinate2) * opacity;

    gl_FragColor = vec4(blend(base, overlay), base.a + overlay.a - base.a * overlay.a);
}