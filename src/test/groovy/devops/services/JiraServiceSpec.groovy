package devops.services

import devops.domain.Project
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author Sion Williams
 */
class JiraServiceSpec extends Specification {

    JiraService jiraService

    RestTemplate restTemplateMock = Mock()
    Project projectMock = Mock()
    ResponseEntity<String> responseEntityMock = Mock()
    HttpHeaders headersMock = Mock()

    void setup() {
        jiraService = new JiraService()
        jiraService.with {
            username = "test"
            password = "tester"
            jiraApiUrl = "http://dummy"
            version = 2
            restTemplate = restTemplateMock
        }
    }

    def "createProject method calls restTemplate.exchange()"() {
        setup:
        responseEntityMock.getHeaders() >> headersMock

        when:
        jiraService.createProject(projectMock)

        then:
        1 * restTemplateMock.exchange(_, _, _, _) >> responseEntityMock
    }
}
