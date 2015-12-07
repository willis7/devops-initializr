package devops.web

import devops.domain.Features
import devops.domain.Project
import devops.services.JiraService
import devops.services.StashService
import org.springframework.ui.Model
import spock.lang.Specification

/**
 * @author Sion Williams
 */
class ProjectControllerSpec extends Specification {

    ProjectController projectController

    JiraService jiraService = Mock()

    StashService stashService = Mock()

    Model model = Mock()

    Project project = Mock()

    Features features = Mock()

    def setup() {
        projectController = new ProjectController()
        projectController.jiraService = jiraService
        projectController.stashService = stashService
    }

    def "calling projectSubmit() calls jira service when checked"() {
        given:
        features.getJira() >> true

        when:
        projectController.processProjectForm(project, features, model)

        then:
        1 * jiraService.createProject(_)
    }

    def "calling projectSubmit() doesnt call jira service when unchecked"() {
        given:
        features.getJira() >> false

        when:
        projectController.processProjectForm(project, features, model)

        then:
        0 * jiraService.createProject(_)
    }

    def "calling projectSubmit() calls stash service when checked"() {
        given:
        features.getStash() >> true

        when:
        projectController.processProjectForm(project, features, model)

        then:
        1 * stashService.createProject(_)
    }

    def "calling projectSubmit() doesnt call stash service when unchecked"() {
        given:
        features.getStash() >> false

        when:
        projectController.processProjectForm(project, features, model)

        then:
        0 * stashService.createProject(_)
    }

    def "projectSubmit() should return the 'result' view when successful"() {
        expect:
        "result" == projectController.processProjectForm(project, features, model)
    }
}
