package UI;

import Model.Appointment;
import SQL.QuerySet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by John on 4/23/15.
 */
public class MainController {

    @FXML
    GridPane monthView;
    @FXML
    ScrollPane dayView;
    @FXML
    Label monthYearLabel;
    @FXML
    Label statusLabel;

    Date selected = new Date();
    Button selectedBtn;
    String monthYear;

    @FXML
    void initialize(){
        populateMonthView(selected.getMonth());
        loadDay();
        statusLabel.setText("All Good!");
        statusLabel.setTextFill(Color.GREEN);
    }

    @FXML
    void addEvent(){
        System.out.println("Adding an event... but not really.");
    }

    @FXML
    void populateMonthView(int month){
        //Get number of days in the month (Modulo?)
        //Remove components from the monthView
        //populate the monthView with the number of days (each day is a button)
        //update monthYearLabel to reflect the month and year selected
        int row = 0;
        for (int i = 0; i < 30; ++i){
            Button day = new Button();
            day.setBackground(Background.EMPTY);
            day.setText("" + (i + 1));
            day.setOnMouseClicked((event -> {
                System.out.println("Button clicked: " + day.getText());
                day.requestFocus();
                monthYearLabel.setText(day.getText() + " - " + monthYear);
            }));
            monthView.add(day, i % 7, row);
            if (i % 7 == 6){
                row ++;
            }
        }
        monthYear = "April - 2015";
        monthYearLabel.setText(monthYear);
    }

    @FXML
    void loadDay(){
        int year = selected.getYear();
        int month = selected.getMonth();
        int day = selected.getDay();

        Calendar c = Calendar.getInstance();
        c.set(year, month, day, 0, 0);

        Date dayStart = c.getTime(), nextDayStart;
        c.add(Calendar.DATE, 1);
        nextDayStart = c.getTime();

        String where = "time >= " + dayStart.getTime() + " AND time < " + nextDayStart.getTime();
        QuerySet<Appointment> meetings = new Appointment().lazyAllWhere(where);

        while (meetings.hasNext()){
            drawAppointment(meetings.next());
        }
    }

    void drawAppointment(Appointment appot){
        System.out.println("Drawing an appointment. Name = " + appot.name);
    }
}
