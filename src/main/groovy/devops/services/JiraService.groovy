package devops.services

import devops.domain.Project
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

import java.util.logging.Logger

import static org.springframework.http.HttpHeaders.AUTHORIZATION
import static org.springframework.http.HttpMethod.POST

/**
 * Rest client for the Jira Api
 * @author Sion Williams
 */
@Service
class JiraService {
    private Logger log = Logger.getLogger("jiraService")
    private static RestTemplate restTemplate

    @Value('${jira.api.url}')
    private String jiraApiUrl

    @Value('${jira.api.un}')
    private String username

    @Value('${jira.api.pw}')
    private String password

    static {
        restTemplate = new RestTemplate()
    }

    protected HttpHeaders getHttpHeaders() {
        assert username, "username must not be null"
        assert password, "password must not be null"
        def encodedAuth = "$username:$password".toString().bytes.encodeBase64()

        HttpHeaders headers = new HttpHeaders()
        //headers.setContentType(MediaType.APPLICATION_JSON)
        headers.set(AUTHORIZATION, 'Basic ' + encodedAuth)

        return headers
    }

    /**
     * Create Jira Project via REST POST
     * @param project
     * @return Location of the new resource
     */
    def createProject(Project project) {
        String resource = "/rest/api/2/project"
        String uri = jiraApiUrl + resource
        HttpEntity httpEntity = new HttpEntity(project, getHttpHeaders())

//        // Some dirtyness to stop an exception being thrown
//        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
//            protected boolean hasError(HttpStatus statusCode) {
//                return false;
//            }})

        ResponseEntity<String> response = restTemplate.exchange(uri, POST, httpEntity, String.class)
        println response.getHeaders().getLocation()
        println response.hasBody()
        return response.getHeaders().getLocation()
    }
}
