package com.example.maphfg6;

import Controller.RegistrationSystem;
import Exceptions.AlreadyExistsException;
import Exceptions.ElementDoesNotExistException;
import Exceptions.MaxCreditsSurpassedException;
import Exceptions.MaxEnrollmentSurpassedException;
import Model.Course;
import Model.Student;
import Model.Teacher;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;


public class HelloController {
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private Label loginFailed;

    private RegistrationSystem registrationSystem;

    @FXML
    private TextField firstNameBox;

    @FXML
    private TextField lastNameBox;

    @FXML
    private CheckBox teacherCheckBox;

    @FXML
    private TextField courseId;

    @FXML
    private Label status;

    private String firstName;
    private String lastName;
    private long id;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private Label credits;

    @FXML
    public void onLogInButtonClick(ActionEvent event) throws SQLException, IOException {
        if (firstName == null) {
            registrationSystem = new RegistrationSystem("jdbc:mysql://localhost:3306/university", "root", "password31");

            if (teacherCheckBox.isSelected()) {

                for (Teacher teacher : registrationSystem.retrieveAllTeachers()) {
                    if (teacher.getFirstName().equals(firstNameBox.getText()) && teacher.getLastName().equals(lastNameBox.getText())) {

                        root = FXMLLoader.load(getClass().getResource("TeacherScene.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                        this.firstName = firstNameBox.getText();
                        this.lastName = lastNameBox.getText();
                        this.id = teacher.getTeacherId();

                    }
                }

            } else {

                for (Student stud : registrationSystem.retrieveAllStudents()) {
                    if (stud.getFirstName().equals(firstNameBox.getText()) && stud.getLastName().equals(lastNameBox.getText())) {

                        root = FXMLLoader.load(getClass().getResource("StudentScene.fxml"));
                        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                        scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();

                        this.firstName = firstNameBox.getText();
                        this.lastName = lastNameBox.getText();
                        this.id = stud.getStudentId();

                    }
                }
            }
            loginFailed.setText("Log in failed, please try again!");
        } else loginFailed.setText(String.format("Already logged in as %s %s !", firstName, lastName));
    }

    @FXML
    public void onDisplayStudentsButtonClick() throws SQLException {
        registrationSystem = new RegistrationSystem("jdbc:mysql://localhost:3306/university", "root", "password31");
        studentTableView = new TableView<>();

        Set<Long> students = new TreeSet<>();
        for (Course course: registrationSystem.filterCoursesWithStudents()) {
            if (course.getTeacher() == id){
                students.addAll(course.getStudentsEnrolled());
            }
        }

        List<Student> studentsEnrolled = new LinkedList<>();
        for (Student stud : registrationSystem.retrieveAllStudents()) {
            if (students.contains(stud.getStudentId())){
                studentsEnrolled.add(stud);
            }
        }
        studentTableView.setItems((ObservableList<Student>) studentsEnrolled);

    }

    @FXML
    public void register() {
        System.out.println(firstName);
        registrationSystem = new RegistrationSystem("jdbc:mysql://localhost:3306/university", "root", "password31");
        try {
            registrationSystem.register(Long.parseLong(courseId.getText()) ,this.id);
            status.setText("Operation successfully performed!");
        } catch (ElementDoesNotExistException e) {
            status.setText("Error! Invalid course id!");
        } catch (MaxCreditsSurpassedException e) {
            status.setText("Error! Credits surpassed!");
        } catch (MaxEnrollmentSurpassedException e) {
            status.setText("Error! Course is full!");
        } catch (AlreadyExistsException e) {
            status.setText("Error! You are already registered!");
        } catch (SQLException e) {
            status.setText("Unexpected error...");
        }
    }

    @FXML
    public void getCredits() throws SQLException {
        int credtis = registrationSystem.calculateStudentCredits(new Student(firstName, lastName, new LinkedList<>(), id));
        credits.setText(credits.toString());
    }

    @FXML
    public void quit(ActionEvent event){
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }
}