package site.devtown.spadeworker.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.devtown.spadeworker.domain.project.dto.CreateProjectRequest;
import site.devtown.spadeworker.domain.project.dto.ProjectDto;
import site.devtown.spadeworker.domain.project.dto.UpdateProjectRequest;
import site.devtown.spadeworker.domain.project.service.ProjectService;
import site.devtown.spadeworker.global.response.CommonResult;
import site.devtown.spadeworker.global.response.ListResult;
import site.devtown.spadeworker.global.response.ResponseService;
import site.devtown.spadeworker.global.response.SingleResult;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.OK;

@RequiredArgsConstructor
@RequestMapping("/api/projects")
@RestController
public class ProjectController {

    private final ProjectService projectService;
    private final ResponseService responseService;

    /**
     * 프로젝트 전체 조회 API
     */
    @GetMapping()
    public ListResult<ProjectDto> getAllProjects() {
        return responseService.getListResult(
                OK.value(),
                "성공적으로 프로젝트를 조회하였습니다.",
                projectService.getAllProjects()
        );
    }

    /**
     * 특정 프로젝트 단건 조회 API
     */
    @GetMapping("/{project-id}")
    public SingleResult<ProjectDto> getProject(
            @PathVariable("project-id") Long projectId
    ) {
        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 프로젝트를 조회하였습니다.",
                projectService.getProject(projectId)
        );
    }

    /**
     * 프로젝트 생성 API
     */
    @PostMapping()
    public CommonResult createProject(
            @ModelAttribute @Valid CreateProjectRequest request
    ) throws Exception {

        // 프로젝트 생성 비즈니스 로직 수행
        projectService.createProject(request);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 프로젝트가 생성되었습니다."
        );
    }

    /**
     * 프로젝트 수정 API
     */
    @PatchMapping("/{project-id}")
    public CommonResult updateProject(
            @PathVariable("project-id") Long projectId,
            @ModelAttribute @Valid UpdateProjectRequest request
    ) throws Exception {

        // 프로젝트 수정 비즈니스 로직 수행
        projectService.updateProject(projectId, request);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 프로젝트가 수정되었습니다."
        );
    }

    /**
     * 프로젝트 좋아요 등록 API
     */
    @PostMapping("/{project-id}/like")
    public CommonResult registerProjectLike(
            @PathVariable("project-id") Long projectId
    ) {
        // 프로젝트에 좋아요 등록
        projectService.registerProjectLike(projectId);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 프로젝트에 좋아요가 등록되었습니다."
        );
    }

    /**
     * 프로젝트 좋아요 취소 API
     */
    @DeleteMapping("/{project-id}/like")
    public CommonResult cancelProjectLike(
            @PathVariable("project-id") Long projectId
    ) {
        // 프로젝트의 좋아요 취소
        projectService.cancelProjectLike(projectId);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 프로젝트의 좋아요가 취소되었습니다."
        );
    }

    /**
     * 프로젝트 구독 API
     */
    @PostMapping("/{project-id}/subscribe")
    public CommonResult registerProjectSubscribe(
            @PathVariable("project-id") Long projectId
    ) {
        // 프로젝트 구독
        projectService.registerProjectSubscribe(projectId);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 프로젝트에 구독되었습니다."
        );
    }

    /**
     * 프로젝트 구독 취소 API
     */
    @DeleteMapping("/{project-id}/subscribe")
    public CommonResult cancelProjectSubscribe(
            @PathVariable("project-id") Long projectId
    ) {
        // 프로젝트 구독 취소
        projectService.cancelProjectSubscribe(projectId);

        return responseService.getSuccessResult(
                OK.value(),
                "성공적으로 프로젝트의 구독이 취소되었습니다."
        );
    }
}