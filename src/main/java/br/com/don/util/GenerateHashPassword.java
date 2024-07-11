package br.com.don.util;

import java.io.Serializable;
import java.util.Scanner;

import org.mindrot.jbcrypt.BCrypt;

public class GenerateHashPassword implements Serializable {

    private static final long serialVersionUID = 1L;


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("Insira a senha a ser criptografada: ");
        String plainPassword = input.nextLine();
        input.close();

        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(plainPassword, salt);

        System.out.println("Senha informada...: " + plainPassword);
        System.out.println("Senha em hash.....: " + hashedPassword);
        System.out.println("Salt..............: " + salt);
    }


    public String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        String hashedPassword = BCrypt.hashpw(password, salt);

        return hashedPassword;
    }
}
