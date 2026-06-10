package ru.hse.eventmanager.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import ru.hse.eventmanager.model.Event;
import ru.hse.eventmanager.model.Registration;
import ru.hse.eventmanager.model.User;
import ru.hse.eventmanager.repository.EventRepository;
import ru.hse.eventmanager.repository.RegistrationRepository;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class ReportService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;

    public ReportService(EventRepository eventRepository,
                         RegistrationRepository registrationRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
    }

    public byte[] generateEventReport(Integer eventId) throws Exception {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Мероприятие не найдено"));

        List<Registration> registrations = registrationRepository.findByIdEvent(event);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, out);
        document.open();

        Font titleFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        Font headerFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL);

        document.add(new Paragraph("Отчёт по мероприятию", titleFont));
        document.add(new Paragraph("Название: " + event.getTitle(), normalFont));
        document.add(new Paragraph("Дата: " + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(event.getEventDate()), normalFont));
        document.add(new Paragraph("Место: " + (event.getIdVenue() != null ? event.getIdVenue().getName() : "—"), normalFont));
        document.add(Chunk.NEWLINE);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new int[]{1, 4, 4, 3});

        String[] headers = {"№", "ФИО", "Email", "Дата регистрации"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, headerFont));
            cell.setBackgroundColor(new java.awt.Color(220, 220, 220));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setPadding(5);
            table.addCell(cell);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        int i = 1;
        for (Registration r : registrations) {
            User u = r.getIdUser();
            table.addCell(new Phrase(String.valueOf(i++), normalFont));
            table.addCell(new Phrase(u.getName() != null ? u.getName() : "—", normalFont));
            table.addCell(new Phrase(u.getEmail() != null ? u.getEmail() : "—", normalFont));
            table.addCell(new Phrase(r.getRegisteredAt() != null ? sdf.format(r.getRegisteredAt()) : "—", normalFont));
        }

        document.add(table);
        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Итого участников: " + registrations.size(), normalFont));

        document.close();
        return out.toByteArray();
    }
}