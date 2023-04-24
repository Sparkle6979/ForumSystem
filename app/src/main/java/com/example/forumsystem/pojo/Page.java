package com.example.forumsystem.pojo;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/4/20 18:58
 */
public class Page {

    //当前页码
    private int current=1;
    //显示上限，每页的数据量
    private int limit=10;
    //数据的总数，用于计算总页数 [数据库表的数据]
    private int rows;
    private int gap = 2;

    //查询路径，用于复用分页链接
    private String path;

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        if(gap >= 1)
            this.gap = gap;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current >= 1)
            this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if(rows >= 0)
            this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setLimit(int limit) {
        if (limit>=1&&limit<=100) {
            this.limit = limit;
        }
    }

    /**
     * 获取当前页的在数据库表中的起始行
     * @return
     */
    public int getOffset(){
        return (current - 1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public int getTotal(){
        if(rows % limit == 0)
            return rows / limit;
        else
            return rows / limit+1;
    }

    /**
     *获取起始页码
     * @return
     */
    public int getFrom(){
        int from=current-gap;
        return from < 1 ? 1 : from;
    }


    /*
     *获取结束页码
     * @return
     */
    public int getTo(){
        int to=current+gap;
        int total=getTotal();
        return to>total?total:to;
    }
}
