package com.xuanxuan.NFChacker.OtherView;

public class ItemBean {
    private String card;
    private String reader;

    public ItemBean(){
    }

    public ItemBean(String reader, String card){
        this.card=card;
        this.reader=reader;
    }
    public String getCard() {
        return card;
    }
    public String getReader() {
        return reader;
    }

    public void setCard(String card) {
        this.card = card;
    }
    public void setReader(String reader) {
        this.reader = reader;
    }
}
