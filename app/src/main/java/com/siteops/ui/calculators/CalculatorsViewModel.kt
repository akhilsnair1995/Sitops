package com.siteops.ui.calculators

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.siteops.util.EngineeringMath

class CalculatorsViewModel : ViewModel() {

    // Duct Sizer State
    var ductCfm by mutableStateOf("")
    var ductFriction by mutableStateOf("0.1")
    var ductResult by mutableStateOf(0.0)

    fun calculateDuct() {
        val cfm = ductCfm.toDoubleOrNull() ?: 0.0
        val friction = ductFriction.toDoubleOrNull() ?: 0.1
        ductResult = EngineeringMath.calculateDuctDiameter(cfm, friction)
    }

    // DOAS Sizer State
    var doasArea by mutableStateOf("")
    var doasOcc by mutableStateOf("")
    var doasResult by mutableStateOf(0.0)

    fun calculateDOAS() {
        val area = doasArea.toDoubleOrNull() ?: 0.0
        val occ = doasOcc.toIntOrNull() ?: 0
        doasResult = EngineeringMath.calculateRequiredOA(area, occ)
    }

    // Psychrometric State
    var psychDb by mutableStateOf("")
    var psychRh by mutableStateOf("")
    var enthalpyResult by mutableStateOf(0.0)
    var dewPointResult by mutableStateOf(0.0)

    fun calculatePsych() {
        val db = psychDb.toDoubleOrNull() ?: 0.0
        val rh = psychRh.toDoubleOrNull() ?: 0.0
        enthalpyResult = EngineeringMath.calculateEnthalpy(db, rh)
        dewPointResult = EngineeringMath.calculateDewPoint(db, rh)
    }

    // Cooling Load State
    var coolingArea by mutableStateOf("")
    var coolingResult by mutableStateOf(0.0)

    fun calculateCooling() {
        val area = coolingArea.toDoubleOrNull() ?: 0.0
        coolingResult = EngineeringMath.calculateCoolingLoadTons(area)
    }
}
