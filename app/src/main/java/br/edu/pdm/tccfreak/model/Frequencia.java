package br.edu.pdm.tccfreak.model;

import com.j256.ormlite.field.DatabaseField;

import java.util.Date;

/**
 * Classe que representa uma frequência
 */
public class Frequencia {
    @DatabaseField(generatedId = true)
    private Integer codigo;
    @DatabaseField(canBeNull = false)
    private Date data;
    @DatabaseField(canBeNull = false,
            foreign = true,
            foreignColumnName = "codigo",
            foreignAutoRefresh = true,
            columnName = "codigotrabalho"
    )
    private Trabalho trabalho;

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Trabalho getTrabalho() {
        return trabalho;
    }

    public void setTrabalho(Trabalho trabalho) {
        this.trabalho = trabalho;
    }
}
