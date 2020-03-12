package pt.ulisboa.tecnico.socialsoftware.tutor.clarification;

import javax.persistence.*;


@Entity
public class Clarification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
