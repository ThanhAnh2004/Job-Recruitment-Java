package thanhanh.job_recruitment.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import thanhanh.job_recruitment.dto.response.File.UploadFileResponse;
import thanhanh.job_recruitment.service.impl.FileService;
import thanhanh.job_recruitment.util.annotation.ApiMessage;
import thanhanh.job_recruitment.util.exception.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/files")
public class FileController {

    private final FileService fileService;

    @Value("${upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    @ApiMessage("Upload a single file")
    public ResponseEntity<UploadFileResponse> upload(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("folder") String folder
            ) throws URISyntaxException, IOException, StorageException {
        // skip validate
        if (file == null || file.isEmpty()){
            throw new StorageException("File is empty. Please upload a file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList(
                "pdf",
                "jpg",
                "jpeg",
                "png",
                "doc",
                "docx"
        );


        // create a directory if not exists
        this.fileService.createDirectory(baseURI + folder);
        // store file
        String uploadFile = this.fileService.storeFile(file, folder);
        boolean isValid = allowedExtensions.stream().anyMatch(item
                -> fileName.toLowerCase().endsWith(item));

        if (!isValid) {
            throw new StorageException(
                    "Invalid file extension. only allows " + allowedExtensions.toString()
            );
        }


        UploadFileResponse response = new UploadFileResponse(uploadFile, Instant.now());

        return ResponseEntity.ok().body(response);
    }
}
