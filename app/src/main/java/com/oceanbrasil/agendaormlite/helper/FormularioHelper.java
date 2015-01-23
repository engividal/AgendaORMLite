package com.oceanbrasil.agendaormlite.helper;

import android.widget.EditText;

import com.oceanbrasil.agendaormlite.R;
import com.oceanbrasil.agendaormlite.activity.ListagemActivity;
import com.oceanbrasil.agendaormlite.model.bean.Contato;

/**
 * Classe criada para utilizar o padrão de projetos helper
 */
public class FormularioHelper {

    // Campos do Formulário
    private EditText nome;
    private EditText telefone;
    private EditText email;

    // Contato Gerado pelo Helper
    private Contato contato;

    public FormularioHelper(ListagemActivity activity){

        // Bind dos componentes da tela com atributos do Helper
        nome = (EditText) activity.findViewById(R.id.edNome);
        telefone = (EditText) activity.findViewById(R.id.edTelefone);
        email = (EditText) activity.findViewById(R.id.edEmail);

        // Criação do objeto Aluno
        contato = new Contato();
    }

    public Contato getContato(){
        contato.setNome(nome.getText().toString());
        contato.setTelefone(telefone.getText().toString());
        contato.setEmail(email.getText().toString());

        return contato;
    }

    public void setContato(Contato contato){
        nome.setText(contato.getNome());
        telefone.setText(contato.getTelefone());
        email.setText(contato.getEmail());

        this.contato = contato;
    }

}
