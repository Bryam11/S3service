package tecazuay.edu.ec.bucket_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClientBuilder;

@Configuration
public class S3Configuration {

    @Value("${spring.cloud.aws.credentials.profile.name}")
    String profileAws;
    @Value("${spring.cloud.aws.region.static}")
    String region;

    public S3Configuration() {
    }

    @Bean
    public S3AsyncClient s3client() {
        return (S3AsyncClient)((S3AsyncClientBuilder)((S3AsyncClientBuilder)S3AsyncClient.builder().region(Region.of(this.region))).credentialsProvider(ProfileCredentialsProvider.create(this.profileAws))).build();
    }
}
