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
 * @author Sion Williams
 */
@Service
class StashService {
    @Autowired
    RestTemplate restTemplate

    @Value('${stash.api.url}')
    private String stashApiUrl

    @Value('${stash.api.un}')
    private String username

    @Value('${stash.api.pw}')
    private String password

    @Value('${stash.api.version}')
    private String version

    /**
     * Create Stash Project via REST POST
     * @param project
     * @return Location of the new resource
     */
    def createProject(Project project) {
        String resource = "/rest/api/${version}/projects"
        String uri = stashApiUrl + resource
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
