package com.lyloou.weibo.app;

import com.sina.weibo.sdk.openapi.models.Status;

import java.util.List;

/**
 * Created by lilou on 2015/5/8.
 */
public class WeiboItem {
    private List<Status> statuses;
    private Integer total_number;

    public Integer getTotal_number() {
        return total_number;
    }

    public void setTotal_number(Integer total_number) {
        this.total_number = total_number;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }
}
