package org.FAQ.Bean;

import java.util.UUID;

/**
 * Created by jahangiri on 07/29/2017.
 */
public class FAQBean {
    private String subjectText;
    private int score;
    private UUID subjectID;
    private String solutionText;
    private UUID SID;

    public UUID getSID() {
        return SID;
    }

    public void setSID(UUID SID) {
        this.SID = SID;
    }

    public FAQBean(String subjectText, int score, UUID subjectID,UUID SID, String solutionText) {
        try {
            setSubjectText(subjectText);
            setScore(score);
            setSubjectID(subjectID);
            if (SID!=null)
                setSID(SID);
            setSolutionText(solutionText);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String getSubjectText() {
        return subjectText;
    }

    public void setSubjectText(String subjectText) {
        this.subjectText = subjectText;
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

    public String getSolutionText() {
        return solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }
}
