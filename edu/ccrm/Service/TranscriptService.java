package edu.ccrm.service;

import edu.ccrm.domain.Enrollment;
import edu.ccrm.domain.Student;
import java.util.List;
import java.util.stream.Collectors;

public class TranscriptService {
    public void printTranscript(Student student, EnrollmentService enrollmentService) {
        System.out.println("\n--- Transcript for " + student.getFullName() + " ---");
        List<Enrollment> studentEnrollments = enrollmentService.getAllEnrollments().stream()
            .filter(e -> e.getStudent().equals(student))
            .collect(Collectors.toList());

        if (studentEnrollments.isEmpty()) {
            System.out.println("No courses enrolled.");
            return;
        }

        double totalPoints = 0;
        int totalCredits = 0;

        for (Enrollment e : studentEnrollments) {
            System.out.println(e); // Uses the toString() override
            if(e.getGradePoints() > 0) {
                totalPoints += e.getGradePoints() * e.getCourse().getCredits();
                totalCredits += e.getCourse().getCredits();
            }
        }

        if (totalCredits > 0) {
            double gpa = totalPoints / totalCredits;
            System.out.printf("GPA: %.2f\n", gpa);
        } else {
            System.out.println("GPA: N/A (no graded courses)");
        }
    }
}
// Note: You will need to add a getCredits() method to your Course.java class.
