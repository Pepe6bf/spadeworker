package site.devtown.spadeworker.domain.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.devtown.spadeworker.domain.file.service.AmazonS3ImageService;
import site.devtown.spadeworker.domain.project.dto.CreateProjectRequest;
import site.devtown.spadeworker.domain.project.dto.ProjectDto;
import site.devtown.spadeworker.domain.project.dto.UpdateProjectRequest;
import site.devtown.spadeworker.domain.project.entity.Project;
import site.devtown.spadeworker.domain.project.entity.ProjectLike;
import site.devtown.spadeworker.domain.project.entity.ProjectSubscribe;
import site.devtown.spadeworker.domain.project.exception.*;
import site.devtown.spadeworker.domain.project.repository.ProjectLikeRepository;
import site.devtown.spadeworker.domain.project.repository.ProjectRepository;
import site.devtown.spadeworker.domain.project.repository.ProjectSubscribeRepository;
import site.devtown.spadeworker.domain.user.model.entity.User;
import site.devtown.spadeworker.domain.user.service.UserService;
import site.devtown.spadeworker.global.exception.InvalidResourceOwnerException;
import site.devtown.spadeworker.global.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Objects;

import static site.devtown.spadeworker.domain.file.constant.ImageFileType.PROJECT_THUMBNAIL_IMAGE;
import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.INVALID_PROJECT_OWNER;
import static site.devtown.spadeworker.domain.project.exception.ProjectExceptionCode.PROJECT_NOT_FOUND;

@RequiredArgsConstructor
@Transactional
@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final ProjectLikeRepository projectLikeRepository;
    private final ProjectSubscribeRepository projectSubscribeRepository;
    private final AmazonS3ImageService amazonS3ImageService;
    private final UserService userService;

    /**
     * 프로젝트 전체 조회
     */
    @Transactional(readOnly = true)
    public List<ProjectDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(p -> ProjectDto.from(
                        p,
                        projectLikeRepository.countAllByProject(p),
                        projectSubscribeRepository.countAllByProject(p))
                )
                .toList();
    }

    /**
     * 프로젝트 단건 조회
     */
    @Transactional(readOnly = true)
    public ProjectDto getProject(
            Long projectId
    ) {
        return projectRepository.findById(projectId)
                .map(p -> ProjectDto.from(
                        p,
                        projectLikeRepository.countAllByProject(p),
                        projectSubscribeRepository.countAllByProject(p))
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(PROJECT_NOT_FOUND)
                );
    }

    /**
     * 프로젝트 생성 비즈니스 로직
     */
    public void createProject(
            CreateProjectRequest request
    ) throws Exception {

        duplicateProjectTitleValidate(request.title());

        Project project = Project.of(
                request.title(),
                request.description(),
                amazonS3ImageService.saveThumbnailImage(
                        PROJECT_THUMBNAIL_IMAGE,
                        request.thumbnailImage()
                ),
                userService.getCurrentAuthorizedUser()
        );

        // create 로직 진행
        projectRepository.save(project);
    }

    /**
     * 프로젝트 수정 비즈니스 로직
     */
    public void updateProject(
            Long projectId,
            UpdateProjectRequest request
    ) throws Exception {

        Project savedProject = projectRepository.findById(projectId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(PROJECT_NOT_FOUND)
                );

        // 현재 인증된 사용자의 리소스가 맞는지 검증
        validateProjectOwner(savedProject.getUser());

        if (!request.title().equals(savedProject.getTitle()))
            duplicateProjectTitleValidate(request.title());

        // update 로직 진행
        savedProject.update(
                request.title(),
                request.description(),
                amazonS3ImageService.updateThumbnailImage(
                        PROJECT_THUMBNAIL_IMAGE,
                        request.thumbnailImage(),
                        savedProject.getThumbnailImageUri()
                )
        );
    }

    /**
     * 프로젝트 좋아요 등록 비즈니스 로직
     */
    public void registerProjectLike(Long projectId) {

        Project savedProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND));
        User currentAuthorizedUser = userService.getCurrentAuthorizedUser();

        // 이미 요청자의 좋아요가 프로젝트에 등록되어 있다면 예외 발생
        if (projectLikeRepository.existsByProjectAndUser(
                savedProject,
                currentAuthorizedUser)
        ) {
            throw new ProjectDuplicateLikeException();
        }

        // 프로젝트에 좋아요 등록
        projectLikeRepository.saveAndFlush(
                ProjectLike.of(savedProject, currentAuthorizedUser)
        );
    }

    /**
     * 프로젝트 좋아요 취소 비즈니스 로직
     */
    public void cancelProjectLike(Long projectId) {
        Project savedProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND));
        User currentAuthorizedUser = userService.getCurrentAuthorizedUser();

        // 프로젝트에 요청자의 좋아요가 없으면 예외 발생
        ProjectLike savedProjectLike = projectLikeRepository.findByProjectAndUser(savedProject, currentAuthorizedUser)
                .orElseThrow(ProjectLikeNotFoundException::new);

        // 프로젝트 좋아요 취소
        projectLikeRepository.delete(savedProjectLike);
    }

    /**
     * 프로젝트 구독 등록 비즈니스 로직
     */
    public void registerProjectSubscribe(Long projectId) {
        Project savedProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND));
        User currentAuthorizedUser = userService.getCurrentAuthorizedUser();

        // 이미 프로젝트에 구독되어 있다면 예외 발생
        if (projectSubscribeRepository.existsByProjectAndSubscriber(
                savedProject,
                currentAuthorizedUser)
        ) {
            throw new ProjectDuplicateSubscribeException();
        }

        // 프로젝트에 구독 등록
        projectSubscribeRepository.saveAndFlush(
                ProjectSubscribe.of(savedProject, currentAuthorizedUser)
        );
    }

    /**
     * 프로젝트 구독 취소 비즈니스 로직
     */
    public void cancelProjectSubscribe(Long projectId) {
        Project savedProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException(PROJECT_NOT_FOUND));
        User currentAuthorizedUser = userService.getCurrentAuthorizedUser();

        // 프로젝트에 구독되어 있지 않다면 예외 발생
        ProjectSubscribe projectSubscribe = projectSubscribeRepository.findByProjectAndSubscriber(savedProject, currentAuthorizedUser)
                .orElseThrow(ProjectSubscribeNotFoundException::new);

        // 프로젝트 구독 취소
        projectSubscribeRepository.delete(projectSubscribe);
    }

    // 프로젝트 소유자와 현재 인가된 사용자가 동일한지 검증
    private void validateProjectOwner(User owner) {
        if (!Objects.equals(owner, userService.getCurrentAuthorizedUser())) {
            throw new InvalidResourceOwnerException(INVALID_PROJECT_OWNER);
        }
    }

    private void duplicateProjectTitleValidate(String projectTitle) {
        if (projectRepository.existsByTitle(projectTitle))
            throw new DuplicateProjectTitleException();
    }

}