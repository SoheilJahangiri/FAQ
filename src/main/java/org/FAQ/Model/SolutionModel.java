package org.FAQ.Model;

import java.util.UUID;

/**
 * Created by jahangiri on 07/11/2017.
 */
public class SolutionModel {

    public SolutionModel() {
        this.solutionCodeField = UUID.randomUUID();
    }
    public SolutionModel(UUID SID,UUID solutionCodeField, String textField) {
        if (SID != null)
            setSID(SID);
        setSolutionCodeField(solutionCodeField);
        setTextField(textField);
    }
    private UUID SID;
    private UUID solutionCodeField;
    private String textField;

    public UUID getSID() {
        return SID;
    }

    public void setSID(UUID SID) {
        this.SID = SID;
    }

    public UUID getSolutionCodeField() {
        return solutionCodeField;
    }

    public void setSolutionCodeField(UUID solutionCodeField) {
        this.solutionCodeField = solutionCodeField;
    }

    public String getTextField() {
        return textField;
    }

    public void setTextField(String textField) {
        this.textField = textField;
    }
}
