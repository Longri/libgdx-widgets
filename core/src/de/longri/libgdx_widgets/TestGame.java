package de.longri.libgdx_widgets;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.PixmapPacker;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class TestGame extends ApplicationAdapter {

    float fpsInfoSize = 5;

    SpriteBatch batch;
    Texture img;
    Stage stage;
    TextButton textButton;
    static public Skin SKIN;
    private Sprite fpsInfoSprite;
    int fpsInfoPos = 0;

    public TestGame() {

    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        stage = new Stage(new ScreenViewport(new OrthographicCamera()), new SpriteBatch());

        final BitmapFont defaultFont = getDefaultFont();

        SKIN = new Skin(Gdx.files.local("uiskin.json")) {
            @Override
            public void load(FileHandle skinFile) {
                //Add BitmapFont to resources first!
                this.add("default-font", defaultFont, BitmapFont.class);

                super.load(skinFile);
            }
        };
        textButton = new TextButton("Test", SKIN.get("default", TextButton.TextButtonStyle.class));
        textButton.pack();
        stage.getRoot().addActor(textButton);

        Gdx.input.setInputProcessor(stage);
    }

    private BitmapFont getDefaultFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.local("DroidSans.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 12;
        parameter.characters = getCyrilCharSet();
        parameter.genMipMaps = true;
        parameter.minFilter = Texture.TextureFilter.Nearest;
        parameter.renderCount = 3;
        parameter.packer = new PixmapPacker(1024, 1024, Pixmap.Format.RGBA8888, 5
                , false, new PixmapPacker.SkylineStrategy());


        FreeTypeFontGenerator.FreeTypeBitmapFontData data = generator.generateData(parameter);
        return generator.generateFont(parameter, data);
    }

    static String getCyrilCharSet() {
        int CharSize = 0x04ff - 0x0400;
        char[] cyril = new char[CharSize + 1];
        for (int i = 0x0400; i < 0x04ff + 1; i++) {
            cyril[i - 0x0400] = (char) i;
        }
        return FreeTypeFontGenerator.DEFAULT_CHARS + String.copyValueOf(cyril) + "—–" + "ŐőŰű√€†„”“’‘☺čěřšťůž…";
    }

    @Override
    public void resize(int width, int height) {
        this.stage.getRoot().setBounds(0, 0, width, height);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.176f, 0.176f, 0.176f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
        batch.begin();
        if (fpsInfoSprite != null) {
            Color lastColor = batch.getColor();
            batch.setColor(1.0f, 0.0f, 0.0f, 1.0f);
            batch.draw(fpsInfoSprite, fpsInfoPos, fpsInfoSize, fpsInfoSize, fpsInfoSize);
            batch.setColor(lastColor);
        } else {
            int w = (int) fpsInfoSize;
            int h = (int) fpsInfoSize;
            Pixmap pix = new Pixmap(w, h, Pixmap.Format.RGB565);
            pix.setColor(Color.WHITE);
            pix.fillRectangle(0, 0, w, h);
            Texture tex = new Texture(pix);
            fpsInfoSprite = new Sprite(tex);
            fpsInfoSprite.setColor(1.0f, 0.0f, 0.0f, 1.0f);
            fpsInfoSprite.setSize(fpsInfoSize, fpsInfoSize);

        }

        fpsInfoPos += fpsInfoSize;
        if (fpsInfoPos > 60 * fpsInfoSize) {
            fpsInfoPos = 0;
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}
