package edu.ccrm.service;

import edu.ccrm.domain.Course;
import java.util.ArrayList;
import java.util.List;
// New import for using Streams and Collectors
import java.util.stream.Collectors;

public class CourseService {
    private List<Course> courses = new ArrayList<>();

    public void addCourse(Course course) {
        courses.add(course);
    }

    public List<Course> getAllCourses() {
        return courses;
    }

    public Course findCourseByCode(String code) {
        for (Course c : courses) {
            if (c.getCode().equals(code)) {
                return c;
            }
        }
        return null;
    }

    // New method to search for courses using the Stream API
    public List<Course> searchCoursesByInstructor(String instructorName) {
        return courses.stream()
                .filter(course -> course.getInstructor().equalsIgnoreCase(instructorName))
                .collect(Collectors.toList());
    }
}
