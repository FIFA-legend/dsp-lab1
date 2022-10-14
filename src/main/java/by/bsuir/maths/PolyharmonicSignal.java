package by.bsuir.maths;

import java.util.ArrayList;
import java.util.List;

public class PolyharmonicSignal implements Signal {

    private final int n;

    private final int[] amplitudes;

    private final int[] frequencies;

    private final double[] phases;

    public PolyharmonicSignal(int n, int[] amplitudes, int[] frequencies, double[] phases) {
        this.n = n;
        this.amplitudes = amplitudes;
        this.frequencies = frequencies;
        this.phases = phases;
    }

    @Override
    public List<Point> calculate() {
        List<Point> points = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < amplitudes.length; j++) {
                sum += amplitudes[j] * Math.sin((2 * Math.PI * frequencies[j] * i) / n + phases[j]);
            }
            points.add(new Point(i, sum));
        }
        return points;
    }
}
