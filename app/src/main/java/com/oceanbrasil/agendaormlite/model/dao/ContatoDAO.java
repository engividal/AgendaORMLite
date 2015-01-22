package com.oceanbrasil.agendaormlite.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.oceanbrasil.agendaormlite.model.bean.Contato;

import java.sql.SQLException;
import java.util.List;

/**
 * Classe usada para persistência da tabela de Contatos
 */
public class ContatoDAO extends OrmLiteSqliteOpenHelper {
    // Nome do arquivo de banco de dados do SQLite
    private static final String DATABASE_NAME = "agenda.db";

    // Versão atual do banco de dados
    private static final int DATABAS_VERSION = 1;

    // Atributo utilizado para a persistencia de Contatos
    private Dao<Contato, Long> dao = null;

    public ContatoDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABAS_VERSION);
    }

    /**
     * Método chamado quando a base de dados é criada
     * @param database
     * @param source
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource source) {
        try {
            Log.i(ContatoDAO.class.getSimpleName(), "onCreate()");
            TableUtils.createTable(source, Contato.class);
            Contato contato = new Contato();
            contato.setNome("Administrator");
            contato.setEmail("admin@oceanbrasil.com");
            contato.setTelefone("99134-3664");
            cadastrar(contato);
        } catch (SQLException ex) {
            Log.e(ContatoDAO.class.getSimpleName(), "onCreate(): Falha ao criar tabelas", ex);
        }
    }

    /**
     *
     * @param database
     * @param source
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource source, int oldVersion, int newVersion) {

        try {
            Log.i(ContatoDAO.class.getSimpleName(), "onUpgrade()");
            // Conexão, tabela é true para ignorar erros
            TableUtils.dropTable(source,Contato.class,true);
        } catch (SQLException ex) {
            Log.e(ContatoDAO.class.getSimpleName(), "onUpgrade(): Falha na atualização", ex);
        }
    }

    public Dao<Contato, Long> getDao(){
        if (dao == null){
            try{
                dao = getDao(Contato.class);
            } catch (SQLException e) {
                Log.e(ContatoDAO.class.getSimpleName(), "getDao(): Falha ao criar DAO", e);
            }
        }
        return dao;
    }

    @Override
    /**
     * Método para encerrar conexão com o BD
     */
    public void close() {
        super.close();
        dao = null;
    }

    /**
     * Método para Cadastro de Contatos
     * @param contato
     * @throws SQLException
     */
    public void cadastrar(Contato contato) throws SQLException{
        getDao().create(contato);
    }

    /**
     * Método para Excluir Contatos
     * @param contato
     * @throws SQLException
     */
    public void excluir(Contato contato) throws SQLException{
        getDao().delete(contato);
    }

    /**
     * Método para Alterar Contatos
     * @param contato
     * @throws SQLException
     */
    public void alterar(Contato contato) throws SQLException{
        getDao().update(contato);
    }

    /**
     * Método para listagem de contatos
     * @return
     * @throws SQLException
     */
    public List<Contato> listar() throws SQLException{
        return getDao().queryForAll();
    }
}
