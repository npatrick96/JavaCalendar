package UI;

import Model.Appointment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;

import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
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
    @FXML
    Button addEvent;
    @FXML 
    ColorPicker colorPicker;

    Date selected = new Date();
    String monthYear;
    int scale = 25;

    Calendar calendar = Calendar.getInstance();

    @FXML
    void initialize(){
        Calendar.getInstance().setTime(new Date());
        populateMonthView();
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
    void advanceMonth(){
        calendar.add(Calendar.MONTH, 1);
        populateMonthView();
    }

    @FXML
    void retreatMonth(){
        calendar.add(Calendar.MONTH, -1);
        populateMonthView();
    }

    @FXML
    void populateMonthView(){
        //TODO: NOT DONE, not dynamic
        //Get number of days in the month (Modulo?)
        //Remove components from the monthView
        //populate the monthView with the number of days (each day is a button)
        //update monthYearLabel to reflect the month and year selected
        monthView.getChildren().removeAll(monthView.getChildren());

        int row = 0;
        int numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < numDays; ++i){
            Button day = new Button();
            day.setBackground(Background.EMPTY);
            day.setText("" + (i + 1));
            day.setOnMouseClicked((event -> {
                System.out.println("Button clicked: " + day.getText());
                day.requestFocus();
                monthYearLabel.setText(day.getText() + " - " + monthYear);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.getText()));
                drawDayStructure();
            }));
            monthView.add(day, i % 7, row);
            if (i % 7 == 6){
                row ++;
            }
        }
        monthYear = "" + getMonthForInt(calendar.get(Calendar.MONTH)) + " " + calendar.get(Calendar.YEAR);
        monthYearLabel.setText(monthYear);
    }

    String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    @FXML
    void loadDay(){
        Date current = calendar.getTime();

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

        for (Appointment appt: meetings){
            drawAppointment(appt);
        }
    }

    void drawAppointment(Appointment appt){
        System.out.println("Drawing an appointment. Name = " + appt.name);

        int startHour = getEventStart(appt);
        int endHour = getEventEnd(appt);

        System.out.println("Start, end: " + startHour + ", " + endHour);

        Button appointmentButton = new Button(appt.name + "\n" + appt.address);
        appointmentButton.setTranslateX(30);
        double offset = ((scale * 2 * startHour) - 1) + ((calendar.get(Calendar.MINUTE) / 30.0f) - scale);
        appointmentButton.setTranslateY(offset);
        appointmentButton.setMinWidth(getDayViewWidth() / 2);
        appointmentButton.setTranslateX(50);

        double height = (endHour - startHour) >= 1 ? (endHour - startHour) * scale : scale;

        appointmentButton.setMinHeight(Control.USE_PREF_SIZE);
        appointmentButton.setPrefHeight(height * 2);
        appointmentButton.setMaxHeight(Control.USE_PREF_SIZE);

        addToCanvas(appointmentButton);
    }

    int getEventStart(Appointment appt){
        Date current = calendar.getTime();

        calendar.setTime(appt.start);
        int hourstart = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(current);
        return hourstart;
    }

    int getEventEnd(Appointment appt){
        Date current = calendar.getTime();

        calendar.setTime(appt.end);
        int hourEnd = calendar.get(Calendar.HOUR_OF_DAY);
        calendar.setTime(current);
        return hourEnd;
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

    @FXML
	private void Joinpage() throws IOException {
        Stage stage = new Stage();

        Parent root = FXMLLoader.load(getClass().getResource("EventAdd.fxml"));
        Scene scene = new Scene(root, 283, 330);

        stage.setTitle("Add a calendar event");
        stage.setScene(scene);
        stage.setOnCloseRequest((event) -> {
            drawDayStructure();
        });
        stage.show();

	}
    
    @FXML
    private Color getSelectedColor(){
    	Color selectedColor = colorPicker.getValue();
    	System.out.println(selectedColor);
    	return selectedColor;
    	 
    }
	
	private void switchScreen(String FXMLFile) throws IOException {
		Parent home_page_parent = FXMLLoader.load(getClass().getResource(FXMLFile));
		Scene home_page_scene = new Scene(home_page_parent);
		Stage app_stage = (Stage) addEvent.getScene().getWindow();
		app_stage.setScene(home_page_scene);
		app_stage.show();
	}

}
