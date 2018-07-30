package org.FAQ.Controller;

import org.FAQ.Bean.CriteriaBean;
import org.FAQ.Bean.FAQResultBean;
import org.FAQ.Bean.IssueBean;
import org.FAQ.Bean.SolutionBean;
import org.FAQ.Model.FAQModel;
import org.FAQ.Service.criteriaService;
import org.FAQ.Service.indexService;
import org.FAQ.Service.searchService;
import org.apache.lucene.queryparser.classic.ParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jahangiri on 07/11/2017.
 */
@Controller
public class FAQController {
    List<FAQModel> faqIndexList = new ArrayList<>();

    @PostConstruct
    private void init() {
        System.out.print("init called on Application start");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String initPage(ModelMap model) {
        model.addAttribute("message", "welcome to Smart FAQ system");
        return "index";
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public
    @ResponseBody
    List<FAQResultBean> search(@RequestBody CriteriaBean criteriaBean) throws IOException, ParseException {
        List<FAQResultBean> outval = new ArrayList<>();
        try {
            List<String> normalizeCriteria = criteriaService.normalize(criteriaBean.getCriteria());
            searchService searchService = new searchService(normalizeCriteria.stream().map(s -> s.toString()).collect(Collectors.joining("', '", "'", "'")));
            outval = searchService.searchSubjectAndSolution(normalizeCriteria);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outval;
    }

    @RequestMapping(value = "/issuer", method = RequestMethod.POST)
    public
    @ResponseBody
    String issuer(@RequestBody IssueBean issueBean) {
        indexService idx=new indexService();
        boolean opFlag = idx.findNewIndexesAndApply(issueBean);
        this.faqIndexList.clear();
        return String.valueOf(opFlag);
    }

    @RequestMapping(value = "/scoreUp", method = RequestMethod.POST)
    public
    @ResponseBody
    String scoreUp(@RequestBody SolutionBean scoreBean) {
        //TODO: to be implemented
        boolean b = FAQModel.thumpsUp(scoreBean);
        return "";
    }

    @RequestMapping(value = "/scoreDown", method = RequestMethod.POST)
    public
    @ResponseBody
    String scoreDown(@RequestBody SolutionBean scoreBean) {
        //TODO: to be implemented
        boolean b = FAQModel.thumpsDown(scoreBean);
        return "";
    }
}
