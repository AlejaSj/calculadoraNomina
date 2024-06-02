module org.asanchez.calculadoracontable {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    exports org.asanchez.calculadoracontable;
    exports org.asanchez.calculadoracontable.controller;
    opens org.asanchez.calculadoracontable.controller to javafx.fxml;
}