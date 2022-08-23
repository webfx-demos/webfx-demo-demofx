package dev.webfx.demo.demofx;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.addon.AddOnFadeInOutEffect;
import com.chrisnewland.demofx.effect.addon.AddOnFadeOutEffect;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.fractal.FractalRings;
import com.chrisnewland.demofx.effect.fractal.Mandelbrot;
import com.chrisnewland.demofx.effect.fractal.Sierpinski;
import com.chrisnewland.demofx.effect.shape.Chord;
import com.chrisnewland.demofx.effect.text.TextWaveSprite;
import com.chrisnewland.demofx.effect.text.WordSearch;
import dev.webfx.platform.resource.Resource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DemoFXApplication extends Application {

    private final StackPane root = new StackPane();
    private final Scene scene = new Scene(root, 800, 600);
    private boolean started;

    @Override
    public void start(Stage stage) {
        stage.setTitle("DemoFX");
        stage.setScene(scene);
        stage.show();
        DemoFX playDemo = new DemoFX(newDemoConfig(null), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                new WordSearch(demoConfig, "Animation using DemoFX\n\nA JavaFX Canvas library\n\nby Chris Newland"),
                new TextWaveSprite(demoConfig, new String[] {"Click to play"}, demoConfig.getHeight() - 200, 1, 5, true)
        ));

        root.getChildren().setAll(playDemo.getPane());
        playDemo.runDemo();
        root.setOnMouseClicked(e -> {
            if (!started) {
                playDemo.stopDemo();
                DemoFX demo = new DemoFX(newDemoConfig("DemoFX3.mp3"), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                        scheduleEffect(new AddOnFadeOutEffect(new StarfieldSprite(demoConfig)), 0, 25000),
                        scheduleEffect(new AddOnFadeInOutEffect(new FractalRings(demoConfig)), 16000, 45000),
                        scheduleEffect(new AddOnFadeInOutEffect(new Mandelbrot(demoConfig)), 40000, -1),
                        scheduleEffect(new AddOnFadeOutEffect(new Sierpinski(demoConfig)), 32500, 45000),
                        scheduleEffect(new Chord(demoConfig, Color.ORANGE), 40500, -1)
                        ));
                root.getChildren().setAll(demo.getPane());
                demo.runDemo();
                started = true;
            }
        });
    }

    private DemoConfig newDemoConfig(String audioResource) {
        DemoConfig demoConfig = new DemoConfig(scene.getWidth(), scene.getHeight());
        demoConfig.setAudioFilename(Resource.toUrl(audioResource, DemoFXApplication.class));
        return demoConfig;
    }

    private IEffect scheduleEffect(IEffect effect, long start, long stop) {
        if (start > 0)
            effect.setStartOffsetMillis(start);
        if (stop > 0)
            effect.setStopOffsetMillis(stop);
        return effect;
    }
}