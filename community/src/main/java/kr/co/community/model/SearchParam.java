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
    private String title;
    private String content;
    private String orderBy="createdAt";
    private String size="10";

    public String getQueryParams() {
        StringJoiner sj = new StringJoiner("&");
        try {
            if (this.title != null && !this.title.equals("")) {
                sj.add(String.format("title=%s", URLEncoder.encode(this.title, "UTF-8")));
            }
            if (this.content != null && !this.content.equals("")) {
                sj.add(String.format("content=%s", URLEncoder.encode(this.content, "UTF-8")));
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
