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
import org.apache.commons.lang3.ObjectUtils;

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
     *Calculates the Dv of a given rut via Modulo 11 formula
     *<p>
     *The Module 11 formula consists in assign a check value to each digit of the base number
     *starting from 2 for the less significative digit (the most to the right) to 7 for the most
     *significative digit (the most to the left). If the base number has more than 6 numbers,
     *then the secuence is restarted from 2. Finally, the addition of all the values is divided by 11
     *and the remnants of that division is subtracted from 11 to get the check digit
     *
     * @author Cristobal Diaz (formula) //cristobaldiaz.cl/blog/validacion-del-rut/
     * @param rut The id of a person in chile
     * @param current The Current object for the invocation.
     * @return The dv of the rut
     *
     */
    @Override
    public String getDigitoVerificador(String rut, Current current) {

        //Variable initialize.
        char dv = 0;
        String digito = null;
        int mult = 0;
        int suma = 1;

        try {

           //rut uppercase just to work with an standard
           rut =  rut.toUpperCase();

           //replacement of "." and "-" to get just alphanumeric characters
           rut = rut.replace(".", "");
           rut = rut.replace("-", "");

           //auxiliary string to resize the rut
           int rutAux = Integer.parseInt(rut.substring(0, rut.length() - 1));

           //gets the supposed dv from the last char of the string
            dv = rut.charAt(rut.length() - 1);

           //calculates de real Dv with the digits of the rut
            for (; rutAux != 0; rutAux /= 10) {
                suma = (suma + rutAux % 10 * (9 - mult++ % 6)) % 11;
            }
           //checks the coincidence between the given DV and the calculated one
           //if they match, returns the dv
            if (dv == (char) (suma != 0 ? suma + 47 : 75)) {
                digito = String.valueOf(dv);
            }

        } catch (java.lang.NumberFormatException e) {
        } catch (Exception e) {
        }
        return digito;

    }

}
