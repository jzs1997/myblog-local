package proj.blog.myblog.configs;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Auditable;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import proj.blog.myblog.models.Post;

@Configuration
@EnableJpaAuditing
@EnableMongoAuditing  // Audition setting, for recording create time
public class AppConfig {
    // MongoDB configurations
    @Value("${my-mongo-config.mongo-db}")
    String databaseName;

    //User MongoClient to build a connection to MongoDB
    @Bean
    public MongoClientFactoryBean mongo(){
        // autowire MongoClient to access the MongoClient
        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        mongo.setHost("localhost");
        return mongo;
    }

    // Register a MongoDatabaseFactory for database connection
    @Bean
    public MongoDatabaseFactory mongoDatabaseFactory(){
        return new SimpleMongoClientDatabaseFactory(MongoClients.create(), databaseName);
    }
}
