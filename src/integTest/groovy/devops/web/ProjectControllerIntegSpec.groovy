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

import devops.Application
import devops.domain.Project
import devops.services.JiraService
import devops.services.StashService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.validation.BindingResult
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.servlet.view.InternalResourceViewResolver
import spock.lang.Shared
import spock.lang.Specification

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

/**
 * @author Sion Williams
 */
@ContextConfiguration(classes = Application, loader = SpringApplicationContextLoader)
@WebAppConfiguration
public class ProjectControllerIntegSpec extends Specification {

    public static final String VIEW = "/project"

    @Shared
    def sharedSetupDone = false

    @Autowired
    private WebApplicationContext context

    @Shared
    private MockMvc mockMvc

    ProjectController projectController
    BindingResult bindingResultMock = Mock()
    JiraService jiraServiceMock = Mock()
    StashService stashServiceMock = Mock()

    void setup() {
        if (!sharedSetupDone) {
            projectController = new ProjectController()
            projectController.jiraService = jiraServiceMock
            projectController.stashService = stashServiceMock

            mockMvc = standaloneSetup(projectController).setViewResolvers(templateResolver()).build()
        }
    }

    InternalResourceViewResolver templateResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/templates/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    void "get on /project should show correct page"() {
        expect:
        mockMvc.perform(get(VIEW))
            .andExpect(view().name("project"))
    }

    void "post on /project with valid args shows results"() {
        expect:
        mockMvc.perform(post(VIEW)
            .param("key", "key")
            .param("name", "name")
            .param("projectTypeKey", "projectTypeKey")
            .param("lead", "lead"))
            .andExpect(view().name("result"))
    }
}
