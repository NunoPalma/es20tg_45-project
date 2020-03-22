package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/tournament/enroll/{studentId}/{tournamentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public void enrollStudent(@PathVariable int studentId, @PathVariable int tournamentId) {
        tournamentService.enrollStudent(studentId, tournamentId);
    }
}
