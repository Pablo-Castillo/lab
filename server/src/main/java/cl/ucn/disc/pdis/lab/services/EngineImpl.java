/*
 * Copyright (c) 2020 Diego Urrutia-Astorga. http://durrutia.cl.
 *
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which accompanies
 * this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package cl.ucn.disc.pdis.lab.services;

import cl.ucn.disc.pdis.lab.zeroice.model.Engine;
import com.zeroc.Ice.Current;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The implementation of {@link cl.ucn.disc.pdis.lab.zeroice.model.Engine}.
 *
 * @author Diego Urrutia-Astorga.
 */
public final class EngineImpl implements Engine {

    /**
     * @see Engine#getDate(Current)
     */
    @Override
    public String getDate(Current current) {
        return ZonedDateTime.now().format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    /**
     *
     * @param rut
     * @param current The Current object for the invocation.
     * @return
     * @author http://cristobaldiaz.cl/blog/validacion-del-rut/ (formula)
     */
    @Override
    public String getDigitoVerificador(String rut, Current current) {

        /**
         * Variable init.
         */
        char dv = 0;
        String digito = null;
        int mult = 0;
        int suma = 1;

        try {
            /**
             * rut uppercase just to work with an standard
             */
            rut =  rut.toUpperCase();

            /**
             * replacement of "." and "-" to get just alphanumeric characters
             */
            rut = rut.replace(".", "");
            rut = rut.replace("-", "");

            /**
             * auxiliary string to resize the rut
             */
            int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

            /**
             * gets the supposed dv from the last char of the string
             */
            dv = rut.charAt(rut.length() - 1);

            /**
             * calculates de real Dv with the digits of the rut
             */
            for (; rutAux != 0; rutAux /= 10) {
                suma = (suma + rutAux % 10 * (9 - mult++ % 6)) % 11;
            }
            /**
             * checks the coincidence between the given DV and the calculated one
             * if they match, returns the dv, else, sends a message of invalid dv
             */
            if (dv == (char) (suma != 0 ? suma + 47 : 75)) {
                digito = String.valueOf(dv);
            }else{
                digito = "Invalid Dv";
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return digito;

    }

}
