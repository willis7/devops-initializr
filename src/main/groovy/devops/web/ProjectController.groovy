package devops.web

import devops.domain.Project
import devops.services.JiraService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

import javax.validation.Valid

/**
 * @author Sion Williams
 */
@Controller
@RequestMapping(value = ["/","/project"])
class ProjectController {
    private JiraService jiraService

    @Autowired
    ProjectController(JiraService jiraService) {
        this.jiraService = jiraService
    }

    @RequestMapping(method = RequestMethod.GET)
    String projectForm(Model model) {
        model.addAttribute("project", new Project())
        return "project"
    }

    @RequestMapping(method = RequestMethod.POST)
    String projectSubmit(@ModelAttribute @Valid Project project, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "project"
        }
        def location = jiraService.createProject(project)
        model.addAttribute("project", project)
        model.addAttribute("location", location)
        return "result"
    }
}
