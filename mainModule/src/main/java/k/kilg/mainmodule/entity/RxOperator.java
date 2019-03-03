package k.kilg.mainmodule.entity;

public class RxOperator {

    private String className;
    private String title;
    private String docs;
    private String docsLong;
    private String code;

    public RxOperator(String className, String title, String docs, String code) {
        this.className = className;
        this.title = title;
        this.docs = docs;
        this.code = code;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String body) {
        this.code = body;
    }

    public String getDocs() {
        return docs;
    }

    public void setDocs(String docs) {
        this.docs = docs;
    }
}
