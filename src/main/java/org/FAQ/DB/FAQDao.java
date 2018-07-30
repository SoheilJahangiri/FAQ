package org.FAQ.DB;

import Utils.HelperClass;
import org.FAQ.Bean.FAQBean;
import org.FAQ.Bean.IssueBean;
import org.FAQ.Bean.SolutionBean;
import org.FAQ.Constants.PostEnum;
import org.FAQ.Constants.TableName;
import org.FAQ.Model.FAQIndex;
import org.FAQ.Model.FAQModel;
import org.FAQ.Model.SolutionModel;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by jahangiri on 07/18/2017.
 */
public class FAQDao {
    private static Connection getConnection() {
        Connection cn = null;
        try {
            Class.forName("org.h2.Driver");
            cn = DriverManager.getConnection("jdbc:h2:C:/FAQ/db/FBTIFAQ", "sa", "");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return cn;
        }
    }

    public List<FAQModel> getAnswers(String ids) {
        List<FAQModel> indexList = new ArrayList<>();
        Connection cn = getConnection();
        String query = "select q.TEXT SUBJECT,q.SCORE,q.SUBJECTID,s.SID, s.TEXT SOLUTION from QUESTION q left join SOLUTION s on q.SUBJECTID=s.SUBJECTID and (s.SUBJECTID in ("+ ids +") or s.SID in ("+ ids +"))";
        Statement statement = null;
        try {
            statement = cn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<FAQBean> beanList = new ArrayList<>();
            while (rs.next()) {
                beanList.add(new FAQBean(rs.getString("SUBJECT"), Integer.parseInt(rs.getString("SCORE")), UUID.fromString(rs.getString("SUBJECTID")), rs.getString("SID") != null ? UUID.fromString(rs.getString("SID")) : null, rs.getString("SOLUTION")));
            }
            List<FAQBean> distinctList = beanList.stream().filter(HelperClass.distinctByKey(p -> p.getSubjectID())).collect(Collectors.toList());
            distinctList.stream().forEach(faqBean -> {
                FAQModel model = new FAQModel();
                model.setCodeField(faqBean.getSubjectID());
                model.setSubjectField(faqBean.getSubjectText());
                model.setScore(faqBean.getScore());
                beanList.stream().filter(origBean -> origBean.getSubjectID().equals(faqBean.getSubjectID()))
                        .forEach(s -> {
                            if (s.getSolutionText() != null && !s.getSolutionText().equals("null"))
                                model.getSolutionFields().add(new SolutionModel(s.getSID(), s.getSubjectID(), s.getSolutionText()));
                        });
                indexList.add(model);
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cn != null)
                try {
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return indexList;
    }

    public Map<Integer, String> getIndexes(String indexes) {
        Map<Integer, String> indexList = new HashMap<>();
        Connection cn = getConnection();
        String query = "select i.ID IDENTITY ,i.indexword INDEX from indextable i where lower (i.indexword) in (" + indexes + ")";
        Statement statement = null;
        try {
            statement = cn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                indexList.put(rs.getInt("IDENTITY"), rs.getString("INDEX"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cn != null)
                try {
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return indexList;
    }

    public Map<String, Integer> getIndexes() {
        Map<String, Integer> indexList = new HashMap<>();
        Connection cn = getConnection();
        String query = "select i.ID IDENTITY ,i.indexword INDEX from indextable i";
        Statement statement = null;
        try {
            statement = cn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                indexList.put(rs.getString("INDEX"), rs.getInt("IDENTITY"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (cn != null)
                try {
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return indexList;
    }

    public Map<String, UUID> defineNewIssue(IssueBean issueBean, int tryTime) {
        if (tryTime == 0) tryTime = 1;
        Map<String, UUID> outMap = new HashMap<>();
        boolean defined = false;
        UUID newSubjectID = UUID.randomUUID();
        UUID solutionID;
        if (checkIfValidUUID(newSubjectID, TableName.Question)) {
            outMap.put("Q", newSubjectID);
            Connection cn = getConnection();
            String query = "INSERT INTO QUESTION(TEXT,SCORE,SUBJECTID) VALUES('" + issueBean.getSubject() + "','" + 1 + "','" + newSubjectID + "')";
            Statement statement;
            try {
                cn.setAutoCommit(false);
                statement = cn.createStatement();
                int i = statement.executeUpdate(query);
                if (i > 0) {
                    solutionID = submitNewSolutions(cn, newSubjectID, issueBean.getSolution(), 0);
                    if (!solutionID.equals(null)) {
                        outMap.put("S", solutionID);
                        defined = true;
                    } else {
                        defined = false;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
                try {
                    cn.rollback();
                    defined = false;
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    defined = false;
                }
            } finally {
                if (cn != null) {
                    try {
                        cn.commit();
                        cn.setAutoCommit(true);
                        cn.close();
                        defined = true;
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            if (tryTime <= 10) //prevent from producing duplicate key (UUID)
                defineNewIssue(issueBean, ++tryTime);
        }
        return defined ? outMap : null;
    }

    private UUID submitNewSolutions(Connection cn, UUID subjectID, String solution, int tryTime) {
        boolean defined = false;
        if (tryTime == 0) tryTime = 1;
        UUID solutionID = UUID.randomUUID();
        if (checkIfValidUUID(solutionID, TableName.Solution)) {
            String query = "INSERT INTO SOLUTION(SUBJECTID,TEXT,SCORE,SID) VALUES('" + subjectID + "','" + solution + "','" + 1 + "','" + solutionID + "')";
            Statement statement;
            try {
                statement = cn.createStatement();
                int i = statement.executeUpdate(query);
                if (i > 0) {
                    defined = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                defined = false;
            }
        } else {
            if (tryTime <= 10)
                submitNewSolutions(cn, subjectID, solution, ++tryTime);
        }
        return defined ? solutionID : null;
    }

    private boolean checkIfValidUUID(UUID newID, TableName tblName) {
        boolean valid = true;
        Connection cn = getConnection();
        String query = "";
        if (tblName.equals(TableName.Question))
            query = "select * from question q where q.subjectid='" + newID + "';";
        else if (tblName.equals(TableName.Solution))
            query = "select * from solution s where s.SID='" + newID + "';";
        Statement statement;
        try {
            statement = cn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next())
                valid = false;
        } catch (SQLException e) {
            valid = false;
        } finally {
            if (cn != null)
                try {
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            return valid;
        }
    }

    public Map<String, UUID> updateCurrentIssue(IssueBean issueBean) {
        Connection cn = getConnection();
        Map<String, UUID> outMap = new HashMap<>();
        Statement statement;
        Statement statement1;
        String query = "UPDATE QUESTION Q SET TEXT='" + issueBean.getSubject() + "' WHERE Q.SUBJECTID='" + issueBean.getSubjectID() + "'";
        try {
            cn.setAutoCommit(false);
            statement = cn.createStatement();
            int i = statement.executeUpdate(query);
            if (i > 0) {
                statement.close();
                outMap.put("Q", UUID.fromString(issueBean.getSubjectID()));
                query = "UPDATE SOLUTION S SET SCORE='1', TEXT='" + issueBean.getSolution() + "' WHERE S.SID='" + issueBean.getSid() + "'";
                statement1 = cn.createStatement();
                int i1 = statement1.executeUpdate(query);
                if (i1 > 0) {
                    statement1.close();
                    outMap.put("S", UUID.fromString(issueBean.getSid()));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            outMap.clear();
        } finally {
            if (cn != null)
                try {
                    cn.setAutoCommit(true);
                    cn.commit();
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
        return outMap;
    }

    public boolean thumpsUp(SolutionBean scoreBean) {
        return false;
    }

    public boolean thumpsDown(SolutionBean scoreBean) {
        return false;
    }

    public boolean addNewIndexes(Connection cn, String newIndex, Integer tryTime) {
        boolean defined = false;
        if (tryTime == 0) tryTime = 1;
        String query = "INSERT INTO INDEXTABLE(INDEXWORD) VALUES(lower('" + newIndex + "'))";
        Statement statement;
        try {
            if (cn==null) {
                cn = getConnection();
            }
            statement = cn.createStatement();
            int i = statement.executeUpdate(query);
            if (i > 0) {
                defined = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (tryTime <= 3)
                addNewIndexes(cn, newIndex, ++tryTime);
            defined = false;
        }
        return defined;
    }

    public static boolean insertIndexXQSPair(Connection cn, Integer idxID, UUID answerID, Integer tryTime) {
        boolean defined = false;
        if (tryTime == 0) tryTime = 1;
        String query = "INSERT INTO WORD_ANSWER_MAP(INDEXID,ANSWERID) VALUES(lower(" + idxID + "),'" + answerID + "')";
        Statement statement;
        try {
            if (cn==null) {
                cn = getConnection();
            }
            statement = cn.createStatement();
            int i = statement.executeUpdate(query);
            if (i > 0) {
                defined = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (tryTime <= 3)
                insertIndexXQSPair(cn, idxID, answerID, ++tryTime);
            defined = false;
        }
        return defined;
    }

    public String getAnswersIDs(Map<Integer, String> indexes) {
        List<String> outList = new ArrayList<>();
        String collect = indexes.entrySet().stream().map(i -> i.getKey().toString()).collect(Collectors.joining("', '", "'", "'"));
        String query = "select w.answerid ANSWERID from WORD_ANSWER_MAP w where w.indexid in (" + collect + ")";
        boolean valid = true;
        Connection cn = getConnection();
        Statement statement;
        try {
            statement = cn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next())
                outList.add(rs.getString("ANSWERID"));
        } catch (SQLException e) {
            valid = false;
        } finally {
            if (cn != null)
                try {
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            return outList.stream().collect(Collectors.joining("', '", "'", "'"));
        }
    }

    public boolean mapexist(Integer indexID, UUID id) {
        boolean valid = false;
        Connection cn = getConnection();
        String query = "";
        query = "select * from WORD_ANSWER_MAP w where w.indexid='" + indexID + "' and w.answerid='"+ id +"';";
        Statement statement;
        try {
            statement = cn.createStatement();
            ResultSet rs = statement.executeQuery(query);
            if (rs.next())
                valid = true;
        } catch (SQLException e) {
            valid = true;
        } finally {
            if (cn != null)
                try {
                    cn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            return valid;
        }
    }
}
