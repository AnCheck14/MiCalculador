package com.l20291017.micalculadora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import com.google.android.material.button.MaterialButton;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText calculatorEditText;
    MaterialButton botonC;
    MaterialButton botonSuma, botonResta, botonMulti, botonDivision, botonIgual;
    MaterialButton boton0, boton1, boton2, boton3, boton4, boton5, boton6, boton7, boton8, boton9;
    MaterialButton botonAC, botonPunto;

    boolean clearButtonEnabled = true;
    boolean buttonsEnabled = true;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        calculatorEditText = findViewById(R.id.calculatorEditText);

        assignId(botonC, R.id.button_C);
        assignId(botonSuma, R.id.button_suma);
        assignId(botonResta, R.id.button_resta);
        assignId(botonMulti, R.id.button_multi);
        assignId(botonDivision, R.id.button_division);
        assignId(boton0, R.id.button_0);
        assignId(boton1, R.id.button_1);
        assignId(boton2, R.id.button_2);
        assignId(boton3, R.id.button_3);
        assignId(boton4, R.id.button_4);
        assignId(boton5, R.id.button_5);
        assignId(boton6, R.id.button_6);
        assignId(boton7, R.id.button_7);
        assignId(boton8, R.id.button_8);
        assignId(boton9, R.id.button_9);
        assignId(botonAC, R.id.button_ac);
        assignId(botonIgual, R.id.button_igual);
        assignId(botonPunto, R.id.button_punto);
    }

    void assignId(MaterialButton btn, int id) {
        btn = findViewById(id);
        btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();
        String dataToCalculate = calculatorEditText.getText().toString();

        if (buttonText.equals("AC")) {
            calculatorEditText.setText("0");
            clearButtonEnabled = true;
            buttonsEnabled = true;
            return;
        }
        if (!buttonsEnabled) {
            return;
        }
        if (buttonText.equals("=")) {
            clearButtonEnabled = false;
            buttonsEnabled = false;

            String finalResult = getResult(dataToCalculate);
            if (!finalResult.equals("Err")) {
                calculatorEditText.setText(dataToCalculate + "\n" + finalResult);
            } else {
                calculatorEditText.setText(dataToCalculate);
            }
            return;
        }
        if (buttonText.equals("C") && clearButtonEnabled) {
            if (dataToCalculate.length() > 1) {
                dataToCalculate = dataToCalculate.substring(0, dataToCalculate.length() - 1);
            } else {
                dataToCalculate = "0";
            }
        } else if (!buttonText.equals("C")) {
            if ("+-*/".contains(buttonText)) {
                if ("+-*/".contains(dataToCalculate.substring(dataToCalculate.length() - 1))) {
                    return;
                }
            }
            if (dataToCalculate.equals("0")) {
                dataToCalculate = buttonText;
            } else {
                dataToCalculate = dataToCalculate + buttonText;
            }
        }
        calculatorEditText.setText(dataToCalculate);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.miInfo) {
            Toast.makeText(this, "Borrar", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    String getResult(String data) {
        try {
            Context context = Context.enter();
            context.setOptimizationLevel(-1);
            Scriptable scriptable = context.initStandardObjects();
            String finalResult = context.evaluateString(scriptable, data, "Javascript", 1, null).toString();
            if (finalResult.endsWith(".0")) {
                finalResult = finalResult.replace(".0", "");
            }
            return finalResult;
        } catch (Exception e) {
            return "Err";
        }
    }
}