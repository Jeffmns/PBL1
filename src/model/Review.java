package model;

import java.util.Date;

public class Review {
    private int avaliacao;
    private Date data;
    private String comentario;

    public Review(int avaliacao, Date data, String comentario){
        this.avaliacao = avaliacao;
        this.data = data;
        this.comentario = comentario;
    }


    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public int getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(int avaliacao) {
        this.avaliacao = avaliacao;
    }
}