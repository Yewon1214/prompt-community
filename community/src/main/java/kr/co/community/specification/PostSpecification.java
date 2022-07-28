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
            Boolean searchWordFlag = false;

            if(searchParam != null){

                if(searchParam.getKeyword() != null && !searchParam.getKeyword().equals("")){
                    searchWordFlag = true;
                    predicates.add(cb.like(root.get(Post_.title), "%" + searchParam.getKeyword() + "%"));
                    predicates.add(cb.like(root.get(Post_.content), "%" + searchParam.getKeyword() + "%"));
                }


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

            for(int i=0; i<predicates.size(); i++){
                if(searchWordFlag && i<2){
                    if(result == null){
                        result = cb.or(predicates.get(i));
                    }else{
                        result = cb.or(result, predicates.get(i));
                    }
                }else{
                    if(result == null){
                        result = cb.and(predicates.get(i));
                    }else {
                        result = cb.and(result, predicates.get(i));
                    }
                }

            }
            query.distinct(true);

            return result;
        });
    }
}
