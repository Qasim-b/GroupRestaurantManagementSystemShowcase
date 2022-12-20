package com.github.team_project.group11;

import java.util.*;

/**
 * This class executes the functions required for the payment functionality on the PaymentScreen fxml scene.
 * 
 * @author Luna Cheung
 * @author Adil Mushtaq
 */
public class Payment {
	
	private float amount;

	public Payment(float amount) {
		this.setAmount(amount);
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * 
	 * Function to fake payment process using customer details.
	 * 
	 * @param cardNum    customer card number.
	 * @param secCode    customer card security code.
	 * @param expiryDate customer card expiry date.
	 * @return boolean response to check if function worked.
	 */
	public boolean processPayment(String cardNum, String secCode, Date expiryDate) {
		if (checkCardNum(cardNum) && checkCardExpirey(expiryDate) && validateSecCode(secCode)) {
			return true;
		} else {
			return false;
		}
	}

//	public boolean checkCardNum(String cardNum) {
//		int length = cardNum.length();
//		int sum = 0;
//		for (int i = 1, pos = length - 1; i < 10; i++, pos--) {
//			char tmp = cardNum.charAt(pos);
//			int num = tmp - 0;
//			int product;
//			if (i % 2 != 0) {
//				product = num * 1;
//			} else {
//				product = num * 2;
//			}
//			if (product > 9)
//				product -= 9;
//			sum += product;
//			boolean valid = (sum % 10 == 0);
//			if (valid) {
//				return true;
//			} else {
//				return false;
//			}
//		}
//		return false;
//	}

	/**
	 * This function checks whether the 16 digit card number entered is valid by checking its length. 
	 * 
	 * @param cardNum - A card number variable.
	 * @return A boolean response to whether the card number is of correct length.
	 */
	public boolean checkCardNum(String cardNum) {
		if (cardNum.length() != 16) {
			return true;
		}
		return false;
	}
	
	/**
	 * This function checks whether the card expiry date is valid.
	 * 
	 * @param expiryDate - An expiry date variable.
	 * @return A boolean response to whether the card expiry date is valid. 
	 */
	public boolean checkCardExpirey(Date expiryDate) {
		Date currentDate = new Date();
		int result = currentDate.compareTo(expiryDate);
		if (result == 0 || result < 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * This function checks the card security code is valid.
	 * 
	 * @param secCode - A variable for the CVV security code.
	 * @return A boolean response to whether the CVV security code is valid.
	 */
	public boolean validateSecCode(String secCode) {
		if (secCode.length() != 3) {
			return false;
		}
		return true;
	}
}
