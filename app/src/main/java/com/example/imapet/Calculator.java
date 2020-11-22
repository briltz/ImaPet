package com.example.imapet;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Calculator extends AppCompatActivity {
    CalculatorDatabase calculatorDB;
    EditText age, weight;
    TextView result;
    Button btnCalculate, btnView, btnDelete;
    Spinner pet;
    String spnChoice;
    int petYear = 0;
    int humanAge;
    int petWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        calculatorDB = new CalculatorDatabase(this);

        age = (EditText) findViewById(R.id.editText_age);
        weight = (EditText) findViewById(R.id.editText_weight);

        result = (TextView) findViewById(R.id.txt_result);

        pet = (Spinner) findViewById(R.id.spn_pet);

        btnCalculate = (Button) findViewById(R.id.btn_calculate);
        calculate();

        btnView = (Button) findViewById(R.id.btn_history);
        viewHistory();

        btnDelete = (Button) findViewById(R.id.btn_delete_history);
        deleteHistory();
    }

    public void calculate() {
        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                humanAge = Integer.parseInt(age.getText().toString());
                petWeight = Integer.parseInt(weight.getText().toString());

                spnChoice = pet.getSelectedItem().toString();

                if (humanAge <= 20) {
                    if (spnChoice.equals("Cat")) {
                        if (humanAge == 0) {
                            petYear = 0;
                        } else if (humanAge == 1) {
                            petYear = 15;
                        } else if (humanAge == 2) {
                            petYear = 24;
                        } else {
                            petYear = 24;
                            for (int i = 3; i <= humanAge; i++) {
                                petYear = petYear + 4;
                            }
                        }
                    } else {
                        if (humanAge == 0) {
                            petYear = 0;
                        } else if (humanAge == 1) {
                            petYear = 15;
                        } else if (humanAge == 2) {
                            petYear = 24;
                        } else if (((humanAge >= 3) && (humanAge <= 5)) || (petWeight <= 20)) {
                            petYear = 24;
                            for (int i = 3; i <= humanAge; i++) {
                                petYear = petYear + 4;
                            }
                        } else if ((humanAge == 6) && ((petWeight >= 21) && (petWeight <= 50))) {
                            petYear = 42;
                        } else if ((humanAge >= 7) && ((petWeight >= 21) && (petWeight <= 50))) {
                            petYear = 42;
                            for (int i = 7; i <= humanAge; i++) {
                                petYear = petYear + 5;
                            }
                        } else if ((humanAge == 6) && (petWeight > 50)) {
                            petYear = 45;
                        } else if ((petWeight > 50) && (humanAge <= 28)) {
                            petYear = 45;
                            for (int i = 7; i <= humanAge; i++) {
                                petYear = petYear + 5;
                            }
                        }
                    }

                    result.setText("Your " + spnChoice + " is " + petYear + " years old in pet years!");

                    boolean isCreated = calculatorDB.insertCalculationData(humanAge, petWeight, spnChoice, petYear);

                    if (isCreated == true) {
                        Toast.makeText(Calculator.this, "Calculation Successful", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Calculator.this, "Error: Failed to calculate!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(Calculator.this, "Please enter a valid age!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void viewHistory() {
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor retrieval = calculatorDB.getAllData();

                if (retrieval.getCount() == 0) {
                    displayMessage("No recent calculations", "");
                    return;
                }

                StringBuffer buffer = new StringBuffer();
                while (retrieval.moveToNext()) {
                    buffer.append("Age in Human Years: " + retrieval.getString(0) + "\n");
                    buffer.append("Weight: " + retrieval.getString(1) + "\n");
                    buffer.append("Pet: " + retrieval.getString(2) + "\n");
                    buffer.append("Pet Years: " + retrieval.getString(3) + "\n\n");
                }

                displayMessage("Recent Calculations", buffer.toString());
            }
        });
    }

    public void deleteHistory() {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor deletion = calculatorDB.deleteAllData();

                if (deletion.getCount() == 0) {
                    Toast.makeText(Calculator.this, "History cleared", Toast.LENGTH_LONG).show();
                    return;
                }

                displayMessage("Error", "History failed to clear");
            }
        });
    }

    public void displayMessage (String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
}
