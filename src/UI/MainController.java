package UI;

import Model.Appointment;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
    @FXML
    Button todayButton,
    lastSelectedButton;

    int scale = 25;
    
    Calendar calendar = Calendar.getInstance();
    ArrayList<Appointment> seen = new ArrayList<>();
    public static final double padding = 20;

    @FXML
    void initialize(){
        Calendar.getInstance().setTime(new Date());

        statusLabel.setText("All Good!");
        statusLabel.setTextFill(Color.GREEN);
        colorPicker.setValue(Color.BLACK);

        populateMonthView();
        drawDayStructure();

        uiScale.valueProperty().addListener(((observable, oldValue, newValue) -> {
            scale = newValue.intValue();
            drawDayStructure();
        }));

        Calendar.getInstance().setTime(new Date());
    }


    @FXML
    void advanceMonth(){
        calendar.add(Calendar.MONTH, 1);
        populateMonthView();
        drawDayStructure();
    }

    @FXML
    void today(){
        Date today = new Date();
        calendar.setTime(today);
        populateMonthView();
        drawDayStructure();
    }

    @FXML
    void retreatMonth(){
        calendar.add(Calendar.MONTH, -1);
        populateMonthView();
        drawDayStructure();
    }

    @FXML
    void populateMonthView(){
        monthView.getChildren().removeAll(monthView.getChildren());
        Date current = calendar.getTime();
        int selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int row = 0,
            numDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 0; i < numDays; ++i){
            Button day = getMonthButton(calendar.getTime());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            monthView.add(day, dayOfWeek - 1, row);
            if (dayOfWeek == Calendar.SATURDAY){
                row ++;
            }
            boolean isCurrentDay = selectedDay == (i + 1);
            if (isCurrentDay){
                day.fire();
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.setTime(current);
        setMonthText();
    }

    Button getMonthButton(Date time){

        Date curr = calendar.getTime();
        calendar.setTime(time);

        Button day = new Button();
        day.setBackground(Background.EMPTY);
        day.setText("" + calendar.get(Calendar.DAY_OF_MONTH));
        day.setTextFill(getSelectedColor());

        day.setOnAction((event -> {
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day.getText()));

            if (lastSelectedButton != null)
                lastSelectedButton.setTextFill(getSelectedColor());

            if (getSelectedColor().getRed() > 0.5){day.setTextFill(Color.BLACK);}
            else{day.setTextFill(Color.RED);}
            lastSelectedButton = day;

            drawDayStructure();
            setMonthText();

        }));
        calendar.setTime(curr);
        return day;
    }

    @FXML
    void closeApp(){
        Stage thisStage = (Stage) dayView.getScene().getWindow();
        thisStage.close();
    }

    @FXML
    void loadFromFile() {
        FileChooser findChooser = new FileChooser();
        findChooser.setTitle("Select a file to import");
        findChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt"), new FileChooser.ExtensionFilter("Calendar Files", "*.jcf"));
        File path = findChooser.showOpenDialog(dayView.getScene().getWindow());
        if (path == null) return;
        try {
            ArrayList<Appointment> appointments = Appointment.loadFromFile(path.getPath());

            appointments.forEach(Model.Appointment::save);

            drawDayStructure();

        } catch (FileNotFoundException e) {
            Alert failedToOpen = new Alert(Alert.AlertType.ERROR);
            failedToOpen.setTitle("Couldn't open file");
            failedToOpen.setHeaderText("There was a problem opening " + path.getName() +"!");
            failedToOpen.setContentText("Couldn't find the file " + path.getName());
            failedToOpen.show();
        }
    }

    @FXML
    void saveToFile() {
        FileChooser saveChooser = new FileChooser();
        saveChooser.setTitle("Select a location to export your calendar");
        saveChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text File", "*.txt"), new FileChooser.ExtensionFilter("Calendar Files", "*.jcf"));
        File path = saveChooser.showSaveDialog(dayView.getScene().getWindow());
        if (path == null) return;
        try {
            Appointment.saveAllToFile(path.getPath());
        } catch (IOException e) {
            Alert failedToOpen = new Alert(Alert.AlertType.ERROR);
            failedToOpen.setTitle("Couldn't save to file");
            failedToOpen.setHeaderText("There was a problem saving to " + path.getName() + "!");
            failedToOpen.setContentText(e.getCause().toString());
            failedToOpen.show();
        }
    }

    @FXML
    void loadDay(){
        Date today = calendar.getTime();
        seen = new ArrayList<>();

        for (int i = 0; i < 24; ++i) {
            ArrayList<Appointment> meetings = Appointment.getAppointmentsFromHour(today, i + 1);
            drawHour(meetings);
        }
    }

    @FXML
    void showAbout(){
        Alert aboutWindow = new Alert(Alert.AlertType.INFORMATION);
        aboutWindow.setTitle("About Java Calendar");
        aboutWindow.setHeaderText("About this app");
        aboutWindow.setContentText("This app was written as part of the Computer Science Scalable Software class at Hendrix College.  All questions and issues can be reported and resolved through our github page :\nhttps://github.com/cd24/JavaCalendar\n\nThank you for using this app!");
        aboutWindow.show();
    }

    void setMonthText(){
        String month = getMonthForInt(calendar.get(Calendar.MONTH));
        String year = "" + calendar.get(Calendar.YEAR);
        String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
        monthYearLabel.setText(month + " " + day + ", " + year);
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

    void drawHour(ArrayList<Appointment> appointments){
        for (int i = 0; i < appointments.size(); ++i) {
            Appointment appt = appointments.get(i);
            if (seen.contains(appt))
                continue;

            int startHour = Appointment.getEventStart(appt);
            int endHour = Appointment.getEventEnd(appt);

            double width = (getDayViewWidth() - padding) / appointments.size();
            double height = (endHour - startHour) >= 1 ? (endHour - startHour) * scale : scale;
            double offsetx = (i * width) + 1 + padding;
            double offsety = ((scale * 2 * startHour) - 1) + (calendar.get(Calendar.MINUTE) - scale);
            String title = appt.name + "\n" + appt.address;

            Button appointment = new Button(title);
            appointment.setTranslateY(offsety);
            appointment.setTranslateX(offsetx);
            appointment.setPrefHeight(height * 2);
            appointment.setPrefWidth(width);
            appointment.setOnAction((event) -> {
                try {
                    editAppointment(appt);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            appointment.setTextFill(getSelectedColor());
            addToCanvas(appointment);
        }
    }
    
    String getSelectedColorAsHex(){
    	String SelectedColorAsString = getSelectedColor().toString();
        return "#" + SelectedColorAsString.substring(2, SelectedColorAsString.length());
    }



    void drawDayStructure(){
        int totalHeight = scale * 2 * 26;
        setScrollableHeight(totalHeight);

        Pane content = (Pane) dayView.getContent();
        content.getChildren().clear();

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
        return 290;
    }

    void setHourLine(String label, int y){
        Line hourLine = new Line();
        hourLine.setStartX(30);
        hourLine.setStartY(y);
        hourLine.setEndX(getDayViewWidth());
        hourLine.setEndY(y);

        hourLine.setStrokeWidth(1);
        hourLine.setStroke(getSelectedColor());

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
        
        hourLine.setStroke(getSelectedColor().brighter());
        hourLine.setStrokeWidth(0.5);

        addToCanvas(hourLine);
        hourLine.setVisible(true);
    }

    void addToCanvas(Node l){
        Pane canvas = (Pane)dayView.getContent();
        canvas.getChildren().addAll(l);
    }

    void editAppointment(Appointment appt) throws IOException {
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Event.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 283, 355);

        EventController controller = loader.getController();
        controller.loadFromAppointment(appt);

        stage.setTitle("Editing event: " + appt.name);
        stage.setScene(scene);
        stage.setOnHiding((event) -> drawDayStructure());
        stage.show();
    }

    @FXML
    void addEvent() throws IOException {
        editAppointment(new Appointment());
    }
    
    @FXML
    void setColor(){
    	drawDayStructure();
        populateMonthView();
    	todayButton.setStyle(" -fx-border-color: " + getSelectedColorAsHex()+ ";");
    	addEvent.setStyle(" -fx-border-color: " + getSelectedColorAsHex()+ ";");
    	colorPicker.setStyle(" -fx-border-color: " + getSelectedColorAsHex()+ ";");
    }
    
    Color getSelectedColor(){
    	return colorPicker.getValue();
    }
}
