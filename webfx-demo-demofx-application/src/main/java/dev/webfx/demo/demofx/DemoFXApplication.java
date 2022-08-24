package dev.webfx.demo.demofx;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.addon.RotateAddOnEffect;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fake3d.SnowfieldSprite;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.fractal.FractalRings;
import com.chrisnewland.demofx.effect.fractal.Mandelbrot;
import com.chrisnewland.demofx.effect.fractal.Sierpinski;
import com.chrisnewland.demofx.effect.shape.*;
import com.chrisnewland.demofx.effect.sprite.Spin;
import com.chrisnewland.demofx.effect.sprite.Tiles;
import com.chrisnewland.demofx.effect.text.TextRing;
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
    private final Scene scene = new Scene(root, 1600, 1200);
    private DemoFX demoFX;
    private boolean started;
    long t1, t2, t3, t4, t5, t6, t7, t8, t9;

    @Override
    public void start(Stage stage) {
        stage.setTitle("DemoFX");
        stage.setScene(scene);
        stage.show();
        demoFX = new DemoFX(newDemoConfig(null), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                new WordSearch(demoConfig, "Animation using DemoFX\n\nA JavaFX Canvas library\n\nby Chris Newland"),
                new TextWaveSprite(demoConfig, new String[] {"Click to play"}, demoConfig.getHeight() - 200, 1, 5, true)
        ));

        root.getChildren().setAll(demoFX.getPane());
        demoFX.runDemo();
        root.setOnMouseClicked(e -> {
            if (!started) {
                demoFX.stopDemo();
                demoFX = new DemoFX(newDemoConfig("DemoFX3.mp3"), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                        scheduleEffect(new StarfieldSprite(demoConfig), 0, t1 = 16200),
                        scheduleEffect(new RotateAddOnEffect(new FractalRings(demoConfig), 24300, 1, t3 = 32000, -1), t1, t2 = 40000),
                        scheduleEffect(new Sierpinski(demoConfig), t3, t2),
                        scheduleEffect(new Mandelbrot(demoConfig), t2, t4 = 64000),
                        scheduleEffect(new TextWaveSprite(demoConfig, new String[] {"Realtime computation"}, demoConfig.getHeight() - 200, 0.8, 10), t2 + 2000, t5 = 64000),
                        scheduleEffect(new Checkerboard(demoConfig), t4, t5 = 70000),
                        scheduleEffect(new Chord(demoConfig, Color.ORANGE), 48300, t5),
                        scheduleEffect(new Glowboard(demoConfig), t5, t6 = 96000),
                        scheduleEffect(new SineLines(demoConfig), t5, t6),
                        scheduleEffect(new Tiles(demoConfig), t6, t7 = 160200),
                        scheduleEffect(new Spin(demoConfig), t7, t8 = 192000),
                        scheduleEffect(new TextRing(demoConfig, new TextRing.RingData[] {
                                new TextRing.RingData("Coded    in    Java    and    JavaFX", 300, 0.15, 1, 3, 2)}), t7 + 5000, t8),
                        scheduleEffect(new WordSearch(demoConfig, "Amazing work\n\nThank you Chris Newland\n\nalias @chriswhocodes"), t8, t9 = 265000),
                        scheduleEffect(new SnowfieldSprite(demoConfig), t8 + 10000, t9)
                        ));
                root.getChildren().setAll(demoFX.getPane());
                demoFX.runDemo();
                //started = true;
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