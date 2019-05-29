package org.reginpaul;

class Notify {
    private int id;
    private String msgtype,msg;

    public Notify(int id, String msgtype,String msg) {
        this.id = id;
        this.msgtype = msgtype;
        this.msg=msg;
    }
    public Notify(String msgtype,String msg) {

        this.msgtype = msgtype;
        this.msg=msg;
    }


    public int getId() {
        return id;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public String getMsg() {
        return msg;
    }

}
