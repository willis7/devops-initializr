package devops.services

import devops.domain.Project
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.http.HttpMethod.POST

/**
 * Rest client for the Jira Api
 * @author Sion Williams
 */
@Service
class JiraService {
    @Autowired
    RestTemplate restTemplate

    @Value('${jira.api.url}')
    private String jiraApiUrl

    @Value('${jira.api.un}')
    private String username

    @Value('${jira.api.pw}')
    private String password

    @Value('${jira.api.version}')
    private String version

    /**
     * Create Jira Project via REST POST
     * @param project
     * @return Location of the new resource
     */
    def createProject(Project project) {
        String resource = "/rest/api/${version}/project"
        String uri = jiraApiUrl + resource
        HttpEntity httpEntity = new HttpEntity(project, getHttpHeaders())

        ResponseEntity<String> response = restTemplate.exchange(uri, POST, httpEntity, String.class)
        return response.getHeaders().getLocation()
    }

    /**
     * Return a Http Header with Basic Authentication set
     * @return HttpHeaders configured for Basic Authentication
     */
    protected HttpHeaders getHttpHeaders() {
        assert username, "username must not be null"
        assert password, "password must not be null"
        def encodedAuth = "$username:$password".toString().bytes.encodeBase64()

        HttpHeaders headers = new HttpHeaders()
        headers.set(AUTHORIZATION, 'Basic ' + encodedAuth)

        return headers
    }
}
