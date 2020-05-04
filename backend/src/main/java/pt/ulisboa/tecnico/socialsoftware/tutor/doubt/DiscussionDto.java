package pt.ulisboa.tecnico.socialsoftware.tutor.doubt;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

public class DiscussionDto implements Serializable {

    private int mainDoubtId;
    private Set<DoubtDto> discussion;

    public DiscussionDto() {}

    public DiscussionDto(Discussion discussion) {
        this.mainDoubtId = discussion.getMainDoubtId();
        this.discussion = discussion.getDiscussion().stream().map(DoubtDto::new).collect(Collectors.toSet());
    }

    public int getMainDoubtId() {
        return mainDoubtId;
    }

    public void setMainDoubtId(int mainDoubtId) {
        this.mainDoubtId = mainDoubtId;
    }

    public Set<DoubtDto> getDiscussion() {
        return discussion;
    }

    public void setDiscussion(Set<DoubtDto> discussion) {
        this.discussion = discussion;
    }
}
