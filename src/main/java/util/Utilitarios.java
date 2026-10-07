package util;

import java.util.Scanner;

public final class Utilitarios {
    private static final Scanner SC = new Scanner(System.in);
    private Utilitarios() {}

    public static int inteiroValido() {
        while (!SC.hasNextInt()) {
            SC.next();
            System.out.print("Digite um número válido: ");
        }
        return SC.nextInt();
    }

    public static String stringValida() {
        while (!SC.hasNext()) SC.nextLine();
        return SC.next();
    }
}
