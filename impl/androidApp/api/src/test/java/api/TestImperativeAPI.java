package api;

import org.junit.Test;
import org.mindroid.api.ImperativeAPI;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestImperativeAPI {

    //@Test
    public void testStartStopMechanism() throws InterruptedException {
        final ImperativeAPI impl_waitForInterruption = new ImperativeAPI("TestImpl") {
            @Override
            public void run() {
                while(!isInterrupted()){
                    delay(5);
                }
            }
        };

        //Test if restart of implementation works properly
        for(int i = 0; i < 2; i++) {
            Runnable runImpl = new Runnable() {
                @Override
                public void run() {
                    impl_waitForInterruption.start();
                }
            };
            //Start impl
            new Thread(runImpl).start();

            Thread.sleep(75);

            //check state
            assertFalse(impl_waitForInterruption.isExecutionFinished());
            assertFalse(impl_waitForInterruption.isInterrupted());

            //Stop impl
            impl_waitForInterruption.stopExecution();

            Thread.sleep(25);

            //check state
            assertTrue(impl_waitForInterruption.isInterrupted());
            assertTrue(impl_waitForInterruption.isExecutionFinished());
        }
    }


    //@Test
    public void testFinishMechanism() throws InterruptedException {
        final ImperativeAPI impl_waitForInterruption = new ImperativeAPI("TestImpl") {
            @Override
            public void run() {
                //Nothing to do
            }
        };

        //start
        impl_waitForInterruption.start();

        //check state - impl finishes immediatly
        assertTrue(impl_waitForInterruption.isExecutionFinished());
        assertFalse(impl_waitForInterruption.isInterrupted());

        //stop impl
        impl_waitForInterruption.stopExecution();

        //check state
        assertTrue(impl_waitForInterruption.isExecutionFinished());
        assertFalse(impl_waitForInterruption.isInterrupted());
    }
}
