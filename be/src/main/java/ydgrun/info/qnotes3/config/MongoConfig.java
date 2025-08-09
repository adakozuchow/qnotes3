package ydgrun.info.qnotes3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;

@Configuration
@EnableMongoRepositories(basePackages = "ydgrun.info.qnotes3.repository")
public class MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        return new MongoCustomConversions(Arrays.asList(
            new OffsetDateTimeToDateConverter(),
            new DateToOffsetDateTimeConverter()
        ));
    }

    private static class OffsetDateTimeToDateConverter implements Converter<OffsetDateTime, Date> {
        @Override
        public Date convert(OffsetDateTime source) {
            return source == null ? null : Date.from(source.toInstant());
        }
    }

    private static class DateToOffsetDateTimeConverter implements Converter<Date, OffsetDateTime> {
        @Override
        public OffsetDateTime convert(Date source) {
            return source == null ? null : source.toInstant().atOffset(ZoneOffset.UTC);
        }
    }
}