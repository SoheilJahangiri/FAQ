package org.FAQ.Service;

import Utils.StringUtils;
import edu.stanford.nlp.ling.TaggedWord;
import jhazm.*;
import jhazm.tokenizer.WordTokenizer;
import org.FAQ.Constants.PostEnum;
import org.FAQ.Model.SentenceModel;
import org.FAQ.Model.TextModel;
import org.FAQ.Model.WordModel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JhazmService {
    private static JhazmService jhazmService;
    private static POSTagger posTagger;
    private static WordTokenizer tokenizer;
    private static Normalizer normalizer;
    private static Lemmatizer lemmatizer;
    private static Stemmer stemmer;
    private static DependencyParser dependencyParser;
    public JhazmService() {
        try {
            posTagger = new POSTagger(new File(getClass().getResource("/models/persian.tagger").getPath()).getAbsolutePath());
            tokenizer = new WordTokenizer(new File(getClass().getResource("/data/verbs.dat").getPath()).getAbsolutePath());
            normalizer = new Normalizer();
            lemmatizer = new Lemmatizer(new File(getClass().getResource("/data/words.dat").getPath()).getAbsolutePath(),
                                        new File(getClass().getResource("/data/verbs.dat").getPath()).getAbsolutePath());
            stemmer = new Stemmer();
            dependencyParser = new DependencyParser(null, null, new File(getClass().getResource("/models/langModel.mco").getPath()).getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static JhazmService getInstance(){
        if (jhazmService==null)
            jhazmService=new JhazmService();
        return jhazmService;
    }
    public TextModel getTextModel(String text){
        TextModel textModel=new TextModel();
        textModel.setText(text);

        String normalize=normalizer.run(text);
        List<String> sentenceList = StringUtils.SplitToSentences(normalize);
        SentenceModel sentenceModel;
        for (String sentence : sentenceList) {
            sentenceModel = new SentenceModel();
            sentenceModel.setSentence(sentence);

            List<String> tokens = tokenizer.tokenize(sentence);
            List<TaggedWord> tagged = posTagger.batchTag(tokens);

            List<WordModel> wordModelList = getWordModelList(tagged);
            sentenceModel.setWords(wordModelList);
            textModel.getSentences().add(sentenceModel);
        }
        return textModel;
    }
    private List<WordModel> getWordModelList(List<TaggedWord> tagged) {
        List<WordModel> wordModels = new ArrayList<>();
        WordModel wordModel;
        for (TaggedWord taggedWord : tagged) {
            wordModel = new WordModel();
            String word = taggedWord.word();
            String tag = taggedWord.tag();
            String lemma = lemmatizer.lemmatize(word);
            String stem = stemmer.stem(word);
            wordModel.setWord(word);
            wordModel.setStem(stem);
            wordModel.setTag(PostEnum.findPostEnum(tag));
            wordModel.setLemma(lemma);
            wordModel.setStopWord(false);

            wordModels.add(wordModel);
        }
        return wordModels;
    }
    public String normalize(String text) {
        return normalizer.run(text);
    }

    public String stemmer(String text) {
        return stemmer.stem(text);
    }

    public String lemmatize(String text) {
        return lemmatizer.lemmatize(text);
    }
}
