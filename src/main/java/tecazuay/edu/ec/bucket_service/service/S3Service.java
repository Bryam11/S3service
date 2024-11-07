package tecazuay.edu.ec.bucket_service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.core.async.AsyncRequestBody;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class S3Service {

    private static final Logger log = LoggerFactory.getLogger(S3Service.class);
    @Value("${spring.cloud.config.s3.bucket.default}")
    private String bucketName;
    private final S3AsyncClient s3AsyncClient;

    public Mono<Void> uploadToS3(FilePart filePart, String fileName) {
        PutObjectRequest putObjectRequest = (PutObjectRequest) PutObjectRequest.builder().bucket(this.bucketName).key(fileName).build();
        return filePart.content().reduce((rec$, xva$0) -> {
            return ((DataBuffer) rec$).write(new DataBuffer[]{xva$0});
        }).flatMap((dataBuffer) -> {
            byte[] bytes = new byte[dataBuffer.readableByteCount()];
            dataBuffer.read(bytes);
            AsyncRequestBody requestBody = AsyncRequestBody.fromBytes(bytes);
            return Mono.fromFuture(() -> {
                return this.s3AsyncClient.putObject(putObjectRequest, requestBody);
            });
        }).then();
    }

    public String folder(String folderName) {
        PutObjectRequest putObjectRequest = (PutObjectRequest) PutObjectRequest.builder().bucket(this.bucketName).key(folderName + "/").build();
        String objectContent = "";
        CompletableFuture<PutObjectResponse> future = this.s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromByteBuffer(ByteBuffer.wrap(objectContent.getBytes())));
        PutObjectResponse resp = (PutObjectResponse) future.join();
        return resp.toString();
    }

    public String uploadToS3(String path, String name, File file, String... metadata) {
        PutObjectRequest putObjectRequest = null;
        if (null != metadata && metadata.length > 0) {
            Map<String, String> metamap = (Map) Arrays.stream(metadata).map((entry) -> {
                return entry.split(":");
            }).collect(Collectors.toMap((parts) -> {
                return parts[0];
            }, (parts) -> {
                return parts[1];
            }));
            putObjectRequest = (PutObjectRequest) PutObjectRequest.builder().bucket(this.bucketName).key(path + name).metadata(metamap).build();
        } else {
            putObjectRequest = (PutObjectRequest) PutObjectRequest.builder().bucket(this.bucketName).key(name).build();
        }

        CompletableFuture<PutObjectResponse> future = this.s3AsyncClient.putObject(putObjectRequest, AsyncRequestBody.fromFile(file));
        PutObjectResponse resp = (PutObjectResponse) future.join();
        return resp.toString();
    }

    public S3Service(S3AsyncClient s3AsyncClient) {
        this.s3AsyncClient = s3AsyncClient;
    }
}