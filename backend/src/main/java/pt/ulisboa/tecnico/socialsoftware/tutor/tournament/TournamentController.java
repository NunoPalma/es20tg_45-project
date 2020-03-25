package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/executions/{executionId}/students/{studentId}/")
    @PreAuthorize("hasRole('STUDENT') and hasPermission(#executionId, 'EXECUTION.ACCESS')")
    public void createTournament(@PathVariable int studentId, @PathVariable int executionId,
                                 @RequestBody List<String> topics, @RequestBody TournamentDto tournamentDto) {
        tournamentService.createTournament(studentId, executionId, topics, tournamentDto);
    }

    @PostMapping("/tournament/enroll/{studentId}/{tournamentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public TournamentDto enrollStudent(@PathVariable int studentId, @PathVariable int tournamentId) {
        return tournamentService.enrollStudent(studentId, tournamentId);
    }
}
