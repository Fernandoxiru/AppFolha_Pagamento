package br.ulbra.appfolha_pagamento;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText nomeEditText, salarioEditText, filhosEditText;
    private RadioGroup sexoRadioGroup;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nomeEditText = findViewById(R.id.edNome);
        salarioEditText = findViewById(R.id.edSalario);
        filhosEditText = findViewById(R.id.edFilhos);
        sexoRadioGroup = findViewById(R.id.rgSexo);
        resultTextView = findViewById(R.id.txResultado);
        Button calcularButton = findViewById(R.id.btCalcular);

        calcularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularSalario();
            }
        });
    }

    private void calcularSalario() {
        String nome = nomeEditText.getText().toString();
        String salarioStr = salarioEditText.getText().toString();
        String filhosStr = filhosEditText.getText().toString();

        if (nome.isEmpty() || salarioStr.isEmpty() || filhosStr.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        double salarioBruto = Double.parseDouble(salarioStr);
        int numeroDeFilhos = Integer.parseInt(filhosStr);
        String sexo = ((RadioButton) findViewById(sexoRadioGroup.getCheckedRadioButtonId())).getText().toString();

        double inss = calcularINSS(salarioBruto);
        double ir = calcularIR(salarioBruto);
        double salarioFamilia = calcularSalarioFamilia(salarioBruto, numeroDeFilhos);
        double salarioLiquido = salarioBruto - (inss + ir) + salarioFamilia;

        String tratamento = sexo.equals("Masculino") ? "Sr." : "Sra.";
        String resultado = String.format("%s %s\nDesconto INSS: R$ %.2f\nDesconto IR: R$ %.2f\nSalário Líquido: R$ %.2f",
                tratamento, nome, inss, ir, salarioLiquido);

        resultTextView.setText(resultado);
    }

    private double calcularINSS(double salarioBruto) {
        if (salarioBruto <= 1212.00) {
            return salarioBruto * 0.075;
        } else if (salarioBruto <= 2427.35) {
            return salarioBruto * 0.09;
        } else if (salarioBruto <= 3641.03) {
            return salarioBruto * 0.12;
        } else if (salarioBruto <= 7087.22) {
            return salarioBruto * 0.14;
        }
        return 0;
    }

    private double calcularIR(double salarioBruto) {
        if (salarioBruto <= 1903.98) {
            return 0;
        } else if (salarioBruto <= 2826.65) {
            return salarioBruto * 0.075;
        } else if (salarioBruto <= 3751.05) {
            return salarioBruto * 0.15;
        } else if (salarioBruto <= 4664.68) {
            return salarioBruto * 0.225;
        }
        return 0;
    }

    private double calcularSalarioFamilia(double salarioBruto, int numeroDeFilhos) {
        if (salarioBruto <= 1212.00) {
            return numeroDeFilhos * 56.47;
        }
        return 0;
    }
}
