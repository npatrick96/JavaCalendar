package Model;

import SQL.SQLRow;

import java.util.Date;

/**
 * Created by John on 4/22/15.
 */
public class Appointment extends SQLRow {
    public String name, description, address;
    public Date start, end;

    public Appointment(){

    }
}
