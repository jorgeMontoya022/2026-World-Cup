package co.edu.uniquindio.cup.world_app.service;

import co.edu.uniquindio.cup.world_app.model.Jugador;
import co.edu.uniquindio.cup.world_app.model.Partido;
import co.edu.uniquindio.cup.world_app.model.RegistroBitacora;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Servicio para la generación de reportes en PDF usando iText 5.
 */
public class ReporteService {

    private static final Font TITULO_FONT  = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD,
            new BaseColor(245, 197, 24));
    private static final Font HEADER_FONT  = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD,
            BaseColor.WHITE);
    private static final Font CELDA_FONT   = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL,
            BaseColor.BLACK);
    private static final Font SUBTITULO    = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD,
            BaseColor.DARK_GRAY);

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // ── R1: Usuarios por rango de fecha/hora ──────────────────────────────────

    public String generarReporteUsuarios(List<RegistroBitacora> registros,
                                          String rutaDestino) throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(rutaDestino));
        doc.open();

        agregarEncabezado(doc, "REPORTE R1 · Usuarios por Fecha y Hora");

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2f, 2f, 2.5f, 2.5f, 1.5f});

        agregarCeldaHeader(tabla, "Usuario");
        agregarCeldaHeader(tabla, "Tipo");
        agregarCeldaHeader(tabla, "Fecha Entrada");
        agregarCeldaHeader(tabla, "Fecha Salida");
        agregarCeldaHeader(tabla, "Duración");

        for (RegistroBitacora r : registros) {
            tabla.addCell(celda(r.getUsuarioUsername()));
            tabla.addCell(celda(r.getUsuarioTipo().getEtiqueta()));
            tabla.addCell(celda(r.getFechaEntrada() != null ? r.getFechaEntrada().format(FMT) : "-"));
            tabla.addCell(celda(r.getFechaSalida() != null ? r.getFechaSalida().format(FMT) : "En sesión"));
            tabla.addCell(celda(r.getDuracion()));
        }

        doc.add(tabla);
        doc.add(new Paragraph("Total registros: " + registros.size(), SUBTITULO));
        doc.close();
        return rutaDestino;
    }

    // ── R2: Jugadores por peso, estatura y equipo ─────────────────────────────

    public String generarReporteJugadores(List<Jugador> jugadores,
                                           String rutaDestino) throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(rutaDestino));
        doc.open();

        agregarEncabezado(doc, "REPORTE R2 · Jugadores por Peso y Estatura");

        PdfPTable tabla = new PdfPTable(7);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2.5f, 2f, 1.5f, 1.5f, 1.5f, 1.5f, 2f});

        agregarCeldaHeader(tabla, "Jugador");
        agregarCeldaHeader(tabla, "Equipo");
        agregarCeldaHeader(tabla, "Posición");
        agregarCeldaHeader(tabla, "Edad");
        agregarCeldaHeader(tabla, "Peso (kg)");
        agregarCeldaHeader(tabla, "Estatura (m)");
        agregarCeldaHeader(tabla, "Valor (M€)");

        for (Jugador j : jugadores) {
            tabla.addCell(celda(j.getNombreCompleto()));
            tabla.addCell(celda(j.getEquipoNombre()));
            tabla.addCell(celda(j.getPosicion()));
            tabla.addCell(celda(String.valueOf(j.getEdad())));
            tabla.addCell(celda(String.format("%.1f", j.getPeso())));
            tabla.addCell(celda(String.format("%.2f", j.getEstatura())));
            tabla.addCell(celda(String.format("%.2f", j.getValor())));
        }

        doc.add(tabla);
        doc.add(new Paragraph("Total jugadores: " + jugadores.size(), SUBTITULO));
        doc.close();
        return rutaDestino;
    }

    // ── R3: Valor total por equipo en una confederación ───────────────────────

    public String generarReporteValor(List<Object[]> datos, String confederacion,
                                       String rutaDestino) throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, new FileOutputStream(rutaDestino));
        doc.open();

        agregarEncabezado(doc, "REPORTE R3 · Valor Total por Equipo — " + confederacion);

        PdfPTable tabla = new PdfPTable(2);
        tabla.setWidthPercentage(80);
        tabla.setWidths(new float[]{3f, 2f});

        agregarCeldaHeader(tabla, "Equipo");
        agregarCeldaHeader(tabla, "Valor Total (M€)");

        double total = 0;
        for (Object[] fila : datos) {
            tabla.addCell(celda((String) fila[0]));
            double valor = (Double) fila[1];
            tabla.addCell(celda(String.format("%.2f", valor)));
            total += valor;
        }

        doc.add(tabla);
        doc.add(Chunk.NEWLINE);
        doc.add(new Paragraph(String.format("Valor total confederación: %.2f M€", total), SUBTITULO));
        doc.close();
        return rutaDestino;
    }

    // ── R4: Partidos por país anfitrión ───────────────────────────────────────

    public String generarReportePartidos(List<Partido> partidos, String pais,
                                          String rutaDestino) throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(doc, new FileOutputStream(rutaDestino));
        doc.open();

        agregarEncabezado(doc, "REPORTE R4 · Partidos en " + pais);

        PdfPTable tabla = new PdfPTable(5);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{2f, 2f, 2.5f, 2f, 1.5f});

        agregarCeldaHeader(tabla, "Local");
        agregarCeldaHeader(tabla, "Visitante");
        agregarCeldaHeader(tabla, "Estadio");
        agregarCeldaHeader(tabla, "Fecha/Hora");
        agregarCeldaHeader(tabla, "Grupo");

        for (Partido p : partidos) {
            tabla.addCell(celda(p.getEquipoLocalNombre()));
            tabla.addCell(celda(p.getEquipoVisitanteNombre()));
            tabla.addCell(celda(p.getEstadioNombre()));
            tabla.addCell(celda(p.getFechaHora() != null ? p.getFechaHora().format(FMT) : "-"));
            tabla.addCell(celda("Grupo " + p.getGrupoNombre()));
        }

        doc.add(tabla);
        doc.add(new Paragraph("Total partidos: " + partidos.size(), SUBTITULO));
        doc.close();
        return rutaDestino;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void agregarEncabezado(Document doc, String titulo) throws DocumentException {
        Paragraph p = new Paragraph("⚽  MUNDIAL 2026 — " + titulo, TITULO_FONT);
        p.setAlignment(Element.ALIGN_CENTER);
        p.setSpacingAfter(16);
        doc.add(p);
    }

    private void agregarCeldaHeader(PdfPTable tabla, String texto) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, HEADER_FONT));
        celda.setBackgroundColor(new BaseColor(10, 22, 40));
        celda.setPadding(6);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        tabla.addCell(celda);
    }

    private PdfPCell celda(String texto) {
        PdfPCell c = new PdfPCell(new Phrase(texto != null ? texto : "", CELDA_FONT));
        c.setPadding(5);
        return c;
    }
}
