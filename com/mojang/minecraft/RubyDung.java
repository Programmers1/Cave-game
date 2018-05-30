package com.mojang.minecraft;

import com.mojang.minecraft.character.Zombie;
import com.mojang.minecraft.level.Chunk;
import com.mojang.minecraft.level.Frustum;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.level.LevelRenderer;
import com.mojang.minecraft.level.Tesselator;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.particle.ParticleEngine;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;



public class RubyDung
  implements Runnable
{
  private static final boolean FULLSCREEN_MODE = false;
  private int width;
  private int height;
  private FloatBuffer fogColor0 = BufferUtils.createFloatBuffer(4);
  private FloatBuffer fogColor1 = BufferUtils.createFloatBuffer(4);
  private Timer timer = new Timer(20.0F);
  private Level level;
  private LevelRenderer levelRenderer;
  private Player player;
  private int paintTexture = 1;
  
  private ParticleEngine particleEngine;
  private ArrayList<Zombie> zombies = new ArrayList();
  





  public RubyDung() {}
  




  public void init()
    throws LWJGLException, IOException
  {
    int col0 = 16710650;
    int col1 = 920330;
    float fr = 0.5F;
    float fg = 0.8F;
    float fb = 1.0F;
    fogColor0.put(new float[] { (col0 >> 16 & 0xFF) / 255.0F, (col0 >> 8 & 0xFF) / 255.0F, (col0 & 0xFF) / 255.0F, 1.0F });
    fogColor0.flip();
    fogColor1.put(new float[] { (col1 >> 16 & 0xFF) / 255.0F, (col1 >> 8 & 0xFF) / 255.0F, (col1 & 0xFF) / 255.0F, 1.0F });
    fogColor1.flip();
    







    Display.setDisplayMode(new DisplayMode(1024, 768));
    
    Display.create();
    Keyboard.create();
    Mouse.create();
    
    width = Display.getDisplayMode().getWidth();
    height = Display.getDisplayMode().getHeight();
    
    GL11.glEnable(3553);
    GL11.glShadeModel(7425);
    GL11.glClearColor(fr, fg, fb, 0.0F);
    GL11.glClearDepth(1.0D);
    GL11.glEnable(2929);
    GL11.glDepthFunc(515);
    GL11.glEnable(3008);
    GL11.glAlphaFunc(516, 0.5F);
    
    GL11.glMatrixMode(5889);
    GL11.glLoadIdentity();
    
    GL11.glMatrixMode(5888);
    
    level = new Level(256, 256, 64);
    levelRenderer = new LevelRenderer(level);
    player = new Player(level);
    particleEngine = new ParticleEngine(level);
    
    Mouse.setGrabbed(true);
    
    for (int i = 0; i < 10; i++)
    {
      Zombie zombie = new Zombie(level, 128.0F, 0.0F, 128.0F);
      zombie.resetPos();
      zombies.add(zombie);
    }
  }
  
  public void destroy()
  {
    level.save();
    
    Mouse.destroy();
    Keyboard.destroy();
    Display.destroy();
  }
  
  public void run()
  {
    try
    {
      init();
    }
    catch (Exception e)
    {
      JOptionPane.showMessageDialog(null, e.toString(), "Failed to start RubyDung", 0);
      System.exit(0);
    }
    
    long lastTime = System.currentTimeMillis();
    int frames = 0;
    try
    {
      do
      {
        timer.advanceTime();
        for (int i = 0; i < timer.ticks; i++)
        {
          tick();
        }
        render(timer.a);
        frames++;
        
        while (System.currentTimeMillis() >= lastTime + 1000L)
        {
          System.out.println(frames + " fps, " + Chunk.updates);
          Chunk.updates = 0;
          
          lastTime += 1000L;
          frames = 0;
        }
        if (Keyboard.isKeyDown(1)) break; } while (!Display.isCloseRequested());










    }
    catch (Exception e)
    {









      e.printStackTrace();
    }
    finally
    {
      destroy();
    }
  }
  
  public void tick()
  {
    while (Keyboard.next())
    {
      if (Keyboard.getEventKeyState())
      {
        if (Keyboard.getEventKey() == 28)
        {
          level.save();
        }
        if (Keyboard.getEventKey() == 2) paintTexture = 1;
        if (Keyboard.getEventKey() == 3) paintTexture = 3;
        if (Keyboard.getEventKey() == 4) paintTexture = 4;
        if (Keyboard.getEventKey() == 5) paintTexture = 5;
        if (Keyboard.getEventKey() == 7) paintTexture = 6;
        if (Keyboard.getEventKey() == 34)
        {
          zombies.add(new Zombie(level, player.x, player.y, player.z));
        }
      }
    }
    
    level.tick();
    particleEngine.tick();
    
    for (int i = 0; i < zombies.size(); i++)
    {
      ((Zombie)zombies.get(i)).tick();
      if (zombies.get(i)).removed)
      {
        zombies.remove(i--);
      }
    }
    
    player.tick();
  }
  



  private void moveCameraToPlayer(float a)
  {
    GL11.glTranslatef(0.0F, 0.0F, -0.3F);
    GL11.glRotatef(player.xRot, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(player.yRot, 0.0F, 1.0F, 0.0F);
    
    float x = player.xo + (player.x - player.xo) * a;
    float y = player.yo + (player.y - player.yo) * a;
    float z = player.zo + (player.z - player.zo) * a;
    GL11.glTranslatef(-x, -y, -z);
  }
  
  private void setupCamera(float a)
  {
    GL11.glMatrixMode(5889);
    GL11.glLoadIdentity();
    GLU.gluPerspective(70.0F, width / height, 0.05F, 1000.0F);
    GL11.glMatrixMode(5888);
    GL11.glLoadIdentity();
    moveCameraToPlayer(a);
  }
  
  private IntBuffer viewportBuffer = BufferUtils.createIntBuffer(16);
  
  private void setupPickCamera(float a, int x, int y)
  {
    GL11.glMatrixMode(5889);
    GL11.glLoadIdentity();
    viewportBuffer.clear();
    GL11.glGetInteger(2978, viewportBuffer);
    viewportBuffer.flip();
    viewportBuffer.limit(16);
    GLU.gluPickMatrix(x, y, 5.0F, 5.0F, viewportBuffer);
    GLU.gluPerspective(70.0F, width / height, 0.05F, 1000.0F);
    GL11.glMatrixMode(5888);
    GL11.glLoadIdentity();
    moveCameraToPlayer(a);
  }
  
  private IntBuffer selectBuffer = BufferUtils.createIntBuffer(2000);
  private HitResult hitResult = null;
  
  private void pick(float a)
  {
    selectBuffer.clear();
    GL11.glSelectBuffer(selectBuffer);
    GL11.glRenderMode(7170);
    setupPickCamera(a, width / 2, height / 2);
    levelRenderer.pick(player, Frustum.getFrustum());
    int hits = GL11.glRenderMode(7168);
    selectBuffer.flip();
    selectBuffer.limit(selectBuffer.capacity());
    
    long closest = 0L;
    int[] names = new int[10];
    int hitNameCount = 0;
    for (int i = 0; i < hits; i++)
    {
      int nameCount = selectBuffer.get();
      long minZ = selectBuffer.get();
      selectBuffer.get();
      
      long dist = minZ;
      
      if ((dist < closest) || (i == 0))
      {
        closest = dist;
        hitNameCount = nameCount;
        for (int j = 0; j < nameCount; j++) {
          names[j] = selectBuffer.get();
        }
      }
      else {
        for (int j = 0; j < nameCount; j++) {
          selectBuffer.get();
        }
      }
    }
    if (hitNameCount > 0)
    {
      hitResult = new HitResult(names[0], names[1], names[2], names[3], names[4]);
    }
    else
    {
      hitResult = null;
    }
  }
  
  public void render(float a)
  {
    float xo = Mouse.getDX();
    float yo = Mouse.getDY();
    player.turn(xo, yo);
    pick(a);
    
    while (Mouse.next())
    {
      if ((Mouse.getEventButton() == 1) && (Mouse.getEventButtonState()))
      {
        if (hitResult != null)
        {
          Tile oldTile = Tile.tiles[level.getTile(hitResult.x, hitResult.y, hitResult.z)];
          boolean changed = level.setTile(hitResult.x, hitResult.y, hitResult.z, 0);
          if ((oldTile != null) && (changed))
          {
            oldTile.destroy(level, hitResult.x, hitResult.y, hitResult.z, particleEngine);
          }
        }
      }
      if ((Mouse.getEventButton() == 0) && (Mouse.getEventButtonState()))
      {
        if (hitResult != null)
        {
          int x = hitResult.x;
          int y = hitResult.y;
          int z = hitResult.z;
          
          if (hitResult.f == 0) y--;
          if (hitResult.f == 1) y++;
          if (hitResult.f == 2) z--;
          if (hitResult.f == 3) z++;
          if (hitResult.f == 4) x--;
          if (hitResult.f == 5) { x++;
          }
          level.setTile(x, y, z, paintTexture);
        }
      }
    }
    


    GL11.glClear(16640);
    setupCamera(a);
    
    GL11.glEnable(2884);
    
    Frustum frustum = Frustum.getFrustum();
    
    levelRenderer.updateDirtyChunks(player);
    
    setupFog(0);
    GL11.glEnable(2912);
    levelRenderer.render(player, 0);
    for (int i = 0; i < zombies.size(); i++)
    {
      Zombie zombie = (Zombie)zombies.get(i);
      if ((zombie.isLit()) && (frustum.isVisible(bb)))
      {
        ((Zombie)zombies.get(i)).render(a);
      }
    }
    particleEngine.render(player, a, 0);
    setupFog(1);
    levelRenderer.render(player, 1);
    for (int i = 0; i < zombies.size(); i++)
    {
      Zombie zombie = (Zombie)zombies.get(i);
      if ((!zombie.isLit()) && (frustum.isVisible(bb)))
      {
        ((Zombie)zombies.get(i)).render(a);
      }
    }
    particleEngine.render(player, a, 1);
    GL11.glDisable(2896);
    GL11.glDisable(3553);
    GL11.glDisable(2912);
    
    if (hitResult != null)
    {
      GL11.glDisable(3008);
      levelRenderer.renderHit(hitResult);
      GL11.glEnable(3008);
    }
    
    drawGui(a);
    
    Display.update();
  }
  
  private void drawGui(float a)
  {
    int screenWidth = width * 240 / height;
    int screenHeight = height * 240 / height;
    
    GL11.glClear(256);
    GL11.glMatrixMode(5889);
    GL11.glLoadIdentity();
    GL11.glOrtho(0.0D, screenWidth, screenHeight, 0.0D, 100.0D, 300.0D);
    GL11.glMatrixMode(5888);
    GL11.glLoadIdentity();
    GL11.glTranslatef(0.0F, 0.0F, -200.0F);
    

    GL11.glPushMatrix();
    GL11.glTranslatef(screenWidth - 16, 16.0F, 0.0F);
    Tesselator t = Tesselator.instance;
    GL11.glScalef(16.0F, 16.0F, 16.0F);
    GL11.glRotatef(30.0F, 1.0F, 0.0F, 0.0F);
    GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
    GL11.glTranslatef(-1.5F, 0.5F, -0.5F);
    GL11.glScalef(-1.0F, -1.0F, 1.0F);
    
    int id = Textures.loadTexture("/terrain.png", 9728);
    GL11.glBindTexture(3553, id);
    GL11.glEnable(3553);
    t.init();
    Tile.tiles[paintTexture].render(t, level, 0, -2, 0, 0);
    t.flush();
    GL11.glDisable(3553);
    GL11.glPopMatrix();
    
    int wc = screenWidth / 2;
    int hc = screenHeight / 2;
    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    t.init();
    t.vertex(wc + 1, hc - 4, 0.0F);
    t.vertex(wc - 0, hc - 4, 0.0F);
    t.vertex(wc - 0, hc + 5, 0.0F);
    t.vertex(wc + 1, hc + 5, 0.0F);
    
    t.vertex(wc + 5, hc - 0, 0.0F);
    t.vertex(wc - 4, hc - 0, 0.0F);
    t.vertex(wc - 4, hc + 1, 0.0F);
    t.vertex(wc + 5, hc + 1, 0.0F);
    t.flush();
  }
  
  FloatBuffer lb = BufferUtils.createFloatBuffer(16);
  
  private void setupFog(int i)
  {
    if (i == 0)
    {
      GL11.glFogi(2917, 2048);
      GL11.glFogf(2914, 0.001F);
      GL11.glFog(2918, fogColor0);
      GL11.glDisable(2896);
    }
    else if (i == 1)
    {
      GL11.glFogi(2917, 2048);
      GL11.glFogf(2914, 0.06F);
      GL11.glFog(2918, fogColor1);
      GL11.glEnable(2896);
      GL11.glEnable(2903);
      
      float br = 0.6F;
      GL11.glLightModel(2899, getBuffer(br, br, br, 1.0F));
    }
  }
  
  private FloatBuffer getBuffer(float a, float b, float c, float d)
  {
    lb.clear();
    lb.put(a).put(b).put(c).put(d);
    lb.flip();
    return lb;
  }
  
  public static void checkError()
  {
    int e = GL11.glGetError();
    if (e != 0)
    {
      throw new IllegalStateException(GLU.gluErrorString(e));
    }
  }
  
  public static void main(String[] args) throws LWJGLException
  {
    new Thread(new RubyDung()).start();
  }
}
