package io.confluent.kafka.jms;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.jms.Connection;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import org.apache.kafka.common.serialization.ByteArraySerializer;

public class MyKafkaConnectionFactory extends KafkaConnectionFactory {

    public MyKafkaConnectionFactory(Properties properties) {
        super(properties);
    }

    @Override
    public JMSContext createContext() {
        return null;
    }

    @Override
    public JMSContext createContext(String userName, String password) {
        return null;
    }

    @Override
    public JMSContext createContext(String userName, String password, int sessionMode) {
        return null;
    }

    @Override
    public JMSContext createContext(int sessionMode) {
        return null;
    }

    @Override
    public Connection createConnection() throws JMSException {
        KafkaConnection connection = (KafkaConnection) super.createConnection();
        Map<String, Object> producerProperties = new HashMap<>(this.config.producer);
        producerProperties.put("value.serializer", ByteArraySerializer.class);
        producerProperties.put("key.serializer", ByteArraySerializer.class);
        connection.producerFactory = new ProducerFactoryImpl(producerProperties);
        return connection;
    }

}
