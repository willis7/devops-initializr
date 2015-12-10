package devops.web

import devops.domain.Features
import devops.domain.Project
import devops.services.JiraService
import devops.services.StashService
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import spock.lang.Specification

/**
 * @author Sion Williams
 */
class ProjectControllerSpec extends Specification {

    ProjectController projectController

    JiraService jiraServiceMock = Mock()
    StashService stashServiceMock = Mock()
    Model modelMock = Mock()
    Project projectMock = Mock()
    Features featuresMock = Mock()
    BindingResult bindingResultMock = Mock()

    def setup() {
        projectController = new ProjectController()
        projectController.jiraService = jiraServiceMock
        projectController.stashService = stashServiceMock
    }

    def "calling projectSubmit() calls jira service when checked"() {
        given:
        featuresMock.getJira() >> true

        when:
        projectController.processProjectForm(projectMock, bindingResultMock, featuresMock, modelMock)

        then:
        1 * jiraServiceMock.createProject(_)
    }

    def "calling projectSubmit() doesnt call jira service when unchecked"() {
        given:
        featuresMock.getJira() >> false

        when:
        projectController.processProjectForm(projectMock, bindingResultMock, featuresMock, modelMock)

        then:
        0 * jiraServiceMock.createProject(_)
    }

    def "calling projectSubmit() calls stash service when checked"() {
        given:
        featuresMock.getStash() >> true

        when:
        projectController.processProjectForm(projectMock, bindingResultMock, featuresMock, modelMock)

        then:
        1 * stashServiceMock.createProject(_)
    }

    def "calling projectSubmit() doesnt call stash service when unchecked"() {
        given:
        featuresMock.getStash() >> false

        when:
        projectController.processProjectForm(projectMock, bindingResultMock, featuresMock, modelMock)

        then:
        0 * stashServiceMock.createProject(_)
    }

    def "projectSubmit() should return the 'result' view when successful"() {
        expect:
        "redirect:/result" == projectController.processProjectForm(projectMock, bindingResultMock, featuresMock, modelMock)
    }
}
