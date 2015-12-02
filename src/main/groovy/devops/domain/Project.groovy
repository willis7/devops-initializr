package devops.domain

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

/**
 * @author Sion Williams
 */
class Project {
    @NotNull
    @Size(min = 3, max = 10)
    String key

    @NotNull
    @Size(min = 3, max = 30)
    String name

    @NotNull
    String projectTypeKey

    @NotNull
    String lead
}
