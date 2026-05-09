package backend.models;

public class Job {
    private int id;
    private int companyId;
    private String companyName; // Added
    private String role;
    private String description;
    private double packageLPA;
    private double eligibilityCGPA;
    private String deadline;

    public Job(int id, int companyId, String companyName, String role, String description, double packageLPA, double eligibilityCGPA, String deadline) {
        this.id = id;
        this.companyId = companyId;
        this.companyName = companyName;
        this.role = role;
        this.description = description;
        this.packageLPA = packageLPA;
        this.eligibilityCGPA = eligibilityCGPA;
        this.deadline = deadline;
    }

    // Getters
    public int getId() { return id; }
    public int getCompanyId() { return companyId; }
    public String getCompanyName() { return companyName; } // Added
    public String getRole() { return role; }
    public String getDescription() { return description; }
    public double getPackageLPA() { return packageLPA; }
    public double getEligibilityCGPA() { return eligibilityCGPA; }
    public String getDeadline() { return deadline; }
}
