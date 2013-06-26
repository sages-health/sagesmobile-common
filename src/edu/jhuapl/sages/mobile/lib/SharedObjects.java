//*****************************************************************************/
/* Copyright (c) 2013 The Johns Hopkins University/Applied Physics Laboratory */
/*                            All rights reserved.                            */
/*                                                                            */
/* This material may be used, modified, or reproduced by or for the U.S.      */
/* Government pursuant to the rights granted under the clauses at             */
/* DFARS 252.227-7013/7014 or FAR 52.227-14.                                  */
/*                                                                            */
/* Licensed under the Apache License, Version 2.0 (the "License");            */
/* you may not use this file except in compliance with the License.           */
/* You may obtain a copy of the License at                                    */
/*                                                                            */
/*     http://www.apache.org/licenses/LICENSE-2.0                             */
/*                                                                            */
/* NO WARRANTY.   THIS MATERIAL IS PROVIDED "AS IS."  JHU/APL DISCLAIMS ALL   */
/* WARRANTIES IN THE MATERIAL, WHETHER EXPRESS OR IMPLIED, INCLUDING (BUT NOT */
/* LIMITED TO) ANY AND ALL IMPLIED WARRANTIES OF PERFORMANCE,                 */
/* MERCHANTABILITY,FITNESS FOR A PARTICULAR PURPOSE, AND NON-INFRINGEMENT OF  */
/* INTELLECTUAL PROPERTY RIGHTS. ANY USER OF THE MATERIAL ASSUMES THE ENTIRE  */
/* RISK AND LIABILITY FOR USING THE MATERIAL.  IN NO EVENT SHALL JHU/APL BE   */
/* LIABLE TO ANY USER OF THE MATERIAL FOR ANY ACTUAL, INDIRECT,               */
/* CONSEQUENTIAL,SPECIAL OR OTHER DAMAGES ARISING FROM THE USE OF, OR         */
/* INABILITY TO USE, THE MATERIAL, INCLUDING, BUT NOT LIMITED TO, ANY DAMAGES */
/* FOR LOST PROFITS.                                                          */
//*****************************************************************************/
package edu.jhuapl.sages.mobile.lib;

import edu.jhuapl.sages.mobile.lib.crypto.engines.CryptoEngine;

import android.util.Log;

import java.security.NoSuchAlgorithmException;

import javax.crypto.NoSuchPaddingException;

public class SharedObjects {

    private static CryptoEngine cryptoEngine;
    private static boolean isEncryptionOn;

    public SharedObjects() throws NoSuchAlgorithmException, NoSuchPaddingException {
        cryptoEngine = new CryptoEngine("PASSWORD12345678".getBytes());
    }

    /**
     * The crypto engine to use on both the client and receiver for testing only
     * 
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     */
    public static CryptoEngine getCryptoEngine() throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (cryptoEngine == null) {
            // TODO need to come up with long term solution to handle this. leave null and throw exception?
            cryptoEngine = new CryptoEngine("PASSWORD12345678".getBytes());
        }
        return cryptoEngine;
    }

    protected static void updateCryptoEngine(String keyVal) throws NoSuchAlgorithmException, NoSuchPaddingException {
        cryptoEngine = new CryptoEngine(keyVal.getBytes());
        Log.i(SharedObjects.class.getName(), "updated CryptoEnginge.");
    }

    public static boolean isEncryptionOn() {
        return isEncryptionOn;
    }

    protected static void setEncryptionOn(boolean isEncryptionOn) {
        SharedObjects.isEncryptionOn = isEncryptionOn;
    }

    public static void test_updateCryptoEngine(String keyVal) throws NoSuchAlgorithmException, NoSuchPaddingException {
        updateCryptoEngine(keyVal);
    }

}
