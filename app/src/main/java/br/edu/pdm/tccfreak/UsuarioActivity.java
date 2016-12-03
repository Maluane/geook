package br.edu.pdm.tccfreak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.Receiver;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;

import br.edu.pdm.tccfreak.helper.DatabaseHelper;
import br.edu.pdm.tccfreak.model.Usuario;

@EActivity(R.layout.activity_usuario)
public class UsuarioActivity extends AppCompatActivity {
    @ViewById
    protected ImageView imvFoto;
    @ViewById
    protected EditText edtNome;
    @ViewById
    protected EditText edtEmail;
    @ViewById
    protected EditText edtLogin;
    @ViewById
    protected EditText edtSenha;
    @ViewById
    protected Button btnSalvar;
    @ViewById
    protected Button btnFechar;

    private DatabaseHelper dh = null;

    @AfterViews
    public void inicializar() {
        dh = new DatabaseHelper(this);
    }

    @Click({R.id.btnSalvar, R.id.btnFechar, R.id.btnMapa})
    public void adicionaBotoes(View v) {
        if (v.getId() == R.id.btnMapa){
            Intent itMapa = new Intent(this, MapaActivity.class);
            startActivityForResult(itMapa, 101);
        }
        if (v.getId() == R.id.btnSalvar) {
            Usuario u = new Usuario();

            u.setNome(edtNome.getText().toString());
            u.setEmail(edtEmail.getText().toString());
            u.setLogin(edtLogin.getText().toString());
            u.setSenha(edtSenha.getText().toString());

            Bitmap foto = ((BitmapDrawable) imvFoto.getDrawable()).getBitmap();
            ByteArrayOutputStream sFoto = new ByteArrayOutputStream();
            foto.compress(Bitmap.CompressFormat.PNG, 100, sFoto);
            u.setFoto(sFoto.toByteArray());
            dh.saveOrUpdateUsuario(u);

           Toast toast = Toast.makeText(this, "Dados cadastrados", Toast.LENGTH_LONG);
            toast.show();

            edtNome.setText("");
            edtEmail.setText("");
            edtLogin.setText("");
            edtSenha.setText("");

        }
    }


    @LongClick(R.id.imvFoto)
    public void acionaCamera(View v) {
        Intent itCamera = new
                Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(itCamera, 100);
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            Bitmap foto = (Bitmap)
                    data.getExtras().get("data");
            // redimensionar a foto
            Bitmap fotoRedimensionada
                    = Bitmap.createScaledBitmap(foto,
                    imvFoto.getWidth(),
                    imvFoto.getHeight(), false
            );
            imvFoto.setImageBitmap(fotoRedimensionada);
        }
        if (requestCode == 101 && resultCode == RESULT_OK){
            LatLng latLng = (LatLng)data.getParcelableExtra("latlng");
            Toast.makeText(UsuarioActivity.this, "Localização: " + latLng.latitude + " - " + latLng.longitude,
                    Toast.LENGTH_LONG).show();
        }
    }
}
