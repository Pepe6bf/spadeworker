package site.devtown.spadeworker.domain.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.devtown.spadeworker.domain.project.dto.CreateProjectRequest;
import site.devtown.spadeworker.domain.project.dto.ProjectDto;
import site.devtown.spadeworker.domain.project.dto.UpdateProjectRequest;
import site.devtown.spadeworker.domain.project.service.ProjectService;
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
    @GetMapping("/{projectId}")
    public SingleResult<ProjectDto> getProject(
            @PathVariable Long projectId
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
    public SingleResult<Long> createProject(
            @ModelAttribute @Valid CreateProjectRequest request
    ) throws Exception {
        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 프로젝트가 생성되었습니다.",
                projectService.createProject(request)
        );
    }

    /**
     * 프로젝트 수정 API
     */
    @PatchMapping("/{projectId}")
    public SingleResult<Long> updateProject(
            @PathVariable Long projectId,
            @ModelAttribute @Valid UpdateProjectRequest request
    ) throws Exception {
        return responseService.getSingleResult(
                OK.value(),
                "성공적으로 프로젝트가 수정되었습니다.",
                projectService.updateProject(
                        projectId,
                        request
                )
        );
    }
}
