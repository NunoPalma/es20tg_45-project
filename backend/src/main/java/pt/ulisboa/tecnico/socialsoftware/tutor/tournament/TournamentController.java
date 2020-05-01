package pt.ulisboa.tecnico.socialsoftware.tutor.tournament;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TournamentController {

    @Autowired
    private TournamentService tournamentService;

    @PostMapping("/tournament/create/{executionId}/{studentId}")
    @PreAuthorize("(hasRole('ROLE_DEMO_STUDENT') or hasRole('ROLE_STUDENT'))")
    public TournamentDto createTournament(@PathVariable int studentId, @PathVariable int executionId,
                                 @RequestBody TournamentDto tournamentDto) {
        // really? at least use a logger
        System.out.println("oh god oh fuck " + tournamentDto.getTopics().size());
        System.out.flush();
        return tournamentService.createTournament(studentId, executionId, tournamentDto);
    }

    @PostMapping("/tournament/enroll/{studentId}/{tournamentId}")
    @PreAuthorize("hasRole('STUDENT') or hasRole('ROLE_DEMO_STUDENT')")
    public TournamentDto enrollStudent(@PathVariable int studentId, @PathVariable int tournamentId) {
        return tournamentService.enrollStudent(studentId, tournamentId);
    }

    @GetMapping("/tournament/show/{studentId}")
    @PreAuthorize("hasRole('ROLE_DEMO_STUDENT') or hasRole('ROLE_STUDENT')")
    public List<TournamentDto> getTournaments(@PathVariable int studentId) {
        return tournamentService.getTournaments(studentId);
    }
}
