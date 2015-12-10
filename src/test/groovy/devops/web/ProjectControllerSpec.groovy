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
