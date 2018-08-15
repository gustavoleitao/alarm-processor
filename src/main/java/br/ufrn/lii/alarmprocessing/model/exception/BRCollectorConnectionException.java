/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.ufrn.lii.alarmprocessing.model.exception;

/**
 *
 * @author Gustavo
 */
public class BRCollectorConnectionException extends Exception {

    public BRCollectorConnectionException() {
    }

    public BRCollectorConnectionException(String message) {
        super(message);
    }

    public BRCollectorConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public BRCollectorConnectionException(Throwable cause) {
        super(cause);
    }

    public BRCollectorConnectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
}
