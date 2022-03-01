package com.nbchand.brs.component;

import com.nbchand.brs.dto.response.ResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.UUID;

/**
 * @author Narendra
 * @version 1.0
 * @since 2022-02-28
 */
@Slf4j
@Component
public class FileStorageComponent {

    public ResponseDto storeFile(MultipartFile multipartFile) {
        String directoryPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "WICC-UPLOADS";
        File directoryFile = new File(directoryPath);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if (extension.equalsIgnoreCase("jpg") ||
                extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("png")) {
            UUID uuid = UUID.randomUUID();
            String filePath = directoryPath + File.separator + uuid + "_" + multipartFile.getOriginalFilename();
            File fileToStore = new File(filePath);
            try {
                multipartFile.transferTo(fileToStore);
                return ResponseDto.builder()
                        .status(true)
                        .message(filePath)
                        .build();
            } catch (IOException exception) {
                return ResponseDto.builder()
                        .status(false)
                        .message("Could not read file")
                        .build();
            }
        } else {
            return ResponseDto.builder()
                    .status(false)
                    .message("Only jpg, jpeg and png format are supported")
                    .build();
        }
    }

    public String returnFileAsBase64(String filePath) {
        File file = new File(filePath);
        try{
            byte[] bytes = Files.readAllBytes(file.toPath());
            String base64EncodedImage = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            return base64EncodedImage;
        }catch (IOException exception){
            log.error(exception.getMessage());
            return null;
        }
    }

}
