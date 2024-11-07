package tecazuay.edu.ec.bucket_service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tecazuay.edu.ec.bucket_service.service.S3Service;
import tecazuay.edu.ec.bucket_service.service.UploadService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UploadServiceImpl implements UploadService {

    private final S3Service s3Service;

    @Override
    public Mono<ResponseEntity<String>> requirementReception(Flux<FilePart> fileParts) {
        return fileParts.hasElements()
                .flatMap(hasElement -> {
                    if (hasElement) {
                        return sendMessageFilesToBucket(fileParts)
                                .then(Mono.just(ResponseEntity.ok("{\"message\": \"Files uploaded successfully\"}")));
                    } else {
                        return Mono.just(ResponseEntity.badRequest().body("{\"error\": \"No files to upload\"}"));
                    }
                });
    }

    private Mono<Void> sendMessageFilesToBucket(Flux<FilePart> fileparts) {
        List<String> fileNames = new ArrayList<>();
        return fileparts.flatMap(part -> {
            // Get the original file name with its extension
            String fileName = part.filename();
            String ext = "pdf";
            int lastIndex = fileName.lastIndexOf('.');
            if (lastIndex >= 0 && lastIndex < fileName.length() - 1) {
                ext = fileName.substring(lastIndex + 1);
            }
            // Generate a random name
            String s3FileName = UUID.randomUUID() + "." + ext;
            fileNames.add(s3FileName + ">" + fileName);
            log.info("Uploading file: " + s3FileName);
            // Upload the file to S3
            return s3Service.uploadToS3(part, s3FileName)
                    .doOnSuccess(success -> log.info("File uploaded successfully: " + s3FileName))
                    .onErrorResume(error -> {
                        log.error("Error uploading file: " + s3FileName, error);
                        return Mono.empty();
                    });
        }).then();
    }
}