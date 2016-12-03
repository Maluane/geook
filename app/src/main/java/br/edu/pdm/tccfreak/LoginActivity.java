package br.edu.pdm.tccfreak;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.edu.pdm.tccfreak.helper.DatabaseHelper;
import br.edu.pdm.tccfreak.model.Usuario;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById
    protected EditText edtLogin;
    @ViewById
    protected EditText edtSenha;
    @ViewById
    protected ImageButton btnLogin;
    @ViewById
    protected ImageButton btnSair;
    @ViewById
    protected TextView txtRegistrar;

    @AfterViews
    public void inicializar() {
        // deixar o texto sublinhado
        txtRegistrar.setPaintFlags(
                txtRegistrar.getPaintFlags() |
                        Paint.UNDERLINE_TEXT_FLAG);
    }

        /**
     * Abre a tela de registro de usuário
     * @param v
     */
    public void abrirRegistro(View v) {
        Intent itUsuario = new Intent(
                this,
                UsuarioActivity_.class
        );
        // espera resultado da tela de cadastro de usuario
        startActivityForResult(itUsuario, 100);
    }

    @Click({R.id.btnSair, R.id.btnLogin})
    public void onClick(View view) {
        DatabaseHelper dh = new DatabaseHelper(this);
        switch (view.getId()) {
            case R.id.btnLogin:
                // recuperar valores da tela
                String strLogin = edtLogin.getText().toString();
                String strSenha = edtSenha.getText().toString();
                Usuario u = dh.getUsuarioByLoginSenha(strLogin, strSenha);
                if (u != null) {
                    Intent it = new Intent(this, PrincipalActivity.class);
                    it.putExtra("usuario", u);
                    startActivity(it);
                    finish();
                } else {
                    edtLogin.setText("");
                    edtSenha.setText("");
                    Toast.makeText(this, R.string.msgLoginErro,
                            Toast.LENGTH_LONG).show();
                    edtLogin.requestFocus();
                }
                break;
            case R.id.btnSair:
                break;
        }
    }

    // TODO: 5) (0,50) Adicionar um link para que o usuário possa se registrar na aplicação.
    // TODO: 6) (1,00) Construir a tela de cadastro de usuário de acordo com os campos mapeados na classe Usuario.
}
