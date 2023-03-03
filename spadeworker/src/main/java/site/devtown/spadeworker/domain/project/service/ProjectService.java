package site.devtown.spadeworker.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.devtown.spadeworker.domain.file.service.ImageFileService;
import site.devtown.spadeworker.domain.project.dto.CreateProjectRequest;
import site.devtown.spadeworker.domain.project.dto.ProjectDto;
import site.devtown.spadeworker.domain.project.dto.UpdateProjectRequest;
import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.project.repository.ProjectRepository;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.exception.InvalidResourceOwnerException;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;
import site.devtown.spadeworker.global.factory.YamlPropertySourceFactory;
import site.devtown.spadeworker.global.util.ImageUtil;

import java.util.List;
import java.util.Objects;

import static site.devtown.spadeworker.domain.file.constant.ImageFileType.PROJECT_THUMBNAIL_IMAGE;
import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.INVALID_PROJECT_OWNER;
import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.PROJECT_NOT_FOUND;

@RequiredArgsConstructor
@PropertySource(
        value = "classpath:/upload-resource-rule.yml",
        factory = YamlPropertySourceFactory.class
)
@Transactional
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ImageFileService imageFileService;
    private final UserService userService;

    @Value("${image.project-thumbnail-image.default-image-name}")
    private String localStorageDefaultProjectThumbnailImageName;
    @Value("${image.project-thumbnail-image.default-image-uri}")
    private String localStorageDefaultProjectThumbnailImageUri;

    /**
     * 프로젝트 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(ProjectDto::from)
                .toList();
    }

    /**
     * 특정 프로젝트 단건 조회
     */
    @Transactional(readOnly = true)
    public ProjectDto getProject(
            Long projectId
    ) {
        return projectRepository.findById(projectId)
                .map(ProjectDto::from)
                .orElseThrow(
                        () -> new ResourceNotFoundException(PROJECT_NOT_FOUND)
                );
    }

    /**
     * 프로젝트 생성 비즈니스 로직
     */
    public Long createProject(
            CreateProjectRequest request
    ) throws Exception {

        Project project = Project.of(
                request.title(),
                request.description(),
                createProjectThumbnailImage(request.thumbnailImage()),
                userService.getCurrentAuthorizedUser()
        );

        // create 로직 진행
        return projectRepository.save(project).getId();
    }

    /**
     * 프로젝트 수정 비즈니스 로직
     */
    public Long updateProject(
            Long projectId,
            UpdateProjectRequest request
    ) throws Exception {

        Project savedProject = projectRepository.findById(projectId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(PROJECT_NOT_FOUND)
                );

        // 현재 인증된 사용자의 리소스가 맞는지 검증
        validateProjectOwner(savedProject.getUser());

        // update 로직 진행
        savedProject.update(
                request.title(),
                request.description(),
                updateProjectThumbnailImage(
                        request.thumbnailImage(),
                        savedProject.getThumbnailImageUri()
                )
        );

        return savedProject.getId();
    }

    // 프로젝트 소유자와 현재 인가된 사용자가 동일한지 검증
    private void validateProjectOwner(User owner) {
        if (!Objects.equals(owner, userService.getCurrentAuthorizedUser())) {
            throw new InvalidResourceOwnerException(INVALID_PROJECT_OWNER);
        }
    }

    // 프로젝트 생성이 썸네일 이미지 업로드 후 uri 리턴
    private String createProjectThumbnailImage(
            MultipartFile requestImage
    ) throws Exception {
        return (!Objects.equals(requestImage.getOriginalFilename(),
                localStorageDefaultProjectThumbnailImageName)) ?
                imageFileService.uploadFile(PROJECT_THUMBNAIL_IMAGE, requestImage) :
                localStorageDefaultProjectThumbnailImageUri;
    }

    // 프로젝트의 썸네일 이미지 업데이트 여부를 판별 후 uri 리턴
    private String updateProjectThumbnailImage(
            MultipartFile requestImage,
            String savedImageUri
    ) throws Exception {
        String savedImageName = ImageUtil.getLocalStorageImageName(savedImageUri);
        String requestImageName = requestImage.getOriginalFilename();

        // 사용자가 기존 이미지를 삭제하고 디폴트 이미지로 설정할 경우
        if (
                (Objects.equals(requestImageName, localStorageDefaultProjectThumbnailImageName)) &&
                (!savedImageName.equals(localStorageDefaultProjectThumbnailImageName))
        ) {
            imageFileService.deleteFile(savedImageUri);
            return localStorageDefaultProjectThumbnailImageUri;

            // 사용자가 이미지 변경을 요청했을 경우
        } else if (!Objects.equals(requestImageName, savedImageName)) {
            imageFileService.deleteFile(savedImageUri);
            return imageFileService.uploadFile(
                    PROJECT_THUMBNAIL_IMAGE,
                    requestImage
            );
        }

        // 이미지가 변경되지 않았을 경우
        return savedImageUri;
    }
}
