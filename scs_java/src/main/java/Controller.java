import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {

  //  private final MainPageView viewMain;
    private final View view;

    public Controller( View view) {
       // this.viewMain = viewMain;
        this.view = view;

       // this.viewMain.addCalculate(new CalculateListener());
    }

    class CalculateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            // When the button is pressed, perform calculations and update the chart
            performCalculations();
            updateChart();
        }
    }

    private void performCalculations() {

        int numberOfAllocations = 1000;
        long totalTimeAlloc = 0;

        view.updateChart();
    }

    private void updateChart() {

        view.updateChart();
    }
}
