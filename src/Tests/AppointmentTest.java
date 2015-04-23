package Tests;

import Model.Appointment;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static junit.framework.TestCase.assertTrue;

/**
 * Created by John on 4/22/15.
 */
public class AppointmentTest {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSaving(){
        new Appointment().drop();

        for (int i = 0; i < 20; ++i){
            Appointment appt = new Appointment();
            appt.address = "1321 Wirt Road";
            appt.description = "This was generated at " + i;
            appt.name = "" + i;
            appt.time = new Date();

            appt.save();
        }

        ArrayList<Appointment> appointments = new Appointment().all();
        assertTrue(appointments.size() == 20);
    }
}