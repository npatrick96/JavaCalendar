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
        this.name = "Default Name";
        this.description = "";
        this.address = "";
        this.start = new Date();
        this.end = new Date();
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

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass() != Appointment.class){
            return false;
        }
        Appointment that = (Appointment) obj;

        return this.name == that.name &&
                this.description == that.description &&
                this.address == that.address &&
                this.start == that.start &&
                this.end == that.end;
    }

    public static int getEventStart(Appointment appt){
        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();

        calendar.setTime(appt.start);
        int hourstart = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(current);
        return hourstart;
    }

    public static int getEventEnd(Appointment appt){
        Calendar calendar = Calendar.getInstance();
        Date current = calendar.getTime();

        calendar.setTime(appt.end);
        int hourEnd = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(current);
        return hourEnd;
    }

    public static Appointment deepCopy(Appointment apt){
        Appointment newAppt = new Appointment();
        newAppt.name = apt.name;
        newAppt.description = apt.description;
        newAppt.address = apt.address;
        newAppt.start = apt.start;
        newAppt.end = apt.end;

        return newAppt;
    }
}
