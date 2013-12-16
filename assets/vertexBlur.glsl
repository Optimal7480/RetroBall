uniform mat4 u_projTrans;
uniform vec4 a_position;
varying vec2 a_texCoord0;

// remember that you should draw a screen aligned quad
void main(void)
{
   gl_Position = u_projTrans * a_position;

   // Clean up inaccuracies
   vec2 Pos;
   Pos = sign(gl_Vertex.xy);

   gl_Position = vec4(Pos, 0.0, 1.0);
   // Image-space
   a_texCoord0 = Pos * 0.5 + 0.5;
}