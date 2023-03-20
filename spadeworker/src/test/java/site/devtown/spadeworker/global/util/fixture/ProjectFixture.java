package site.devtown.spadeworker.global.util.fixture;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.project.dto.CreateProjectRequest;
import site.devtown.spadeworker.domain.project.dto.ProjectDto;
import site.devtown.spadeworker.domain.project.dto.UpdateProjectRequest;
import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.user.model.entity.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ProjectFixture {

    private static final String TITLE = "Java Crew";
    private static final String DESCRIPTION = "이곳은 자바 광신도를 위한 공간입니다.";
    private static final String THUMBNAIL_IMAGE_ORIGINAL_FILE_NAME = "java.png";
    private static final String NEW_TITLE = "이제부터 이곳은 JS 게시판이다.";
    private static final String NEW_DESCRIPTION = "꺄르르르륵";
    private static final String NEW_THUMBNAIL_IMAGE_ORIGINAL_FILE_NAME = "js.png";
    private static final User USER = UserFixture.getUserEntity();
    private static final int LIKE_COUNT = 0;
    private static final int SUBSCRIBER_COUNT = 0;

    /**
     * CreateProjectRequest
     * 요소 전체 기본값 사용
     */
    public static CreateProjectRequest getCreateProjectRequest()
            throws IOException{
        return getCreateProjectRequest(
                TITLE,
                DESCRIPTION,
                createMockImage(THUMBNAIL_IMAGE_ORIGINAL_FILE_NAME)
        );
    }

    /**
     * CreateProjectRequest
     * 요소 전체 선택
     */
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

    /**
     * UpdateProjectRequest
     * 요소 전체 기본값 사용
     */
    public static UpdateProjectRequest getUpdateProjectRequest()
            throws IOException {
        return getUpdateProjectRequest(
                NEW_TITLE,
                NEW_DESCRIPTION,
                createMockImage(NEW_THUMBNAIL_IMAGE_ORIGINAL_FILE_NAME)
        );
    }

    /**
     * UpdateProjectRequest
     * 요소 전체 선택
     */
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

    /**
     * ProjectEntity
     * 모든 요소 기본 값 사용
     */
    public static Project getProject() {
        return getProject(
                TITLE,
                DESCRIPTION,
                getStoredImageUri(THUMBNAIL_IMAGE_ORIGINAL_FILE_NAME),
                USER
        );
    }

    /**
     * ProjectEntity
     * CreateProjectRequest & 파라미터로 넘어온 user 를 기반으로 생성
     */
    public static Project getProject(
            CreateProjectRequest request,
            User user
    ) {
        return getProject(
                request.title(),
                request.description(),
                request.thumbnailImage().getOriginalFilename(),
                user
        );
    }

    /**
     * ProjectEntity
     * 요소 전체 선택
     */
    public static Project getProject(
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

    /**
     * ProjectEntity List
     */
    public static List<Project> getProjects(int size) {
        ArrayList<Project> projects = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            projects.add(getProject());
        }

        return projects;
    }

    /**
     * ProjectDto
     * 요소 전체 기본 값 사용
     */
    public static ProjectDto getProjectDto() {
        return ProjectDto.from(getProject(), LIKE_COUNT, SUBSCRIBER_COUNT);
    }

    /**
     * ProjectDto List
     */
    public static List<ProjectDto> getProjectDtos(int size) {
        ArrayList<ProjectDto> projectDtos = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            projectDtos.add(getProjectDto());
        }

        return projectDtos;
    }

    // 저장된 이미지 전체 uri 반환
    private static String getStoredImageUri(String thumbnailImageOriginalName) {
        return "/storage/images/" + thumbnailImageOriginalName;
    }

    // Mock 썸네일 이미지 생성
    private static MockMultipartFile createMockImage(
            String thumbnailImageOriginalFileName
    ) throws IOException {
        return new MockMultipartFile(
                "thumbnailImage",
                thumbnailImageOriginalFileName,
                "image/png",
                new FileInputStream(ProjectFixture.class.getResource("/images/test.png").getFile())
        );
    }
}