package org.mindroid.impl.ev3;

public class EV3PortID
{
   private final String label;

   private final int number;

   public EV3PortID(String label, int number)
   {
      this.label = label;
      this.number = number;
   }

   public int getNumber()
   {
      return number;
   }

   public String getLabel()
   {
      return label;
   }
}
