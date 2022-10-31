package com.pbl6.bookstore.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

/**
 * @author lkadai0801
 * @since 30/10/2022
 */
@Log4j2
public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = file.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            File dir = new File(uploadDir);
            for (var fileTmp : Objects.requireNonNull(dir.listFiles())){
                if (!fileTmp.isDirectory()){
                    fileTmp.delete();
                }
            }
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e){
           e.printStackTrace();
        }
        log.info(uploadDir.toString() + "");
       ;
    }
}
