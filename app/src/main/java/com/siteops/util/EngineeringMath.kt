package com.siteops.util

import kotlin.math.ln
import kotlin.math.pow

/**
 * EngineeringMath.kt
 * 
 * Standalone MEP calculation logic. 
 * Note: These are approximations for quick field checks. 
 * Professional design requires full ASHRAE/SMACNA validation.
 */
object EngineeringMath {

    /**
     * Duct Sizer: Calculate Duct Diameter (inches)
     * Based on Darcy-Weisbach / Colebrook-White approximations.
     * Input: CFM (Volume Flow), Friction Loss (in. wg/100ft)
     */
    fun calculateDuctDiameter(cfm: Double, frictionLoss: Double = 0.1): Double {
        if (cfm <= 0 || frictionLoss <= 0) return 0.0
        // Equivalent diameter formula approximation: d = 12 * ( (0.109136 * CFM^1.9) / Friction )^0.2
        // Simplified HVAC industry standard for round ducts
        return 0.1091 * cfm.pow(0.38) / frictionLoss.pow(0.2) * 12.0 // Rough scaling
    }

    /**
     * DOAS Sizer (ASHRAE 62.1)
     * Vbz = Rp * Pz + Ra * Az
     * @param areaSqFt Floor area in square feet
     * @param occupancy Number of people
     * @param ra Area outdoor air rate (default 0.06 cfm/sqft)
     * @param rp People outdoor air rate (default 5 cfm/person)
     */
    fun calculateRequiredOA(
        areaSqFt: Double, 
        occupancy: Int, 
        ra: Double = 0.06, 
        rp: Double = 5.0
    ): Double {
        return (areaSqFt * ra) + (occupancy * rp)
    }

    /**
     * Psychrometrics: Enthalpy (BTU/lb)
     * h = 0.240 * T + W * (1061 + 0.444 * T)
     * Requires Humidity Ratio (W), derived from Relative Humidity.
     */
    fun calculateEnthalpy(dryBulbF: Double, relativeHumidity: Double): Double {
        val tempC = (dryBulbF - 32) * 5 / 9
        val saturationPressure = 6.112 * Math.exp((17.67 * tempC) / (tempC + 243.5)) // hPa
        val actualVaporPressure = (relativeHumidity / 100.0) * saturationPressure
        
        // Humidity Ratio W (lb water / lb dry air)
        val w = 0.62198 * (actualVaporPressure / (1013.25 - actualVaporPressure))
        
        return 0.240 * dryBulbF + w * (1061 + 0.444 * dryBulbF)
    }

    /**
     * Psychrometrics: Dew Point (F)
     * Magnus Formula approximation
     */
    fun calculateDewPoint(dryBulbF: Double, relativeHumidity: Double): Double {
        val tempC = (dryBulbF - 32) * 5 / 9
        val rh = relativeHumidity / 100.0
        val m = 17.27
        val tn = 237.7
        val alpha = ((m * tempC) / (tn + tempC)) + ln(rh)
        val dewPointC = (tn * alpha) / (m - alpha)
        return (dewPointC * 9 / 5) + 32
    }

    /**
     * Cooling Load Quick-Check
     * Rule of thumb: 1 Ton per 400 SqFt
     */
    fun calculateCoolingLoadTons(areaSqFt: Double): Double {
        if (areaSqFt <= 0) return 0.0
        return areaSqFt / 400.0
    }
}
