//import javax.print.DocFlavor.URL;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class real_time extends JFrame implements ActionListener {

    private final Map<String, Double> selectedCryptos = new HashMap<>();
    private final JCheckBox[] checkBoxes;
    private final JLabel[] priceLabels;
    private final JButton startStopButton;
    private final JComboBox<String> refreshRateComboBox;
    private Timer timer;

    public real_time(String[] cryptocurrencies) {
        super("real time crypto tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(cryptocurrencies.length + 2, 2));

        checkBoxes = new JCheckBox[cryptocurrencies.length];
        priceLabels = new JLabel[cryptocurrencies.length];

        for (int i = 0; i < cryptocurrencies.length; i++) {
            checkBoxes[i] = new JCheckBox(cryptocurrencies[i]);
            priceLabels[i] = new JLabel("Fetching...");

            add(checkBoxes[i]);
            add(priceLabels[i]);
        }

        startStopButton = new JButton("Start");
        startStopButton.addActionListener(this);

        String[] refreshRates = {"1 second", "5 seconds", "10 seconds", "30 seconds", "1 minute"};
        refreshRateComboBox = new JComboBox<>(refreshRates);

        add(startStopButton);
        add(refreshRateComboBox);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startStopButton) {
            if (timer == null) {
                startStopButton.setText("Stop");
                startTimer();
            } else {
                startStopButton.setText("Start");
                stopTimer();
            }
        }
    }

    private void startTimer() {
        String refreshRateStr = (String) refreshRateComboBox.getSelectedItem();
        int refreshRate = parseRefreshRate(refreshRateStr);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                updatePrices();
            }
        }, 0, refreshRate * 1000);
    }

    private int parseRefreshRate(String refreshRateStr) {
        int seconds = Integer.parseInt(refreshRateStr.split(" ")[0]);
        return seconds;
    }

    private void stopTimer() {
        timer.cancel();
        timer = null;
    }

    private void updatePrices() {
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isSelected()) {
                String cryptoName = checkBoxes[i].getText();
                double price = fetchPriceFromAPI(cryptoName);
                selectedCryptos.put(cryptoName, price);
                priceLabels[i].setText(String.format("$%.2f", price));
            }
        }
    }

    private double fetchPriceFromAPI(String cryptoName) {
        // Replace the following API_URL with your chosen API's endpoint for real-time price data.
        String API_URL = "https://api.coingecko.com/api/v3/simple/price?ids=" + cryptoName.toLowerCase() + "&vs_currencies=usd";
        // Perform API call and return the price.

        // For simplicity, let's assume a fixed value here.
        // In a real application, you would make an HTTP request to the API to fetch the real-time price.
        return Math.random() * 1000;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String[] cryptocurrencies = {"Bitcoin", "Ethereum", "Ripple", "Litecoin"};
            new real_time(cryptocurrencies);
        });
    }
}

