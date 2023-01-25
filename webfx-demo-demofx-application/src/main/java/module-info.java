// File managed by WebFX (DO NOT EDIT MANUALLY)

module webfx.demo.demofx.application {

    // Direct dependencies modules
    requires java.base;
    requires javafx.graphics;
    requires javafx.media;
    requires webfx.kit.util.scene;
    requires webfx.lib.demofx;
    requires webfx.platform.os;
    requires webfx.platform.resource;
    requires webfx.platform.uischeduler;
    requires webfx.platform.util;

    // Exported packages
    exports dev.webfx.demo.demofx;

    // Resources packages
    opens dev.webfx.demo.demofx;

    // Provided services
    provides javafx.application.Application with dev.webfx.demo.demofx.DemoFXApplication;

}