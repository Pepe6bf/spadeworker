package site.devtown.spadeworker.global.util.fixture;

import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.project.dto.CreateProjectRequest;
import site.devtown.spadeworker.domain.project.dto.UpdateProjectRequest;
import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.io.IOException;

public class ProjectFixture {

    public static String TITLE = "Java Crew";
    public static String DESCRIPTION = "이곳은 자바 광신도를 위한 공간입니다.";
    public static String NEW_TITLE = "이제부터 이곳은 JS 게시판이다.";
    public static String NEW_DESCRIPTION = "꺄르르르륵";


    /**
     * 임의적인 프로젝트 생성 요청 DTO 를 생성 후 반환
     */
    public static CreateProjectRequest getCreateProjectRequest(
            MultipartFile thumbnailImage
    ) throws IOException {
        return getCreateProjectRequest(
                TITLE,
                DESCRIPTION,
                thumbnailImage
        );
    }

    public static CreateProjectRequest getCreateProjectRequest(
            String title,
            String description,
            MultipartFile thumbnailImage
    ) throws IOException {
        return new CreateProjectRequest(
                title,
                description,
                thumbnailImage
        );
    }

    public static UpdateProjectRequest getUpdateProjectRequest(
            MultipartFile thumbnailImage
    ) {
        return getUpdateProjectRequest(
                NEW_TITLE,
                NEW_DESCRIPTION,
                thumbnailImage
        );
    }

    public static UpdateProjectRequest getUpdateProjectRequest(
            String title,
            String description,
            MultipartFile thumbnailImage
    ) {
        return new UpdateProjectRequest(
                title,
                description,
                thumbnailImage
        );
    }

    public static Project getProjectEntity(
            String title,
            String description,
            String thumbnailImageOriginalName,
            User user
    ) {
        return Project.of(
                title,
                description,
                getStoredImageUri(thumbnailImageOriginalName),
                user
        );
    }

    private static String getStoredImageUri(String thumbnailImageOriginalName) {
        return "/storage/images/" + thumbnailImageOriginalName;
    }
}
