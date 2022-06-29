package kr.co.community.model.enums;

public enum Author {
    ADMIN("관리자"), MEMBER("회원");

    private final String author;

    Author(String author) {
        this.author = author;
    }

    public String authority(){
        return this.name();
    }

    public String getAuthor(){
        return this.author;
    }
}
