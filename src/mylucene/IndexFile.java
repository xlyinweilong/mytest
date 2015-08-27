package mylucene;

import java.io.File;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class IndexFile {

    protected String[] ids = {"1", "2"};

    protected String[] content = {"楼挺高的", "地铁真堵"};

    protected String[] titles = {"大头爸爸小偷儿子", "那些年我们一起犯过的2"};

    private Directory dir;

    /**
     * 初始添加文档
     *
     * @throws Exception
     *
     */
    public static void main(String[] args) throws Exception {
        new IndexFile().doXX();
    }

    public void doXX() throws Exception {
        String pathFile = "D://lucene/index";
        dir = FSDirectory.open(new File(pathFile));
        IndexWriter writer = getWriter();
        for (int i = 0; i < ids.length; i++) {
            Document doc = new Document();
            //StringField不会被分词器分词，TextField会被分词
            doc.add(new StringField("id", ids[i], Store.YES));
            doc.add(new TextField("title", titles[i], Store.YES));
            doc.add(new TextField("content", content[i], Store.YES));
            writer.updateDocument(new Term("id", ids[i]), doc);
            // writer.deleteDocuments(new Term("id", ids[i])); //删除索引
//            writer.addDocument(doc);
        }
        writer.close();
    }

    /**
     * 获得IndexWriter对象
     *
     * @return
     * @throws Exception
     */
    public IndexWriter getWriter() throws Exception {
        Analyzer analyzer = new CJKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LATEST, analyzer);
        // 最大缓存文档数,控制写入一个新的segment前内存中保存的document的数目
        iwc.setMaxBufferedDocs(100);
        // 控制一个segment中可以保存的最大document数目，值较大有利于追加索引的速度，默认Integer.MAX_VALUE，无需修改。
        iwc.setMaxBufferedDocs(Integer.MAX_VALUE);
        return new IndexWriter(dir, iwc);
    }
}
