package kr.co.community.specification;

import kr.co.community.model.Post;
import kr.co.community.model.Post_;
import kr.co.community.model.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PostSpecification {
    public static Specification<Post> search(SearchParam searchParam){

        return ((root, query, cb) -> {
            Class clazz = query.getResultType();
            if(!clazz.equals(Long.class) && !clazz.equals(long.class)){

            }

            Predicate result = null;
            List<Predicate> predicates = new ArrayList<>();

            if(searchParam != null){

                if(searchParam.getKeyword() != null && !searchParam.getKeyword().equals("")){
                    predicates.add(cb.like(root.get(Post_.title), "%" + searchParam.getKeyword() + "%"));
                    predicates.add(cb.like(root.get(Post_.content), "%" + searchParam.getKeyword() + "%"));
                }

//                if(searchParam.getTag() != null && !searchParam.getTag().equals("")){
//                    predicates.add(cb.like(root.get(Post_.tag), "%" + searchParam.getTag() + "%"));
//                }

                if(searchParam.getOrderBy() != null && !searchParam.getOrderBy().equals("")){
                    if(searchParam.getOrderBy().equals("createdAt")){
                        query.orderBy(cb.desc(root.get(Post_.createdAt)));
                    } else if(searchParam.getOrderBy().equals("viewCnt")){
                        query.orderBy(cb.desc(root.get(Post_.viewCnt)), cb.desc(root.get(Post_.createdAt)));
                    } else if(searchParam.getOrderBy().equals("likeCnt")){
                        query.orderBy(cb.desc(root.get(Post_.likeCnt)), cb.desc(root.get(Post_.createdAt)));
                    }
                }else{
                    query.orderBy(cb.desc(root.get(Post_.createdAt)));
                }

            }

            for(Predicate p :predicates){
                if(result == null){
                    result = cb.and(p);
                }else {
                    result = cb.and(result, p);
                }
            }
            query.distinct(true);

            return result;
        });
    }
}
