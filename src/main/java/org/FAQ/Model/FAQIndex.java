package org.FAQ.Model;

import org.FAQ.DB.FAQDao;

import java.util.List;
import java.util.UUID;

/**
 * Created by jahangiri on 07/18/2017.
 */
public class FAQIndex {
    public FAQIndex(Integer id,String index) {
        this.setId(id);
        this.setIndex(index);
    }
    private String index;
    private Integer id;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
