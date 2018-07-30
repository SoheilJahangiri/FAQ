package org.FAQ.Bean;

import org.FAQ.Model.SolutionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jahangiri on 07/30/2017.
 */
public class FAQResultBean {
    private String subject;
    private int score;
    private UUID subjectID;
    private List<SolutionBean> solutions;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UUID getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(UUID subjectID) {
        this.subjectID = subjectID;
    }

    public List<SolutionBean> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<SolutionModel> solutions) {
        if (this.solutions == null) this.solutions = new ArrayList<>();
        for (SolutionModel s :
                solutions) {
            this.solutions.add(new SolutionBean(s.getTextField(), s.getSID() != null ? String.valueOf(s.getSID()) : ""));
        }
    }

    public FAQResultBean(String subject, int score, UUID subjectID, List<SolutionModel> solutions) {
        setSubject(subject);
        setScore(score);
        setSubjectID(subjectID);
        setSolutions(solutions);
    }
}
