/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Sion Williams
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
