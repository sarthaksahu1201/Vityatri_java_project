package edu.ccrm.service;

import edu.ccrm.domain.Student;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public List<Student> getAllStudents() {
        return students;
    }

    public Student findStudentById(String id) {
        for (Student s : students) {
            if (s.getId().equals(id)) {   // ✅ use getId()
                return s;
            }
        }
        return null;
    }

    // Inside the StudentService class

public void deactivateStudent(String id) {
    Student student = findStudentById(id);
    if (student != null) {
        student.setActive(false);
    }
}

public void updateStudent(String id, String newName, String newEmail) {
    Student student = findStudentById(id);
    if (student != null) {
        student.updateDetails(newName, newEmail);
    }
}
}
