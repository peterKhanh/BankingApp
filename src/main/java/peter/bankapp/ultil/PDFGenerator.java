package peter.bankapp.ultil;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import peter.bankapp.entity.User;

public class PDFGenerator {
	private static Logger logger = LoggerFactory.getLogger(PDFGenerator.class);
	
	public static ByteArrayInputStream customerPDFReport(List<User> list) {
		Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
        	
        	PdfWriter.getInstance(document, out);
            document.open();
        	
			// Add Text to PDF file ->
			Font font = FontFactory.getFont(FontFactory.COURIER, 14, BaseColor.BLACK);
			Paragraph para = new Paragraph( "List User Table", font);
			para.setAlignment(Element.ALIGN_CENTER);
			document.add(para);
			document.add(Chunk.NEWLINE);
        	
        	PdfPTable table = new PdfPTable(4);
        	// Add PDF Table Header ->
			Stream.of("Account Number", "First Name", "Last Name", "State of Origin")
			    .forEach(headerTitle -> {
			          PdfPCell header = new PdfPCell();
			          Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
			          header.setBackgroundColor(BaseColor.LIGHT_GRAY);
			          header.setHorizontalAlignment(Element.ALIGN_CENTER);
			          header.setBorderWidth(2);
			          header.setPhrase(new Phrase(headerTitle, headFont));
			          table.addCell(header);
			    });
           
            for (User user : list) {
            	PdfPCell idCell = new PdfPCell(new Phrase(user.getAccountNumber().toString()));
            	idCell.setPaddingLeft(4);
            	idCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            	idCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                table.addCell(idCell);

                PdfPCell firstNameCell = new PdfPCell(new Phrase(user.getFirstName()));
                firstNameCell.setPaddingLeft(4);
                firstNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                firstNameCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(firstNameCell);

                PdfPCell lastNameCell = new PdfPCell(new Phrase(String.valueOf(user.getLastName())));
                lastNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                lastNameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                lastNameCell.setPaddingRight(4);
                table.addCell(lastNameCell);

                PdfPCell stateOfOriginNameCell = new PdfPCell(new Phrase(String.valueOf(user.getStateOfOrigin())));
                lastNameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                lastNameCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                lastNameCell.setPaddingRight(4);
                table.addCell(stateOfOriginNameCell);
            }
           
            document.add(table);
            
            document.close();
        }catch(DocumentException e) {
        	logger.error(e.toString());
        }
        
		return new ByteArrayInputStream(out.toByteArray());
	}
}
