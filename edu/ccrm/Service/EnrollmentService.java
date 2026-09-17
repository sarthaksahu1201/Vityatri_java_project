package edu.ccrm.service;

import edu.ccrm.domain.Course;
import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Grade;
import edu.ccrm.domain.Student;
// New import for the custom exception
import edu.ccrm.exception.DuplicateEnrollmentException;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentService {
    private List<Enrollment> enrollments = new ArrayList<>();

    // Modified enroll method to prevent duplicates and throw a custom exception
    public void enroll(Student student, Course course) throws DuplicateEnrollmentException {
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(student) && e.getCourse().equals(course)) {
                // Throw the custom exception if the student is already enrolled
                throw new DuplicateEnrollmentException(
                    "Enrollment failed: Student is already registered for this course."
                );
            }
        }
        Enrollment newEnrollment = new Enrollment(student, course);
        enrollments.add(newEnrollment);
    }

    // Modified assignGrade method for better efficiency
    public void assignGrade(Student student, Course course, Grade grade) {
        for (Enrollment e : enrollments) {
            if (e.getStudent().equals(student) && e.getCourse().equals(course)) {
                e.setGrade(grade);
                break; // Exit the loop once the correct enrollment is found and updated
            }
        }
    }

    public List<Enrollment> getAllEnrollments() {
        return enrollments;
    }
}
