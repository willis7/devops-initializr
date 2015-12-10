package devops.web

import devops.domain.Features
import devops.domain.Project
import devops.services.JiraService
import devops.services.StashService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping

import javax.validation.Valid

import static org.springframework.web.bind.annotation.RequestMethod.GET
import static org.springframework.web.bind.annotation.RequestMethod.POST

/**
 * @author Sion Williams
 */
@Controller
@RequestMapping(value = "/project")
class ProjectController {
    @Autowired
    JiraService jiraService

    @Autowired
    StashService stashService


    @RequestMapping(method = GET)
    String showProjectForm(Model model) {
        model.addAttribute("project", new Project())
        model.addAttribute("features", new Features())
        return "project"
    }

    /**
     * Handle form submission for creating a new project
     * @param project
     * @param projectBindingResult
     * @param features
     * @param model
     * @return view name
     */
    @RequestMapping(method = POST)
    String processProjectForm(
        @ModelAttribute @Valid Project project,
        BindingResult projectBindingResult,
        Features features,
        Model model) {

        if (projectBindingResult.hasErrors()) {
            return "project"
        }

        if (features.jira) {
            def location = jiraService.createProject(project)
            model.addAttribute("jiraLocation", location)
        }

        if (features.stash) {
            def location = stashService.createProject(project)
            model.addAttribute("stashLocation", location)
        }

        model.addAttribute("project", project)
        return "redirect:/result"
    }
}
