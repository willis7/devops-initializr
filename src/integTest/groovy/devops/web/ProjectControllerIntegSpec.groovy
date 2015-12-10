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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.web.context.WebApplicationContext
import spock.lang.Ignore
import spock.lang.Shared
import spock.lang.Specification

import org.springframework.test.web.servlet.setup.MockMvcBuilders
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

/**
 * @author Sion Williams
 */
@ContextConfiguration(classes = Application, loader = SpringApplicationContextLoader)
@WebAppConfiguration
public class ProjectControllerIntegSpec extends Specification {

    @Shared
    def sharedSetupDone = false

//    ProjectController projectController

    @Autowired
    private WebApplicationContext context

    @Shared
    private MockMvc mockMvc

    void setup() {
        // sharedSetupDone is a hack because @Autowired webApplicationContext is not yet available in setupSpec()
        if (!sharedSetupDone) {
            mockMvc = MockMvcBuilders.webAppContextSetup(context).build()
            sharedSetupDone = true
        }
//        projectController = new ProjectController()
//        mockMvc = standaloneSetup(projectController)
//            .setSingleView(new InternalResourceView("/templates/project.html"))
//            .build()
    }

    @Ignore("Fix me")
    void "GET on /project returns project.html"() {
        when:
        def response = mockMvc.perform(get("/project")).andReturn().response

        then:
        response.status == 200
        response.contentType.contains('text/html')
        response.contentAsString.contains('DevOps Initializr')
    }

    @Ignore("Fix me")
    void "should process form"() {

    }
}
