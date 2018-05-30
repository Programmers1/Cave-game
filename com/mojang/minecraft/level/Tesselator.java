package com.mojang.minecraft.level;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;




public class Tesselator
{
  private static final int MAX_MEMORY_USE = 4194304;
  private static final int MAX_FLOATS = 524288;
  private FloatBuffer buffer = BufferUtils.createFloatBuffer(524288);
  private float[] array = new float[524288];
  


  private int vertices = 0;
  private float u;
  private float v;
  private float r; private float g; private float b; private boolean hasColor = false;
  private boolean hasTexture = false;
  private int len = 3;
  private int p = 0;
  
  public static Tesselator instance = new Tesselator();
  

  private Tesselator() {}
  

  public void flush()
  {
    buffer.clear();
    buffer.put(array, 0, p);
    buffer.flip();
    





    if ((hasTexture) && (hasColor)) {
      GL11.glInterleavedArrays(10794, 0, buffer);
    } else if (hasTexture) {
      GL11.glInterleavedArrays(10791, 0, buffer);
    } else if (hasColor) {
      GL11.glInterleavedArrays(10788, 0, buffer);
    } else {
      GL11.glInterleavedArrays(10785, 0, buffer);
    }
    GL11.glEnableClientState(32884);
    if (hasTexture) GL11.glEnableClientState(32888);
    if (hasColor) { GL11.glEnableClientState(32886);
    }
    

    GL11.glDrawArrays(7, 0, vertices);
    
    GL11.glDisableClientState(32884);
    if (hasTexture) GL11.glDisableClientState(32888);
    if (hasColor) { GL11.glDisableClientState(32886);
    }
    clear();
  }
  
  private void clear()
  {
    vertices = 0;
    
    buffer.clear();
    p = 0;
  }
  


  public void init()
  {
    clear();
    hasColor = false;
    hasTexture = false;
  }
  
  public void tex(float u, float v)
  {
    if (!hasTexture) {
      len += 2;
    }
    hasTexture = true;
    this.u = u;
    this.v = v;
  }
  
  public void color(float r, float g, float b)
  {
    if (!hasColor) {
      len += 3;
    }
    hasColor = true;
    this.r = r;
    this.g = g;
    this.b = b;
  }
  
  public void vertexUV(float x, float y, float z, float u, float v)
  {
    tex(u, v);
    vertex(x, y, z);
  }
  
  public void vertex(float x, float y, float z)
  {
    if (hasTexture)
    {
      array[(p++)] = u;
      array[(p++)] = v;
    }
    if (hasColor)
    {
      array[(p++)] = r;
      array[(p++)] = g;
      array[(p++)] = b;
    }
    array[(p++)] = x;
    array[(p++)] = y;
    array[(p++)] = z;
    



    vertices += 1;
    if ((vertices % 4 == 0) && (p >= 524288 - len * 4))
    {
      flush();
    }
  }
}
