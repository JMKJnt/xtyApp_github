package com.example.aiden.xtapp.entity.base;

/**
 * Created by ldn on 2018/3/26.
 */

public class XtUpload {

    /**
     * 主键
     */
    private String xtUploadId;
    /**
     * 用户名
     */

    private String testData;

    /**
     * 创建时间
     */
    private String createdate;
    /**
     * 更新时间
     */
    private String changedate;


    public String getXtUploadId() {
        return xtUploadId;
    }

    public void setXtUploadId(String xtUploadId) {
        xtUploadId = xtUploadId;
    }

    public String getTestData() {
        return testData;
    }

    public void setTestData(String testData) {
        this.testData = testData;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getChangedate() {
        return changedate;
    }

    public void setChangedate(String changedate) {
        this.changedate = changedate;
    }
}
