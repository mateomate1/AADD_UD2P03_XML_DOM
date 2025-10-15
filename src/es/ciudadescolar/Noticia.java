package es.ciudadescolar;

import java.util.List;

public class Noticia {
    private String title;
    private String description;
    private String creator;
    private List<String> categorias;

    public Noticia() {
    }

    public Noticia(String title, String description, String creator, List<String> categorias) {
        this.title = title;
        this.description = description;
        this.creator = creator;
        this.categorias = categorias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<String> categorias) {
        this.categorias = categorias;
    }

    public boolean hasCategoria(String categoria){
        categoria = categoria.toLowerCase();
        for (String c : categorias) {
            c = c.toLowerCase();
            if(categoria.equals(c))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return title + "|" + creator + "|" + description;
    }
}
