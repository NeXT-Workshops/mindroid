package usability;

import org.junit.Test;

public class OpenRobertaMiniprogramsTest
{
   @Test
   public void testSquareDriver() throws Exception
   {
     // new TestSimulationRuntime().runSingle(new SquareDriver());
   }
/*
   private static class SquareDriver extends AbstractRobodancer
   {
      @Override
      public void initializeScheduler(Scheduler scheduler)
      {
         scheduler.schedule(0, this::driveSquare);
      }
      
      @Override
      public void onMessageReceived(Message message)
      {
         this.stopMotor();
      }
      
      private void driveSquare()
      {
         for (int i = 0; i < 4; ++i)
         {
            this.drive(30, 20);
            this.turn(TurningDirection.RIGHT, 10, 90);
         }
         
      }

   }

   @Test
   public void testShowEnvironmentalLight() throws Exception
   {
      new TestSimulationRuntime().runSingle(new EnvironmentalLightShower());
   }

   private static class EnvironmentalLightShower extends AbstractRobodancer
   {

      private int pressQueryCount = 0;


      @Override
      public void initializeScheduler(Scheduler scheduler)
      {
         scheduler.schedule(0, this::show);
      }
      
      public void show()
      {
         while (!this.isEV3ButtonPressed(EV3Buttons.MIDDLE))
         {
            this.showText("" + this.getEV3EnvironmentalLight(EV3PortIDs.PORT_3), 1, 1);
            this.sleepSeconds(100);
         }
         
      }

      @Override
      public boolean isEV3ButtonPressed(EV3Button button)
      {
         if (pressQueryCount++ == 0)
            return false;
         return true;
      }

   }
   */
}
