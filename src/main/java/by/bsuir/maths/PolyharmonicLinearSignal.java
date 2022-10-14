package by.bsuir.maths;

import java.util.ArrayList;
import java.util.List;

public class PolyharmonicLinearSignal implements Signal {

    private final int n;

    private double amplitude;

    private double frequency;

    private final double[] phases;

    public PolyharmonicLinearSignal(int n, double amplitude, double frequency, double[] phases) {
        this.n = n;
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phases = phases;
    }

    public String parameters() {
        return "A = " + amplitude + ", f = " + frequency + ", phases = [" + phases[0] + ", " + phases[1] + "]";
    }

    @Override
    public List<Point> calculate() {
        List<Point> points = new ArrayList<>(n);
        for (var i = 0; i < n; i++)
        {
            amplitude += 0.2 * i / n;
            frequency -= 0.1 * i / n;
            phases[0] += 0.05 * i / n;
            phases[1] -= 0.05 * i / n;

            double sum = 0;
            for (var j = 0; j < 2; j++)
            {
                sum += amplitude * Math.sin(2 * Math.PI * frequency * i / n + phases[j]);
            }
            points.add(new Point(i, sum));
        }
        return points;
    }
}
