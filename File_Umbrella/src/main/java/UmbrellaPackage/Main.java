/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package UmbrellaPackage;
import java.awt.AWTException;

/**
 *
 * @authors Team 19
 */
public class Main {
    public static void main(String args[]) throws AWTException { 
            TrayInterface TI = new TrayInterface();
            TI.addIcon();
            TI.displayNotification("folder");
            
    }
}
