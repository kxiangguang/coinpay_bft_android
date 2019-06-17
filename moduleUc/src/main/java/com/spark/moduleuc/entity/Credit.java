package com.spark.moduleuc.entity;

/**
 * 身份验证
 */

public class Credit {
    private DataBean data;
    private int code;
    private String message;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int id;
        private String realName;
        private String idCard;
        private String identityCardImgFront;
        private String identityCardImgReverse;
        private String identityCardImgInHand;
        private int auditStatus;
        private Object rejectReason;
        private String createTime;
        private String updateTime;
        private String certifiedType;

        public String getCertifiedType() {
            return certifiedType;
        }

        public String getRealName() {
            return realName;
        }

        public String getIdCard() {
            return idCard;
        }

        public String getIdentityCardImgFront() {
            return identityCardImgFront;
        }

        public String getIdentityCardImgReverse() {
            return identityCardImgReverse;
        }

        public String getIdentityCardImgInHand() {
            return identityCardImgInHand;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public void setIdentityCardImgFront(String identityCardImgFront) {
            this.identityCardImgFront = identityCardImgFront;
        }

        public void setIdentityCardImgReverse(String identityCardImgReverse) {
            this.identityCardImgReverse = identityCardImgReverse;
        }

        public void setIdentityCardImgInHand(String identityCardImgInHand) {
            this.identityCardImgInHand = identityCardImgInHand;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public Object getRejectReason() {
            return rejectReason;
        }

        public void setRejectReason(Object rejectReason) {
            this.rejectReason = rejectReason;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public void setCertifiedType(String certifiedType) {
            this.certifiedType = certifiedType;
        }
    }
}
