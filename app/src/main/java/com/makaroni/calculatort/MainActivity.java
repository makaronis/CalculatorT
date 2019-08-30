package com.makaroni.calculatort;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button button1,button2,button3,button4,button5,button6,button7,button8,
            button9,button0,buttonPlus,buttonMinus,buttonDivide,buttonPercent,buttonSum,buttonClear,buttonMinusPlus,
            buttonComma,buttonMultiply;
    private EditText operationText;
    private TextView lastOperation;
    private String currentDigits = "";
    private List<Float> enteredDigits = new ArrayList<>();
    private List<String> enteredOperators = new ArrayList<>();
    private boolean operatorEntered = false;
    private boolean minusEntered = false;
    private boolean dotEntered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeButtons();
        operationText = findViewById(R.id.resultText);
        lastOperation = findViewById(R.id.lastoperation);
        setUpButtonListeners();
    }
    // Выполняем действия пользователя в хронологическом порядке
    public float calculate(List<Float> numbers , List<String> operators){

        float calculation = 0;
        int op = 0;

        for (int n = 0 ; n < numbers.size() - 1; n++){
            float c = numbers.get(n);
            float b = numbers.get(n+1);
            String operator = operators.get(op);
            switch (operator) {
                case "+" :
                    numbers.set(n+1,c+b);
                    break;
                case "-" :
                    numbers.set(n+1,c-b);
                    break;
                case "÷" :
                    numbers.set(n+1,c/b);
                    break;
                case "×" :
                    numbers.set(n+1,c*b);
                    break;
                case "%" :
                    numbers.set(n+1,c/100*b);
                    break;
            }
            op++;
        }
        return numbers.get(numbers.size()-1);
    }
    public void setUpButtonListeners(){
        // Добавляем в список все кнопки с цифрами
        ArrayList<Button> buttonArrayList = new ArrayList<Button>(){{
            add(button0);add(button1);add(button2);add(button3);add(button4);
            add(button5);add(button6);add(button7);add(button8);add(button9);
            }
        };
        setDigitButtons(buttonArrayList);
        // Добавляем в список все кнопки с операторами
        ArrayList<Button> operatorButtonsArrayList = new ArrayList<Button>(){{
                add(buttonDivide);add(buttonMinus);add(buttonPlus);
                add(buttonMultiply);add(buttonPercent);
            }};
        setOperatorButtons(operatorButtonsArrayList);
        //Очищаем поля
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                operationText.setText("");
                currentDigits = "";
                enteredDigits.clear();
                enteredOperators.clear();
                operatorEntered = false;
                minusEntered = false;
            }
        });
        //Ставим точку после последней цифры
        buttonComma.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (operationText.getText().toString().isEmpty()) {
                        operationText.setText("");
                        return;
                    }
                    if (dotEntered)
                        return;
                    if (currentDigits.isEmpty())
                        return;
                    else {
                        operationText.setText(operationText.getText().toString() + ".");
                        currentDigits += ".";
                        dotEntered = true;
                    }
                }
            });
        // Меняем знак перед числом
         buttonMinusPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (operationText.getText().toString().isEmpty()) {
                        operationText.setText("");

                    }else {
                        if(enteredOperators.size()>0)return;
                        if (!minusEntered){
                            currentDigits = "-" + currentDigits;
                            operationText.setText("-" + operationText.getText().toString());
                            minusEntered = true;
                        } else {
                            currentDigits = currentDigits.substring(1);
                            String test = operationText.getText().toString();
                            test = test.substring(1);
                            operationText.setText(test);
                            minusEntered = false;
                        }

//                            if (operatorEntered){
//                                String digitSequence = operationText.getText().toString();
//                                String [] ar = digitSequence.split("\\W");
//                                currentDigits = currentDigits.substring(1);
//                                operationText.setText(ar[0] + ar[1].substring(1));
//                            }else {
//                                currentDigits = currentDigits.substring(1);
//                                String test = operationText.getText().toString();
//                                test = test.substring(1);
//                                operationText.setText(test);
//
//                            }
//                             minusEntered = false;
//                            }
                        }

                    }

            });


            buttonSum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (operationText.getText() == null ) {
                        operationText.setText("");
                        return;
                    }
                    if (operatorEntered)
                        return;
                     else {
                        if (enteredOperators.isEmpty() && enteredDigits.isEmpty())
                            return;
                        enteredDigits.add(Float.parseFloat(currentDigits));
                        currentDigits = "";
                        float f = calculate(enteredDigits, enteredOperators);
                        enteredOperators.clear();
                        enteredDigits.clear();
                        String formatedOutput = format(f);
                        currentDigits = formatedOutput;
                        lastOperation.setText(operationText.getText().toString());
                        operationText.setText(formatedOutput);
                    }
                }
            });

        }

    public void setOperatorButtons(List<Button> buttons) {
        for (final Button button : buttons) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (operationText.getText().toString().isEmpty()) {
                        operationText.setText("");
                    } else {
                        String buttonOperator = button.getText().toString();
                        if (!operatorEntered) {
                            operatorEntered = true;
                            enteredDigits.add(Float.parseFloat(currentDigits));
                            currentDigits = "";
                            enteredOperators.add(buttonOperator);
                            operationText.setText(operationText.getText().toString() + buttonOperator);
                        } else {
                            String s = operationText.getText().toString();
                            enteredOperators.set(enteredOperators.size()-1,buttonOperator);
                            operationText.setText(s.substring(0, s.length() - 1) + buttonOperator);
                        }
                        //minusEntered = false;
                        dotEntered = false;
                    }
                }
            });
        }
    }

        public void setDigitButtons (List <Button> buttons) {
            for (final Button button : buttons) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        operationText.setText(operationText.getText() + button.getText().toString());
                        currentDigits += button.getText().toString();
                        operatorEntered = false;
                    }
                });
            }
        }
     // Находим все кнопки
    public void initializeButtons(){
        button1 = findViewById(R.id.oneB); button2 = findViewById(R.id.twoB); button3 = findViewById(R.id.threeB); button4 = findViewById(R.id.fourB);
        button5 = findViewById(R.id.fiveB); button6 = findViewById(R.id.sixB); button7 = findViewById(R.id.sevenB); button8 = findViewById(R.id.eightB);
        button9 = findViewById(R.id.nineB); button0 = findViewById(R.id.zeroB);
        buttonPlus = findViewById(R.id.plusB);
        buttonMinus = findViewById(R.id.minusB);
        buttonMinusPlus = findViewById(R.id.plusMinusB);
        buttonDivide = findViewById(R.id.divideB);
        buttonPercent = findViewById(R.id.percentB);
        buttonSum = findViewById(R.id.sumB);
        buttonClear = findViewById(R.id.clearB);
        buttonComma = findViewById(R.id.commaB);
        buttonMultiply = findViewById(R.id.multiplyB);
    }

    // Отсекаем ненужные нули
    public static String format(float d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
    }
