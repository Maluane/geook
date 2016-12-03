package br.edu.pdm.tccfreak.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.List;

import br.edu.pdm.tccfreak.R;
import br.edu.pdm.tccfreak.model.Aluno;
import br.edu.pdm.tccfreak.model.Frequencia;
import br.edu.pdm.tccfreak.model.FrequenciaAluno;
import br.edu.pdm.tccfreak.model.Trabalho;
import br.edu.pdm.tccfreak.model.Usuario;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "tccfreak.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the SimpleData table
    private Dao<Usuario, Integer> usuarioDao = null;
    private RuntimeExceptionDao<Usuario, Integer> usuarioRuntimeDao = null;
    private RuntimeExceptionDao<Aluno, Integer> alunoRuntimeDao = null;
    private RuntimeExceptionDao<Trabalho, Integer> trabalhoRuntimeDao = null;
    private RuntimeExceptionDao<Frequencia, Integer> frequenciaRuntimeDao = null;
    private RuntimeExceptionDao<FrequenciaAluno, Integer> frequenciaAlunoRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTable(connectionSource, Usuario.class);
            TableUtils.createTable(connectionSource, Aluno.class);
            TableUtils.createTable(connectionSource, Trabalho.class);
            TableUtils.createTable(connectionSource, Frequencia.class);
            TableUtils.createTable(connectionSource, FrequenciaAluno.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

        // here we try inserting data in the on-create as a test
        RuntimeExceptionDao<Usuario, Integer> uDao = getUsuarioDao();
        // create some entries in the onCreate
        Usuario usuario = new Usuario();
        usuario.setNome("tccfreak");
        usuario.setEmail("tccfreak@teste.com");
        usuario.setLogin("tccfreak");
        usuario.setSenha("tccfreak");
        uDao.create(usuario);

        // carrega dados de trabalhos e alunos
        loadDados();

        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate!");
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Usuario.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Usuario, Integer> getDao() throws SQLException {
        if (usuarioDao == null) {
            usuarioDao = getDao(Usuario.class);
        }
        return usuarioDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our SimpleData class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Usuario, Integer> getUsuarioDao() {
        if (usuarioRuntimeDao == null) {
            usuarioRuntimeDao = getRuntimeExceptionDao(Usuario.class);
        }
        return usuarioRuntimeDao;
    }

    public RuntimeExceptionDao<Aluno, Integer> getAlunoDao() {
        if (alunoRuntimeDao == null) {
            alunoRuntimeDao = getRuntimeExceptionDao(Aluno.class);
        }
        return alunoRuntimeDao;
    }

    public RuntimeExceptionDao<Trabalho, Integer> getTrabalhoDao() {
        if (trabalhoRuntimeDao == null) {
            trabalhoRuntimeDao = getRuntimeExceptionDao(Trabalho.class);
        }
        return trabalhoRuntimeDao;
    }

    public RuntimeExceptionDao<Frequencia, Integer> getFrequenciaDao() {
        if (frequenciaRuntimeDao == null) {
            frequenciaRuntimeDao = getRuntimeExceptionDao(Frequencia.class);
        }
        return frequenciaRuntimeDao;
    }

    public RuntimeExceptionDao<FrequenciaAluno, Integer> getFrequenciaAlunoDao() {
        if (frequenciaAlunoRuntimeDao == null) {
            frequenciaAlunoRuntimeDao = getRuntimeExceptionDao(FrequenciaAluno.class);
        }
        return frequenciaAlunoRuntimeDao;
    }

    public List<Trabalho> listTrabalhos() {
        List<Trabalho> trabalhos = null;
        try {
            trabalhos = getTrabalhoDao().
                    queryBuilder().
                    orderBy("titulo", true).
                    query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trabalhos;
    }

    public List<Aluno> listAlunos() {
        List<Aluno> alunos = null;
        try {
            alunos = getAlunoDao().
                     queryBuilder().
                     orderBy("nome", true).
                     query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alunos;
    }

    public List<Trabalho> listTrabalhosByCurso
    (String curso) {
        List<Trabalho> trabalhos = null;
        try {
            trabalhos = getTrabalhoDao().
                    queryBuilder().
                    where().eq("curso", curso).
                    query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return trabalhos;
    }

    public Usuario getUsuarioByLoginSenha(String login, String senha) {
        Usuario u = null;
        try {
            QueryBuilder<Usuario, Integer> qry =
                    getUsuarioDao().queryBuilder();
            qry.where().eq("login", login).and().eq("senha", senha);
            List<Usuario> lstUsuarios = qry.query();
            if (lstUsuarios != null && lstUsuarios.size() > 0) {
                u = lstUsuarios.get(0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return u;
    }

    private void loadDados() {
        Trabalho t = new Trabalho();
        t.setTitulo("Pokemon Go");
        t.setCurso("Ciência da Computação");
        getTrabalhoDao().create(t);
        t = new Trabalho();
        t.setTitulo("Java JCP");
        t.setCurso("Ciência da Computação");
        getTrabalhoDao().create(t);
        t = new Trabalho();
        t.setTitulo("Administração de empresas");
        t.setCurso("Administração");
        getTrabalhoDao().create(t);

        // criando alguns alunos
        Aluno a = new Aluno();
        a.setNome("Adroaldo Braga");
        a.setCurso("Ciência da Computação");
        getAlunoDao().create(a);
        a = new Aluno();
        a.setNome("Armando Joergensen");
        a.setCurso("Ciência da Computação");
        getAlunoDao().create(a);
        a = new Aluno();
        a.setNome("Douglas Roldo");
        a.setCurso("Ciência da Computação");
        getAlunoDao().create(a);
        a = new Aluno();
        a.setNome("Douglas Roldo");
        a.setCurso("Ciência da Computação");
        getAlunoDao().create(a);
        a = new Aluno();
        a.setNome("Michael Oliveira");
        a.setCurso("Administração");
        getAlunoDao().create(a);
        a = new Aluno();
        a.setNome("Cristian Cardoso");
        a.setCurso("Administração");
        getAlunoDao().create(a);
        a = new Aluno();
        a.setNome("Marcos Perin");
        a.setCurso("Administração");
        getAlunoDao().create(a);
    }

    public void saveOrUpdateUsuario(Usuario usuario) {
        getUsuarioDao().createOrUpdate(usuario);
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        usuarioDao = null;
        usuarioRuntimeDao = null;
    }
}