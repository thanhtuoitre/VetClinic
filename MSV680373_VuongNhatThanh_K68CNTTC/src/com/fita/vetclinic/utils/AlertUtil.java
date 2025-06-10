package com.fita.vetclinic.utils;

import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class AlertUtil {

	public static void showErrorAlert(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static void showErrorAlert(String title, String contentText) {
		showErrorAlert(title, null, contentText);
	}

	public static void showInfoAlert(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static void showInfoAlert(String title, String contentText) {
		showInfoAlert(title, null, contentText);
	}

	public static void showWarningAlert(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);
		alert.showAndWait();
	}

	public static void showWarningAlert(String title, String contentText) {
		showWarningAlert(title, null, contentText);
	}

	public static boolean showConfirmationAlert(String title, String headerText, String contentText) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(contentText);

		Optional<ButtonType> result = alert.showAndWait();
		return result.isPresent() && result.get() == ButtonType.OK; 
	}

	public static boolean showConfirmationAlert(String title, String contentText) {
		return showConfirmationAlert(title, null, contentText);
	}
}