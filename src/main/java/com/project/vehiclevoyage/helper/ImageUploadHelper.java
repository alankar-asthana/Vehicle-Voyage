package com.project.vehiclevoyage.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class ImageUploadHelper {
    public static final String UPLOAD_DIR = "src/main/resources/static/images/";


    public boolean uploadImage(MultipartFile file) throws IOException {
        boolean f = false;
        try {
            if (file.isEmpty()) {
                return false;
            }
            InputStream inputStream = file.getInputStream();
            byte[] data = new byte[inputStream.available()];

            String fileName = file.getOriginalFilename();
            String uniqueFileName = UUID.randomUUID().toString() + "-" + fileName;
            FileOutputStream fos = new FileOutputStream(UPLOAD_DIR + uniqueFileName);

            fos.write(data);
            fos.flush();
            fos.close();
            f = true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }
}
