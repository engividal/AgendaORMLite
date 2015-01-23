package com.oceanbrasil.agendaormlite.activity;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.oceanbrasil.agendaormlite.R;
import com.oceanbrasil.agendaormlite.helper.FormularioHelper;
import com.oceanbrasil.agendaormlite.model.bean.Contato;
import com.oceanbrasil.agendaormlite.model.dao.ContatoDAO;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class ListagemActivity extends ActionBarActivity {

    // Coleção de strings que serão exibidas
    private List<String> listaDeContatos = new ArrayList<>();

    // Tag usada para controle no LogCat
    private final String TAG = ActionBarActivity.class.getSimpleName();

    // Adapter usado para exibir as Strings na ListView
    private ArrayAdapter<String> adapter = null;

    // Objeto Helper do Formulário
    private FormularioHelper formularioHelper = null;

    // Layout de exibição da listview
    int adapterLayout = android.R.layout.simple_list_item_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listagemlayout);

        // ListView que exibirá os dados do Contato
        ListView lvListagem = (ListView) findViewById(R.id.lvListagem);

        carregarLista();
        adapter = new ArrayAdapter<String>(this, adapterLayout, listaDeContatos);
        lvListagem.setAdapter(adapter);

        formularioHelper = new FormularioHelper(this);

        Button btSalvar = (Button) findViewById(R.id.btSalvar);
        btSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContatoDAO dao = new ContatoDAO(ListagemActivity.this);
                try {
                    dao.cadastrar(formularioHelper.getContato());
                    carregarLista();
                    adapter.notifyDataSetChanged();
                    formularioHelper.setContato(new Contato());
                } catch (SQLException e) {
                    Log.e(TAG, "falha ao salvar Contato");
                } finally {
                    dao.close();
                }
            }
        });
    }

    /**
     * Método que solicita serviço da camada de modelo
     * e atualiza a lista de contatos exibida
     */
    private void carregarLista() {
        // Limpa a colção de contatos exibidos
        listaDeContatos.clear();

        // Criação do objeto de persistência
        ContatoDAO dao = new ContatoDAO(this);
        List<Contato> lista = null;
        try{
            // Solicitação de serviço da camada model
            lista = dao.listar();
        }catch (SQLException e){
            // Tratamento da exceção lançada pela model
            Log.e(TAG, "falha ao carregar lista");
        }
        for(Contato contato: lista){
            listaDeContatos.add(contato.toString());
        }
        // Encerramento da conexão
        dao.close();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listagemmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
