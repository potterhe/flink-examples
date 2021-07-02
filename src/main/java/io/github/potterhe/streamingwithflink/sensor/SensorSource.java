package io.github.potterhe.streamingwithflink.sensor;

import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;

import java.util.Calendar;
import java.util.Random;

public class SensorSource extends RichParallelSourceFunction<SensorReading> {

    private boolean running = true;

    @Override
    public void run(SourceContext<SensorReading> ctx) throws Exception {
        Random rand = new Random();
        int nSensor = 10;

        // initialize sensor ids and temperatures
        int taskIdx = this.getRuntimeContext().getIndexOfThisSubtask();
        String[] sensorIds = new String[nSensor];
        double[] curFTemp = new double[nSensor];
        for (int i = 0; i < nSensor; i++) {
            sensorIds[i] = "sensor_" + (taskIdx * 10 + i);
            curFTemp[i] = 65 + (rand.nextGaussian() * 20);
        }

        while (running) {
            long currTime = Calendar.getInstance().getTimeInMillis();
            for (int i = 0; i < 10; i++) {
                // update current temperature
                curFTemp[i] += rand.nextGaussian() * 0.5;
                ctx.collect(new SensorReading(sensorIds[i], currTime, curFTemp[i]));
            }
            Thread.sleep(1000L);
        }
    }

    @Override
    public void cancel() {
        running = false;
    }
}
