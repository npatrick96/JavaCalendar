package UI;

import Model.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;



public class EventController {

	@FXML
	Button Confirm;
	@FXML
	TextField Event;
	@FXML
	TextField StartTime;
	@FXML
	TextField EndTime;
	@FXML
	TextField Location;
	@FXML
	TextArea Notes;
	@FXML
	DatePicker date;
	@FXML
	Label status;

	public Appointment current;

	@FXML
	void CloseWindow(){
		if (getFromView() == true){
			current.save();
			Stage parent = (Stage) StartTime.getScene().getWindow();
			parent.hide();
		}
		return;
	}
	

	
	boolean getFromView(){
		if (current == null){
			current = new Appointment();
		}
		if (Event.getText().isEmpty() ){
			status.setText("Please enter the event title");
			status.setTextFill(Color.RED);
			return false;
		}else if (StartTime.getText().isEmpty()) {
			status.setText("Please enter the start time");
			status.setTextFill(Color.RED);
			return false;
		}else if(EndTime.getText().isEmpty()){
			status.setText("Please enter the end time");
			status.setTextFill(Color.RED);
			return false;
		}else {
		
			current.name = Event.getText();
			current.description = Notes.getText();
			current.address = Location.getText();
	
	
			Calendar calendar = Calendar.getInstance();
			Date now = calendar.getTime();
	
			Instant selected = date.getValue().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant();
			Date day = Date.from(selected);
	
			current.start = hourStringToDate(StartTime.getText(), day);
			current.end = hourStringToDate(EndTime.getText(), day);
	
			calendar.setTime(now);
			return true;
		}
	}

	public void initialize(){
		Date currentTime = Calendar.getInstance().getTime();
		ZonedDateTime selected = currentTime.toInstant().atZone(ZoneId.systemDefault());
		LocalDate now = selected.toLocalDate();
		date.setValue(now);
	}

	public void loadFromAppointment(Appointment appt){
		current = appt;
		Event.setText(appt.name);
		Notes.setText(appt.description);
		Location.setText(appt.address);
		setDateTimeField(appt.start);
		String startHour = dateToHourString(appt.start);
		String endHour = dateToHourString(appt.end);
		StartTime.setText(startHour);
		EndTime.setText(endHour);
	}

	public Date hourStringToDate(String time, Date day){
		Calendar c = Calendar.getInstance();
		Date prev = c.getTime();
		c.setTime(day);
		String[] hhmm = time.split(":");
		try{
			int hour = Integer.parseInt(hhmm[0]);
			c.set(Calendar.HOUR_OF_DAY, hour);
		}catch(NumberFormatException e){
			status.setText("Invalid date state format");
			status.setTextFill(Color.RED);
			return null;
		}


		if (hhmm.length > 1){
			int minutes = Integer.parseInt(hhmm[1].replace(" ", ""));
			c.set(Calendar.MINUTE, minutes);
		}
		Date finalTime = c.getTime();

		c.setTime(prev);

		return finalTime;
	}

	public void setDateTimeField(Date time){
		ZonedDateTime selected = time.toInstant().atZone(ZoneId.systemDefault());
		LocalDate now = selected.toLocalDate();
		date.setValue(now);
	}

	public String dateToHourString(Date time){
		Calendar calendar = Calendar.getInstance();
		Date prev = calendar.getTime();

		calendar.setTime(time);
		String hourString = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
		return hourString;
	}



}

