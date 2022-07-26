package com.xlt.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页信息")
@Builder
public class PageVo implements Serializable {
    private static final long serialVersionUID = 8798896750104833391L;
    @ApiModelProperty("当前页")
    private int currentPage=1;
    @ApiModelProperty("每页数量")
    private int pageSize=10;
    @ApiModelProperty("总数量")
    private int total;
    @ApiModelProperty("总页数")
    private int totalPages;

    public PageVo(int pageSize, int currentPage) {
        this.setCurrentPage(currentPage);
        this.pageSize=pageSize;
    }

    public void setCurrentPage(int currentPage) {
        if(currentPage<=0){
            currentPage=1;
        }
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        totalPages = (total%pageSize==0)?total/pageSize:(total/pageSize+1);
        return totalPages;
    }
}
