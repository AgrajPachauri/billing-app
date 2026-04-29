package com.app.billing.controller;

import com.app.billing.dto.BillRequest;
import com.app.billing.model.Customer;
import com.app.billing.model.Entry;
import com.app.billing.repository.CustomerRepository;
import com.app.billing.repository.EntryRepository;
import java.io.File;
import com.app.billing.dto.BillRequest;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;
import java.math.BigInteger;


import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPageSz;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private EntryRepository entryRepo;

    @GetMapping("/test")
    public String test() {
        return "Bill API working";
    }
    @PostMapping("/generate")
public String generateBill(@RequestBody BillRequest request) {

    try {
        XWPFDocument doc = new XWPFDocument();

        List<Customer> customers = customerRepo.findAllById(request.getCustomerIds());

        for (Customer c : customers) {

            // Header
            XWPFParagraph title = doc.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun run = title.createRun();
            run.setText("AGRAJ STORE");   // later dynamic
            run.setBold(true);
            run.setFontSize(16);

            // Owner + Contact (Left & Right)
            XWPFParagraph top = doc.createParagraph();
            XWPFRun topRun = top.createRun();
            topRun.setText("Owner: Agraj        Contact: 9876543210");

            // Address
            XWPFParagraph address = doc.createParagraph();
            address.createRun().setText("Address: Gurgaon");

            // Customer Info
            XWPFParagraph customerInfo = doc.createParagraph();
            XWPFRun cRun = customerInfo.createRun();

            cRun.setText("Customer: " + c.getName());
            cRun.addBreak();
            cRun.setText("Address: " + c.getAddress());
            cRun.addBreak();
            cRun.setText("Contact: " + c.getContact());
            cRun.addBreak();
            cRun.setText("Month: " + request.getMonth() + "/" + request.getYear());

            // Date range
            LocalDate start = LocalDate.of(request.getYear(), request.getMonth(), 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

            List<Entry> entries = entryRepo.findByCustomerIdAndDateBetween(
                    c.getId(), start, end
            );

            // Table (32 rows, 3 cols)
            XWPFTable table = doc.createTable(16, 6);

            table.getRow(0).getCell(0).setText("Date");
            table.getRow(0).getCell(1).setText("Product");
            table.getRow(0).getCell(2).setText("Qty");
            table.getRow(0).getCell(3).setText("Date");
            table.getRow(0).getCell(4).setText("Product");
            table.getRow(0).getCell(5).setText("Qty");

            int totalQty = 0;

            for (int i = 1; i <= 15; i++) {
                int row = i;
                // Left side
                table.getRow(row).getCell(0).setText(String.valueOf(i));
                // Right side
                table.getRow(row).getCell(3).setText(String.valueOf(i + 15));

                for (Entry e : entries) {
                    int day = e.getDate().getDayOfMonth();

                    if (day == i) {
                    table.getRow(row).getCell(1).setText(e.getProduct().getProductName());
                    table.getRow(row).getCell(2).setText(String.valueOf(e.getQuantity()));
                    }

                    if (day == i + 15) {
                    table.getRow(row).getCell(4).setText(e.getProduct().getProductName());
                    table.getRow(row).getCell(5).setText(String.valueOf(e.getQuantity()));
                    }
                }
            }
            double pricePerUnit = 50; // temporary (later from DB)
            double totalAmount = totalQty * pricePerUnit;
            // Total
            XWPFParagraph total = doc.createParagraph();
            XWPFRun tRun = total.createRun();
            tRun.addBreak();
            tRun.setText("Total Quantity: " + totalQty);
            tRun.addBreak();
            tRun.setText("Price per unit: ₹" + pricePerUnit);
            tRun.addBreak();
            tRun.setText("Total Amount: ₹" + totalAmount);

            // // Page break
            // XWPFParagraph breakPara = doc.createParagraph();
            // breakPara.setPageBreak(true);

            // A5 Page Layout
            CTSectPr sectPr = doc.getDocument().getBody().addNewSectPr();
            CTPageSz pageSize = sectPr.addNewPgSz();

            pageSize.setW(BigInteger.valueOf(11900)); // A5 width
            pageSize.setH(BigInteger.valueOf(16840)); // A5 height

            // // Page break
            // XWPFParagraph breakPara = doc.createParagraph();
            // breakPara.setPageBreak(true);
        }

        // Create folder
        File folder = new File("Files");
        if (!folder.exists()) folder.mkdirs();

        String fileName = "Files/Bill_" + request.getMonth() + "_" + request.getYear() + ".docx";

        FileOutputStream out = new FileOutputStream(fileName);
        doc.write(out);
        out.close();

        return "Bill generated: " + fileName;

    } catch (Exception e) {
        e.printStackTrace();
        return "Error: " + e.getMessage();
    }
}
@GetMapping("/generate-test2")
public String testBill() {

    BillRequest req = new BillRequest();
    req.setMonth(1);
    req.setYear(2026);

    req.setCustomerIds(customerRepo.findAll()
            .stream()
            .map(Customer::getId)
            .toList());

    return generateBill(req);
}
}