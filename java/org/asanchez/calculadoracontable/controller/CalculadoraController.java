package org.asanchez.calculadoracontable.controller;

import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;



public class CalculadoraController {
    final private float UMA = 108.57f;
    @FXML
    Label welcomeText;

    @FXML
    ComboBox<String> cbDiasDelMes,cdPeriodoDePago;
    @FXML
    TextField diasVacaciones,diasAguinaldo,primaVacacional,primaDeRiesgo,salarioBruto;
    @FXML
    DatePicker inicioRelacionLaboral;
    @FXML
    Label totalFinal, imssTotal, isrTotal,sbclabel, sdiLabel, factorIntegracion, lbPercepcion,rsPercepcion,rsSalarioDiario,rsAniosTrabajados,rsDiasTrabajados,rsSemanaIMSS,rsAntiguedadSAT,rsVacaciones,rsPrimaVacacional;
    @FXML
    private PieChart pieChart;

    float a,b,c;

    @FXML
    void calculadoraBoton() {

        pieChart.getData().clear();
        ObservableList<PieChart.Data> pieChartData ;

        calculoPercepcion();
        float salarioDiario = calculoSalarioDiario();

        calculoAnio();
        calculoDias();
        calculoSemana();
        antiguedadSAT();
        float fiSinRedondear =  (vacaciones(salarioDiario) + totalPrimaVacacional(salarioDiario)+ 365f) /365f;
        float fi = Math.round(fiSinRedondear*10000.0f)/10000.0f;
        factorIntegracion.setText(String.valueOf(fi));
        float sdiSinRedondear = salarioDiario*fi;
        float sdi = Math.round(sdiSinRedondear*100.0f)/100.0f;
        sdiLabel.setText("$ " + sdi);
        sbclabel.setText("$ " + sdi);
        a = calcularImpuestos();
        b = calcularIMSS(sdi);
        c=Float.parseFloat(salarioBruto.getText());
        totalFinal.setText("$ "+ (c-a-b));
        pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Salario Neto", c-a-b),
                new PieChart.Data("ISR", a),
                new PieChart.Data("IMSS", b));
        pieChartData.forEach(data -> data.nameProperty().bind(Bindings.concat(data.getName())));
        pieChart.getData().addAll(pieChartData);
    }
    private float calcularIMSS(float sdi) {
        float tope = UMA*25;
        float cuotaimm;
        if (sdi<tope){
            float cuotaSinRedondear = sdi*0.02375f*30.4f;
            cuotaimm = Math.round(cuotaSinRedondear*100.0f)/100.0f;
            imssTotal.setText("$ " + cuotaimm);
        }else {
            float cuotaSinRedondear = tope*0.02375f*30.4f;
            cuotaimm = Math.round(cuotaSinRedondear*100.0f)/100.0f;
            imssTotal.setText("$ " + cuotaimm);
        }

        return cuotaimm;
    }
    private float calcularImpuestos() {
        float impuesto,isr;

        float salario = Float.parseFloat(salarioBruto.getText());

        if (salario<0){
            return 0f ;
        } else if (salario<746.04f) {
            impuesto = (salario-0.01f)*0.0192f;
        }else if (salario<6332.05f) {
            impuesto = (salario-746.05f) * 0.064f + 14.32f;
        }else if (salario<11128.01f) {
            impuesto = (salario-6332.06f) * 0.1088f + 371.83f;
        }else if (salario<12935.82f) {
            impuesto = (salario-11128.02f) * 0.16f + 893.63f;
        }else if (salario<15487.71f) {
            impuesto = (salario-12935.83f) * 0.1792f+1182.88f;
        }else if (salario<31236.49f) {
            impuesto = (salario-15487.72f) * 0.2136f + 1640.18f;
        }else if (salario<49233f) {
            impuesto = (salario-31236.50f) * 0.2352f+5004.12f;
        }else if (salario<93993.9f) {
            impuesto = (salario-49233.01f) * 0.30f+9236.89f;
        }else if (salario<125325.2) {
            impuesto = (salario-93993.91f) * 0.32f+22665.17f;
        }else if (salario<375975.61) {
            impuesto = (salario-125325.21f) * .34f+32691.18f;
        }else {
            impuesto = (salario-375975.62f) * .35f+117912.32f;
        }

        if (salario>9081){
            isr = Math.round(impuesto*100.0f)/100.0f;
            isrTotal.setText("$ "+ isr);
        }else {
            if (impuesto-390<0){
                isr = 0;
                isrTotal.setText("$ "+ 0);
            }else {
                float isrSinRedondear = impuesto-390;
                isr = Math.round(isrSinRedondear*100.0f)/100.0f;
                isrTotal.setText("$ "+ isr);
            }

        }


        return isr;

    }

    private float totalPrimaVacacional(float salarioDiario) {
        float diasPV = (Float.parseFloat(primaVacacional.getText())/100)*Float.parseFloat(diasVacaciones.getText());
        float pv = diasPV * salarioDiario;
        float numeroRedondeado = Math.round(pv*100.0f)/100.0f;
        rsPrimaVacacional.setText("$ " +numeroRedondeado);
        return diasPV;
    }

    private float vacaciones(float salarioDiario) {
        float aguinaldo = salarioDiario * Float.parseFloat(diasAguinaldo.getText());
        float numeroRedondeado = Math.round(aguinaldo*100.0f)/100.0f;
        rsVacaciones.setText("$ " + numeroRedondeado);
        return Float.parseFloat(diasAguinaldo.getText());
    }

    private void antiguedadSAT() {
        float number =(Float.parseFloat(salarioBruto.getText())*12);
        float numeroRedondeado = Math.round(number*100.0f)/100.0f;
        rsAntiguedadSAT.setText("$ " + numeroRedondeado );
    }

    private void calculoSemana() {
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicio = inicioRelacionLaboral.getValue();
        long diferenciasDias = ChronoUnit.DAYS.between(fechaInicio,fechaActual);
        int aniosAntiguedad = (int) (diferenciasDias/7);
        rsSemanaIMSS.setText(String.valueOf(aniosAntiguedad));
    }
    private  void calculoPercepcion() {
        lbPercepcion.setText("Percepción " + cdPeriodoDePago.getValue());

        if (cdPeriodoDePago.getValue().equals("Quincenal")) {
            rsPercepcion.setText("$ "+(Double.parseDouble(salarioBruto.getText())/2));
        } else if (cdPeriodoDePago.getValue().equals("Mensual")) {
            rsPercepcion.setText("$ "+(Double.parseDouble(salarioBruto.getText())));
        }else {
            rsPercepcion.setText("$ "+(Double.parseDouble(salarioBruto.getText())/4));
        }
    }
    private  float calculoSalarioDiario(){

        if (cbDiasDelMes.getValue().equals("30.4 (Tradicional)")){
            float number =(Float.parseFloat(salarioBruto.getText())/30.4f);
            float numeroRedondeado = Math.round(number*100.0f)/100.0f;
            rsSalarioDiario.setText("$ " + numeroRedondeado );
            return numeroRedondeado;
        } else if (cbDiasDelMes.getValue().equals("30 (General)")) {
            float number =(Float.parseFloat(salarioBruto.getText())/30f);
            float numeroRedondeado = Math.round(number*100.0f)/100.0f;
            rsSalarioDiario.setText("$ " + numeroRedondeado );
            return numeroRedondeado;
        }else {
            float number =(Float.parseFloat(salarioBruto.getText())/30.41666f);
            float numeroRedondeado = Math.round(number*100.0f)/100.0f;
            rsSalarioDiario.setText("$ " + numeroRedondeado );
            return numeroRedondeado;
        }
    }
    private void calculoAnio(){
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicio = inicioRelacionLaboral.getValue();
        long diferenciasDias = ChronoUnit.DAYS.between(fechaInicio,fechaActual);
        int aniosAntiguedad = (int) (diferenciasDias/365);
        rsAniosTrabajados.setText(String.valueOf(aniosAntiguedad));
    }
    void calculoDias(){
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicio = inicioRelacionLaboral.getValue();
        long diferenciasDias = ChronoUnit.DAYS.between(fechaInicio,fechaActual);
        rsDiasTrabajados.setText(String.valueOf(diferenciasDias));
    }

    @FXML
    void operacionFechaInicio(){
        LocalDate fechaActual = LocalDate.now();
        LocalDate fechaInicio = inicioRelacionLaboral.getValue();
        long diferenciasDias = ChronoUnit.DAYS.between(fechaInicio,fechaActual);

        int aniosAntiguedad = (int) (diferenciasDias/365)+1;
        int diasDeVacaciones;
        if (diferenciasDias<0){
            diasVacaciones.setText("0");
        }else {
            if (aniosAntiguedad<5){
               diasDeVacaciones = 10+(aniosAntiguedad*2);
            }else {
                diasDeVacaciones = 20 + ((aniosAntiguedad/5)*2) ;
                if (aniosAntiguedad%5==0){
                    diasDeVacaciones-=2;
                }
            }
            diasVacaciones.setText(String.valueOf(diasDeVacaciones));
        }
    }
}

