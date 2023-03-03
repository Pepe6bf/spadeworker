package site.devtown.spadeworker.domain.file.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.file.constant.ImageFileType;
import site.devtown.spadeworker.domain.file.exception.ImageFileNotFoundException;
import site.devtown.spadeworker.global.factory.YamlPropertySourceFactory;

import java.io.File;
import java.util.Objects;
import java.util.UUID;

@PropertySource(
        value = "classpath:/upload-resource-rule.yml",
        factory = YamlPropertySourceFactory.class
)
@Service
public class LocalImageFileService
        implements ImageFileService {

    @Value("${image.local-image-storage-location}")
    private String localFileStorageLocation;

    @Override
    public String uploadFile(
            ImageFileType imageFileType,
            MultipartFile fileData
    ) throws Exception {
        String storedFileName = getStoredFileName(
                imageFileType,
                Objects.requireNonNull(fileData.getOriginalFilename())
        );
        String fileStoredFullPath = getFileStoredFullPath(
                imageFileType,
                storedFileName
        );

        // 파일 업로드
        fileData.transferTo(new File(fileStoredFullPath));

        return fileStoredFullPath;
    }

    @Override
    public String getStoredFileName(
            ImageFileType imageFileType,
            String originalFileName
    ) {
        String uuid = UUID.randomUUID().toString();
        String extension = originalFileName
                .substring(originalFileName.lastIndexOf(".") + 1);

        return imageFileType.getImageType() + "_" + uuid + "." + extension;
    }

    @Override
    public String getFileStoredFullPath(
            ImageFileType imageFileType,
            String storedFileName
    ) {
        return localFileStorageLocation + "/" + imageFileType.getImageType() + "/" + storedFileName;
    }

    @Override
    public void deleteFile(String fileStoredFullPath) {
        File deleteFile = new File(fileStoredFullPath);

        System.out.println(fileStoredFullPath);

        // 삭제할 파일이 존재하는지 검증
        if (!deleteFile.exists()) {
            throw new ImageFileNotFoundException();
        }

        deleteFile.delete();
    }
}
