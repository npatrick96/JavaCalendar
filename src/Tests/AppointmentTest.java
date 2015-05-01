package Tests;

import Model.Appointment;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Created by John on 4/22/15.
 */
public class AppointmentTest {

    @Before
    public void setUp() throws Exception {
        new Appointment().drop();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (int i = 0; i < 100; ++i){
            if (i % 10 == 0 && i != 0){
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            Appointment newAppt = new Appointment();
            Date current = calendar.getTime();
            newAppt.name = "" + i;
            newAppt.start = current;
            newAppt.end = current;
            newAppt.save();


        }
        calendar.setTime(new Date());
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSaving(){
        ArrayList<Appointment> appointments = new Appointment().all();
        TestCase.assertEquals(100, appointments.size());

        new Appointment().drop();
        appointments = new Appointment().all();
        TestCase.assertEquals(0, appointments.size());
    }

    @Test
    public void testGetByDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (int i = 0; i < 10; ++i){
            ArrayList<Appointment> forDay = new Appointment().getAppointments(calendar.getTime());
            assertEquals(10, forDay.size());
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar.setTime(new Date());
        calendar.add(calendar.DAY_OF_MONTH, -1);
        ArrayList<Appointment> yesterday = new Appointment().getAppointments(calendar.getTime());
        assertTrue(yesterday.size() == 0);
    }

    @Test (expected = NullPointerException.class)
    public void testEquals(){
        Appointment obj1 = new Appointment();
        Appointment obj2 = Appointment.deepCopy(obj1);
        String notAnAppointment = "";

        assertTrue(obj1.equals(obj2));
        assertFalse(obj1.equals(notAnAppointment));
        TestCase.assertFalse(obj1.equals(null));
    }

    @Test (expected = NullPointerException.class)
    public void testGetAppointmentEnd(){
        Date today = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.HOUR_OF_DAY, 8);

        Date start = calendar.getTime();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        Date end = calendar.getTime();

        Appointment apt = new Appointment();
        apt.start = start;
        apt.end = end;

        int startHour = Appointment.getEventStart(apt);
        int endHour = Appointment.getEventEnd(apt);

        assertEquals(8, startHour);
        assertEquals(10, endHour);

        int shouldThrowException = Appointment.getEventEnd(null);
        shouldThrowException = Appointment.getEventStart(null);
    }
}