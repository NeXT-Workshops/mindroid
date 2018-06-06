package ev3;

import org.mindroid.api.ev3.EV3Image;
import org.mindroid.api.ev3.IEV3Display;


public class SysoutEV3Display implements IEV3Display
{

   private static final int DEFAULT_HEIGHT = 128;

   private static final int DEFAULT_WIDTH = 178;

   private final int width;

   private final int height;

   public SysoutEV3Display()
   {
      this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
   }

   public SysoutEV3Display(int width, int height)
   {
      this.width = width;
      this.height = height;
   }

   @Override
   public void drawImage(EV3Image image, int row, int col)
   {
      StringBuilder sb = new StringBuilder();
      sb.append(createRule());
      for (int r = 0; r < height; ++r)
      {
         sb.append('|');
         for (int c = 0; c < width; ++c)
         {
            if (containsPixel(image, row, col, r, c))
            {
               sb.append("+");
            } else
            {
               sb.append(" ");
            }
         }
         sb.append('|');
         sb.append('\n');
      }
      sb.append(createRule());
      System.out.println(sb);
   }

   @Override
   public int getWidth()
   {
      return width;
   }

   @Override
   public int getHeight()
   {
      return height;
   }

   private boolean containsPixel(EV3Image image, int anchorRow, int anchorCol, int r, int c)
   {
      if (r >= anchorRow && r < anchorRow + image.getHeight() && c >= anchorCol && c < anchorCol + image.getWidth())
      {
         return image.getPixelAt(r - anchorRow, c - anchorCol);
      }
      return false;
   }

   private String createRule()
   {
	   //TODO ?
	   //return '+' + StringUtils.repeat("-", width) + "+\n";
	   return null;
   }

}