import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MemoryAlloc {

    public static void main(String[] args) {

        // MainPageView mainView = new MainPageView();
        View view = new View("Measurements", 2);  // Pass the JComboBox to the View
        Controller controller = new Controller(view);

    }
}
