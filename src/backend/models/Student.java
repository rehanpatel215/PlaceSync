package backend.models;

public class Student {
    private int studentId;
    private int userId;
    private String name;
    private String branch;
    private double cgpa;
    private String resumePath;
    private String placementStatus;

    public Student(int studentId, int userId, String name, String branch, double cgpa, String resumePath, String placementStatus) {
        this.studentId = studentId;
        this.userId = userId;
        this.name = name;
        this.branch = branch;
        this.cgpa = cgpa;
        this.resumePath = resumePath;
        this.placementStatus = placementStatus;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public double getCgpa() { return cgpa; }
    public void setCgpa(double cgpa) { this.cgpa = cgpa; }

    public String getResumePath() { return resumePath; }
    public void setResumePath(String resumePath) { this.resumePath = resumePath; }

    public String getPlacementStatus() { return placementStatus; }
    public void setPlacementStatus(String placementStatus) { this.placementStatus = placementStatus; }
}


