package kr.pongponglabs.pongpong.sql;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String code;

    @Column()
    private String state;

    @Column(columnDefinition = "TEXT")
    private String result;

    public User(String fileName, String state, String result) {
        this.fileName = fileName;
        this.state = state;
        this.result = result;
    }

    public User(String fileName, String code) {
        this.fileName = fileName;
        this.code = code;
        this.state = "Ready";
    }

    public User() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
