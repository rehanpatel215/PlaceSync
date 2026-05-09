package backend.models;

public class JobApplication {
    private int applicationId;
    private String companyName;
    private String jobRole;
    private String status;

    public JobApplication(int applicationId, String companyName, String jobRole, String status) {
        this.applicationId = applicationId;
        this.companyName = companyName;
        this.jobRole = jobRole;
        this.status = status;
    }

    // Getters
    public int getApplicationId() { return applicationId; }
    public String getCompanyName() { return companyName; }
    public String getJobRole() { return jobRole; }
    public String getStatus() { return status; }
}
