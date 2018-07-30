package org.FAQ.Model;

import org.FAQ.Constants.PostEnum;

public class WordModel {
    private String word;
    private String lemma;
    private String stem;
    private PostEnum tag;
    private Integer label;
    private Integer newLabel;
    private boolean isStopWord;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getLemma() {
        return lemma;
    }

    public void setLemma(String lemma) {
        this.lemma = lemma;
    }

    public String getStem() {
        return stem;
    }

    public void setStem(String stem) {
        this.stem = stem;
    }

    public PostEnum getTag() {
        return tag;
    }

    public void setTag(PostEnum tag) {
        this.tag = tag;
    }

    public Integer getLabel() {
        return label;
    }

    public void setLabel(Integer label) {
        this.label = label;
    }

    public Integer getNewLabel() {
        return newLabel;
    }

    public void setNewLabel(Integer newLabel) {
        this.newLabel = newLabel;
    }

    public boolean isStopWord() {
        return isStopWord;
    }

    public void setStopWord(boolean stopWord) {
        isStopWord = stopWord;
    }
}
