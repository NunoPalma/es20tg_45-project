package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pt.ulisboa.tecnico.socialsoftware.tutor.user.User;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class DoubtController {

    @Autowired
    DoubtService doubtService;

    @GetMapping("/doubts")
    @PreAuthorize("hasRole('ROLE_STUDENT')")
    public List<DoubtDto> getStudentDoubts(Principal principal) {
        User user = (User) ((Authentication) principal).getPrincipal();
        return doubtService.findUserDoubts(user.getId());
    }

}
