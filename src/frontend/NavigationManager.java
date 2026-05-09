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
        mainFrame.refreshAdminPanels();
        mainFrame.showPanel("ADMIN_DASHBOARD");
    }

    public static void showStudentDashboard() {
        mainFrame.refreshStudentPanels();
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

    public static void showJobDiscovery() {
        mainFrame.refreshStudentPanels();
        mainFrame.showPanel("JOB_DISCOVERY");
    }

    public static void showProfile() {
        mainFrame.refreshStudentPanels();
        mainFrame.showPanel("STUDENT_PROFILE");
    }

    public static void showSettings() {
        if (backend.auth.AuthManager.getInstance().isAdmin()) {
            mainFrame.refreshAdminPanels();
        } else {
            mainFrame.refreshStudentPanels();
        }
        mainFrame.showPanel("SETTINGS");
    }
}


