package es.ciudadescolar;

import java.io.File;
import java.util.List;

public class TextManager {
    File ficheroTxt;

    public TextManager(String path) {
        this.ficheroTxt = new File(path);
    }

    public void printNoticias(List<Noticia> noticias){
        
    }

}
