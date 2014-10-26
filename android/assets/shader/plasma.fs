#ifdef GL_ES
	#define LOWP lowp
	precision mediump float;
#else
	#define LOWP
#endif

#define PI 3.14159

varying LOWP vec4 v_color;
varying vec2 v_texCoords;

uniform float u_speed;
uniform vec2 u_resolution;
uniform float u_time;
uniform sampler2D u_texture;
uniform float u_brightness;
uniform float u_intensity;

float np21 (float v) {
	return v * 0.5 + 0.5;
}

vec4 blob(vec2 position, vec2 mult, float timeMult, vec4 color) {
	vec2 p = position.xy * mult + u_time / timeMult;
	return np21(sin( p.x * PI )) * np21(sin(p.y * PI)) * color;
}

void main()
{
	vec2 position = gl_FragCoord.xy / u_resolution;

	vec4 invert = vec4(vec3(-1.0), 1.0);
	vec4 sub = vec4(1.0);

	vec4 texColor = texture2D(u_texture, v_texCoords);
	vec4 color = v_color * texColor;
	
	vec4 blob0 = blob(position, vec2(2, 3), 1.0 / u_speed, vec4(u_brightness, 0.0, 0.0, u_intensity));
	vec4 blob1 = blob(position, vec2(-1, 2), -1.0 / u_speed, vec4(0.0, u_brightness, 0.0, u_intensity));
	vec4 blob2 = blob(position, vec2(1, 2), 1.0 / u_speed, vec4(0.0, 0.0, u_brightness, u_intensity));
	
	color *= blob0 + blob1 + blob2;
	
	gl_FragColor = color;
}
