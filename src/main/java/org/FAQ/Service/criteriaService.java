package org.FAQ.Service;

import org.FAQ.Constants.PostEnum;
import org.FAQ.Model.TextModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class criteriaService {
    public static List<String> normalize(String criteria) {
        JhazmService service = new JhazmService();
        String normalize = service.normalize(criteria);
        TextModel textModel = service.getTextModel(normalize);
        List<String> criteriaList = new ArrayList<>();
        /*textModel.getSentences().forEach(sm -> criteriaList.add(sm.getWords().stream().filter(w -> w.getTag().equals(PostEnum.NOUN)).map(w1 -> w1.getWord()).collect(Collectors.joining()).toString()));*/
        textModel.getSentences().forEach(sm -> sm.getWords().forEach(word-> {
            if (word.getTag().equals(PostEnum.NOUN))
                criteriaList.add(word.getWord());
        }));
        return criteriaList;
    }
}
