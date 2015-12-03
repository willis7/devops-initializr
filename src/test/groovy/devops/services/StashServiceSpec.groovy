package devops.services

import devops.domain.Project
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate
import spock.lang.Specification

/**
 * @author Sion Williams
 */
class StashServiceSpec extends Specification {
    StashService stashService

    RestTemplate restTemplateMock = Mock()
    Project projectMock = Mock()
    ResponseEntity<String> responseEntityMock = Mock()
    HttpHeaders headersMock = Mock()

    void setup() {
        stashService = new StashService()
        stashService.with {
            username = "test"
            password = "tester"
            stashApiUrl = "http://dummy"
            version = 2
            restTemplate = restTemplateMock
        }
    }

    def "createProject method calls restTemplate.exchange()"() {
        setup:
        responseEntityMock.getHeaders() >> headersMock

        when:
        stashService.createProject(projectMock)

        then:
        1 * restTemplateMock.exchange(_, _, _, _) >> responseEntityMock
    }
}
