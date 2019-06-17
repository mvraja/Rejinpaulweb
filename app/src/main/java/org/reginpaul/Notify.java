package org.reginpaul;

class Notify {
    private int id;
    private String msgtype, msg, link;

    public Notify(int id, String msgtype, String msg, String link) {
        this.id = id;
        this.msgtype = msgtype;
        this.msg=msg;
        this.link = link;
    }
    public Notify(String msgtype,String msg, String link) {

        this.msgtype = msgtype;
        this.msg=msg;
        this.link = link;
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
    public String getLink() {
        return link;
    }

}
