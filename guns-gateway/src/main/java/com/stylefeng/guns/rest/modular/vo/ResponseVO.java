package com.stylefeng.guns.rest.modular.vo;

public class ResponseVO<M> {
    /**
     * 返回状态
     * 0-成功
     * 1-业务失败
     * 9990表示系统异常
     */
    private int status;
    private String msg;
    private M data;

    private ResponseVO(){}

    public static<M> ResponseVO sussess(M m){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(0);
        responseVO.setData(m);
        return responseVO;
    }

    public static<M> ResponseVO serviceFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(1);
        responseVO.setData(msg);
        return responseVO;
    }
    public static<M> ResponseVO appFail(String msg){
        ResponseVO responseVO = new ResponseVO();
        responseVO.setStatus(999);
        responseVO.setData(msg);
        return responseVO;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public M getData() {
        return data;
    }

    public void setData(M data) {
        this.data = data;
    }
}
