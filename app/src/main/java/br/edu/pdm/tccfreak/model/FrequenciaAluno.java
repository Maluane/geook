package br.edu.pdm.tccfreak.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Classe que representa a frequencia dos alunos ...
 */
@DatabaseTable
public class FrequenciaAluno {
    @DatabaseField(generatedId = true)
    private Integer codigo;
    @DatabaseField(canBeNull = false,
            foreign = true,
            foreignColumnName = "codigo",
            foreignAutoRefresh = true,
            columnName = "codigofrequencia"
    )
    private Frequencia frequencia;
    @DatabaseField(canBeNull = false,
            foreign = true,
            foreignColumnName = "codigo",
            foreignAutoRefresh = true,
            columnName = "codigoaluno"
    )
    private Aluno aluno;
    @DatabaseField
    private String situacao;
    @DatabaseField
    private String assinatura;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Frequencia getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(Frequencia frequencia) {
        this.frequencia = frequencia;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getAssinatura() {
        return assinatura;
    }

    public void setAssinatura(String assinatura) {
        this.assinatura = assinatura;
    }
}
