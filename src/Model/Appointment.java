package Model;

import SQL.SQLRow;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by John on 4/22/15.
 */
public class Appointment extends SQLRow {
    public String name, description, address;
    public Date start, end;

    public Appointment(){

    }

    public static ArrayList<Appointment> getAppointments(Date date){
        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();

        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date dayStart = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date dayEnd = calendar.getTime();

        calendar.setTime(current);

        String where = "start >= " + dayStart.getTime() + " AND start < " + dayEnd.getTime();
        ArrayList<Appointment> meetings = new Appointment().allWhere(where);

        return meetings;
    }
}
