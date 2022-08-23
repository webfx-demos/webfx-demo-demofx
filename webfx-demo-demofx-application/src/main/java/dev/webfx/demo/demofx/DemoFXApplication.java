package dev.webfx.demo.demofx;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.fractal.FractalRings;
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
        demoFX = new DemoFX(config, (IEffectFactory) conf -> dev.webfx.platform.util.collection.Collections.listOf(
                new WordSearch(conf, "Animation using DemoFX\n\nA JavaFX Canvas library\n\nby Chris Newland"),
                new TextWaveSprite(conf, new String[] {"Click to start"}, conf.getHeight() - 200, 1, 5, true)
        ));

        root.getChildren().setAll(demoFX.getPane());
        demoFX.runDemo();
        root.setOnMouseClicked(e -> {
            if (!started) {
                demoFX.stopDemo();
                config.setAudioFilename(Resource.toUrl("DemoFX3.mp3", DemoFXApplication.class));
                demoFX = new DemoFX(config, (IEffectFactory) conf -> dev.webfx.platform.util.collection.Collections.listOf(
                        new StarfieldSprite(conf),
                        schedule(new FractalRings(conf), 16_000, -1)
                ));
                root.getChildren().setAll(demoFX.getPane());
                demoFX.runDemo();
                started = true;
            }
        });
    }

    private IEffect schedule(IEffect effect, long start, long stop) {
        if (start > 0)
            effect.setStartOffsetMillis(start);
        if (stop > 0)
            effect.setStopOffsetMillis(stop);
        return effect;
    }

    private static <R extends Region> R setBackgroundColor(Color color, R region) {
        region.setBackground(new Background(new BackgroundFill(color, null, null)));
        return region;
    }

}