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
