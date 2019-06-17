package com.spark.moduleotc.entity;

/**
 * 主界面认证状态
 */
public class AuthenticationStatusEntity {

    /**
     * code : 200
     * message : SUCCESS
     * data : {"isReal":{"isCompleted":0,"status":0},"isCertified":{"isCompleted":0,"status":0},"isSetPayPass":{"isCompleted":0,"status":0},"isSetPayType":{"isCompleted":0,"status":0}}
     * count : null
     * responseString : 200~SUCCESS
     * url : null
     * cid : null
     */

    private int code;
    private String message;
    private DataBean data;
    private Object count;
    private String responseString;
    private Object url;
    private Object cid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public Object getCount() {
        return count;
    }

    public void setCount(Object count) {
        this.count = count;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public Object getUrl() {
        return url;
    }

    public void setUrl(Object url) {
        this.url = url;
    }

    public Object getCid() {
        return cid;
    }

    public void setCid(Object cid) {
        this.cid = cid;
    }

    public static class DataBean {
        /**
         * isReal : {"isCompleted":0,"status":0}
         * isCertified : {"isCompleted":0,"status":0}
         * isSetPayPass : {"isCompleted":0,"status":0}
         * isSetPayType : {"isCompleted":0,"status":0}
         */

        private IsRealBean isReal;
        private IsCertifiedBean isCertified;
        private IsSetPayPassBean isSetPayPass;
        private IsSetPayTypeBean isSetPayType;

        public IsRealBean getIsReal() {
            return isReal;
        }

        public void setIsReal(IsRealBean isReal) {
            this.isReal = isReal;
        }

        public IsCertifiedBean getIsCertified() {
            return isCertified;
        }

        public void setIsCertified(IsCertifiedBean isCertified) {
            this.isCertified = isCertified;
        }

        public IsSetPayPassBean getIsSetPayPass() {
            return isSetPayPass;
        }

        public void setIsSetPayPass(IsSetPayPassBean isSetPayPass) {
            this.isSetPayPass = isSetPayPass;
        }

        public IsSetPayTypeBean getIsSetPayType() {
            return isSetPayType;
        }

        public void setIsSetPayType(IsSetPayTypeBean isSetPayType) {
            this.isSetPayType = isSetPayType;
        }

        public static class IsRealBean {
            /**
             * isCompleted : 0
             * status : 0
             */

            private int isCompleted;
            private int status;

            public int getIsCompleted() {
                return isCompleted;
            }

            public void setIsCompleted(int isCompleted) {
                this.isCompleted = isCompleted;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class IsCertifiedBean {
            /**
             * isCompleted : 0
             * status : 0
             */

            private int isCompleted;
            private int status;

            public int getIsCompleted() {
                return isCompleted;
            }

            public void setIsCompleted(int isCompleted) {
                this.isCompleted = isCompleted;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class IsSetPayPassBean {
            /**
             * isCompleted : 0
             * status : 0
             */

            private int isCompleted;
            private int status;

            public int getIsCompleted() {
                return isCompleted;
            }

            public void setIsCompleted(int isCompleted) {
                this.isCompleted = isCompleted;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }

        public static class IsSetPayTypeBean {
            /**
             * isCompleted : 0
             * status : 0
             */

            private int isCompleted;
            private int status;

            public int getIsCompleted() {
                return isCompleted;
            }

            public void setIsCompleted(int isCompleted) {
                this.isCompleted = isCompleted;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
