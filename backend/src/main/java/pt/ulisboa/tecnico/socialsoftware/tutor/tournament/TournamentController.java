package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/executions/students/{executionId}/{studentId}")
    @PreAuthorize("(hasRole('ROLE_DEMO_STUDENT') or hasRole('ROLE_STUDENT')) and hasPermission(#executionId, 'EXECUTION.ACCESS')")
    public TournamentDto createTournament(@PathVariable int studentId, @PathVariable int executionId,
                                 @RequestBody TournamentDto tournamentDto) {
        return tournamentService.createTournament(studentId, executionId, tournamentDto);
    }

    @PostMapping("/tournament/enroll/{studentId}/{tournamentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public void enrollStudent(@PathVariable int studentId, @PathVariable int tournamentId) {
        tournamentService.enrollStudent(studentId, tournamentId);
    }
}
