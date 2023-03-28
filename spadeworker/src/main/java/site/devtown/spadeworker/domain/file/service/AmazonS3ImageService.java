package site.devtown.spadeworker.domain.file.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.file.constant.ImageFileType;
import site.devtown.spadeworker.domain.file.exception.ImageFileNotFoundException;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.util.ImageUtil;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import static site.devtown.spadeworker.global.util.ImageUtil.*;

@RequiredArgsConstructor
@Service
public class AmazonS3ImageService {

    private final AmazonS3Client amazonS3Client;
    private final UserService userService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${image.project-thumbnail-image.default-image-path}")
    private String defaultProjectThumbnailImagePath;
    @Value("${image.project-thumbnail-image.default-image-name}")
    private String defaultProjectThumbnailImageName;

    /**
     * 썸네일 이미지 저장
     */
    public String saveThumbnailImage(
            ImageFileType imageFileType,
            MultipartFile imageFile
    ) throws Exception {
        String localStorageDefaultImageUri = "";

        switch (imageFileType) {
            case PROJECT_THUMBNAIL_IMAGE -> {
                localStorageDefaultImageUri = defaultProjectThumbnailImagePath;
            }
        }

        return (!imageFile.isEmpty()) ?
                uploadImage(imageFileType, imageFile) :
                localStorageDefaultImageUri;
    }

    /**
     * 썸네일 이미지 변경
     */
    public String updateThumbnailImage(
            ImageFileType imageFileType,
            MultipartFile imageFile,
            String storedImageFullPath
    ) throws Exception {
        String storedImageName = ImageUtil.getStoredImageName(storedImageFullPath);
        String storedImageResourcePath = getStoredImageResourcePath(storedImageFullPath);
        String requestImageName = imageFile.getOriginalFilename();
        String defaultThumbnailImagePath = "";
        String defaultThumbnailImageName = "";

        switch (imageFileType) {
            case PROJECT_THUMBNAIL_IMAGE -> {
                defaultThumbnailImagePath = defaultProjectThumbnailImagePath;
                defaultThumbnailImageName = defaultProjectThumbnailImageName;
            }
        }

        // 사용자가 기본 이미지를 선택한 경우
        if (imageFile.isEmpty()) {
            // 현재 저장된 이미지가 기본 이미지가 아닌 경우
            if (!storedImageName.equals(defaultThumbnailImageName))
                deleteImage(storedImageResourcePath);

            return defaultThumbnailImagePath;

            // 사용자가 이미지 변경을 요청했을 경우
        } else if (!requestImageName.equals(storedImageName)) {
            // 기존 이미지가 기본 이미지가 아닐 경우에만 삭제
            if (!storedImageName.equals(defaultThumbnailImageName))
                deleteImage(storedImageResourcePath);

            return uploadImage(
                    imageFileType,
                    imageFile
            );
        }

        // 이미지가 변경되지 않았을 경우
        return storedImageFullPath;
    }

    /**
     * S3 파일 업로드
     */
    public String uploadImage(
            ImageFileType imageFileType,
            MultipartFile imageFile
    ) throws IOException {

        String storedImageName = getStoredImageName(
                Objects.requireNonNull(imageFile.getOriginalFilename())
        );

        String storedImageFullPath = getStoredImageFullPath(
                imageFileType,
                storedImageName
        );

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(imageFile.getSize());
        objectMetadata.setContentType(imageFile.getContentType());

        // S3 에 폴더 및 파일 업로드
        amazonS3Client.putObject(
                new PutObjectRequest(
                        bucketName,
                        storedImageFullPath,
                        imageFile.getInputStream(),
                        objectMetadata
                )
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );

        // S3에 업로드한 폴더 및 파일 URL 조회
        return amazonS3Client.getUrl(bucketName, storedImageFullPath).toString();
    }

    /**
     * S3 파일 삭제
     */
    public void deleteImage(
            String storedImageFullPath
    ) {

        String storedImageResourcePath = getStoredImageResourcePath(storedImageFullPath);

        // 삭제할 파일이 존재하는지 검증
        if (!amazonS3Client.doesObjectExist(
                bucketName,
                storedImageResourcePath
        ))
            throw new ImageFileNotFoundException();

        amazonS3Client.deleteObject(
                bucketName,
                storedImageResourcePath
        );
    }

    // 스토리지에 저장될 난수의 이미지 이름 생성
    private String getStoredImageName(
            String originalFileName
    ) {

        String uuid = UUID.randomUUID().toString();
        String imageExtension = originalFileName
                .substring(originalFileName.lastIndexOf(".") + 1);

        return uuid + "." + imageExtension;
    }

    // 스토리지에 저장될 이미지 전체 경로 생성
    private String getStoredImageFullPath(
            ImageFileType imageFileType,
            String storedFileName
    ) {

        String userPersonalId = userService.getCurrentAuthorizedUser().getPersonalId();

        return "images/" +
                userPersonalId +
                "/" +
                imageFileType.getImageType() +
                "/" +
                storedFileName;
    }
}