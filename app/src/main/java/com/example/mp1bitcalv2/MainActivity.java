package com.example.mp1bitcalv2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;
    private String currentOperation = "";
    private String currentResult = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

        // Initialize Buttons and Set OnClickListener for Each Button
        Button btnOpenParenthesis = findViewById(R.id.btn_open_parenthesis);
        Button btnClosingParenthesis = findViewById(R.id.btn_closing_parenthesis);
        Button btnShiftLeft = findViewById(R.id.btn_shift_left);
        Button btnShiftRight = findViewById(R.id.btn_shift_right);
        Button btnBitwiseAND = findViewById(R.id.btn_bitwise_AND);
        Button btnBitwiseOR = findViewById(R.id.btn_bitwise_OR);
        Button btnBitwiseXOR = findViewById(R.id.btn_bitwise_XOR);
        Button btnBitwiseNOT = findViewById(R.id.btn_bitwise_inversion);
        Button btnClear = findViewById(R.id.btn_clear);
        Button btnEquals = findViewById(R.id.btn_equals);
        Button btnZero = findViewById(R.id.btn0);
        Button btnOne = findViewById(R.id.btn1);
        Button btnTwo = findViewById(R.id.btn2);
        Button btnThree = findViewById(R.id.btn3);
        Button btnFour = findViewById(R.id.btn4);
        Button btnFive = findViewById(R.id.btn5);
        Button btnSix = findViewById(R.id.btn6);
        Button btnSeven = findViewById(R.id.btn7);
        Button btnEight = findViewById(R.id.btn8);
        Button btnNine = findViewById(R.id.btn9);

        btnOpenParenthesis.setOnClickListener(v -> {
            currentOperation += "(";
            updateOperationTextView();
        });

        btnClosingParenthesis.setOnClickListener(v -> {
            currentOperation += ")";
            updateOperationTextView();
        });

        btnShiftLeft.setOnClickListener(v -> {
            currentOperation += " << ";
            updateOperationTextView();
        });

        btnShiftRight.setOnClickListener(v -> {
            currentOperation += " >> ";
            updateOperationTextView();
        });

        btnBitwiseAND.setOnClickListener(v -> {
            currentOperation += " & ";
            updateOperationTextView();
        });

        btnBitwiseOR.setOnClickListener(v -> {
            currentOperation += " | ";
            updateOperationTextView();
        });

        btnBitwiseXOR.setOnClickListener(v -> {
            currentOperation += " ^ ";
            updateOperationTextView();
        });

        btnBitwiseNOT.setOnClickListener(v -> {
            currentOperation += "~";
            updateOperationTextView();
        });

        btnClear.setOnClickListener(v -> {
            currentOperation = "";
            currentResult = "";
            updateOperationTextView();
            updateResultTextView();
        });

        btnEquals.setOnClickListener(v -> {
            currentResult = calculateResult(currentOperation);
            updateResultTextView();
        });

        btnZero.setOnClickListener(v -> {
            currentOperation += "0";
            updateOperationTextView();
        });

        btnOne.setOnClickListener(v -> {
            currentOperation += "1";
            updateOperationTextView();
        });

        btnTwo.setOnClickListener(v -> {
            currentOperation += "2";
            updateOperationTextView();
        });

        btnThree.setOnClickListener(v -> {
            currentOperation += "3";
            updateOperationTextView();
        });

        btnFour.setOnClickListener(v -> {
            currentOperation += "4";
            updateOperationTextView();
        });

        btnFive.setOnClickListener(v -> {
            currentOperation += "5";
            updateOperationTextView();
        });

        btnSix.setOnClickListener(v -> {
            currentOperation += "6";
            updateOperationTextView();
        });

        btnSeven.setOnClickListener(v -> {
            currentOperation += "7";
            updateOperationTextView();
        });

        btnEight.setOnClickListener(v -> {
            currentOperation += "8";
            updateOperationTextView();
        });

        btnNine.setOnClickListener(v -> {
            currentOperation += "9";
            updateOperationTextView();
        });
    }

    private void updateOperationTextView() {
        tv1.setText(currentOperation);
    }

    private void updateResultTextView() {
        tv2.setText(currentResult);
    }

    private String calculateResult(String operation) {
        String processedOperation = operation.replaceAll("\\s+", "");
        try {
            int result = evaluateExpression(processedOperation);
            return String.valueOf(result);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private int evaluateExpression(String operation) throws Exception {
        if (operation.matches(".*\\d+\\(.*") || operation.matches(".*\\)\\d+.*") || operation.matches(".*\\d+\\)\\d+.*")) {
            throw new Exception("Invalid Syntax");
        }

        if (operation.contains("(")) {
            int open = operation.lastIndexOf("(");
            int close = operation.indexOf(")", open);
            if(close == -1) {
                throw new Exception("Unmatched ()");
            }
            String insideParentheses = operation.substring(open + 1, close);
            int resultInside = evaluateExpression(insideParentheses);
            return evaluateExpression(operation.substring(0, open) + resultInside + operation.substring(close + 1));
        } else {
            return evaluateOr(operation.trim());
        }
    }

    private int evaluateOr(String operation) throws Exception {
        String[] parts = operation.split("\\|", 2);
        if (parts.length > 1) {
            return evaluateXor(parts[0].trim()) | evaluateOr(parts[1].trim());
        }
        return evaluateXor(operation.trim());
    }

    private int evaluateXor(String operation) throws Exception {
        String[] parts = operation.split("\\^", 2);
        if (parts.length > 1) {
            return evaluateAnd(parts[0].trim()) ^ evaluateXor(parts[1].trim());
        }
        return evaluateAnd(operation.trim());
    }

    private int evaluateAnd(String operation) throws Exception {
        String[] parts = operation.split("&", 2);
        if (parts.length > 1) {
            return evaluateShifts(parts[0].trim()) & evaluateAnd(parts[1].trim());
        }
        return evaluateShifts(operation.trim());
    }

    private int evaluateShifts(String operation) throws Exception {
        if (operation.contains("<<")) {
            String[] parts = operation.split("<<", 2);
            return evaluateNot(parts[0].trim()) << evaluateShifts(parts[1].trim());
        } else if (operation.contains(">>")) {
            String[] parts = operation.split(">>", 2);
            return evaluateNot(parts[0].trim()) >> evaluateShifts(parts[1].trim());
        }
        return evaluateNot(operation.trim());
    }

    private int evaluateNot(String operation) throws Exception {
        if (operation.startsWith("~")) {
            return ~evaluateOr(operation.substring(1).trim());
        } else {
            if (operation.contains("~")) {
                throw new Exception("Invalid Syntax");
            }
            return Integer.parseInt(operation.trim());
        }
    }
    
}