package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="discussion")
public class Discussion {

    @Id
    @Column(name="mainDoubtId")
    private Integer mainDoubtId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "discussion", fetch = FetchType.EAGER)
    private Set<Doubt> discussion;

    public Discussion() {
    }

    public Discussion(Doubt doubt) {
        this.mainDoubtId = doubt.getId();
        this.discussion = new HashSet<Doubt>();
        doubt.setDiscussion(this);
    }

    public void addOptionalDoubt(Doubt doubt) {
        this.discussion.add(doubt);
    }

    public Integer getMainDoubtId() {
        return mainDoubtId;
    }

    public void setMainDoubtId(Integer mainDoubtId) {
        this.mainDoubtId = mainDoubtId;
    }

    public Set<Doubt> getDiscussion() {
        return discussion;
    }

    public void setDiscussion(Set<Doubt> discussion) {
        this.discussion = discussion;
    }
}
