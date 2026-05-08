package frontend;

public class NavigationManager {
    private static MainFrame mainFrame;

    public static void setMainFrame(MainFrame frame) {
        mainFrame = frame;
    }

    public static void showLogin() {
        mainFrame.showPanel("LOGIN");
    }

    public static void showAdminDashboard() {
        mainFrame.showPanel("ADMIN_DASHBOARD");
    }

    public static void showStudentDashboard() {
        mainFrame.showPanel("STUDENT_DASHBOARD");
    }

    public static void showCompanyMgmt() {
        mainFrame.showPanel("COMPANY_MGMT");
    }

    public static void showJobMgmt() {
        mainFrame.showPanel("JOB_MGMT");
    }

    public static void showAppMgmt() {
        mainFrame.showPanel("APP_MGMT");
    }
}


