package dev.webfx.demo.demofx;

import com.chrisnewland.demofx.DemoConfig;
import com.chrisnewland.demofx.DemoFX;
import com.chrisnewland.demofx.effect.IEffect;
import com.chrisnewland.demofx.effect.addon.FadeOutAddOnEffect;
import com.chrisnewland.demofx.effect.addon.RotateAddOnEffect;
import com.chrisnewland.demofx.effect.addon.VolumeAddOnEffect;
import com.chrisnewland.demofx.effect.effectfactory.IEffectFactory;
import com.chrisnewland.demofx.effect.fake3d.SnowfieldSprite;
import com.chrisnewland.demofx.effect.fake3d.StarfieldSprite;
import com.chrisnewland.demofx.effect.fractal.FractalRings;
import com.chrisnewland.demofx.effect.fractal.Mandelbrot;
import com.chrisnewland.demofx.effect.fractal.Sierpinski;
import com.chrisnewland.demofx.effect.pixel.Twister;
import com.chrisnewland.demofx.effect.shape.*;
import com.chrisnewland.demofx.effect.spectral.Equaliser;
import com.chrisnewland.demofx.effect.sprite.*;
import com.chrisnewland.demofx.effect.text.*;
import com.chrisnewland.demofx.util.ImageUtil;
import dev.webfx.extras.imagestore.ImageStore;
import dev.webfx.platform.resource.Resource;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class DemoFXApplication extends Application {
    private final StackPane root = new StackPane();
    private final Scene scene = new Scene(root, 800, 600);
    private DemoFX introDemo, actualDemo;
    private boolean started;
    private final Image quaver =  loadDemoImage("quaver.png");
    private final Image quaver2 =  loadDemoImage("quaver2.png");
    private final Image tiger = ImageUtil.loadImageFromResources("tiger.jpeg");
    private Image purpleQuaver;
    long t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12, t13, t14, t15, t16, tend = 264000;

    @Override
    public void start(Stage stage) {
        stage.setTitle("DemoFX");
        stage.setScene(scene);
        stage.show();
        runIntroDemo();
        GraphicsContext loadingContext = new Canvas().getGraphicsContext2D();
        loadingContext.drawImage(quaver, 0, 0);
        loadingContext.drawImage(quaver2, 0, 0);
        loadingContext.drawImage(tiger, 0, 0);
    }

    private void runIntroDemo() {
        introDemo = newIntroDemo();
        root.getChildren().setAll(introDemo.getPane());
        introDemo.runDemo();
        root.setOnMousePressed(e -> { // Using setOnMousePressed() because sound doesn't start on iPad if using setOnMouseClicked()
            if (!started)
                introDemo.stopDemo();
            else {
                actualDemo.stopDemo();
                actualDemo = newActualDemo();
            }
            root.getChildren().setAll(actualDemo.getPane());
            actualDemo.runDemo();
            started = true;
        });
        actualDemo = newActualDemo();
    }

    private DemoConfig newDemoConfig(String audioResource) {
        DemoConfig demoConfig = new DemoConfig(scene.getWidth(), scene.getHeight());
        demoConfig.setAudioFilename(Resource.toUrl(audioResource, DemoFXApplication.class));
        demoConfig.setAudioVolume(0.8); // To prevent saturation during rocky music
        return demoConfig;
    }

    private DemoFX newIntroDemo() {
        return new DemoFX(newDemoConfig(null), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                new WordSearch(demoConfig, "Animation using DemoFX\n\nA JavaFX Canvas library\n\nby Chris Newland"),
                new TextWaveSprite(demoConfig, new String[] {"Click to play"}, demoConfig.getHeight() - 200, 1, 5, true)
                ));
    }

    private DemoFX newActualDemo() {
        return new DemoFX(newDemoConfig("DemoFX3.mp3"), (IEffectFactory) demoConfig -> dev.webfx.platform.util.collection.Collections.listOf(
                // Starting sequence: Star field
                scheduleEffect(new StarfieldSprite(demoConfig), 0, t1 = 15820),

                // Fractal sequence:
                // 1) Fractal rings
                scheduleEffect(new RotateAddOnEffect(new FractalRings(demoConfig), 23920, -1, t2 = 32000, 1), t1, t3 = 39900),
                // 3) Mandelbrot (declared before 2) so it is displayed behind Sierpinski)
                scheduleEffect(new Mandelbrot(demoConfig), t3, t6 = 80000),
                // 2) Sierpinski
                scheduleEffect(new Sierpinski(demoConfig), t2, t4 = 47850),
                // 4) Chord on top of Mandelbrot (still running)
                scheduleEffect(new Chord(demoConfig, Color.ORANGE), t4, t5 = 64000),
                // Text wave between 4) and 5)
                scheduleEffect(new TextWaveSprite(demoConfig, new String[] {"Realtime Mandelbrot computation"}, demoConfig.getHeight() - 200, 0.8, 10), t4 + 2000, t5),
                // 5) Concentric colored quavers on top of Mandelbrot (still running)
                scheduleEffect(new Concentric(demoConfig, 20, createTintedQuaver(Color.web("#00ACEB")), createTintedQuaver(Color.web("#00A656")), createTintedQuaver(Color.web("#FCE400")), createTintedQuaver(Color.web("#F36126")), createTintedQuaver(Color.web("#CE0166")), createTintedQuaver(Color.web("#91248D")))
                        // Pulse times matching the music:
                        .setPulseTimes(64000, 64257, 64500, 64758, 65268, 66000, 66497, 66753, 67258, 68011, 68261, 68754, 69258, 69751, 70251, 70754, 71246, 72005, 72259, 72756, 73254, 73754, 74018, 76001, 76254, 76756, 77256, 77751, 78503, 78754, 79255, 80000, 80256, 80755, 81256, 81650), t5, t6),

                // Audio spectrum sequence:
                // 1) Equaliser with falling quavers on top of a glow board
                scheduleEffect(new Glowboard(demoConfig, 32), t6, t7 = 96000),
                scheduleEffect(new FadeOutAddOnEffect(new Falling(demoConfig, purpleQuaver = createTintedQuaver(Color.web("#BD4AEA")), quaver2), 1000), t6, 88000 + 1000),
                scheduleEffect(new Equaliser(demoConfig), t6, 88000),
                // 2) Sine lines with volume effect, on top of a sea of quavers, on top of the same glow board (still running)
                scheduleEffect(new Sea(demoConfig, purpleQuaver), 88000, t7),
                scheduleEffect(new VolumeAddOnEffect(new SineLines(demoConfig), 10.0, 23, 14, 22, 15, 17, 5, 21, 12, 13), 88000, t7),
                scheduleEffect(new SineLines(demoConfig), 88000, t7),

                // Rocky music 1 sequence:
                // 2) Moire2 effect (declared before 1) to be behind the still running JavaFX tiles)
                scheduleEffect(new Moire2(demoConfig), 112000, t8 = 128000),
                // 1) JavaFX tiles
                scheduleEffect(new Tiles(demoConfig), t7, t8),
                // 3) Moire effect on top of a red checkerboard
                scheduleEffect(new Checkerboard(demoConfig, Color.RED), t8, t9 = 136000),
                scheduleEffect(new Moire(demoConfig), t8, t9),
                // 4) Rings effect on top of an orange checkerboard
                scheduleEffect(new Checkerboard(demoConfig, Color.ORANGE), t9, t10 = 144000),
                scheduleEffect(new Rings(demoConfig), t9, t10),
                // 5) Mask stack effect (with tiger image), on top of a blue checkerboard
                scheduleEffect(new Checkerboard(demoConfig), t10, t11 = 152000),
                scheduleEffect(new MaskStack(demoConfig, tiger), t10, t11 = 152000),
                // 6) Twister effect, on top of a cyan blue checkerboard
                scheduleEffect(new Checkerboard(demoConfig, Color.web("#00C2B6")), t10 = 152000, t11 = 159874),
                scheduleEffect(new Twister(demoConfig, Color.web("#FCC738"), Color.web("#E6933F"), Color.web("#FC6038"), Color.web("#F2386E")), t10 = 152000, t11 = 159874),

                // Rocky music 2 sequence:
                // 1) Spin effect (Java logo) with a fading out effect at the end
                scheduleEffect(new FadeOutAddOnEffect(new Spin(demoConfig), 2000), t12 = 159874, (t13 = 175000) + 2000),
                // 2) Text ring (Entirely in Java and JavaFX), on top of mandalas (starting later), on top of a Moire2 effect
                scheduleEffect(new Moire2(demoConfig), t13, t14 = 192000),
                scheduleEffect(new Mandala(demoConfig, 32), 179750, t14),
                scheduleEffect(new TextRing(demoConfig, new TextRing.RingData[] {
                        new TextRing.RingData("Entirely    in    Java    and    JavaFX", 250, 0.13, -1, 3, 2)}), t13, t14),

                // Ending sequence:
                // 1) Amazing work (word search effect)
                scheduleEffect(new FadeOutAddOnEffect(new WordSearch(demoConfig, "Amazing work\n\nThank you Chris Newland\n\nalias @chriswhocodes"), 2500), t14, (t15 = t14 + 18000) + 2000),
                // 3) Credits (declared before 2) so it's behind the snow)
                scheduleEffect(new FadeOutAddOnEffect(new Credits(demoConfig, Color.web("#D0D0D0"), (t16 = tend - 4 * (75 + 100 + 75)) - t15 - 1500), 2500), t15, t16),
                // 2) Snow field
                scheduleEffect(new FadeOutAddOnEffect(new SnowfieldSprite(demoConfig), 2500), t14 + 8000, t16),
                // 4) Thank you for watching (flash text)
                scheduleEffect(new TextFlash(demoConfig, "Thank you for watching", false, 75, 100, 75), t16, tend + 1000) // Waiting 1s more before returning to intro
        )).setOnCompleted(this::runIntroDemo);
    }

    private IEffect scheduleEffect(IEffect effect, long start, long stop) {
        if (start > 0)
            effect.setStartOffsetMillis(start);
        if (stop > 0)
            effect.setStopOffsetMillis(stop);
        return effect;
    }

    private Image loadDemoImage(String name) {
        return ImageStore.getOrCreateImage("dev/webfx/demo/demofx/" + name);
    }

    private Image createTintedQuaver(Color color) {
        WritableImage image = new WritableImage(24, 45);
        if (quaver.getProgress() >= 1)
            tintQuaver(quaver, image, color);
        else
            quaver.progressProperty().addListener((observableValue, number, t1) -> tintQuaver(quaver, image, color));
        return image;
    }

    private void tintQuaver(Image quaver, WritableImage image, Color color) {
        if (quaver.getProgress() == 1)
            ImageUtil.tintImage(quaver, color.getHue(), image);
    }
}