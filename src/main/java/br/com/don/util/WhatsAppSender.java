package br.com.don.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;

public class WhatsAppSender {
    private final String initialStartup = "wa_web_initial_startup";
    private final String qrCodeXPath = "/html/body/div[2]/div/div/div[2]/div[3]/div[1]/div/div/div[2]/div/canvas";
    private final String sendButtonXPath = "/html/body/div[1]/div/div/div[2]/div[4]/div/footer/div[1]/div/span[2]/div/div[2]/div[2]/button";

    /* private ChromeOptions options = new ChromeOptions().addArguments("--headless");
    private WebDriver browser = new ChromeDriver(options); */

    private WebDriver browser = new ChromeDriver();

    private WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(120));

    private Path qrCodeImagePath;

    public Boolean setup() {

        try {
            System.out.println("Accessing WhatsAppWeb...");
            browser.get("https://web.whatsapp.com/");

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(initialStartup)));

            System.out.println("Loading QR Code...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(qrCodeXPath)));

            captureQRCode(browser);
            System.out.println("Scan the QR code to continue...");

           /*  PrimeFaces.current().ajax().update("@root:@id(qrcodezap)");
            PrimeFaces.current().ajax().update("@root:@id(zapDialog)");
			PrimeFaces.current().executeScript("PF('zapDialog').show()"); */

            System.out.println("Loading...");
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pane-side")));

            System.out.println("Success...");
            /* Thread.sleep(2000); */

            Files.delete(qrCodeImagePath);

            /* PrimeFaces.current().executeScript("PF('zapDialog').hide()"); */

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Login efetuado com sucesso.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            return true;

        } catch (Exception e) {
            System.out.println("[!] Error: " + e);

            browser.quit();

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Não foi possível efetuar o login.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

            return false;
        }
    }


    public void captureQRCode(WebDriver browser) {
        WebElement qrCodeElement = browser.findElement(By.xpath("//*[@id=\"app\"]/div/div[2]/div[3]/div[1]/div/div"));

        File screenshot = ((TakesScreenshot) qrCodeElement).getScreenshotAs(OutputType.FILE);

        String serverFolderPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/resources/qrcodes/");
        File serverFolder = new File(serverFolderPath);

        if (!serverFolder.exists()) {
            serverFolder.mkdirs();
        }

        Path qrCodeImagePath = Paths.get(serverFolderPath, "qrcode.png");

        try {
            Files.copy(screenshot.toPath(), qrCodeImagePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("QR Code saved to: " + qrCodeImagePath);

        } catch (IOException e) {
            System.out.println("Failed to save QR Code: " + e.getMessage());
        }
    }


    public void sendText(String phone, String message) {
        try {
            System.out.println("Sending messages...");

            browser.get("https://web.whatsapp.com/send?phone=" + phone + "&text=" + message.replace("\n", "%0A"));

            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(sendButtonXPath)));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(sendButtonXPath)));

            WebElement sendButton = browser.findElement(By.xpath(sendButtonXPath));
            sendButton.click();

            Thread.sleep(3000);

            System.out.println("[+] Message sent to phone: " + phone);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Mensagem enviada com sucesso.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } catch (Exception e) {
            System.out.println("[!] Error: " + e);
            browser.quit();

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Não foi possível enviar a mensagem.");
            FacesContext.getCurrentInstance().addMessage(null, msg);

        } finally {
            browser.quit();
        }
    }


    public String getQrCode() {
        return this.qrCodeImagePath.toString();
    }


    public void cleanup() {
        browser.quit();
    }


    public static void main(String[] args) {
        WhatsAppSender was = new WhatsAppSender();

        if(was.setup()) {
            System.out.println("bingo");
        }
    }
}
