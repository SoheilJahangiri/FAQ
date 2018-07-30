package org.FAQ.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public enum  PostEnum {
    ADJECTIVE("Adjective", "صفت"),
    ADVERB("Adverb", "قید"),
    CONJUNCTION("", "?"),
    CLASSIFIER("", "?"),
    DETERMINER("", "?"),
    INTERJECTION("", "?"),
    NOUN("", "اسم"),
    NUMBER("", "عدد"),
    PRE_POSITION("PrePosition", "?"),
    POST_POSITION("", "?"),
    PRONOUN("ProNoun", "?"),
    PUNCTUATION("", "?"),
    RESIDUAL("", "?"),
    VERB("", "فعل"),
    UNKNOWN("", "نامشخص");

    private String Description;
    private String labelFa;

    private static Map<PostEnum, String[]> enumListMap = getMap();
    PostEnum(String labelFa, String Description) {
        this.labelFa = labelFa;
        this.Description = Description;
    }
    private static Map<PostEnum, String[]> getMap() {
        Map<PostEnum, String[]> postEnumMap = new HashMap<>();
        postEnumMap.put(PostEnum.ADJECTIVE, new String[]{"ADJ", "AJ"});
        postEnumMap.put(PostEnum.ADVERB, new String[]{"ADV"});
        postEnumMap.put(PostEnum.CLASSIFIER, new String[]{"CL"});
        postEnumMap.put(PostEnum.CONJUNCTION, new String[]{"CONJ"});
        postEnumMap.put(PostEnum.DETERMINER, new String[]{"DET"});
        postEnumMap.put(PostEnum.INTERJECTION, new String[]{"INT"});
        postEnumMap.put(PostEnum.NOUN, new String[]{"N"});
        postEnumMap.put(PostEnum.NUMBER, new String[]{"NUM"});
        postEnumMap.put(PostEnum.PUNCTUATION, new String[]{"PUNC"});
        postEnumMap.put(PostEnum.PRONOUN, new String[]{"PRO", "PR"});
        postEnumMap.put(PostEnum.PRE_POSITION, new String[]{"P", "PREP"});
        postEnumMap.put(PostEnum.POST_POSITION, new String[]{"POSTP"});
        postEnumMap.put(PostEnum.RESIDUAL, new String[]{"RES"});
        postEnumMap.put(PostEnum.VERB, new String[]{"V"});
        postEnumMap.put(PostEnum.UNKNOWN, new String[]{"QUA", "IF", "OH", "AR", "DEFAULT", "PS", "PP", "SPEC", "MORP", "MQUA", "OHH", "NP", "MS"});
        return postEnumMap;
    }
    public static PostEnum findPostEnum(String searchString) {
        for (Map.Entry<PostEnum, String[]> postEnumEntry : enumListMap.entrySet()) {
            for (String s : postEnumEntry.getValue()) {
                if (Objects.equals(s.toUpperCase(), searchString.toUpperCase())) {
                    return postEnumEntry.getKey();
                }
            }
        }

        return PostEnum.valueOf(searchString.toUpperCase());
    }
}
