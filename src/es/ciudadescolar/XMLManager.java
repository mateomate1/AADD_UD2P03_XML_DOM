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
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class XMLManager {
    File ficheroXML;

    public XMLManager(String path) {
        this.ficheroXML = new File(path);
    }

    public List<Noticia> getNoticiasCategoria(String categoriaString){
        List<Noticia> noticiasCategoria = new ArrayList<>();
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating(false);
        dbf.setIgnoringElementContentWhitespace(true);
        int numNoticias = 0;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler(new RssErrorHandler());
            Document documento = db.parse(ficheroXML);
            Element elementoRaiz = documento.getDocumentElement();
            NodeList noticias = elementoRaiz.getElementsByTagName("item");
            for (int i = 0; i < noticias.getLength(); i++) {
                Element elementNoticia = (Element)noticias.item(i);
                String titulo = elementNoticia.getElementsByTagName("title").item(0).getTextContent();
                String description = elementNoticia.getElementsByTagName("description").item(0).getTextContent();
                String creator = elementNoticia.getElementsByTagName("dc:creator").item(0).getTextContent();
                List<String> categorias = new ArrayList<>();
                NodeList NodosCategorias = elementNoticia.getElementsByTagName("category");
                for (int j = 0; j < NodosCategorias.getLength(); j++) {
                    Element categoria = (Element) NodosCategorias.item(j);
                    categorias.add(categoria.getTextContent());
                }
                Noticia noticia = new Noticia(titulo, description, creator, categorias);
                numNoticias++;
                if (noticia.hasCategoria(categoriaString))
                    noticiasCategoria.add(noticia);
            }
            System.out.println("El listado de noticias con categoria " + categoriaString + " esta compuesto por "+noticiasCategoria.size()+" noticias");
        } catch (DOMException | ParserConfigurationException | SAXException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return noticiasCategoria;
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

    public boolean downloadFileFromInternet(String urlString) throws MalformedURLException, URISyntaxException {
        URL url = new URI(urlString).toURL();
        BufferedInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(ficheroXML.getPath());
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

    public String getFechaPortada() {
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
            documento = db.parse(ficheroXML);
            Element elementoRss = documento.getDocumentElement();
            String fechaPortada = elementoRss.getElementsByTagName("lastBuildDate").item(0).getTextContent();
            fecha = ZonedDateTime.parse(fechaPortada, formatoEntrada);
        } catch (Exception e) {
            System.err.println("Error" + e.getMessage());
        }
        return fecha.format(formatoSalida);
    }
}
