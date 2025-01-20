package com.ilham.kalkulator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView display;
    private String currentNumber = "";
    private String operator = "";
    private double firstNumber = 0;
    private boolean isNewOperation = true;
    private boolean hasDecimal = false;
    private boolean isOpenParenthesis = true;
    private StringBuilder displayText = new StringBuilder();
    private String lastResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);

        setButtonClickListener(R.id.btn0);
        setButtonClickListener(R.id.btn1);
        setButtonClickListener(R.id.btn2);
        setButtonClickListener(R.id.btn3);
        setButtonClickListener(R.id.btn4);
        setButtonClickListener(R.id.btn5);
        setButtonClickListener(R.id.btn6);
        setButtonClickListener(R.id.btn7);
        setButtonClickListener(R.id.btn8);
        setButtonClickListener(R.id.btn9);
        setButtonClickListener(R.id.btnPlus);
        setButtonClickListener(R.id.btnMinus);
        setButtonClickListener(R.id.btnMultiply);
        setButtonClickListener(R.id.btnDivide);
        setButtonClickListener(R.id.btnEquals);
        setButtonClickListener(R.id.btnClear);
        setButtonClickListener(R.id.btnDot);
        setButtonClickListener(R.id.btnPercent);
        setButtonClickListener(R.id.btnParentheses);
    }

    private void setButtonClickListener(int buttonId) {
        findViewById(buttonId).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnClear) {
            clearCalculator();
        } else if (id == R.id.btnEquals) {
            calculateResult();
        } else if (id == R.id.btnParentheses) {
            handleParentheses();
        } else if (id == R.id.btnPercent) {
            calculatePercent();
        } else if (id == R.id.btnDot) {
            addDecimal();
        } else if (id == R.id.btnPlus || id == R.id.btnMinus ||
                   id == R.id.btnMultiply || id == R.id.btnDivide) {
            handleOperator(((Button) view).getText().toString());
        } else {
            // Numbers
            if (isNewOperation) {
                currentNumber = lastResult;
                displayText = new StringBuilder(lastResult);
                isNewOperation = false;
            }
            appendNumber(((Button) view).getText().toString());
        }
    }

    private void appendNumber(String number) {
        currentNumber += number;
        displayText.append(number);
        display.setText(displayText.toString());
    }

    private void handleOperator(String newOperator) {
        if (!currentNumber.isEmpty()) {
            firstNumber = Double.parseDouble(currentNumber);
            operator = newOperator;
            displayText.append(" ").append(operator).append(" ");
            display.setText(displayText.toString());
            currentNumber = "";
            hasDecimal = false;
            isNewOperation = false;
        } else if (!lastResult.isEmpty()) {
            firstNumber = Double.parseDouble(lastResult);
            operator = newOperator;
            displayText = new StringBuilder(lastResult + " " + operator + " ");
            display.setText(displayText.toString());
            currentNumber = "";
            hasDecimal = false;
            isNewOperation = false;
        }
    }

    private void calculateResult() {
        if (!operator.isEmpty() && !currentNumber.isEmpty()) {
            double secondNumber = Double.parseDouble(currentNumber);
            double result = 0;
            String expression = displayText.toString() + " = ";

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "×":
                    result = firstNumber * secondNumber;
                    break;
                case "÷":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        display.setText("Error");
                        return;
                    }
                    break;
            }

            display.setText(expression + formatResult(result));
            lastResult = formatResult(result);
            currentNumber = "";
            displayText = new StringBuilder(expression + lastResult);
            operator = "";
            isNewOperation = true;
            hasDecimal = lastResult.contains(".");
        }
    }

    private void calculatePercent() {
        if (!currentNumber.isEmpty()) {
            double number = Double.parseDouble(currentNumber);
            double result = number / 100;
            display.setText(formatResult(result));
            currentNumber = formatResult(result);
            lastResult = currentNumber;
            displayText = new StringBuilder(currentNumber);
            isNewOperation = true;
        }
    }

    private void handleParentheses() {
        if (isOpenParenthesis) {
            appendNumber("(");
        } else {
            appendNumber(")");
        }
        isOpenParenthesis = !isOpenParenthesis;
    }

    private void addDecimal() {
        if (!hasDecimal) {
            if (currentNumber.isEmpty()) {
                currentNumber = "0";
                displayText.append("0");
            }
            currentNumber += ".";
            displayText.append(".");
            display.setText(displayText.toString());
            hasDecimal = true;
        }
    }

    private void clearCalculator() {
        display.setText("0");
        currentNumber = "";
        operator = "";
        firstNumber = 0;
        isNewOperation = true;
        hasDecimal = false;
        isOpenParenthesis = true;
        displayText = new StringBuilder();
        lastResult = "";
    }

    private String formatResult(double result) {
        if (result == (long) result) {
            return String.format("%d", (long) result);
        } else {
            return String.format("%s", result);
        }
    }
}