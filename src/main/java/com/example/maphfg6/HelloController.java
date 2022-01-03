package com.example.maphfg6;

import Controller.RegistrationSystem;
import Model.Student;
import Model.Teacher;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class HelloController {
    @FXML
    private Label loginFailed;

    private RegistrationSystem registrationSystem;

    @FXML
    private TextField firstNameBox;

    @FXML
    private TextField lastNameBox;

    @FXML
    private CheckBox teacherCheckBox;

    private String firstName;
    private String lastName;

    @FXML
    protected void onLogInButtonClick() throws SQLException {

        if (firstName == null) {
            registrationSystem = new RegistrationSystem("jdbc:mysql://localhost:3306/university", "root", "password31");

            if (teacherCheckBox.isSelected()) {

                for (Teacher teacher : registrationSystem.retrieveAllTeachers()) {
                    if (teacher.getFirstName().equals(firstNameBox.getText()) && teacher.getLastName().equals(lastNameBox.getText())) {
                        this.firstName = firstNameBox.getText();
                        this.lastName = lastNameBox.getText();
                        loginFailed.setText(String.format("Logged In as %s %s !", firstName, lastName));
                        return;
                    }
                }

            } else {

                for (Student stud : registrationSystem.retrieveAllStudents()) {
                    if (stud.getFirstName().equals(firstNameBox.getText()) && stud.getLastName().equals(lastNameBox.getText())) {
                        this.firstName = firstNameBox.getText();
                        this.lastName = lastNameBox.getText();
                        loginFailed.setText(String.format("Logged in as %s %s !", firstName, lastName));
                        return;
                    }
                }
            }
            loginFailed.setText("Log in failed, please try again!");
        } else loginFailed.setText(String.format("Already logged in as %s %s !", firstName, lastName));
    }
}