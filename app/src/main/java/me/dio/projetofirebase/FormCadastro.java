package me.dio.projetofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FormCadastro extends AppCompatActivity {

    private EditText ed_nome,ed_email,ed_senha;
    private Button bt2;
    String[] mensagem = {"Preencha todos os campos", "cadastro realizado com sucesso"};
    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro);

        getSupportActionBar().hide();
        IniciarCompomentes();

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = ed_nome.getText().toString();
                String email = ed_email.getText().toString();
                String senha = ed_senha.getText().toString();

                if (nome.isEmpty()  || email.isEmpty() || senha.isEmpty()){
                    Snackbar snackbar = Snackbar.make(v,mensagem[0],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else{
                    CadastrarUsuarios(v);
                }
            }
        });
    }

    private void CadastrarUsuarios(View v){
        String email = ed_email.getText().toString();
        String senha = ed_senha.getText().toString();

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    SalvarDadosUsuario();


                    Snackbar snackbar = Snackbar.make(v,mensagem[1],Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();

                }else{
                    String erro;
                    try {
                        throw task.getException();

                    }catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Digite uma senha com no minimo 6 caracteres";

                    }catch (FirebaseAuthUserCollisionException e) {
                        erro = "Essa conta ja existe";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        erro = "E-mail invalido";

                    }catch (Exception e){
                        erro = "Erro ao cadastrar";

                    }
                    Snackbar snackbar = Snackbar.make(v,erro,Snackbar.LENGTH_SHORT);
                    snackbar.setBackgroundTint(Color.WHITE);
                    snackbar.setTextColor(Color.BLACK);
                    snackbar.show();
                }
            }
        });

    }

    private void SalvarDadosUsuario(){
        String nome = ed_nome.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String,Object> usuarios = new HashMap<>();
        usuarios.put("nome",nome);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("usuarios").document(usuarioID);

        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db","Sucesso ao salvar os dados");

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("db_erro","Erro ao salvar os dados" + e.toString());

                    }
                });
    }


    private void IniciarCompomentes(){

        ed_nome = findViewById(R.id.ed_nome);
        ed_email = findViewById(R.id.ed_email);
        ed_senha = findViewById(R.id.ed_senha);
        bt2 = findViewById(R.id.bt2);
    }

}