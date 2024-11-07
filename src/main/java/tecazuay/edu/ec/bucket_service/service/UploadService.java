package tecazuay.edu.ec.bucket_service.service;

import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UploadService {

    Mono<ResponseEntity<String>> requirementReception(Flux<FilePart> fileparts);
}
