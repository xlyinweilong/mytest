package mylucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopFieldCollector;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * http://lucene.apache.org/core/4_3_0/core
 *
 * @author Administrator
 *
 */
public class IndexSearch {

    public static void main(String args[]) throws Exception {
        String filePath = "D://lucene/index";
        Directory dir = FSDirectory.open(new File(filePath));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        new IndexSearch().searToHighlighter(new CJKAnalyzer(), searcher);
//         IndexSearcher searcher = new IndexSearcher(reader);
//         Term term = new Term("content", "堵车");
//         TermQuery query = new TermQuery(term);
//         TopDocs topdocs = searcher.search(query, 1);
//         ScoreDoc[] scoreDocs = topdocs.scoreDocs;
//         System.out.println("查询结果总数---" + topdocs.totalHits + "最大的评分--" +
//         topdocs.getMaxScore());
//         for (int i = 0; i < scoreDocs.length; i++) {
//         int doc = scoreDocs[i].doc;
//         Document document = searcher.doc(doc);
//         System.out.println("content====" + document.get("content"));
//         System.out.println("id--" + scoreDocs[i].doc + "---scors--" +
//         scoreDocs[i].score + "---index--" + scoreDocs[i].shardIndex);
//         }
//         reader.close();
    }

    public void searToHighlighter(Analyzer analyzer, IndexSearcher searcher) throws IOException, InvalidTokenOffsetsException, ParseException {
        // Term term =new Term("sex", "男生");//查询条件，意思是我要查找性别为“男生”的人
        // PrefixQuery query =new PrefixQuery(term);
        int pageSize = 1;
        int pageStartIndex = 0;
        String[] fields = {"desc", "contents"};
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse("我们");
        Sort sort = new Sort(new SortField("price", SortField.Type.DOUBLE, true), new SortField("count", SortField.Type.DOUBLE, false));// 排序
//        TermQuery cityQuery = new TermQuery(new Term("city", "莆田"));
        BooleanQuery booleanQuery = new BooleanQuery();
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
//        booleanQuery.add(cityQuery, Occur.SHOULD);
        TopDocs docs = searcher.search(booleanQuery, pageSize, sort);// 查找
        ScoreDoc scoreDoc = docs.scoreDocs[pageStartIndex];
        TopDocs hits = searcher.searchAfter(scoreDoc, query, pageSize, sort);
        // Query query= MultiFieldQueryParser.parse(q, fields, clauses,
        // analyzer);
        System.out.println("searcherDoc()->数：" + docs.totalHits);
        QueryScorer scorer = new QueryScorer(query);
        Formatter formater = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        Highlighter highlight = new Highlighter(formater, scorer);
        int seq = 0;
        for (ScoreDoc doc : hits.scoreDocs) {// 获取查找的文档的属性数据
            seq++;
            int docID = doc.doc;
            Document document = searcher.doc(docID);
            String str = "序号：" + seq + ",ID:" + document.get("id") + ",姓名：" + document.get("name") + "，性别：";
            String value = document.get("title");
            if (value != null) {
                TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(value));
                String str1 = highlight.getBestFragment(tokenStream, value);
                str = str + str1;
            }
            System.out.println("查询出人员:" + str);
        }
    }
}
