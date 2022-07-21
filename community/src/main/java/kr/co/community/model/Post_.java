package kr.co.community.model;

import javax.persistence.GeneratedValue;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.Instant;
import java.util.List;

@StaticMetamodel(Post.class)
public class Post_ {
    public static volatile SingularAttribute<Post, Long> id;
    public static volatile SingularAttribute<Post, String> title;
    public static volatile SingularAttribute<Post, String> content;
    public static volatile SingularAttribute<Post, Integer> viewCnt;
    public static volatile SingularAttribute<Post, Instant> createdAt;
    public static volatile SingularAttribute<Post, Instant> updatedAt;
    public static volatile SingularAttribute<Post, List<File>> files;
    public static volatile SingularAttribute<Post, List<Comment>> comments;
    public static volatile SingularAttribute<Post, Member> member;

}
