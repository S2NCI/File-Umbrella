module UmbrellaPackage {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.desktop;
    requires mongo.java.driver;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.prefs;
    requires jsch;
    requires org.apache.commons.net;
    requires java.dotenv;
    requires com.jfoenix;
    requires org.apache.tika.core;
    requires org.apache.commons.io;

    opens Controllers to javafx.fxml;
    exports UmbrellaPackage;
}