package me.dio.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class FormLogin extends AppCompatActivity {
    private TextView text_cad;
    private FirebaseAuth mAuth;
    private EditText edit_email;
    private EditText edit_senha;
    private Button bt_entrar;
    private ProgressBar pp_progressbar;
    String[] Mensagem = {"Preencha todos os campos", "cadastro realizado com sucesso"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);

        getSupportActionBar().hide();


        text_cad = findViewById(R.id.text_cad);
        edit_email = findViewById(R.id.ed_email);
        edit_senha = findViewById(R.id.ed_senha);
        bt_entrar = findViewById(R.id.bt_entrar);
        pp_progressbar = findViewById(R.id.pp_progressbar);
        mAuth = FirebaseAuth.getInstance();


        bt_entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginemail = edit_email.getText().toString();
                String loginsenha = edit_senha.getText().toString();

                if (loginemail.isEmpty() || loginsenha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,Mensagem[1],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();


                }else{

                    TelaPrincipal(v);

                }


            }
        });




        text_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, FormCadastro.class);
                startActivity(intent);
            }

        });
    }

    private void TelaPrincipal(View view) {
        String loginemail = edit_email.getText().toString();
        String loginsenha = edit_senha.getText().toString();

        mAuth.signInWithEmailAndPassword(loginemail,loginsenha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    pp_progressbar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            User();

                        }
                    },3000);

                }else{
                    String error;
                    try {
                        throw task.getException();

                    }catch (Exception e){
                        error = "Erro ao logar";

                    }
                    Snackbar snackbar = Snackbar.make(view,error,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }

            }
        });

    }



    private void User() {
        Intent intent = new Intent(FormLogin.this, Usuario_info.class);
        startActivity(intent);
        finish();
    }





}

