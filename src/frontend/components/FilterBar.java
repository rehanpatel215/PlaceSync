package frontend.components;

import frontend.theme.Theme;
import javax.swing.*;
import java.awt.*;
import java.util.function.Consumer;

public class FilterBar extends JPanel {
    private StyledTextField companyField;
    private StyledTextField roleField;
    private StyledTextField packageField;
    private RoundedButton searchBtn;
    private Consumer<FilterData> onSearch;

    public static class FilterData {
        public String company;
        public String role;
        public double minPackage;
    }

    public FilterBar(Consumer<FilterData> onSearch) {
        this.onSearch = onSearch;
        setLayout(new FlowLayout(FlowLayout.LEFT, 15, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Theme.BORDER_COLOR),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        companyField = new StyledTextField("Company Name...");
        companyField.setPreferredSize(new Dimension(180, 35));
        
        roleField = new StyledTextField("Job Role...");
        roleField.setPreferredSize(new Dimension(180, 35));
        
        packageField = new StyledTextField("Min Package (LPA)...");
        packageField.setPreferredSize(new Dimension(150, 35));

        searchBtn = new RoundedButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 35));
        searchBtn.setBackground(Theme.PRIMARY_TEAL);
        searchBtn.addActionListener(e -> triggerSearch());

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(Theme.FONT_SMALL);
        clearBtn.setForeground(Theme.TEXT_SECONDARY);
        clearBtn.setBorderPainted(false);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            companyField.setText("");
            roleField.setText("");
            packageField.setText("");
            triggerSearch();
        });

        add(new JLabel("Filters:"));
        add(companyField);
        add(roleField);
        add(packageField);
        add(searchBtn);
        add(clearBtn);
    }

    private void triggerSearch() {
        FilterData data = new FilterData();
        data.company = companyField.getText().trim();
        if (data.company.equals("Company Name...")) data.company = "";
        
        data.role = roleField.getText().trim();
        if (data.role.equals("Job Role...")) data.role = "";

        String pkgStr = packageField.getText().trim();
        if (pkgStr.equals("Min Package (LPA)...")) pkgStr = "";
        
        try {
            data.minPackage = pkgStr.isEmpty() ? 0 : Double.parseDouble(pkgStr);
        } catch (NumberFormatException e) {
            data.minPackage = 0;
        }
        
        onSearch.accept(data);
    }
}
