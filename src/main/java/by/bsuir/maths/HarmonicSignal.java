package by.bsuir.maths;

import java.util.ArrayList;
import java.util.List;

public class HarmonicSignal implements Signal {

    private final int amplitude;

    private final int frequency;

    private final int n;

    private final double phase;

    public HarmonicSignal(int amplitude, int frequency, int n, double phase) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.n = n;
        this.phase = phase;
    }

    @Override
    public List<Point> calculate() {
        List<Point> points = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double y = amplitude * Math.sin((2 * Math.PI * frequency * i) / n + phase);
            points.add(new Point(i, y));
        }
        return points;
    }
}
