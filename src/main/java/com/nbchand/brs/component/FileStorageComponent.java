package com.nbchand.brs.component;

import com.nbchand.brs.dto.book.BookDto;
import com.nbchand.brs.dto.response.ResponseDto;
import com.nbchand.brs.entity.book.Book;
import com.nbchand.brs.repository.book.BookRepo;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
public class FileStorageComponent {

    private final BookRepo bookRepo;
    private final String DUMMYIMAGE = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
            + File.separator + "resources" + File.separator + "static" + File.separator + "image"
            + File.separator + "dummy.jpg";

    public ResponseDto storeFile(BookDto bookDto) {

        MultipartFile multipartFile = bookDto.getPhoto();

        //return status as 'true' and set book image as 'dummy image' if
        //book is being created for the first time and user uploaded no image
        if (bookDto.getId() == null && multipartFile.isEmpty()) {
            return ResponseDto.builder()
                    .status(true)
                    .message(DUMMYIMAGE)
                    .build();
        }
        //if book was edited and user didn't upload any image return null
        if(bookDto.getId() != null && multipartFile.isEmpty()) {
            return null;
        }
        //stores file at a location and sends the location as message
        String directoryPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + "WICC-UPLOADS";
        File directoryFile = new File(directoryPath);
        if (!directoryFile.exists()) {
            directoryFile.mkdirs();
        }

        //extracts multipart file extension
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        //checks the extension of the file
        if (extension.equalsIgnoreCase("jpg") ||
                extension.equalsIgnoreCase("jpeg") ||
                extension.equalsIgnoreCase("png")) {

            //gives unique name to the file
            UUID uuid = UUID.randomUUID();
            String filePath = directoryPath + File.separator + uuid + "_" + multipartFile.getOriginalFilename();
            File fileToStore = new File(filePath);

            //copies multipart file content to the file at given location
            try {
                multipartFile.transferTo(fileToStore);
                return ResponseDto.builder()
                        .status(true)
                        .message(filePath)
                        .build();
            }
            //file can't be read
            catch (IOException exception) {
                return ResponseDto.builder()
                        .status(false)
                        .message("Could not read file")
                        .build();
            }
        }
        //file can't be stored because of extension issues
        else {
            return ResponseDto.builder()
                    .status(false)
                    .message("Only jpg, jpeg and png format are supported")
                    .build();
        }
    }

    public String returnFileAsBase64(String filePath) {
        File file = new File(filePath);
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            String base64EncodedImage = "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
            return base64EncodedImage;
        } catch (IOException exception) {
            log.error(exception.getMessage());
            return null;
        }
    }

    public Boolean isPhotoChanged(Book book, Book prevBook) {
        //book photo was not changed if
        //book is being created for the first time
        if(book.getId()==null) {
            return false;
        }
        String prevPhoto = prevBook.getPhoto();
        String newPhoto = book.getPhoto();

        //if previous photo was dummy image user is uploading image for the first time
        if(prevPhoto.equals(DUMMYIMAGE)) {
            return false;
        }
        return !(prevPhoto.equals(newPhoto));
    }

    public void deletePhoto(String photoLocation) {
        File file = new File(photoLocation);
        log.info("Book photo deletion: "+file.delete());
    }

    public Boolean isImageDummy(String image) {
        return image.equals(DUMMYIMAGE);
    }
}
