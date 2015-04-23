package UI;

import Model.Appointment;
import SQL.QuerySet;
import javafx.fxml.FXML;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

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
    @FXML
    Slider uiScale;

    Date selected = new Date();
    Button selectedBtn;
    String monthYear;
    int scale = 15;

    @FXML
    void initialize(){
        populateMonthView(selected.getMonth());
        loadDay();
        statusLabel.setText("All Good!");
        statusLabel.setTextFill(Color.GREEN);
        drawDayStructure();

        uiScale.valueProperty().addListener(((observable, oldValue, newValue) -> {
            scale = newValue.intValue();
            drawDayStructure();
        }));
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
        if (selected == null){
            return;
        }

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

    void drawAppointment(Appointment appt){
        System.out.println("Drawing an appointment. Name = " + appt.name);
    }

    void drawDayStructure(){
        int totalHeight = scale * 2 * 26;
        setScrollableHeight(totalHeight);

        Pane content = (Pane) dayView.getContent();
        content.getChildren().removeAll(content.getChildren());
        setHalfHourLine(scale);
        for (int i = 1; i < 24; ++i){
            int y = i * 2 * scale;
            setHourLine("" + i, y);
            setHalfHourLine(y + scale);
        }
        setHourLine("24", 48 * scale);

        setHalfHourLine(50 * scale);

        loadDay();
    }

    void setScrollableHeight(int height){
        Bounds newBounds = new BoundingBox(0, 0, dayView.getViewportBounds().getWidth(), height);
        dayView.setViewportBounds(newBounds);
    }

    double getDayViewWidth(){
        return 293;
    }

    void setHourLine(String label, int y){
        Line hourLine = new Line();
        hourLine.setStartX(30);
        hourLine.setStartY(y);
        hourLine.setEndX(getDayViewWidth());
        hourLine.setEndY(y);

        hourLine.setStrokeWidth(1);

        Label hour = new Label(label);
        hour.setTranslateY(y - scale);

        addToCanvas(hourLine);
        addToCanvas(hour);
    }

    void setHalfHourLine(int y){
        Line hourLine = new Line();
        hourLine.setStartX(0);
        hourLine.setStartY(y);
        hourLine.setEndX(getDayViewWidth() / 2);
        hourLine.setEndY(y);

        hourLine.setStrokeWidth(0.5);

        addToCanvas(hourLine);
    }

    void addToCanvas(Node l){
        Pane canvas = (Pane)dayView.getContent();
        canvas.getChildren().addAll(l);
    }
}
