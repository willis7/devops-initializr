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
