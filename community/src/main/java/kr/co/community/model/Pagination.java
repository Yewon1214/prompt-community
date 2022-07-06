package kr.co.community.model;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;


public class Pagination implements Pageable {
    private static final int DEFAULT_GUTTER = 5;

    private int pageNum;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private Sort sort;

    public Pagination(int pageNum, int pageSize, Sort sort) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.sort = sort;
    }

    public Pagination(Pageable pageable){
        this.pageNum = pageable.getPageNumber();
        this.pageSize = pageable.getPageSize();
        this.sort = pageable.getSort();
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }


    public long getIndex(int index){
        if(this.getTotalElements() < this.getPageSize()){
            return index + 1;
        }
        return Math.max(1, this.getTotalElements() - ((this.getCurrent()-1) * this.getPageSize()) - index);
    }

    private int getCurrent() {
        return this.getPageNumber() + 1;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getBegin(){
        return Math.max(1, this.getCurrent()-4);
    }

    public int getEnd(){
        return Math.min(this.getCurrent()+4, this.totalPages);
    }

    public long getTotalElements() {
        return totalElements;
    }

    @Override
    public int getPageNumber() {
        return this.pageNum;
    }

    @Override
    public int getPageSize() {
        return this.pageSize;
    }

    @Override
    public long getOffset() {
        return (long)this.pageNum * (long)this.pageSize;
    }

    @Override
    public Sort getSort() {
        return this.sort;
    }

    @Override
    public Pageable next() {
        return new Pagination(this.getPageNumber()+1, this.getPageSize(), this.getSort());
    }

    @Override
    public Pageable previousOrFirst() {
        return this.hasPrevious() ? this.previous() : this.first();
    }

    @Override
    public Pageable first() {
        return new Pagination(0, this.getPageSize(), this.getSort());
    }

    public Pagination previous(){
        return this.getPageNumber()==0 ? this : new Pagination(this.getPageNumber() - 1, this.getPageNumber(), this.getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return new Pagination(pageNumber, this.getPageSize(), this.getSort());
    }

    @Override
    public boolean hasPrevious() {
        return this.pageNum > 0;
    }
}
