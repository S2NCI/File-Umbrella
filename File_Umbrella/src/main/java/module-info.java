module UmbrellaPackage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires java.sql;
    requires java.desktop;
    requires mongo.java.driver;
    requires de.jensd.fx.glyphs.fontawesome;
    requires javafx.swing;
    requires java.prefs;

    opens Controllers to javafx.fxml;
    exports UmbrellaPackage;
}