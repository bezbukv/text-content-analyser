/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jsr;

/**
 *
 * @author кей
 */
public class JSRException extends Exception  {
    public JSRException (int errCode, String msg) {
          errCode_ = errCode;
          msg_ = msg;
    }
    private String msg_ = "None";
    private int errCode_;
    public String getMessage() { return msg_; }
    public int getErrCode() { return errCode_;}
}
