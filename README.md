# FAQ system based on Jhazm and Apache Lucene.

this application is a production example of using Jhazm library (Java hazm) and lucene library to create a smart FAQ with search
and create capabilities for Persian language but you can add your language model file into resources and support different
languages.
<br/>
to be able to use this application you need do the following:<br/>
&nbsp;&nbsp;1.include Jhazm library into POM by installing Jhazm library into your own maven repository.<br/>
&nbsp;&nbsp;&nbsp;&nbsp;1.1.Download Jhazm from "https://github.com/mojtaba-khallash/JHazm"<br/>
&nbsp;&nbsp;&nbsp;&nbsp;1.2.mvn install:install-file -Dfile="C:\Jhazm.jar" -DgroupId=jhazm -DartifactId=jhazm -Dversion=1.2 -Dpackaging=JAR<br/>
&nbsp;&nbsp;2.database with these tables (based on H2 but you can choose any other db as long as you change the db info in FAQDao.class):<br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.1.INDEXTABLE (ID Identity PrimaryKey, INDEXWORD varchar(50))<br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.2.QUESTION (ID Identity PrimaryKey, TEXT CLOB, SCORE INT, SUBJECTID UUID)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.3.SOLUTION (ID Identity PrimaryKey, SUBJECTID UUID, TEXT CLOB, SCORE int, SID UUID)<br/>
&nbsp;&nbsp;&nbsp;&nbsp;2.4.WORD_ANSWER_MAP (ID Identity PrimaryKey, INDEXID int, ANSWERID UUID, SCORE)<br/>
    
