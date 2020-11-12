package kr.pongponglabs.pongpong;

public class Language {
    public enum Type {
        Python,
        Java,
        C,
        CPP,
        Other
    }

    public Type type;

    public Language(String fileName) {
        if(fileName.contains("python"))
            type = Type.Python;
        else if(fileName.contains("java"))
            type = Type.Java;
        else if(fileName.contains("cpp"))
            type = Type.CPP;
        else if(fileName.contains("c"))
            type = Type.C;
        else
            type = Type.Other;
    }

    public Language(Type type) {
        this.type = type;
    }
}
