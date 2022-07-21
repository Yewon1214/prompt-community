package kr.co.community.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringJoiner;

@Slf4j
@Getter
@Setter
public class SearchParam {
    private String keyword;
    private String tag;
    private String orderBy="createdAt";

    public String getQueryParams() {
        StringJoiner sj = new StringJoiner("&");
        try {
            if (this.keyword != null && !this.keyword.equals("")) {
                sj.add(String.format("keyword=%s", URLEncoder.encode(this.keyword, "UTF-8")));
            }
            if (this.tag != null && !this.tag.equals("")) {
                sj.add(String.format("tag=%s", URLEncoder.encode(this.tag, "UTF-8")));
            }
            if (this.orderBy != null && !this.orderBy.equals("")) {
                sj.add(String.format("orderBy=%s", URLEncoder.encode(this.orderBy, "UTF-8")));
            }
        } catch (UnsupportedEncodingException e) {
            log.error("[" + e.getCause() + "] UnsupportedEncodingException Occured");
        }
        return sj.toString();
    }
}
