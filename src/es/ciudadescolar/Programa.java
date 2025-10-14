package es.ciudadescolar;
public class Programa {
    public static void main(String[] args) throws Exception {
        String link = "https://feeds.elpais.com/mrss-s/pages/ep/site/elpais.com/portada";
        TextManager.downloadFileFromInternet(link, "test.xml");
    }
}
