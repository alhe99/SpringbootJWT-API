package com.springboot.app.view.pdf;

import java.awt.Color;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.springboot.app.models.entity.Factura;
import com.springboot.app.models.entity.ItemFactura;

@Component("factura/ver")
public class FacturaPdfView extends AbstractPdfView {

	@Override
	protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		Factura factura = (Factura) model.get("factura");
		
		//MessageSourceAccessor mensajes = getMessageSourceAccessor();

		PdfPTable tabla = new PdfPTable(1);
		tabla.setSpacingAfter(20);

		PdfPCell cell = null;
		cell = new PdfPCell(new Phrase("Datos del Cliente"));
		cell.setBackgroundColor(new Color(184, 218, 255));
		cell.setPadding(8f);
		tabla.addCell(cell);

		tabla.addCell(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());
		tabla.addCell(factura.getCliente().getEmail());

		PdfPTable tabla2 = new PdfPTable(1);
		tabla2.setSpacingAfter(20);

		cell = new PdfPCell(new Phrase("Datos de la Factura"));
		cell.setBackgroundColor(new Color(195, 230, 203));
		cell.setPadding(8f);

		tabla2.addCell(cell);
		tabla2.addCell("Folio: " + factura.getId());
		tabla2.addCell("Descripción: " + factura.getDescripcion());
		tabla2.addCell("Fecha: " + factura.getCreateAt());

		PdfPTable tabla3 = new PdfPTable(4);
		tabla3.setWidths(new float[] { 2.5f, 1, 1, 1 });
		tabla3.addCell("Producto");
		tabla3.addCell("Precio");
		tabla3.addCell("Cantidad");
		tabla3.addCell("Total");

		for (ItemFactura item : factura.getDetalleFactura()) {
			tabla3.addCell(item.getProducto().getNombre());
			tabla3.addCell(item.getProducto().getPrecio().toString());

			cell = new PdfPCell(new Phrase(item.getCantidad().toString()));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			tabla3.addCell(cell);

			tabla3.addCell("$" + item.calcularTotal().toString());
		}

		cell = new PdfPCell(new Phrase("Total: "));
		cell.setColspan(3);
		cell.setHorizontalAlignment(Cell.ALIGN_RIGHT);
		tabla3.addCell(cell);
		tabla3.addCell("$" + factura.getTotal().toString());

		document.add(tabla);
		document.add(tabla2);
		document.add(tabla3);
	}

}
