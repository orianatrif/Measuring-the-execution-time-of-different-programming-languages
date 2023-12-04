import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.*;

public class View extends JFrame {

    private JCheckBox checkBoxJava;
    private JCheckBox checkBoxC;
    private JCheckBox checkBoxCPP;
    private JCheckBox checkBoxPython;
    private JButton btnUpdateChart;
    private JComboBox<String> comboBox;
    private JLabel labelJava;
    private JLabel labelC;
    private JLabel labelCPP;
    private JLabel labelPython;

    private JFreeChart chart;

    public View(String title, int startLine) {
        super(title);

        checkBoxJava = new JCheckBox("Java");
        checkBoxC = new JCheckBox("C");
        checkBoxCPP = new JCheckBox("C++");
        checkBoxPython = new JCheckBox("Python");

        btnUpdateChart = new JButton("Update Chart");

        JPanel checkBoxPanel = new JPanel();
        checkBoxPanel.setLayout(new FlowLayout());
        checkBoxPanel.add(checkBoxJava);
        checkBoxPanel.add(checkBoxC);
        checkBoxPanel.add(checkBoxCPP);
        checkBoxPanel.add(checkBoxPython);
        checkBoxPanel.add(btnUpdateChart);

        comboBox = new JComboBox<>();
        comboBox.setModel(new DefaultComboBoxModel<>(new String[]{
                "Memory allocation", "Static memory access", "Dynamic memory access", "Thread creation",
                "Thread context switch", "Element addition to list", "Element removal from list", "List reversal"
        }));
        checkBoxPanel.add(comboBox);

        labelJava = new JLabel("Java:");
        labelC = new JLabel("C:");
        labelCPP = new JLabel("C++:");
        labelPython = new JLabel("Python:");

        checkBoxPanel.add(labelJava);
        checkBoxPanel.add(labelC);
        checkBoxPanel.add(labelCPP);
        checkBoxPanel.add(labelPython);

        chart = createChart();
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(560, 370));

        setLayout(new BorderLayout());
        add(checkBoxPanel, BorderLayout.NORTH);
        add(chartPanel, BorderLayout.CENTER);

        btnUpdateChart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateChart();
                updateLabels();
            }
        });

        ItemListener itemListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateLabels();
            }
        };

        checkBoxJava.addItemListener(itemListener);
        checkBoxC.addItemListener(itemListener);
        checkBoxCPP.addItemListener(itemListener);
        checkBoxPython.addItemListener(itemListener);

        pack();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

        private JFreeChart createChart() {
    int nr = 0;
    CategoryDataset dataset = createDataset(false, false, false, false, nr);

    JFreeChart chart = ChartFactory.createBarChart(
            "Execution time",
            "Programming Language",
            "Time (nanosecond)",
            dataset
    );

    // Set the bar color to pink using a custom renderer
    BarRenderer renderer = new BarRenderer();
    renderer.setSeriesPaint(0, Color.PINK);
    chart.getCategoryPlot().setRenderer(renderer);

    return chart;
}

    private CategoryDataset createDataset(boolean isJavaChecked, boolean isCChecked, boolean isCPPChecked, boolean isPythonChecked, int nr) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        if (isJavaChecked) addDataFromFile(dataset, "Category Java", "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_java/java_results.txt", nr);
        if (isCChecked) addDataFromFile(dataset, "Category C", "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c/cmake-build-debug/c_results.txt", nr);
        if (isCPPChecked) addDataFromFile(dataset, "Category C++", "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c++/cmake-build-debug/c++_results.txt", nr);
        if (isPythonChecked) addDataFromFile(dataset, "Category Python", "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_python/python_results.txt", nr);

        return dataset;
    }

    private void addDataFromFile(DefaultCategoryDataset dataset, String category, String filePath, int nr) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < nr - 1; i++) {
                br.readLine();
            }
            String line = br.readLine();
            if (line != null) {
                double value = Double.parseDouble(line.trim());
                int nrList = 0;
                String rowKey = "";
                if (nrList == 0) {
                    rowKey = "Memory Allocation Time";
                } else if (nrList == 1) {
                    rowKey = "Static memory access";
                } else if (nrList == 2) {
                    rowKey = "Dynamic memory access";
                } else if (nrList == 3) {
                    rowKey = "Thread Creation time";
                } else if (nrList == 4) {
                    rowKey = "Thread context switch";
                } else if (nrList == 5) {
                    rowKey = "Element addition to list";
                } else if (nrList == 6) {
                    rowKey = "Element removal from list";
                } else if (nrList == 7) {
                    rowKey = "List Reversal";
                }

                dataset.addValue(value, rowKey, category);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void updateChart() {
        boolean isJavaChecked = checkBoxJava.isSelected();
        boolean isCChecked = checkBoxC.isSelected();
        boolean isCPPChecked = checkBoxCPP.isSelected();
        boolean isPythonChecked = checkBoxPython.isSelected();
        int nr = getProcessInt();

        runExternalCodeC();
        runExternalCodeCPP();
        runExternalCodePython();
        runExternalCodeJava();

        CategoryDataset updatedDataset = createDataset(isJavaChecked, isCChecked, isCPPChecked, isPythonChecked, nr);

        ChartPanel chartPanel = (ChartPanel) getContentPane().getComponent(1);
        chart = chartPanel.getChart();  // Update the chart reference
        chart.getCategoryPlot().setDataset(updatedDataset);

        chartPanel.repaint();
    }

    public int getProcessInt() {
        switch (comboBox.getSelectedItem().toString()) {
            case "Memory allocation":
                return 1;
            case "Static memory access":
                return 2;
            case "Dynamic memory access":
                return 3;
            case "Thread creation":
                return 4;
            case "Thread context switch":
                return 5;
            case "Element addition to list":
                return 6;
            case "Element removal from list":
                return 7;
            case "List reversal":
                return 8;
            default:
                return 9;
        }
    }

    private void runExternalCodeC() {
        String commandC = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c/cmake-build-debug/scs_c";

        try {
            FileWriter fileWriter = new FileWriter("/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c/cmake-build-debug/c_results.txt");

            ProcessBuilder processBuilder = new ProcessBuilder(commandC);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                fileWriter.write(line + "\n");
            }

            fileWriter.close();

            int exitCode = process.waitFor();

            System.out.println("External process exited with code C: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runExternalCodeCPP() {
        String commandCPP = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c++/cmake-build-debug/scs_c__";

        try {
            FileWriter fileWriter = new FileWriter("/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c++/cmake-build-debug/c++_results.txt");

            ProcessBuilder processBuilder = new ProcessBuilder(commandCPP);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                fileWriter.write(line + "\n");
            }

            fileWriter.close();

            int exitCode = process.waitFor();

            System.out.println("External process exited with code CPP: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void runExternalCodeJava() {
         // Specify the path to your Java executable
        String javaCommand = "/usr/bin/java";

        // Specify the classpath and the full path to your Java program's main class
        String classpath = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_java/src/main/java";
        String mainClass = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_java/src/main/java/JavaMeasurements.java";

        try {
            // Create a FileWriter to write the output to a text file
            FileWriter fileWriter = new FileWriter("/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_java/java_results.txt");

            // Start the external process
            ProcessBuilder processBuilder = new ProcessBuilder(javaCommand, "-cp", classpath, mainClass);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // Get the InputStream to read the output from the external process
            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            // Read each line from the output and write it to the text file
            while ((line = reader.readLine()) != null) {
                fileWriter.write(line + "\n");
            }

            // Close the FileWriter
            fileWriter.close();

            // Wait for the process to finish
            int exitCode = process.waitFor();

            // Print the exit code
            System.out.println("External process exited with code Java: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        }
    }

    private void runExternalCodePython() {
        String pythonCommand = "/Library/Frameworks/Python.framework/Versions/3.12/bin/python3";
        String pythonScript = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_python/main.py";

        try {
            FileWriter fileWriter = new FileWriter("/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_python/python_results.txt");

            ProcessBuilder processBuilder = new ProcessBuilder(pythonCommand, pythonScript);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            InputStream inputStream = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                fileWriter.write(line + "\n");
            }

            fileWriter.close();

            int exitCode = process.waitFor();

            System.out.println("External process exited with code Python: " + exitCode);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void updateLabels() {
        boolean isJavaChecked = checkBoxJava.isSelected();
        boolean isCChecked = checkBoxC.isSelected();
        boolean isCPPChecked = checkBoxCPP.isSelected();
        boolean isPythonChecked = checkBoxPython.isSelected();
        int nr = getProcessInt();

        labelJava.setText("Java: " + getValueForLanguage("Java", nr, isJavaChecked));
        labelC.setText("C: " + getValueForLanguage("C", nr, isCChecked));
        labelCPP.setText("C++: " + getValueForLanguage("C++", nr, isCPPChecked));
        labelPython.setText("Python: " + getValueForLanguage("Python", nr, isPythonChecked));
    }

    private String getValueForLanguage(String language, int nr, boolean isChecked) {
        if (!isChecked) {
            return "";
        }

        String filePath = "";
        switch (language) {
            case "Java":
                filePath = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_java/java_results.txt";
                break;
            case "C":
                filePath = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c/cmake-build-debug/c_results.txt";
                break;
            case "C++":
                filePath = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_c++/cmake-build-debug/c++_results.txt";
                break;
            case "Python":
                filePath = "/Users/triforiana/Documents/facultate/scs/proiect/proiect/scs_python/python_results.txt";
                break;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            for (int i = 0; i < nr - 1; i++) {
                br.readLine();
            }
            String line = br.readLine();
            if (line != null) {
                double value = Double.parseDouble(line.trim());
                return String.valueOf(value);
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            int startLine = 3;
            View example = new View("JFreeChart Example", startLine);
             Controller controller = new Controller(example);
        });
    }
}
