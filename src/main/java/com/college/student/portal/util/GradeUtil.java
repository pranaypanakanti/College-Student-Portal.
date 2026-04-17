package com.college.student.portal.util;

public class GradeUtil {

    public static String calculateGrade(int totalMarks) {
        if (totalMarks >= 90) return "A+";
        else if (totalMarks >= 80) return "A";
        else if (totalMarks >= 70) return "B+";
        else if (totalMarks >= 60) return "B";
        else if (totalMarks >= 50) return "C";
        else if (totalMarks >= 40) return "D";
        else return "F";
    }

    public static String generateReceiptNumber() {
        return "RCP-" + System.currentTimeMillis();
    }
}