package es.ciudadescolar;

import java.util.List;

public class Programa {
    public static void main(String[] args) throws Exception {
        String urlString = "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada";
        XMLManager xmlManager = new XMLManager("test.xml");
        //xmlManager.downloadFileFromInternet(urlString);
        List<Noticia> noticiasCultura = xmlManager.getNoticiasCategoria("cultura");
        String fecha = xmlManager.getFechaPortada();
        TextManager txtManager = new TextManager(" noticias_Cultura_"+fecha+".txt");
    }
}
