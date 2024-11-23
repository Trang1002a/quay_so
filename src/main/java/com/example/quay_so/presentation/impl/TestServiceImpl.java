package com.example.quay_so.presentation.impl;

import com.example.quay_so.model.request.rate.PostVoteRequest;
import com.example.quay_so.model.response.ResStatus;
import com.example.quay_so.presentation.service.TestService;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public ResStatus postTestDownloadFile(PostVoteRequest request) {
        ClassLoader classLoader = TestService.class.getClassLoader();

        // Sử dụng ClassLoader để tìm URL của file trong thư mục resources
        URL resource = classLoader.getResource("templates/pbn.html");
        String htmlTemplatePath = resource.getPath(); // Đường dẫn tới file template HTML
        Map<String, String> data = new HashMap<>();
        // Thêm dữ liệu vào map, ví dụ:
        data.put("company_name", "Tên công ty");
        data.put("product", "Sản phẩm");
        data.put("description", "Mô tả");
        data.put("location", "Địa chỉ");
        data.put("contact_email", "Email");
        data.put("contact_phone", "Số điện thoại");

        try {
            String base64Pdf = generatePdfBase64(htmlTemplatePath, data);
            return new ResStatus(base64Pdf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResStatus generatorPDF() {

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
// Gán giá trị "Thomas" cho biến name để lát nữa bind dữ liệu
        context.setVariable("name", "Thomas");
// Trả về chuỗi là html string sau khi thực hiện bind dữ liệu

        ClassLoader classLoader = TestService.class.getClassLoader();

        // Sử dụng ClassLoader để tìm URL của file trong thư mục resources
        URL resource = classLoader.getResource("templates/pbn.html");
        String htmlTemplatePath = resource.getPath();
        String htmlContent = templateEngine.process(htmlTemplatePath, context);
        byte[] pdfBytes = convertHtmlToPdf(htmlContent);
        return new ResStatus(Base64.getEncoder().encodeToString(pdfBytes));
    }

    public static String generatePdfBase64(String htmlTemplatePath, Map<String, String> data) throws Exception {
        // Đọc nội dung từ file HTML template
        String htmlContent = readFileToString(htmlTemplatePath);

        // Thay thế các biến dữ liệu trong template HTML với giá trị thực
        for (Map.Entry<String, String> entry : data.entrySet()) {
            htmlContent = htmlContent.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        byte[] pdfBytes = convertHtmlToPdf(htmlContent);

        return Base64.getEncoder().encodeToString(pdfBytes);
    }

    public static String readFileToString(String filePath) throws IOException {
        FileInputStream inputStream = new FileInputStream(new File(filePath));
        byte[] buffer = new byte[(int) new File(filePath).length()];
        inputStream.read(buffer);
        inputStream.close();
        return new String(buffer, StandardCharsets.UTF_8);
    }

    public static byte[] convertHtmlToPdf(String html){
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(html);
            renderer.layout();
            renderer.createPDF(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            return outputStream.toByteArray();
        }
    }


}
