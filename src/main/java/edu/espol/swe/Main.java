package edu.espol.swe;

/**
 * Entry point of the Student Grade Management System.
 *
 * <p>Runs a demonstration with valid and invalid inputs. Every invalid
 * operation is caught and reported with a clear error message, so the
 * program never crashes.</p>
 */
public final class Main {

    /** High grade used in the demonstration. */
    private static final double HIGH_GRADE = 95.0;

    /** Top grade used in the demonstration. */
    private static final double TOP_GRADE = 98.0;

    /** Average grade used in the demonstration. */
    private static final double AVERAGE_GRADE = 72.5;

    /** Failing grade used in the demonstration. */
    private static final double LOW_GRADE = 45.0;

    /** Invalid grade above the allowed maximum. */
    private static final double ABOVE_MAX_GRADE = 105.0;

    /** Invalid negative grade. */
    private static final double NEGATIVE_GRADE = -3.0;

    /** Grade value that is never recorded. */
    private static final double MISSING_GRADE = 50.0;

    /** Index that is out of bounds for the recorded grades. */
    private static final int OUT_OF_RANGE_INDEX = 9;

    /**
     * Prevents instantiation of this utility class.
     */
    private Main() {
    }

    /**
     * Runs the demonstration.
     *
     * @param args command line arguments, not used
     */
    public static void main(final String[] args) {
        System.out.println("=== Student Grade Management System ===");

        System.out.println();
        System.out.println("--- Invalid students ---");
        createStudent("S000", null);
        createStudent("", "Maria Lopez");

        System.out.println();
        System.out.println("--- Honor roll student ---");
        final Student honorStudent = createStudent("S001", "Ana Torres");
        if (honorStudent != null) {
            addGrade(honorStudent, HIGH_GRADE);
            addGrade(honorStudent, TOP_GRADE);
            System.out.println(honorStudent.generateSummaryReport());
        }

        System.out.println();
        System.out.println("--- Student with invalid operations ---");
        final Student regularStudent = createStudent("S002", "Luis Perez");
        if (regularStudent != null) {
            addGrade(regularStudent, AVERAGE_GRADE);
            addGrade(regularStudent, LOW_GRADE);
            addGrade(regularStudent, "Ninety");
            addGrade(regularStudent, ABOVE_MAX_GRADE);
            addGrade(regularStudent, NEGATIVE_GRADE);
            removeGradeByIndex(regularStudent, OUT_OF_RANGE_INDEX);
            removeGradeByValue(regularStudent, MISSING_GRADE);
            removeGradeByValue(regularStudent, LOW_GRADE);
            System.out.println(regularStudent.generateSummaryReport());
        }

        System.out.println();
        System.out.println("--- Failing student ---");
        final Student failingStudent = createStudent("S003", "Carlos Ruiz");
        if (failingStudent != null) {
            addGrade(failingStudent, LOW_GRADE);
            System.out.println(failingStudent.generateSummaryReport());
        }
    }

    /**
     * Creates a student and reports an error if the data is invalid.
     *
     * @param studentId identifier of the student
     * @param studentName name of the student
     * @return the created student, or null if the data is invalid
     */
    private static Student createStudent(final String studentId,
                                         final String studentName) {
        try {
            final Student student = new Student(studentId, studentName);
            System.out.println("[OK] Student " + student.getId()
                    + " created.");
            return student;
        } catch (IllegalArgumentException e) {
            printError(e);
            return null;
        }
    }

    /**
     * Adds a numeric grade and reports an error if it is invalid.
     *
     * @param student student that receives the grade
     * @param grade grade to add
     */
    private static void addGrade(final Student student, final double grade) {
        try {
            student.addGrade(grade);
            System.out.println("[OK] Grade " + grade + " added.");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    /**
     * Adds a grade received as text and reports an error if it is invalid.
     *
     * @param student student that receives the grade
     * @param rawGrade grade as text
     */
    private static void addGrade(final Student student,
                                 final String rawGrade) {
        try {
            student.addGrade(rawGrade);
            System.out.println("[OK] Grade " + rawGrade + " added.");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    /**
     * Removes a grade by its position and reports an error if it fails.
     *
     * @param student student whose grade is removed
     * @param index zero-based position of the grade
     */
    private static void removeGradeByIndex(final Student student,
                                           final int index) {
        try {
            final double removed = student.removeGradeByIndex(index);
            System.out.println("[OK] Grade " + removed + " at index "
                    + index + " removed.");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    /**
     * Removes a grade by its value and reports an error if it fails.
     *
     * @param student student whose grade is removed
     * @param value grade value to remove
     */
    private static void removeGradeByValue(final Student student,
                                           final double value) {
        try {
            student.removeGradeByValue(value);
            System.out.println("[OK] Grade " + value + " removed.");
        } catch (IllegalArgumentException e) {
            printError(e);
        }
    }

    /**
     * Prints a clear error message for an invalid operation.
     *
     * @param error the error raised by the invalid operation
     */
    private static void printError(final IllegalArgumentException error) {
        System.out.println("[ERROR] " + error.getMessage());
    }
}
