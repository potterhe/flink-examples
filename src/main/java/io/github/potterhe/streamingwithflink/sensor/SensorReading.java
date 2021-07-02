package io.github.potterhe.streamingwithflink.sensor;

/**
 * POJO to hold sensor reading data.
 * p115
 * 是一个公有类
 * 有一个公有的无参数默认构造函数
 * 所有字段都是公有或提供了相应的getter及setter方法
 * 所有字段类型都必须是Flink所支持的
 */
public class SensorReading {

    public String id;
    public long timestamp;
    public double temperature;

    /**
     * Empty default constructor to satify Flink's POJO requirements.
     */
    public SensorReading(){}

    public SensorReading(String id, long timestamp, double temperature) {
        this.id = id;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        return "SensorReading{" +
                "id='" + id + '\'' +
                ", timestamp=" + timestamp +
                ", temperature=" + temperature +
                '}';
    }
}
