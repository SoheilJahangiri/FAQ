package org.FAQ.Model;

import java.util.ArrayList;
import java.util.List;

public class TextModel {
    private String text;
    private List<SentenceModel> sentences=new ArrayList<>();

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<SentenceModel> getSentences() {
        return sentences;
    }

    public void setSentences(List<SentenceModel> sentences) {
        this.sentences = sentences;
    }
}
