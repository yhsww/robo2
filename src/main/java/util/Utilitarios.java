package util;

import java.util.Scanner;

public final class Utilitarios {
    private static final Scanner sc = new Scanner(System.in);
    private Utilitarios() {}

    public static int inteiroValido() {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Digite um número válido: ");
        }
        return sc.nextInt();
    }

    public static String stringValida() {
        while (!sc.hasNext()) sc.nextLine();
        return sc.next();
    }
}
