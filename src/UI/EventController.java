package UI;

import Model.Appointment;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

		Stage parent = (Stage) StartTime.getScene().getWindow();
		parent.close();
	}
	int getStartHour(){
		String[] input = StartTime.getText().split(":");
		if (input.length > 0){
			return Integer.parseInt(input[0]);
		}
		//todo: Popup requesting a valid time
		return 8;
	}

	int getStartMinutes(){
		String[] input = StartTime.getText().split(":");
		if (input.length > 1){
			return Integer.parseInt(input[1]);
		}
		//todo: Popup requesting a valid time
		return 0;
	}
	int getEndHour(){
		String[] input = EndTime.getText().split(":");
		if (input.length > 0){
			return Integer.parseInt(input[0].replace(" ", ""));
		}
		//todo: Popup requesting a valid time
		return 8;
	}

	int getEndMinutes(){
		String[] input = EndTime.getText().split(":");
		if (input.length > 1){
			return Integer.parseInt(input[1].replace(" ", ""));
		}
		//todo: Popup requesting a valid time
		return 0;
	}


}

