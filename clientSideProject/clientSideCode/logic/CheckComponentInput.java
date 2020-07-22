package logic;

import java.time.LocalDate;

import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

/**This class contain some static method, that making validation test to deferment GUI component,
 * if the test don't pass successfully, the method return false, 
 * and generate appropriate comment on the user window.
 * @author Nadav Shwartz.
 *
 */
public class CheckComponentInput {
	
	/**
	 * @param str - the given string for checking.
	 * @return return true if and only if the given string contain numbers only.
	 */
	public static boolean isNumber(String str) { 
		  try {  
		    Double.parseDouble(str);  
		    return true;
		  } catch(NumberFormatException e){  
		    return false;  
		  }  
		}
	
	/**This method get String str and check if this string contains numbers, 
	 * if not return true, else return false.
	 * @param str
	 * @return
	 */
	public static boolean notContainNumbers(String str) {
		     
		      char[] chars = str.toCharArray();
		      for(char c : chars){
		         if(Character.isDigit(c)){
		        	 return false;
		         }
		      }
		      return true;
	}
	
	/**This method get String and check if it is a number, 
	 * and if it is true, check that the Cvv number is between 100 to 999.
	 * @param str
	 * @return
	 */
	public static boolean checkCreditCardCvvNumber(String str) {
		boolean isInputValid = true;
		
		if(!CheckComponentInput.isNumber(str)) {
			isInputValid = false;
			return isInputValid;
		}
		
		if(Integer.parseInt(str) < 100 || Integer.parseInt(str) > 999) {
			isInputValid = false;
			return isInputValid;
		}
		
		return isInputValid;
	}
	
	/**This method check the year of the customer credit cared, the year supose to be between 2020 to 2030
	 * @param str
	 * @return
	 */
	public static boolean isYear(String str) {
		boolean isInputValid = true;
		
		if(!CheckComponentInput.isNumber(str)) {
			isInputValid = false;
			return isInputValid;
		}
		
		if(Integer.parseInt(str) < 2020 || Integer.parseInt(str) > 2030) {
			isInputValid = false;
			return isInputValid;
		}

		return isInputValid;
	}
	
	public static boolean isMonth(String str) {
		boolean isInputValid = true;
		
		if(!CheckComponentInput.isNumber(str)) {
			isInputValid = false;
			return isInputValid;
		}
		
		if(Integer.parseInt(str) < 1 || Integer.parseInt(str) > 13) {
			isInputValid = false;
			return isInputValid;
		}
		
		return isInputValid;
	}
	
	
	/**This method get TextField T and Label L and check if the input for TextField T 
	 * is not empty, and contain number only. If the condition is false, the Label L get some error message.
	 * @param textField T
	 * @param label L
	 * @return return true if and only if the TextField T got numeric input only.
	 */
	public static boolean numericTextFieldValidation(TextField textField, Label label) {
		boolean isInputValid = true;
		String str = textField.getText();
		
		if(textField.getText().isEmpty()) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Empty Field");
				}
			});
		}else if(!CheckComponentInput.isNumber(str)) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Enter number");
				}
			});
		}else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("");
				}
			});
		}
		
		return isInputValid;
	}
	
	/**This method get TextField T and Label L and check if the input for TextField T 
	 * is not empty, and don't contain number. If the condition is false, the Label L get some error message.
	 * @param textField T
	 * @param label L
	 * @return
	 */
	public static boolean notNumericTextFieldValidation(TextField textField, Label label) {
		boolean isInputValid = true;
		String str = textField.getText();
		
		if(textField.getText().isEmpty()) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Empty Field");
				}
			});
		}else if(!CheckComponentInput.notContainNumbers(str)) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Remove number");
				}
			});
		}else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("");
				}
			});
		}
		
		return isInputValid;
	}
	
	/**This method get TextField T and Label L and check if the input for TextField T 
	 * is not empty. If the condition is false, the Label L get some error message.
	 * @param textField T
	 * @param label L
	 * @return return true if and only if the TextField T don't empty.
	 */
	public static boolean notEmptyTextFieldValidation(TextField textField, Label label) {
		boolean isInputValid = true;
		
		if(textField.getText().isEmpty()) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Empty Field");
				}
			});
		}else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("");
				}
			});
		}

		
		return isInputValid;
	}
	
	/**This method get ComboBox C and Label L and check if the ComboBox C contain some value. 
	 * If the condition is false the Label L get some error message.
	 * @param comboBox C
	 * @param label L
	 * @return return true if and only if the ComboBox C don't empty.
	 */
	@SuppressWarnings("rawtypes")
	public static boolean notEmptyComboBoxValidation(ComboBox comboBox, Label label) {
		boolean isInputValid = true;
		
		if(comboBox.getSelectionModel().isEmpty()) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Empty Field");

				}
			});
		}else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("");

				}
			});
		}

		
		return isInputValid;

	}
	
	/**This method get ChoiceBox C and Label L and check if the ChoiceBox C contain some value. 
	 * If the condition is false the Label L get some error message
	 * @param choiceBox C
	 * @param label L
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean notEmptyChoiceBoxValidation(ChoiceBox choiceBox, Label label) {
		boolean isInputValid = true;
		
		if(choiceBox.getSelectionModel().isEmpty()) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Empty Field");

				}
			});
		}else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("");

				}
			});
		}
		
		return isInputValid;
	}
	
	/**This method get DatePicker D and Label L and check if the DatePicker D contain some value.
	 * @param datePicker D
	 * @param label L
	 * @return return true if and only if the DatePicker D don't empty.
	 */
	public static boolean notEmptyDatePickerValidation(DatePicker datePicker, Label label) {
		boolean isInputValid = true;
		
		if(datePicker.getValue() == null) {
			isInputValid = false;
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("Empty Field");
				}
			});
		}else {
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					label.setText("");
				}
			});
		}

		
		
		return isInputValid;
	}
	
	/**This method check if the value of the given datePicker is before the current date. 
	 * @param datePicker
	 * @param orderType 
	 * @param label
	 * @return
	 */
	public static boolean DatePickerValueValidation(DatePicker datePicker, String orderType, Label label) {
		boolean isInputValid = true;
		
		LocalDate localDate;
		if(orderType.equals("Regular")) {
			localDate = LocalDate.now().plusDays(1);
			
			if(datePicker.getValue().isBefore(localDate)) {
				isInputValid = false;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						label.setText("Error date value");
					}
				});
			}else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						label.setText("");
					}
				});
			}
		}else {
			localDate = LocalDate.now();
			
			if(datePicker.getValue().isBefore(localDate) || 
					datePicker.getValue().isAfter(localDate)) {
				isInputValid = false;
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						label.setText("Error date value");
					}
				});
			}else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						label.setText("");
					}
				});
			}

		}
		
	
		
		return isInputValid;

	}
	
	

}