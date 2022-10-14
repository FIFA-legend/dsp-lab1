package by.bsuir;

import by.bsuir.maths.HarmonicSignal;
import by.bsuir.maths.Point;
import by.bsuir.maths.PolyharmonicLinearSignal;
import by.bsuir.maths.PolyharmonicSignal;
import by.bsuir.maths.Signal;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainController {

    @FXML
    private RadioButton harmonicRadio;

    @FXML
    private RadioButton polyharmonicRadio;

    @FXML
    private RadioButton polyharmonicLinearRadio;

    @FXML
    private ComboBox<Integer> nComboBox;

    @FXML
    private Label constantLabel;

    @FXML
    private ComboBox<String> constantComboBox;

    @FXML
    private Button buildButton;

    @FXML
    private LineChart<String, Number> chart;

    @FXML
    private CategoryAxis xAxis;

    private final String AMPLITUDE_FREQUENCY = "Амплитуда и частота (A и f)";
    private final String AMPLITUDE_PHASE = "Амплитуда и фаза (A и fi)";
    private final String FREQUENCY_PHASE = "Фаза и частота (fi и а)";

    @FXML
    void initialize() {
        xAxis.setLabel("N");
        chart.setAnimated(false);

        initializeN();
        initializeConstants();

        harmonicRadio.setOnAction(handler -> disableLabelsAndComboBoxes(false));
        polyharmonicRadio.setOnAction(handler -> disableLabelsAndComboBoxes(true));
        polyharmonicLinearRadio.setOnAction(handler -> disableLabelsAndComboBoxes(true));

        buildButton.setOnAction(handler -> processButton());
    }

    private void processButton() {
        if (harmonicRadio.isSelected()) {
            String constant = constantComboBox.getValue();
            if (constant.equals(AMPLITUDE_FREQUENCY)) processAmplitudeFrequencyConstant();
            else if (constant.equals(AMPLITUDE_PHASE)) processAmplitudePhaseConstant();
            else processFrequencyPhaseConstant();
        } else if (polyharmonicRadio.isSelected()) {
            processPoly();
        } else {
            processPolyLinear();
        }
    }

    private void processPolyLinear() {
        int n = nComboBox.getValue();
        int amplitude = 9;
        int frequency = 1;
        double[] phases = new double[]{100, 120};

        PolyharmonicLinearSignal signal = new PolyharmonicLinearSignal(n, amplitude, frequency, phases);
        String start = signal.parameters();
        List<Signal> signals = List.of(signal);
        List<String> names = List.of(start);
        drawGraphic(signals, names);
    }

    private void processPoly() {
        int n = nComboBox.getValue();
        int[] amplitudes = new int[]{9, 9, 9, 9, 9};
        int[] frequencies = new int[]{1, 2, 3, 4, 5};
        double[] phases = new double[]{Math.PI / 2, 0, Math.PI / 4, Math.PI / 3, Math.PI / 6};

        Signal signal = new PolyharmonicSignal(n, amplitudes, frequencies, phases);
        List<Signal> signals = List.of(signal);
        List<String> names = List.of("Полигармонический");
        drawGraphic(signals, names);
    }

    private void processAmplitudeFrequencyConstant() {
        int n = nComboBox.getValue();
        int amplitude = 7;
        int frequency = 5;
        List<Double> phases = List.of(Math.PI, 0d, Math.PI / 3, Math.PI / 6, Math.PI / 2);

        List<Signal> signals = phases.stream()
                .map(phase -> new HarmonicSignal(amplitude, frequency, n, phase))
                .collect(Collectors.toList());
        List<String> names = List.of("fi = pi", "fi = 0", "fi = pi / 3", "fi = pi / 6", "fi = pi / 2");
        drawGraphic(signals, names);
    }

    private void processAmplitudePhaseConstant() {
        int n = nComboBox.getValue();
        int amplitude = 5;
        double phase = 3 * Math.PI / 4;
        List<Integer> frequencies = List.of(1, 5, 11, 6, 3);

        List<Signal> signals = frequencies.stream()
                .map(frequency -> new HarmonicSignal(amplitude, frequency, n, phase))
                .collect(Collectors.toList());
        List<String> names = List.of("f = 1", "f = 5", "f = 11", "f = 6", "f = 3");
        drawGraphic(signals, names);
    }

    private void processFrequencyPhaseConstant() {
        int n = nComboBox.getValue();
        int frequency = 3;
        double phase = 3 * Math.PI / 4;
        List<Integer> amplitudes = List.of(1, 2, 11, 4, 2);

        List<Signal> signals = amplitudes.stream()
                .map(amplitude -> new HarmonicSignal(amplitude, frequency, n, phase))
                .collect(Collectors.toList());
        List<String> names = List.of("A = 1", "A = 2", "A = 11", "A = 4", "A = 2");
        drawGraphic(signals, names);
    }

    private void drawGraphic(List<Signal> signals, List<String> names) {
        chart.getData().removeAll(chart.getData());

        List<XYChart.Series<String, Number>> seriesList = IntStream.range(0, signals.size())
                .parallel()
                .mapToObj(i -> {
                    Signal signal = signals.get(i);
                    List<Point> point = signal.calculate();
                    String name = names.get(i);

                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    point.forEach(p -> series.getData().add(new XYChart.Data<>(String.valueOf(p.getX()), p.getY())));
                    series.setName(name);
                    return series;
                })
                .collect(Collectors.toList());
        chart.getData().addAll(seriesList);

        chart.getData().stream().parallel().forEach(series -> {
            series.getData().forEach(data -> {
                data.getNode().setScaleX(0.1);
                data.getNode().setScaleY(0.1);
            });
        });
    }

    private void initializeN() {
        nComboBox.getItems().addAll(512, 1024, 2048, 4096);
        nComboBox.setValue(512);
    }

    private void initializeConstants() {
        constantComboBox.getItems().addAll(AMPLITUDE_FREQUENCY, AMPLITUDE_PHASE, FREQUENCY_PHASE);
        constantComboBox.setValue(AMPLITUDE_FREQUENCY);
    }

    private void disableLabelsAndComboBoxes(boolean disable) {
        constantLabel.setDisable(disable);
        constantComboBox.setDisable(disable);
    }

}
