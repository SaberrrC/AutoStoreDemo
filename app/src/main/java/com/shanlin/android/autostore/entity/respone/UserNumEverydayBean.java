package com.shanlin.android.autostore.entity.respone;

import com.shanlin.android.autostore.entity.BaseBean;

/**
 * Created by cuieney on 15/08/2017.
 */

public class UserNumEverydayBean  extends BaseBean{

    /**
     * code :
     * data : {"femaleCount":50870,"maleCount":15163,"memberCount":1}
     * message :
     */

    private DataBean data;

    @Override
    public String toString() {
        return "UserNumEverydayBean{" +
                ", data=" + data +
                '}';
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean {
        /**
         * femaleCount : 50870
         * maleCount : 15163
         * memberCount : 1
         */

        private int femaleCount;
        private int maleCount;
        private int memberCount;

        public int getFemaleCount() {
            return femaleCount;
        }

        public void setFemaleCount(int femaleCount) {
            this.femaleCount = femaleCount;
        }

        public int getMaleCount() {
            return maleCount;
        }

        public void setMaleCount(int maleCount) {
            this.maleCount = maleCount;
        }

        public int getMemberCount() {
            return memberCount;
        }

        public void setMemberCount(int memberCount) {
            this.memberCount = memberCount;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "femaleCount=" + femaleCount +
                    ", maleCount=" + maleCount +
                    ", memberCount=" + memberCount +
                    '}';
        }
    }
}
