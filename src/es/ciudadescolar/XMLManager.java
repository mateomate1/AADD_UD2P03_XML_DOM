package es.ciudadescolar;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class XMLManager {
    File ficheroXML;

    public XMLManager(String path) {
        this.ficheroXML = new File(path);
    }

    public boolean getNoticiasCultura(){
        List<Noticia> noticiasCultura = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setIgnoringElementContentWhitespace(true);
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new RssErrorHandler());
            Document documento = db.parse(ficheroXML);
            Element elementoRaiz = documento.getDocumentElement();
            NodeList noticias = elementoRaiz.getElementsByTagName("item");
            for (int i = 0; i < noticias.getLength(); i++) {
                Element noticia = (Element)noticias.item(i);
                String titulo = noticia.getElementsByTagName("title").item(0).getTextContent();
                
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return true; //TODO: Gestionar return
    }

    public boolean rename(String nombreFichero, String nombreFinal) {
        try {
            File fichero = new File(nombreFichero);
            fichero.renameTo(new File(nombreFinal));
            ficheroXML = fichero;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean downloadFileFromInternet(String urlString, String fichName) throws MalformedURLException, URISyntaxException {
        URL url = new URI(urlString).toURL();
        BufferedInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(fichName);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (IOException e) {
                System.err.println("error de E/S durante el cierre de los flujos");
                return false;
            }
        }
        return true;
    }

    public static String getFechaPortada(File ficheroXmlNoticias) {
        DocumentBuilderFactory dbf = null;
        DocumentBuilder db = null;
        Document documento = null;
        // Formato de la fecha original en RSS (RFC_1123)
        DateTimeFormatter formatoEntrada = DateTimeFormatter.RFC_1123_DATE_TIME;
        // Formato de salida deseado: yyyyMMdd
        DateTimeFormatter formatoSalida = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.ENGLISH);
        ZonedDateTime fecha = ZonedDateTime.now();
        try {
            dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false); // Cuando el xml a procesar no se valida
            db = dbf.newDocumentBuilder();
            db.setErrorHandler(new RssErrorHandler());
            documento = db.parse(ficheroXmlNoticias);
            Element elementoRss = documento.getDocumentElement();
            String fechaPortada = elementoRss.getElementsByTagName("lastBuildDate").item(0).getTextContent();
            fecha = ZonedDateTime.parse(fechaPortada, formatoEntrada);
        } catch (Exception e) {
            System.err.println("Error" + e.getMessage());
        }
        return fecha.format(formatoSalida);
    }
}
