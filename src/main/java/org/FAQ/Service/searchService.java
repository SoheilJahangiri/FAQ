package org.FAQ.Service;

import Utils.HelperClass;
import org.FAQ.Bean.FAQResultBean;
import org.FAQ.DB.FAQDao;
import org.FAQ.Model.FAQIndex;
import org.FAQ.Model.FAQModel;
import org.FAQ.Model.SolutionModel;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jahangiri on 07/29/2017.
 */
public class searchService {
    static StandardAnalyzer analyzer;
    static Directory index;
    static IndexWriterConfig config;
    static IndexWriter w;
    static IndexSearcher searcher;

    public searchService(String criteria) throws IOException {
        analyzer = new StandardAnalyzer();
        config = new IndexWriterConfig(analyzer);
        index = new RAMDirectory();
        w = new IndexWriter(index, config);
        FAQDao faqDao=new FAQDao();
        /*Map<String, Integer> indexList = faqDao.getIndexes(criteria);*/
        Map<Integer, String> indexList = faqDao.getIndexes(criteria);
        for (Map.Entry<Integer, String> i : indexList.entrySet()) {
            addDoc(w, i.getValue());
            /*if (i.getSolutionFields() != null && i.getSolutionFields().size() > 0) {
                for (SolutionModel sModel :
                        i.getSolutionFields()) {
                    String textField = sModel.getTextField();
                    UUID SID=sModel.getSID();
                    addDoc(w, i.getSubjectField(), i.getCodeField().toString(),i.getScore(), textField,SID);
                }
            } else
                addDoc(w, i.getSubjectField(), i.getCodeField().toString(),i.getScore(), null,null);*/
        }
        w.close();
    }

    public static void addDoc(IndexWriter w, String keyword) {
        Document doc = new Document();
        doc.add(new TextField("index", keyword, Field.Store.YES));
        /*doc.add(new TextField("subjectID", subjectID, Field.Store.YES));
        doc.add(new TextField("score", String.valueOf(score), Field.Store.YES));
        if (solution != null && !solution.equals("null"))
            doc.add(new TextField("solution", solution, Field.Store.YES));
        if (solutionID != null && !solutionID.equals("null"))
            doc.add(new TextField("solutionID", String.valueOf(solutionID), Field.Store.YES));*/
        try {
            w.addDocument(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<FAQResultBean> searchSubjectAndSolution(List<String> criteria) throws ParseException, IOException {
        List<FAQResultBean> finalFAQResultBean = new ArrayList<>();
        for (String c : criteria) {
            Query q;
            List<FAQModel> faqModels = new ArrayList<>();
            q = new QueryParser("index", analyzer).parse(c);
            List<FAQModel> subjectSearchResult = search(q);
            subjectSearchResult.stream().forEach(faqModel -> faqModels.add(new FAQModel(faqModel.getCodeField(), faqModel.getSubjectField(), faqModel.getSolutionFields())));
            convertFAQModelToBean(faqModels).forEach(faqResultBean -> finalFAQResultBean.add(faqResultBean));
        }
        return finalFAQResultBean;
    }

    public static List<FAQModel> search(Query q) throws IOException {
        int hitsPerPage = 20;
        IndexReader reader = DirectoryReader.open(index);
        searcher = new IndexSearcher(reader);
        TopDocs docs = searcher.search(q, hitsPerPage);
        ScoreDoc[] hits = docs.scoreDocs;
        return returnResult(hits);
    }

    public static List<FAQModel> returnResult(ScoreDoc[] hits) throws IOException {
        List<List> outList = new ArrayList<>();
        List<FAQIndex> faqIndices=new ArrayList<>();
        List<String> idxList=new ArrayList<>();
        System.out.println("Found " + hits.length + " hits.");
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            idxList.add(d.get("index"));
        }
        FAQDao faqDao=new FAQDao();
        Map<Integer, String> indexes = faqDao.getIndexes(idxList.stream().map(s -> s.toLowerCase()).collect(Collectors.joining("', '", "'", "'")));
        indexes.forEach((id, index) -> faqIndices.add(new FAQIndex(id,index)));
        String answerIDs = faqDao.getAnswersIDs(indexes);
        List<FAQModel> answers = faqDao.getAnswers(answerIDs);
        return answers;
    }

    private static List<FAQResultBean> convertFAQModelToBean(List<FAQModel> faqModels) {
        List<FAQResultBean> outList = new ArrayList<>();
        List<FAQModel> o=new ArrayList<>();
        List<FAQModel> distinctList = faqModels.stream().filter(HelperClass.distinctByKey(p -> p.getCodeField())).collect(Collectors.toList());
        distinctList.stream().forEach(faqModel -> {
            FAQModel model = new FAQModel();
            model.setCodeField(faqModel.getCodeField());
            model.setSubjectField(faqModel.getSubjectField());
            model.setScore(faqModel.getScore());
            List<SolutionModel> sModels = new ArrayList<>();
            faqModels.stream().filter(origBean -> origBean.getCodeField().equals(faqModel.getCodeField()))
                    .forEach(s -> {
                        s.getSolutionFields().stream().forEach(solutionModel -> {
                            if (solutionModel.getTextField() != null && !solutionModel.getTextField().equals("null"))
                                sModels.add(new SolutionModel(solutionModel.getSID(), solutionModel.getSolutionCodeField(), solutionModel.getTextField()));
                        });
                    });
            List<SolutionModel> solutionDistinctList = sModels.stream().filter(HelperClass.distinctByKey(p -> p.getSID())).collect(Collectors.toList());
            solutionDistinctList.stream().forEach(solutionModel -> model.getSolutionFields().add(new SolutionModel(solutionModel.getSID(), solutionModel.getSolutionCodeField(), solutionModel.getTextField())));
            o.add(model);
        });
        String s="";
        o.stream().forEach(faqModel -> outList.add(new FAQResultBean(faqModel.getSubjectField(), faqModel.getScore(), faqModel.getCodeField(), faqModel.getSolutionFields())));
        return outList;
    }

    private static void countWordInDocs() {

    }
}
