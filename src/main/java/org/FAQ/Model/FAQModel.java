package org.FAQ.Model;

import org.FAQ.Bean.IssueBean;
import org.FAQ.Bean.SolutionBean;
import org.FAQ.DB.FAQDao;
import org.FAQ.Service.indexService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jahangiri on 07/11/2017.
 */
public class FAQModel {
    public FAQModel(){

    }
    public FAQModel(UUID code,String subjectID,List<SolutionModel> solutions) {
        setCodeField(code);
        setSubjectField(subjectID);
        setSolutionFields(solutions);
    }
    private UUID codeField;
    private String subjectField;
    private int score;
    private List<SolutionModel> solutionFields;
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public UUID getCodeField() {
        return codeField;
    }
    public void setCodeField(UUID codeField) {
        this.codeField = codeField;
    }
    public String getSubjectField() {
        return subjectField;
    }
    public void setSubjectField(String subjectField) {
        this.subjectField = subjectField;
    }
    public List<SolutionModel> getSolutionFields() {
        if (solutionFields == null)
            solutionFields = new ArrayList<>();
        return solutionFields;
    }
    public void setSolutionFields(List<SolutionModel> solutionFields) {
        if (this.solutionFields == null) this.solutionFields = new ArrayList<>();
        for (SolutionModel s :
                solutionFields) {
            this.solutionFields.add(new SolutionModel(s.getSID(), s.getSolutionCodeField(), s.getTextField()));
        }
    }
    /*public List<FAQModel> getIndexes() {
        indexService inx=new indexService();
        List<FAQModel> indexes = inx.getIndexes();
        return indexes;
    }*/
    public static boolean submitNewIssue(IssueBean issueBean) {
        indexService indexServiceService =new indexService();
        return indexServiceService.findNewIndexesAndApply(issueBean);
    }

    /*public static boolean updateExistingIssue(IssueBean issueBean) {
        *//*FAQDao faqDao=new FAQDao();*//*
        return faqDao.updateCurrentIssue(issueBean);
    }*/

    public static boolean thumpsUp(SolutionBean scoreBean) {
        FAQDao faqDao=new FAQDao();
        return faqDao.thumpsUp(scoreBean);
    }

    public static boolean thumpsDown(SolutionBean scoreBean) {
        FAQDao faqDao=new FAQDao();
        return faqDao.thumpsDown(scoreBean);
    }
}