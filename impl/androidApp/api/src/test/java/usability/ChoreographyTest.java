package usability;

public class ChoreographyTest
{
//   @Test
//   public void testRobo1() throws Exception
//   {
//      new TestSimulationRuntime().run(Arrays.asList(new Robo1()));
//   }
//
//   @FunctionalInterface
//   private interface NoArg
//   {
//      void run();
//   }
//
//   private class Robo1 extends AbstractRobodancer
//   {	   
//	   
//      @Override
//      public void initializeScheduler(final Scheduler s)
//      {
//         s.schedule(0, this::playMusic);
//         s.schedule(5, this::onTimePassed1);
//         s.schedule(10, this::onTimePassed2);
//         s.schedule(15, this::onTimePassed3);
//      }
//
//      public void playMusic()
//      {
//      }
//
//      public void onTimePassed1()
//      {
//         /* First move group */
//         turn(TurningDirection.RIGHT, 360);
//      }
//
//      public void onTimePassed2()
//      {
//         drive(5 /*cm*/); // sync
//         driveBackward(5 /*cm*/); // sync
//      }
//
//      public void onTimePassed3()
//      {
//         // 
//      }
//
//      @Override
//      public void onMessageReceived(Message message)
//      {
//         if (message.getContent().startsWith("Go") && this.getEV3UltrasonicDistance(EV3PortIDs.PORT_A) <= 3)
//            this.drive(10);
//         if (message.getContent().startsWith("Stop"))
//            this.stopMotor();
//
//      }
//   }
}
