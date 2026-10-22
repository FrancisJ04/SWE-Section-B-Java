package edu.espol.swe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Represents a student with an identifier, a name and a list of grades.
 *
 * <p>All inputs are validated. Invalid data raises an
 * {@link IllegalArgumentException} with a clear message so the caller can
 * inform the user without crashing the system.</p>
 */
public final class Student {

    /** Lowest grade accepted. */
    public static final double MIN_GRADE = 0.0;

    /** Highest grade accepted. */
    public static final double MAX_GRADE = 100.0;

    /** Minimum average required for an A letter grade. */
    private static final double A_THRESHOLD = 90.0;

    /** Minimum average required for a B letter grade. */
    private static final double B_THRESHOLD = 80.0;

    /** Minimum average required for a C letter grade. */
    private static final double C_THRESHOLD = 70.0;

    /** Minimum average required for a D letter grade and to pass. */
    private static final double PASSING_THRESHOLD = 60.0;

    /** Minimum average required to be on the honor roll. */
    private static final double HONOR_ROLL_THRESHOLD = 90.0;

    /** Unique identifier of the student. */
    private final String id;

    /** Full name of the student. */
    private final String name;

    /** Grades recorded for the student. */
    private final List<Double> grades;

    /**
     * Creates a student record.
     *
     * @param studentId identifier of the student, must not be empty
     * @param studentName name of the student, must not be empty
     * @throws IllegalArgumentException if the id or the name is empty
     */
    public Student(final String studentId, final String studentName) {
        this.id = requireNotEmpty(studentId, "Student ID");
        this.name = requireNotEmpty(studentName, "Student name");
        this.grades = new ArrayList<>();
    }

    /**
     * Returns the student identifier.
     *
     * @return the student identifier
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the student name.
     *
     * @return the student name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns a read-only view of the recorded grades.
     *
     * @return the list of grades
     */
    public List<Double> getGrades() {
        return Collections.unmodifiableList(grades);
    }

    /**
     * Adds a numeric grade.
     *
     * @param grade grade to add, must be between 0 and 100
     * @throws IllegalArgumentException if the grade is out of range
     */
    public void addGrade(final double grade) {
        if (Double.isNaN(grade) || grade < MIN_GRADE || grade > MAX_GRADE) {
            throw new IllegalArgumentException(String.format(Locale.US,
                    "Invalid grade %s: it must be between %.0f and %.0f.",
                    grade, MIN_GRADE, MAX_GRADE));
        }
        grades.add(grade);
    }

    /**
     * Adds a grade received as text, such as user input.
     *
     * @param rawGrade text that must represent a number between 0 and 100
     * @throws IllegalArgumentException if the text is not numeric or the
     *         grade is out of range
     */
    public void addGrade(final String rawGrade) {
        if (rawGrade == null || rawGrade.trim().isEmpty()) {
            throw new IllegalArgumentException("Grade must not be empty.");
        }
        final double grade;
        try {
            grade = Double.parseDouble(rawGrade.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Invalid grade \"" + rawGrade + "\": it must be numeric.",
                    e);
        }
        addGrade(grade);
    }

    /**
     * Removes the grade stored at the given position.
     *
     * @param index zero-based position of the grade
     * @return the removed grade
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public double removeGradeByIndex(final int index) {
        if (index < 0 || index >= grades.size()) {
            throw new IllegalArgumentException("Cannot remove grade at index "
                    + index + ": valid range is 0 to "
                    + (grades.size() - 1) + ".");
        }
        return grades.remove(index);
    }

    /**
     * Removes the first occurrence of the given grade value.
     *
     * @param value grade value to remove
     * @throws IllegalArgumentException if the value is not recorded
     */
    public void removeGradeByValue(final double value) {
        if (!grades.remove(Double.valueOf(value))) {
            throw new IllegalArgumentException("Cannot remove grade " + value
                    + ": it does not exist for student " + id + ".");
        }
    }

    /**
     * Calculates the average of all grades.
     *
     * @return the average, or 0 when the student has no grades
     */
    public double calculateAverage() {
        if (grades.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        for (double grade : grades) {
            total += grade;
        }
        return total / grades.size();
    }

    /**
     * Converts the average into a letter grade.
     *
     * @return A, B, C, D or F
     */
    public char getLetterGrade() {
        final double average = calculateAverage();
        if (average >= A_THRESHOLD) {
            return 'A';
        }
        if (average >= B_THRESHOLD) {
            return 'B';
        }
        if (average >= C_THRESHOLD) {
            return 'C';
        }
        if (average >= PASSING_THRESHOLD) {
            return 'D';
        }
        return 'F';
    }

    /**
     * Indicates whether the student passed.
     *
     * @return true if the average is 60 or higher
     */
    public boolean hasPassed() {
        return calculateAverage() >= PASSING_THRESHOLD;
    }

    /**
     * Returns the pass/fail status as text.
     *
     * @return "Passed" or "Failed"
     */
    public String getPassStatus() {
        if (hasPassed()) {
            return "Passed";
        }
        return "Failed";
    }

    /**
     * Indicates whether the student is on the honor roll.
     *
     * @return true if the average is 90 or higher
     */
    public boolean isHonorRoll() {
        return calculateAverage() >= HONOR_ROLL_THRESHOLD;
    }

    /**
     * Generates a formatted summary report of the student.
     *
     * @return the summary report
     */
    public String generateSummaryReport() {
        String honorRoll = "No";
        if (isHonorRoll()) {
            honorRoll = "Yes";
        }
        return String.format(Locale.US,
                "========== Student Summary ==========%n"
                + "Student ID       : %s%n"
                + "Student Name     : %s%n"
                + "Number of Grades : %d%n"
                + "Average Grade    : %.2f%n"
                + "Letter Grade     : %c%n"
                + "Status           : %s%n"
                + "Honor Roll       : %s%n"
                + "=====================================",
                id, name, grades.size(), calculateAverage(),
                getLetterGrade(), getPassStatus(), honorRoll);
    }

    /**
     * Validates that a text value is not null or blank.
     *
     * @param value text to validate
     * @param fieldName name of the field, used in the error message
     * @return the trimmed value
     * @throws IllegalArgumentException if the value is empty
     */
    private static String requireNotEmpty(final String value,
                                          final String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    fieldName + " must not be empty.");
        }
        return value.trim();
    }
}
