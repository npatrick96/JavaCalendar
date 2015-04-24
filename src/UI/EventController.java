package UI;

import java.util.Calendar;
import java.util.Date;

import Model.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

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
	void Save(){
	Appointment test = new Appointment();
    test.name = Event.getText();
    test.description = Notes.getText();
    test.address = Location.getText();

    
    Calendar calendar = Calendar.getInstance();

    Date current = calendar.getTime();
    
    calendar.set(Calendar.HOUR_OF_DAY, getStartHour());
    calendar.set(Calendar.MINUTE, getStartMinutes());

    test.start = calendar.getTime();

    calendar.set(Calendar.HOUR_OF_DAY, getEndHour());
    calendar.set(Calendar.MINUTE, getEndMinutes());

    test.end = calendar.getTime();

    test.save();
    calendar.setTime(current);
}
	int getStartHour(){
		return 8;
	}
	
	int getStartMinutes(){
		return 30;
	}
	int getEndHour(){
		return 8;
	}
	
	int getEndMinutes(){
		return 30;
	}
	}

