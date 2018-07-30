package org.FAQ.Service;

import Utils.StringUtils;
import org.FAQ.Bean.IssueBean;
import org.FAQ.Constants.PostEnum;
import org.FAQ.DB.FAQDao;
import org.FAQ.Model.FAQIndex;
import org.FAQ.Model.FAQModel;
import org.FAQ.Model.SolutionModel;
import org.FAQ.Model.TextModel;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class indexService {
    public boolean findNewIndexesAndApply(IssueBean bean) {
        boolean opFlag = true;
        JhazmService service = JhazmService.getInstance();
        Map<String, PostEnum> wordsDef = new HashMap<>();
        TextModel textModel = service.getTextModel(bean.getSubject() + " " + bean.getSolution());
        textModel.getSentences().stream().forEach(s -> s.getWords().forEach(w -> wordsDef.put(w.getWord(), w.getTag())));

        List<String> outList = new ArrayList<>();
        wordsDef.entrySet().stream().forEachOrdered(s -> {
            if (s.getValue().equals(PostEnum.NOUN))
                outList.add(s.getKey());
        });
        String collect = outList.stream().map(s -> s.toLowerCase()).collect(Collectors.joining("', '", "'", "'"));

        FAQDao faqDao = new FAQDao();
        Map<Integer, String> indexes = faqDao.getIndexes(collect);

        for (Map.Entry<Integer, String> x : indexes.entrySet()) {
            if (outList.contains(x.getValue()))
                outList.remove(x.getValue());
        }
        boolean inserted = addNewIndexes(faqDao, outList);
        Map<String, UUID> qsKeyPairs =new HashMap<>();
        if (inserted) {
            Map<String, Integer> indexList = faqDao.getIndexes();
            if (!bean.getSubjectID().equals("") && !bean.getSid().equals(""))
                qsKeyPairs = faqDao.updateCurrentIssue(bean);
            else {
                qsKeyPairs = faqDao.defineNewIssue(bean, 0);
            }
            Map<String, UUID> finalQsKeyPairs = qsKeyPairs;
            outList.forEach(s -> {
                Integer indexID = indexList.get(s.toLowerCase());
                if (indexID!=null) {
                    UUID questionID = finalQsKeyPairs.get("Q");
                    UUID solutionID = finalQsKeyPairs.get("S");
                    if (!faqDao.mapexist(indexID,questionID))
                        FAQDao.insertIndexXQSPair(null, indexID, questionID, 0);
                    if(!faqDao.mapexist(indexID,solutionID))
                        FAQDao.insertIndexXQSPair(null, indexID, solutionID, 0);
                }
            });
        }
        return opFlag;
    }

    public boolean addNewIndexes(FAQDao faqDao, List<String> outList) {
        boolean bSucceed = true;
        for (String x :
                outList) {
            boolean b = faqDao.addNewIndexes(null, x, 0);
            if (!b) {
                bSucceed = false;
                break;
            }
        }
        return bSucceed;
    }
}
