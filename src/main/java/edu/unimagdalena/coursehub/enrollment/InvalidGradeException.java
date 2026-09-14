package edu.unimagdalena.coursehub.enrollment;

import java.math.BigDecimal;

public class InvalidGradeException extends RuntimeException {
    public InvalidGradeException(BigDecimal grade) { super("Final grade must be between 0.00 and 5.00. Received: " + grade); }
}
