package devops.web

import devops.domain.Project
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
class ProjectController {

    @RequestMapping(value="/project", method=RequestMethod.GET)
    String projectForm(Model model) {
        model.addAttribute("project", new Project())
        return "project"
    }

    @RequestMapping(value="/project", method=RequestMethod.POST)
    String greetingSubmit(@ModelAttribute @Valid Project project, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            return "project"
        }

        model.addAttribute("project", project)
        return "result"
    }

}
