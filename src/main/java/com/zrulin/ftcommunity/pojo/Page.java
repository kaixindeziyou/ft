package com.zrulin.ftcommunity.pojo;

import lombok.Data;

/**
 * @author zrulin
 * @create 2022-03-10 8:37
 */

public class Page {
    // 当前页码
    private Integer current = 1;
    // 显示上限
    private Integer limit = 5;
    // 数据总数 （用于计算总页数）
    private Integer rows;
    //查询路径 （用于复用分页链接）
    private String path;

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        if(current >= 1){  //数据合法判断
            this.current = current;
        }
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        if(limit >= 1 && limit <= 100){  // 基于服务器压力和用户体验
            this.limit = limit;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        if(rows >= 0){
            this.rows = rows;
        }
    }

    /**
     * 获取当前页起始行
     * @return
     */
    public Integer getOffset(){
        // current * Limit - limit
        return (current -1) * limit;
    }

    /**
     * 获取总页数
     * @return
     */
    public Integer getTotal(){
        if(rows % limit == 0){
            return rows / limit;
        }else{
            return  rows / limit + 1 ;
        }
    }

    /**
     * 获得起始页码
     * @return
     */
    public Integer getFrom(){
        int from = current -2;
        return from < 1 ? 1 : from;
    }

    /**
     * 获得结束页码
     * @return
     */
    public Integer getTo(){
        int to = current +2;
        int total = getTotal();
        return to > total? total: to;
    }
}
