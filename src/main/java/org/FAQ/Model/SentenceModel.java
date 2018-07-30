package org.FAQ.Model;

import java.util.ArrayList;
import java.util.List;

public class SentenceModel {
    private String sentence;
    private List<WordModel> words=new ArrayList<>();

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public List<WordModel> getWords() {
        return words;
    }

    public void setWords(List<WordModel> words) {
        this.words = words;
    }
}
