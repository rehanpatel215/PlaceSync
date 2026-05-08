package backend.models;

public class Company {
    private int id;
    private String name;
    private String industry;
    private String website;
    private String contactEmail;

    public Company(int id, String name, String industry, String website, String contactEmail) {
        this.id = id;
        this.name = name;
        this.industry = industry;
        this.website = website;
        this.contactEmail = contactEmail;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getIndustry() { return industry; }
    public String getWebsite() { return website; }
    public String getContactEmail() { return contactEmail; }
}
