package tecazuay.edu.ec.bucket_service.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Description;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tecazuay.edu.ec.bucket_service.service.UploadService;

@RestController
@Description("Controller for S3 Bucket")
@Tag(name = "s3-bucket")
@RequestMapping("/s3")
@RequiredArgsConstructor
public class S3Controller {

    private final UploadService uploadService;

    @Operation(summary = "Upload file", description = "Upload file to S3 Bucket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "400", description = "Not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))}),
            @ApiResponse(responseCode = "500", description = "Internal error", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = String.class))})
    })
    @PostMapping(value = "/send", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public Mono<ResponseEntity<String>> mailReception(
            @Parameter(name = "file", description = "This is for attaching one or more files into the data form")
            @RequestPart(value = "file", required = false)
            Flux<FilePart> fileParts) {
        return uploadService.requirementReception(fileParts);
    }
}
