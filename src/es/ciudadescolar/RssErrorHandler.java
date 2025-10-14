package es.ciudadescolar;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class RssErrorHandler implements ErrorHandler {

    @Override
    public void warning(SAXParseException exception) throws SAXException {
        System.out.println("Unimplemented method 'warning'");
    }

    @Override
    public void error(SAXParseException exception) throws SAXException {
        throw new UnsupportedOperationException("Unimplemented method 'error'");
    }

    @Override
    public void fatalError(SAXParseException exception) throws SAXException {
        throw new UnsupportedOperationException("Unimplemented method 'fatalError'");
    }

}
