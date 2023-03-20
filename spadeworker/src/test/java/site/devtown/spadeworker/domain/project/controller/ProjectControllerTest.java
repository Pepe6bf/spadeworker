package site.devtown.spadeworker.domain.project.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import site.devtown.spadeworker.domain.project.dto.CreateProjectRequest;
import site.devtown.spadeworker.domain.project.dto.UpdateProjectRequest;
import site.devtown.spadeworker.domain.project.repository.ProjectRepository;
import site.devtown.spadeworker.domain.project.service.ProjectService;
import site.devtown.spadeworker.global.response.ResponseService;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static site.devtown.spadeworker.global.util.fixture.ProjectFixture.*;

@DisplayName("[컨트롤러 로직] - Project(게시판)")
@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    private final MockMvc mvc;

    @MockBean
    private ProjectService projectService;
    @MockBean
    private ProjectRepository projectRepository;
    @MockBean
    private ResponseService responseService;

    private static final String MOCK_USER_PERSONAL_ID = "tester";

    public ProjectControllerTest(
            @Autowired MockMvc mvc
    ) {
        this.mvc = mvc;
    }

    @DisplayName("[성공 테스트] - 프로젝트 전체 조회")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_get_all_projects() throws Exception {
        // Given
        given(projectService.getAllProjects()).willReturn(getProjectDtos(10));

        // When & Then
        mvc.perform(
                        get("/api/projects")
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[성공 테스트] - 프로젝트 단건 조회")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_get_project() throws Exception {
        // Given
        Long projectId = 1L;

        given(projectService.getProject(projectId)).willReturn(getProjectDto());

        // When & Then
        mvc.perform(
                        get("/api/projects/" + projectId)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[성공 테스트] - 프로젝트 생성")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_create_project() throws Exception {
        // Given
        CreateProjectRequest request = getCreateProjectRequest();

        willDoNothing().given(projectService).createProject(request);

        // When & Then
        mvc.perform(
                        multipart("/api/projects")
                                .file((MockMultipartFile) request.thumbnailImage())
                                .with(csrf())
                                .param("title", request.title())
                                .param("description", request.description())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[성공 테스트] - 프로젝트 수정")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_update_project() throws Exception {
        // Given
        Long projectId = 1L;
        UpdateProjectRequest request = getUpdateProjectRequest();

        willDoNothing().given(projectService).updateProject(projectId, request);

        // When & Then
        mvc.perform(
                        multipart("/api/projects")
                                .file((MockMultipartFile) request.thumbnailImage())
                                .with(csrf())
                                .param("title", request.title())
                                .param("description", request.description())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[성공 테스트] - 프로젝트 좋아요 등록")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_register_project_like() throws Exception {
        // Given
        Long projectId = 1L;

        willDoNothing().given(projectService).registerProjectLike(projectId);

        // When & Then
        mvc.perform(
                        post("/api/projects/" + projectId + "/like")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[성공 테스트] - 프로젝트 좋아요 취소")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_cancel_project_like() throws Exception {
        // Given
        Long projectId = 1L;

        willDoNothing().given(projectService).cancelProjectLike(projectId);

        // When & Then
        mvc.perform(
                        delete("/api/projects/" + projectId + "/like")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[성공 테스트] - 프로젝트 구독 등록")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_register_project_subscribe() throws Exception {
        // Given
        Long projectId = 1L;

        willDoNothing().given(projectService).registerProjectSubscribe(projectId);

        // When & Then
        mvc.perform(
                        post("/api/projects/" + projectId + "/subscribe")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

    @DisplayName("[성공 테스트] - 프로젝트 구독 취소")
    @WithMockUser(value = MOCK_USER_PERSONAL_ID)
    @Test
    void success_cancel_project_subscribe() throws Exception {
        // Given
        Long projectId = 1L;

        willDoNothing().given(projectService).cancelProjectSubscribe(projectId);

        // When & Then
        mvc.perform(
                        delete("/api/projects/" + projectId + "/subscribe")
                                .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}