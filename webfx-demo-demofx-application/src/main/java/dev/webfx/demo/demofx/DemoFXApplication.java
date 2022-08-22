package dev.webfx.demo.demofx;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.text.TextWaveSprite;
import com.chrisnewland.demofx.effect.text.WordSearch;
import dev.webfx.platform.resource.Resource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.function.Function;

public class DemoFXApplication extends Application {

    private final StackPane root = new StackPane();
    private final Scene scene = new Scene(root, 800, 600);

    private DemoFX demoFX;
    private boolean started;

    @Override
    public void start(Stage stage) {
        stage.setTitle("DemoFX");
        stage.setScene(scene);
        stage.show();
        DemoConfig config = new DemoConfig(scene.getWidth(), scene.getHeight());
        demoFX = new DemoFX(config, (IEffectFactory) config1 -> dev.webfx.platform.util.collection.Collections.listOf(
                new WordSearch(config1, "Animation using DemoFX\n\nA JavaFX Canvas library\n\nby Chris Newland"),
                new TextWaveSprite(config1, new String[] {"Click to start"}, config1.getHeight() - 200, 1, 5, true)
        ));

        root.getChildren().setAll(demoFX.getPane());
        demoFX.runDemo();
        root.setOnMouseClicked(e -> {
            if (!started) {
                demoFX.stopDemo();
                config.setAudioFilename(Resource.toUrl("DemoFX3.mp3", DemoFXApplication.class));
                demoFX = new DemoFX(config, (Function<DemoConfig, IEffect>) StarfieldSprite::new);
                root.getChildren().setAll(demoFX.getPane());
                demoFX.runDemo();
                started = true;
            }
        });
    }

    private static <R extends Region> R setBackgroundColor(Color color, R region) {
        region.setBackground(new Background(new BackgroundFill(color, null, null)));
        return region;
    }

}