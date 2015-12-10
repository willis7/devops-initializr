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
