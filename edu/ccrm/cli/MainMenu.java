package edu.ccrm.cli;

import edu.ccrm.domain.*;
import edu.ccrm.service.*;
import edu.ccrm.util.FileUtil;
import java.util.*;
import java.io.IOException;

// New import for the Grade enum
import edu.ccrm.domain.Grade;

public class MainMenu {
    private static Scanner sc = new Scanner(System.in);

    // Use service classes instead of raw lists
    private static StudentService studentService = new StudentService();
    private static CourseService courseService = new CourseService();
    private static EnrollmentService enrollmentService = new EnrollmentService();
    // New service for handling transcripts
    private static TranscriptService transcriptService = new TranscriptService();

    public static void main(String[] args) {
        int choice;
        do {
            // Modified Menu with new options
            System.out.println("\n== Campus Course & Records Manager ==");
            System.out.println("--- Student Menu ---");
            System.out.println("1. Add Student");
            System.out.println("2. List Students");
            System.out.println("3. Update Student");
            System.out.println("4. Deactivate Student");
            System.out.println("--- Course Menu ---");
            System.out.println("5. Add Course");
            System.out.println("6. List Courses");
            System.out.println("7. Search Courses by Instructor");
            System.out.println("--- Enrollment Menu ---");
            System.out.println("8. Enroll Student in Course");
            System.out.println("9. Assign Grade");
            System.out.println("10. View Student Transcript");
            System.out.println("--- System ---");
            System.out.println("11. Backup Example File");
            System.out.println("12. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt(); sc.nextLine(); // Consume newline

            // Modified switch to handle new options
            switch (choice) {
                case 1: addStudent(); break;
                case 2: listStudents(); break;
                case 3: updateStudent(); break;
                case 4: deactivateStudent(); break;
                case 5: addCourse(); break;
                case 6: listCourses(); break;
                case 7: searchCourses(); break;
                case 8: enrollStudent(); break;
                case 9: assignGrade(); break;
                case 10: viewTranscript(); break;
                case 11: tryBackup(); break;
                case 12: System.out.println("Bye!"); break;
                default: System.out.println("Invalid choice!");
            }
        } while (choice != 12);
    }

    private static void addStudent() {
        System.out.print("Enter id: ");
        String id = sc.nextLine();
        System.out.print("Enter name: ");
        String name = sc.nextLine();
        System.out.print("Enter email: ");
        String email = sc.nextLine();
        System.out.print("Enter regNo: ");
        String reg = sc.nextLine();

        Student s = new Student(id, name, email, reg);
        studentService.addStudent(s);
        System.out.println("Student added successfully.");
    }

    private static void listStudents() {
        System.out.println("\n--- All Students ---");
        for (Student s : studentService.getAllStudents()) {
            s.printProfile();
            System.out.println("--------------------");
        }
    }

    // New method to update a student
    private static void updateStudent() {
        System.out.print("Enter student id to update: ");
        String sid = sc.nextLine();
        if (studentService.findStudentById(sid) == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.print("Enter new full name: ");
        String newName = sc.nextLine();
        System.out.print("Enter new email: ");
        String newEmail = sc.nextLine();
        studentService.updateStudent(sid, newName, newEmail);
        System.out.println("Student details updated successfully.");
    }

    // New method to deactivate a student
    private static void deactivateStudent() {
        System.out.print("Enter student id to deactivate: ");
        String sid = sc.nextLine();
        studentService.deactivateStudent(sid);
        System.out.println("Student deactivated successfully.");
    }

    private static void addCourse() {
        System.out.print("Enter code: ");
        String code = sc.nextLine();
        System.out.print("Enter title: ");
        String title = sc.nextLine();
        System.out.print("Enter credits: ");
        int cr = sc.nextInt(); sc.nextLine();
        System.out.print("Enter instructor: ");
        String inst = sc.nextLine();

        Course c = new Course.Builder()
                        .setCode(code)
                        .setTitle(title)
                        .setCredits(cr)
                        .setInstructor(inst)
                        .setSemester(Semester.SPRING)
                        .build();
        courseService.addCourse(c);
        System.out.println("Course added successfully.");
    }

    private static void listCourses() {
        System.out.println("\n--- All Courses ---");
        for (Course c : courseService.getAllCourses()) {
            System.out.println(c);
        }
    }

    // New method to search for courses
    private static void searchCourses() {
        System.out.print("Enter instructor name to search for: ");
        String instructorName = sc.nextLine();
        List<Course> results = courseService.searchCoursesByInstructor(instructorName);
        
        if (results.isEmpty()) {
            System.out.println("No courses found for this instructor.");
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(System.out::println);
        }
    }

    private static void enrollStudent() {
        System.out.print("Enter student id: ");
        String sid = sc.nextLine();
        Student s = studentService.findStudentById(sid);

        System.out.print("Enter course code: ");
        String ccode = sc.nextLine();
        Course c = courseService.findCourseByCode(ccode);

        if (s != null && c != null) {
            enrollmentService.enroll(s, c);
            System.out.println("Enrollment successful.");
        } else {
            System.out.println("Invalid student id or course code.");
        }
    }
    
    // New method to assign a grade
    private static void assignGrade() {
        System.out.print("Enter student id: ");
        String sid = sc.nextLine();
        Student s = studentService.findStudentById(sid);

        System.out.print("Enter course code: ");
        String ccode = sc.nextLine();
        Course c = courseService.findCourseByCode(ccode);

        if (s == null || c == null) {
            System.out.println("Invalid student or course.");
            return;
        }

        System.out.print("Enter grade (S, A, B, C, D, E, F): ");
        String gradeStr = sc.nextLine().toUpperCase();
        try {
            Grade grade = Grade.valueOf(gradeStr);
            enrollmentService.assignGrade(s, c, grade);
            System.out.println("Grade assigned successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid grade entered. Please use S, A, B, etc.");
        }
    }

    // New method to view a transcript
    private static void viewTranscript() {
        System.out.print("Enter student id to view transcript: ");
        String sid = sc.nextLine();
        Student s = studentService.findStudentById(sid);
        if (s != null) {
            transcriptService.printTranscript(s, enrollmentService);
        } else {
            System.out.println("Student not found.");
        }
    }

    private static void listEnrollments() {
        System.out.println("\n--- All Enrollments ---");
        for (Enrollment e : enrollmentService.getAllEnrollments()) {
            System.out.println(e);
        }
    }

    private static void tryBackup() {
        try {
            FileUtil.backupFile("sample.txt");
            System.out.println("Backup done.");
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
