package upatras.smartdevices.gridcontrol;

import org.joda.time.Duration;
import org.junit.Test;
import upatras.utilitylibrary.library.measurements.types.Power;
import upatras.utilitylibrary.library.measurements.units.electrical.ComplexPower;

/**
 * Created by Paris on 25-Jun-18.
 */
public class PowerGenerationControllerTest {
    @Test
    public void testfunction() {
        ComplexPower current_power = new ComplexPower(1000, 0);
        ComplexPower remaining_power = new ComplexPower(1000, 0);
        ComplexPower ramp_speed = new ComplexPower(0.4, 0);

        double step_s = Duration.standardSeconds(60).getMillis() / 1000.0;
        double power = new Power(10000).value;
        double l = ramp_speed.real.value * 1000.0;

        double ramp_s = step_s + Math.sqrt(step_s * step_s - 2 * power * step_s / l);
        double ramp_s2 = step_s - Math.sqrt(step_s * step_s - 2 * power * step_s / l);
        double final_power = ramp_s * l;
        double average_power = l * ramp_s * ramp_s / 2 / step_s + l * ramp_s * (step_s - ramp_s) / step_s;

        System.out.println("test");
    }
}