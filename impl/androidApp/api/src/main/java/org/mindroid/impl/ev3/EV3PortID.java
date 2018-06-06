package org.mindroid.impl.ev3;

import java.util.Objects;

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

   @Override
   public String toString() {
      return "EV3PortID{" +
              "label='" + label + '\'' +
              ", number=" + number +
              '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      EV3PortID ev3PortID = (EV3PortID) o;
      return number == ev3PortID.number &&
              Objects.equals(label, ev3PortID.label);
   }

   @Override
   public int hashCode() {

      return Objects.hash(label, number);
   }
}
