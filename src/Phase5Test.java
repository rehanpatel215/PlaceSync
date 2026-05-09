import backend.queries.StudentDAO;
import backend.models.Job;
import backend.models.JobApplication;
import java.util.List;

public class Phase5Test {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();
        System.out.println("--- Phase 5: Student Module Logic Test ---");

        // 1. Test getAvailableJobs
        System.out.println("\n[1] Testing getAvailableJobs...");
        List<Job> jobs = dao.getAvailableJobs();
        if (!jobs.isEmpty()) {
            System.out.println("✅ Found " + jobs.size() + " jobs.");
            for (Job j : jobs) {
                System.out.println(" - " + j.getRole() + " at " + j.getCompanyName() + " (Min CGPA: " + j.getEligibilityCGPA() + ")");
            }
        } else {
            System.out.println("❓ No jobs found in database. Please seed some data.");
        }

        // 2. Test getApplications (for student_id 1, assuming it exists)
        System.out.println("\n[2] Testing getApplicationsByStudentId(1)...");
        List<JobApplication> apps = dao.getApplicationsByStudentId(1);
        System.out.println("✅ Found " + apps.size() + " applications for Student ID 1.");
        for (JobApplication app : apps) {
            System.out.println(" - " + app.getJobRole() + " @ " + app.getCompanyName() + " | Status: " + app.getStatus());
        }

        System.out.println("\n-------------------------------------------");
    }
}
