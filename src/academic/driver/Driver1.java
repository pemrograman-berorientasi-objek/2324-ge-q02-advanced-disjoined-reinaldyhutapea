package academic.driver;

import java.util.*;
import academic.model.*;

/**
 * 
 * @author 12S22010 - reinaldi hutapea
 * @author 12S22048 - ira silalahi
 */

public class Driver1 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        ArrayList<Lecturer> lecturers = new ArrayList<>();
        ArrayList<Enrollment> enrollments = new ArrayList<>();
        HashSet<String> registeredStudents = new HashSet<>();
        HashSet<String> remedialRecords = new HashSet<>();
        HashMap<String, String> previousGrades = new HashMap<>();
        String previousGrade = "";

        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            String[] data = input.split("#");
            String command = data[0];

            if (command.equals("course-open")) {
                String courseCode = data[1];
                String academicYear = data[2];
                String semester = data[3];
                String lecturerList = data[4];

                // Cek apakah mata kuliah dan dosen terkait sudah terdaftar
                boolean validCourse = false;
                boolean validLecturers = true;
                Course existingCourse = null;

                // Periksa apakah mata kuliah sudah terdaftar
                for (Course course : courses) {
                    if (course.getCode().equals(courseCode)) {
                        validCourse = true;
                        existingCourse = course;
                        break;
                    }
                }
                // Jika mata kuliah dan dosen terkait valid, tambahkan data periode pembukaan mata kuliah
                if (validCourse && validLecturers) {
                    CourseOpening newCourseOpening = new CourseOpening(courseCode, academicYear, semester, lecturerList);
                    existingCourse.addCourseOpening(newCourseOpening);
                }

            } else if (command.equals("course-history")) {
                String courseCode = data[1];

                // Cari mata kuliah yang sesuai dengan kode yang diberikan
                Course selectedCourse = null;
                for (Course course : courses) {
                    if (course.getCode().equals(courseCode)) {
                        selectedCourse = course;
                        break;
                    }
                }

                //pengurutan courseOpenings berdasarkan semester dan tahun akademik
                Collections.sort(selectedCourse.getCourseOpenings(), new Comparator<CourseOpening>() {
                    @Override
                    public int compare(CourseOpening o1, CourseOpening o2) {
                        if (!o1.getSemester().equals(o2.getSemester())) {
                            return o2.getSemester().compareTo(o1.getSemester());
                        }
                        return o1.getAcademicYear().compareTo(o2.getAcademicYear());
                    }
                });
                selectedCourse.printCourseHistory();

                for (Course course : courses) {
                    // Iterasi semua periode pembukaan mata kuliah
                    for (CourseOpening opening : course.getCourseOpenings()) {
                        String academicYear = opening.getAcademicYear();
                        String semester = opening.getSemester();

                        System.out.print(courseCode + "|" + course.getName() + "|" + course.getCredits() + "|" +
                                course.passingGrade() + "|" + academicYear + "|" + semester + "|" + opening.getLecturerList());

                        // Dapatkan daftar dosen dari periode pembukaan mata kuliah
                        String lecturerList = opening.getLecturerList();
                        String[] lecturerInitials = lecturerList.split(",");
                        for (int i = 0; i < lecturerInitials.length; i++) {
                            String initial = lecturerInitials[i].trim(); // Hilangkan spasi ekstra
                            // Cari dosen berdasarkan inisial
                            for (Lecturer lecturer : lecturers) {
                                if (lecturer.getInitial().equals(initial)) {
                                    // Cetak email dosen
                                    System.out.print(" (" + lecturer.getEmail() + ")");
                                    // Tambahkan titik koma jika bukan dosen terakhir dalam daftar
                                    if (i < lecturerInitials.length - 1) {
                                        System.out.println(";");
                                    }
                                    break;
                                }
                            }
                        }
                        System.out.println();

                        // Iterasi semua pendaftaran pada periode tersebut
                        for (Enrollment enrollment : enrollments) {
                            if (enrollment.getCourseId().equals(courseCode) &&
                                    enrollment.getAcademicYear().equals(academicYear) &&
                                    enrollment.getSemester().equals(semester)) {


                                if (enrollment.getCourseId().equals(course.getCode())) {
                                    //cek apakah enrolment ada di remedialRecords
                                    boolean isRemedial = remedialRecords.contains(enrollment.getCourseId() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester());
                                    // jika ada, cetak output remedial
                                    if (isRemedial) {
                                        String previous = previousGrades.get(enrollment.getCourseId() + "|" + enrollment.getStudentId());
                                        System.out.println(enrollment.getCourseId() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade() + "(" + previous + ")");
                                    } else {
                                        System.out.println(enrollment.getCourseId() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade());
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (command.equals("course-add")) {
                String code = data[1];
                String name = data[2];
                String credits = data[3];
                String passingGrade = data[4];

                Course newCourse = new Course(code, name, credits, passingGrade);
                courses.add(newCourse);

            } else if (command.equals("student-add")) {
                String id = data[1];
                String name = data[2];
                String year = data[3];
                String major = data[4];

                if (registeredStudents.contains(id)) {
                    continue;
                }

                Student newStudent = new Student(id, name, year, major);
                students.add(newStudent);
                registeredStudents.add(id);

            } else if (command.equals("lecturer-add")) {
                String id = data[1];
                String name = data[2];
                String initial = data[3];
                String email = data[4];
                String studyProgram = data[5];

                boolean validLecturer = false;
                for (Lecturer lecturer : lecturers) {
                    if (lecturer.getId().equals(id)) {
                        validLecturer = true;
                        break;
                    }
                }
                if (!validLecturer) {
                    Lecturer newLecturer = new Lecturer(id, name, initial, email, studyProgram);
                    lecturers.add(newLecturer);
                }

            } else if (command.equals("enrollment-add")) {
                String courseId = data[1];
                String studentId = data[2];
                String academicYear = data[3];
                String semester = data[4];

                boolean validCourse = false;
                boolean validStudent = false;
                for (Course course : courses) {
                    if (course.getCode().equals(courseId)) {
                        validCourse = true;
                        break;
                    }
                }

                for (Student student : students) {
                    if (student.getId().equals(studentId)) {
                        validStudent = true;
                        break;
                    }
                }

                if (validCourse && validStudent) {
                    // mengecek apakah student melakukan re-enrollment untuk course yang sama
                    boolean isReEnrollment = false;
                    for (Enrollment enrollment : enrollments) {
                        if (enrollment.getCourseId().equals(courseId) &&
                                enrollment.getStudentId().equals(studentId) &&
                                enrollment.getAcademicYear().equals(academicYear) &&
                                enrollment.getSemester().equals(semester)) {
                            isReEnrollment = true;
                            break;
                        }
                    }
                    if (!isReEnrollment) {
                        String grade = data.length > 5 ? data[5] : "None"; // Memeriksa apakah ada nilai grade yang diberikan atau tidak

                        // Tambahkan nilai grade sebelumnya ke dalam hashmap
                        String previous = previousGrades.get(courseId + "|" + studentId);
                        if (previous == null) {
                            previousGrades.put(courseId + "|" + studentId, grade);
                        }

                        Enrollment newEnrollment = new Enrollment(studentId, courseId, academicYear, semester, grade);
                        enrollments.add(newEnrollment);
                    }

                } else if (!validCourse) {
                    System.out.println("invalid course|" + courseId);
                    Enrollment newEnrollment = new Enrollment(studentId, courseId, academicYear, semester, "None"); // Menambahkan "None" sebagai nilai grade
                    enrollments.add(newEnrollment);
                } else {
                    System.out.println("invalid student|" + studentId);
                    Enrollment newEnrollment = new Enrollment(studentId, courseId, academicYear, semester, "None"); // Menambahkan "None" sebagai nilai grade
                    enrollments.add(newEnrollment);
                }

            } else if (command.equals("enrollment-grade")) {
                String courseId = data[1];
                String studentId = data[2];
                String academicYear = data[3];
                String semester = data[4];
                String grade = data[5];

                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getCourseId().equals(courseId) &&
                            enrollment.getStudentId().equals(studentId) &&
                            enrollment.getAcademicYear().equals(academicYear) &&
                            enrollment.getSemester().equals(semester)) {
                        enrollment.updateGrade(grade);
                        break;
                    }
                }

            } else if (command.equals("enrollment-remedial")) {
                String courseId = data[1];
                String studentId = data[2];
                String academicYear = data[3];
                String semester = data[4];
                String newGrade = data[5];

                // Cek apakah siswa memiliki nilai sebelumnya
                boolean hasPreviousGrade = false;
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getCourseId().equals(courseId) &&
                            enrollment.getStudentId().equals(studentId) &&
                            enrollment.getAcademicYear().equals(academicYear) &&
                            enrollment.getSemester().equals(semester) &&
                            !enrollment.getGrade().equals("None")) {
                        hasPreviousGrade = true;
                        previousGrade = enrollment.getGrade();
                        break;
                    }
                }

                // Cek apakah siswa belum pernah remedial
                boolean hasRemedial = !remedialRecords.contains(courseId + "|" + studentId + "|" + academicYear + "|" + semester);
                if (hasPreviousGrade && hasRemedial) {
                    for (Enrollment enrollment : enrollments) {
                        if (enrollment.getCourseId().equals(courseId) &&
                                enrollment.getStudentId().equals(studentId) &&
                                enrollment.getAcademicYear().equals(academicYear) &&
                                enrollment.getSemester().equals(semester)) {
                            // Cek apakah remedial sudah dilakukan sebelumnya
                            boolean remedialDone = remedialRecords.contains(courseId + "|" + studentId + "|" + academicYear + "|" + semester + newGrade);
                            if (!remedialDone) {
                                enrollment.updateGrade(newGrade);
                                // Tambahkan nilai grade sebelumnya ke dalam hashmap
                                previousGrades.put(courseId + "|" + studentId, previousGrade);
                                // Tandai bahwa remedial telah dilakukan
                                remedialRecords.add(courseId + "|" + studentId + "|" + academicYear + "|" + semester);

                                break;
                            }
                        }
                    }
                }
            } else if (command.equals("student-details")) {
                String studentId = data[1];

                Student selectedStudent = null;
                for (Student student : students) {
                    if (student.getId().equals(studentId)) {
                        selectedStudent = student;
                        break;
                    }
                }

                if (selectedStudent == null) {
                    continue;
                }

                ArrayList<Enrollment> studentEnrollments = new ArrayList<>();
                for (Enrollment enrollment : enrollments) {
                    if (enrollment.getStudentId().equals(studentId)) {
                        studentEnrollments.add(enrollment);
                    }
                }

                double gpa = calculateGPA(studentEnrollments, courses);

                System.out.println(selectedStudent.getId() + "|" + selectedStudent.getName() + "|" +
                        selectedStudent.getYear() + "|" + selectedStudent.getMajor() + "|" +
                        String.format("%.2f", gpa) + "|" + getTotalCredits(studentEnrollments, courses));
            } 
            
            
            else if(command.equals("---")) {
                for (Lecturer lecturer : lecturers) {
                    System.out.println(lecturer.getId() + "|" + lecturer.getName() + "|" + lecturer.getInitial() + "|" + lecturer.getEmail() + "|" + lecturer.getStudyProgram());
                }
                for (Course course : courses) {
                    System.out.println(course.getCode() + "|" + course.getName() + "|" + course.getCredits() + "|" + course.passingGrade());
                }
                for (Student student : students) {
                    System.out.println(student.getId() + "|" + student.getName() + "|" + student.getYear() + "|" + student.getMajor());
                }


                // Bagian yang mengatur pencetakan output
                for (Course course : courses) {
                    for (Enrollment enrollment : enrollments) {
                        if (enrollment.getCourseId().equals(course.getCode())) {
                            //cek apakah enrolment ada di remedialRecords
                            boolean isRemedial = remedialRecords.contains(enrollment.getCourseId() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester());
                            // jika ada, cetak output remedial
                            if (isRemedial) {
                                String previous = previousGrades.get(enrollment.getCourseId() + "|" + enrollment.getStudentId());
                                System.out.println(enrollment.getCourseId() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade() + "(" + previous + ")");
                            } else {
                                System.out.println(enrollment.getCourseId() + "|" + enrollment.getStudentId() + "|" + enrollment.getAcademicYear() + "|" + enrollment.getSemester() + "|" + enrollment.getGrade());
                            }
                        }
                    }

                }
                //cetak best-student 
                
               
              
                
            }else if (command.equals("find-the-best-student")) {
                String year= data[1];
                String semester=data[2];

                
                double maxGPA = 0;
                String bestStudentId = "";
                String bestGradeA = "";
                String bestGradeB = "";
                for (Student student : students) {
                    ArrayList<Enrollment> studentEnrollments = new ArrayList<>();
                    for (Enrollment enrollment : enrollments) {
                        if (enrollment.getStudentId().equals(student.getId()) &&
                                enrollment.getAcademicYear().equals(year) ) {
                            studentEnrollments.add(enrollment);
                        }
                    }

                    double gpa = calculateGPA(studentEnrollments, courses);
                    if (gpa > maxGPA) {
                        maxGPA = gpa;
                        bestStudentId = student.getId();
                        for (Enrollment e : enrollments) {
                            if (e.getStudentId().equals(bestStudentId) && e.getAcademicYear().equals(year)) {
                                bestGradeA = e.getGrade();
                            }
                            if (e.getStudentId().equals(bestStudentId) && e.getSemester().equals("odd")  && e.getAcademicYear().equals(year)) {
                                bestGradeB = e.getGrade();
                            }
                            
                        }
                    }
                }
             
               //cetak best-student
                System.out.println(bestStudentId + "|" + bestGradeB + "/" +bestGradeA);
              
             
            } 

            
        }

        scanner.close();
    }

    public static double calculateGPA(ArrayList<Enrollment> enrollments, ArrayList<Course> courses) {
        double totalGradePoints = 0;
        int totalCredits = 0;

        for (Course course : courses) {
            String courseId = course.getCode();
            String lastGrade = "";
            boolean hasGrade = false;

            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseId().equals(courseId)) {
                    lastGrade = enrollment.getGrade();
                    hasGrade = true;
                }
            }


            if (hasGrade) {
                for (Course c : courses) {
                    if (c.getCode().equals(courseId)) {
                        String grade = lastGrade;
                        int credits = Integer.parseInt(c.getCredits());
                        totalCredits += credits;

                        switch (grade) {
                            case "A":
                                totalGradePoints += 4.0 * credits;
                                break;
                            case "AB":
                                totalGradePoints += 3.5 * credits;
                                break;
                            case "B":
                                totalGradePoints += 3.0 * credits;
                                break;
                            case "BC":
                                totalGradePoints += 2.5 * credits;
                                break;
                            case "C":
                                totalGradePoints += 2.0 * credits;
                                break;
                            case "D":
                                totalGradePoints += 1.0 * credits;
                                break;
                            case "E":
                                totalGradePoints += 0.0 * credits;
                                break;
                            default:
                                break;
                        }
                        break;
                    }
                }
            }
        }

        if (totalCredits == 0 || totalGradePoints == 0) {
            return 0;
        }

        return totalGradePoints / totalCredits;
    }

    public static int getTotalCredits(ArrayList<Enrollment> enrollments, ArrayList<Course> courses) {
        int totalCredits = 0;

        for (Course course : courses) {
            String courseId = course.getCode();
            String lastGrade = "";
            boolean hasValidGrade = false;

            for (Enrollment enrollment : enrollments) {
                if (enrollment.getCourseId().equals(courseId)) {
                    lastGrade = enrollment.getGrade();

                    if (!lastGrade.equals("None")) {
                        hasValidGrade = true;
                    }
                }
            }

            if (hasValidGrade) {
                for (Course c : courses) {
                    if (c.getCode().equals(courseId)) {
                        int credits = Integer.parseInt(c.getCredits());
                        totalCredits += credits;
                        break;
                    }
                }
            }
        }
        return totalCredits;
    }
}
