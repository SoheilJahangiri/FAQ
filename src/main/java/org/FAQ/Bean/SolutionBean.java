package org.FAQ.Bean;


/**
 * Created by jahangiri on 07/30/2017.
 */
public class SolutionBean {
    public SolutionBean(String solutionText,String SID){
        setSolutionText(solutionText);
        setSID(SID);
    }
    private String solutionText;
    private String SID;

    public String getSID() {
        return SID;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public String getSolutionText() {
        return solutionText;
    }

    public void setSolutionText(String solutionText) {
        this.solutionText = solutionText;
    }
}
