package com.niopullus.NioLib.scene.mapeditorscene;

import com.niopullus.NioLib.Draw;
import com.niopullus.NioLib.DrawElement;
import com.niopullus.NioLib.Main;
import com.niopullus.NioLib.scene.Scene;
import com.niopullus.NioLib.scene.dynscene.Node;
import com.niopullus.NioLib.scene.dynscene.World;
import com.niopullus.NioLib.scene.dynscene.tile.*;
import com.niopullus.NioLib.utilities.Utilities;
import com.niopullus.app.Config;
import com.niopullus.app.InitScene;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;

/**
 * Created by Owen on 4/7/2016.
 */
public class MapEditorScene extends Scene {

    private Tilemap fgMap;
    private Tilemap bgMap;
    private Node world;
    private Point selectedTile;
    private Point selection;
    private int pages;
    private int page;
    private int choice;
    private TileReference tile;
    private Point commitPoint;
    private boolean whold;
    private boolean ahold;
    private boolean shold;
    private boolean dhold;
    private Point fillingAnchor;
    private int cols;
    private int mm;
    private String fileName;
    private int loadMode;

    public MapEditorScene() {
        super();
        this.fgMap = new Tilemap(Config.TILESIZE, Config.TILEMAPRAD / Config.TILEREGIONSIZE, Config.TILEMAPRAD / Config.TILEREGIONSIZE, Config.TILEREGIONSIZE);
        this.fgMap.setZ(10);
        this.bgMap = new Tilemap(Config.TILESIZE, Config.TILEMAPRAD / Config.TILEREGIONSIZE, Config.TILEMAPRAD / Config.TILEREGIONSIZE, Config.TILEREGIONSIZE);
        this.bgMap.setZ(5);
        this.world = new Node();
        this.bgMap.setWorld(this.world);
        this.fgMap.setWorld(this.world);
        this.selectedTile = new Point();
        this.selection = new Point();
        this.cols = Main.Height() / (Main.Width() / 12);
        this.pages = (int) Math.ceil((double) TileReference.getTileQuant() / ((cols - 2) * 2));
        this.commitPoint = new Point();
        this.page = 0;
        this.mm = 0;
        this.loadMode = 0;
    }

    public MapEditorScene(String worldFileName) {
        this();
        World world = World.loadWorld(worldFileName);
        this.bgMap = world.getBgTilemap();
        this.fgMap = world.getFgTilemap();
        this.bgMap.setWorld(this.world);
        this.fgMap.setWorld(this.world);
        this.fileName = worldFileName;
    }

    @Override
    public void draw() {
        Draw.rect(0, 0, Main.Width(), Main.Height(), 0, new Color(172, 172, 172));
        Draw.rect(Main.Width() * 5 / 6 - 50, 0, Main.Width() / 6, Main.Height(), 100, new Color(0, 0, 0, 127));
        Draw.rect(this.commitPoint.x * Main.Width() / 12 + Main.Width() * 5 / 6 - 50, this.commitPoint.y * Main.Width() / 12, Main.Width() / 12, Main.Width() / 12, 480, new Color(255, 0, 0, 100));
        Draw.text(Main.Width() * 21 / 24 - 50 - Main.Width() / 2, (Main.Height() / 2) - (Main.Width() * ((cols - 2) * 2 + 1) / 24), 1000, "Prev Page", new Font("Bold", Font.BOLD, 30), Color.BLACK, DrawElement.MODE_TEXTCENTERED);
        Draw.text(Main.Width() * 23 / 24 - 50 - Main.Width() / 2, (Main.Height() / 2) - (Main.Width() * ((cols - 2) * 2 + 1) / 24), 1000, "Next Page", new Font("Bold", Font.BOLD, 30), Color.BLACK, DrawElement.MODE_TEXTCENTERED);
        Draw.text(Main.Width() * 21 / 24 - 50 - Main.Width() / 2, (Main.Height() / 2) - (Main.Width() * ((cols - 1) * 2 + 1) / 24), 1000, "FG Map", new Font("Bold", Font.BOLD, 30), Color.BLACK, DrawElement.MODE_TEXTCENTERED);
        Draw.text(Main.Width() * 23 / 24 - 50 - Main.Width() / 2, (Main.Height() / 2) - (Main.Width() * ((cols - 1) * 2 + 1) / 24), 1000, "BG Map", new Font("Bold", Font.BOLD, 30), Color.BLACK, DrawElement.MODE_TEXTCENTERED);
        if (this.mm == 0) {
            Draw.rect(Main.Width() * 5 / 6 - 50, (cols - 1) * Main.Width() / 12, Main.Width() / 12, Main.Width() / 12, 500, new Color(255, 196, 46, 100));
        } else if (this.mm == 1) {
            Draw.rect(Main.Width() / 12 + Main.Width() * 5 / 6 - 50, (cols - 1) * Main.Width() / 12, Main.Width() / 12, Main.Width() / 12, 500, new Color(255, 196, 46, 100));
        }
        if (this.choice == 1) {
            if (this.fillingAnchor == null) {
                Draw.rect(this.selectedTile.x * this.fgMap.getTileSize() + this.world.getX(), Main.Height() - ((this.selectedTile.y + 1) * this.fgMap.getTileSize()) - this.world.getY(), this.fgMap.getTileSize(), this.fgMap.getTileSize(), 200, new Color(52, 240, 255, 100));
            } else {
                int x1 = this.fillingAnchor.x, x2 = this.fillingAnchor.x;
                int y1 = this.fillingAnchor.y, y2 = this.fillingAnchor.y;
                if (this.fillingAnchor.x > this.selectedTile.x) {
                    x1 = this.selectedTile.x;
                    x2 = this.fillingAnchor.x;
                } else if (this.fillingAnchor.x < this.selectedTile.x) {
                    x1 = this.fillingAnchor.x;
                    x2 = this.selectedTile.x;
                }
                if (this.fillingAnchor.y > this.selectedTile.y) {
                    y1 = this.selectedTile.y;
                    y2 = this.fillingAnchor.y;
                } else if (this.fillingAnchor.y < this.selectedTile.y) {
                    y1 = this.fillingAnchor.y;
                    y2 = this.selectedTile.y;
                }
                Draw.rect(x1 * this.fgMap.getTileSize() + this.world.getX(), Main.Height() - ((y1) * this.fgMap.getTileSize()) - this.world.getY() - (this.fgMap.getTileSize() * (y2 - y1 + 1)), this.fgMap.getTileSize() * (x2 - x1 + 1), this.fgMap.getTileSize() * (y2 - y1 + 1), 200, new Color(52, 240, 255, 100));
            }
        } else if (choice == 2) {
            Draw.rect(this.selection.x * Main.Width() / 12 + Main.Width() * 5 / 6 - 50, this.selection.y * Main.Width() / 12, Main.Width() / 12, Main.Width() / 12, 500, new Color(74, 255, 64, 100));
        }
        int id = this.page * 16;
        for (int y = 1; y <= this.cols - 2; y++) {
            for  (int x = 1; x <= 2; x++) {
                if (id <= TileReference.getTileQuant()) {
                    TileReference tile = TileReference.getRef(id);
                    if (tile != null) {
                        if (!(tile instanceof MultiTileReference)) {
                            Draw.image((int) ((double) Main.Width() * 81 / 96 + ((double) (x - 1) / 12) * Main.Width()) - 50, (int) ((double) Main.Width() / 96 + ((double) (y - 1) / 12) * Main.Width()), 300, Main.Width() / 16, Main.Width() / 16, tile.getImage());
                        } else {
                            MultiTileReference mtr = (MultiTileReference) tile;
                            int size = Utilities.greater(mtr.getWidth(), mtr.getHeight());
                            int smallTileSize = (Main.Width() / 16) / size;
                            int xStart = (size - mtr.getWidth()) * smallTileSize / 2;
                            int yStart = (size - mtr.getHeight()) * smallTileSize / 2;
                            int part = 0;
                            for (int i = 0; i < mtr.getWidth(); i++) {
                                for (int j = 0; j < mtr.getHeight(); j++) {
                                    Draw.image((int) ((double) Main.Width() * 81 / 96 + ((double) (x - 1) / 12) * Main.Width()) - 50 + xStart + (i * smallTileSize), (int) ((double) Main.Width() / 96 + ((double) (y - 1) / 12) * Main.Width()) + yStart + (j * smallTileSize), 300, smallTileSize, smallTileSize, mtr.getImage(0, (mtr.getWidth() * mtr.getHeight()) - part - 1));
                                    part++;
                                }
                            }
                        }
                    }
                    id++;
                }
            }
        }
        Draw.rect(this.world.getX() - 50, Main.Height() - (this.world.getY() - 50), 100, 100, 500, Color.RED);
        this.bgMap.draw();
        this.fgMap.draw();
        if (this.getSubscene() != null) {
            this.getSubscene().draw();
        }
    }

    @Override
    public void tick() {
        if (this.ahold) {
            this.world.moveX(15);
            mouseMove();
        } else if (this.dhold) {
            this.world.moveX(-15);
            mouseMove();
        }
        if (this.whold) {
            this.world.moveY(-15);
            mouseMove();
        } else if (this.shold) {
            this.world.moveY(15);
            mouseMove();
        }
    }

    @Override
    public void keyPress(KeyEvent key) {
        int k = key.getKeyCode();
        switch (k) {
            case KeyEvent.VK_A: this.ahold = true; break;
            case KeyEvent.VK_S: this.shold = true; break;
            case KeyEvent.VK_W: this.whold = true; break;
            case KeyEvent.VK_D: this.dhold = true; break;
            case KeyEvent.VK_ESCAPE: this.addSubScene(new ExitMenu(this)); break;
            case KeyEvent.VK_O: this.addSubScene(new SaveMenu(this, this.fileName));
        }
    }

    @Override
    public void keyReleased(KeyEvent key) {
        int k = key.getKeyCode();
        switch (k) {
            case KeyEvent.VK_A: this.ahold = false; break;
            case KeyEvent.VK_S: this.shold = false; break;
            case KeyEvent.VK_W: this.whold = false; break;
            case KeyEvent.VK_D: this.dhold = false; break;
        }
    }

    public void saveMap(String name) {
        World world = new World();
        world.setName(name);
        world.setFgTilemap(this.fgMap);
        world.setBgTilemap(this.bgMap);
        World.saveWorld(world);
        presentScene(new InitScene());
    }

    public void exit() {
        presentScene(new InitScene());
    }

    public void mouseMove() {
        if (this.getSubscene() != null) {
            this.getSubscene().mouseMove();
        }
        if (this.getMousePos().x < Main.Width() * 5 / 6 - 50 || this.getMousePos().x > Main.Width() - 50) {
            this.choice = 1;
            this.selectedTile = new Point(Math.floorDiv(-this.world.getX() + this.getMousePos().x, this.fgMap.getTileSize()), Math.floorDiv((Main.Height() - (this.world.getY() + this.getMousePos().y)), this.fgMap.getTileSize()));
        } else {
            this.choice = 2;
            this.selection = new Point((this.getMousePos().x - (Main.Width() * 5 / 6 - 50)) / (Main.Width() / 12), this.getMousePos().y / (Main.Width() / 12));
        }
    }

    public void mousePress() {
        if (this.getMousePos().x < Main.Width() * 5 / 6 - 50 || this.getMousePos().x > Main.Width() - 50) {
            this.fillingAnchor = new Point(this.selectedTile);
        } else if (TileReference.getRef(this.selection.y * 2 + this.selection.x) != null || this.selection.y * 2 + this.selection.x == 0) {
            this.tile = TileReference.getRef(this.selection.y * 2 + this.selection.x);
            this.commitPoint = new Point(this.selection);
        } else if (this.selection.y >= this.cols - 2) {
            if (this.selection.x == 0 && this.selection.y == this.cols - 2) {
                if (this.page - 1 >= 0) {
                    this.page--;
                }
            } else if (this.selection.x == 1 && this.selection.y == this.cols - 2) {
                if (this.page + 1 < this.pages - 1) {
                    this.page++;
                }
            } else if (this.selection.x == 0 && this.selection.y == this.cols - 1) {
                this.mm = 0;
            } else if (this.selection.x == 1 && this.selection.y == this.cols - 1) {
                this.mm = 1;
            }
        }
    }

    public void mouseRelease() {
        if (this.fillingAnchor != null) {
            Tilemap map = null;
            if (this.mm == 0) {
                map = this.fgMap;
            } else if (this.mm == 1) {
                map = this.bgMap;
            }
            int x1 = this.fillingAnchor.x, x2 = this.fillingAnchor.x;
            int y1 = this.fillingAnchor.y, y2 = this.fillingAnchor.y;
            if (this.fillingAnchor.x > this.selectedTile.x) {
                x1 = this.selectedTile.x;
                x2 = this.fillingAnchor.x;
            } else if (this.fillingAnchor.x < this.selectedTile.x) {
                x1 = this.fillingAnchor.x;
                x2 = this.selectedTile.x;
            }
            if (this.fillingAnchor.y > this.selectedTile.y) {
                y1 = this.selectedTile.y;
                y2 = this.fillingAnchor.y;
            } else if (this.fillingAnchor.y < this.selectedTile.y) {
                y1 = this.fillingAnchor.y;
                y2 = this.selectedTile.y;
            }
            for (int i = x1; i <= x2; i++) {
                for (int j = y1; j <= y2; j++) {
                    if (this.tile != null) {
                        if (!(this.tile instanceof MultiTileReference)) {
                            map.setTile(this.tile.getSample().clone(), i, j);
                        } else {
                            MultiTileReference mtr = (MultiTileReference) this.tile;
                            map.setMultiTile((MultiTile) mtr.getSample().clone(), i, j);
                        }
                    } else {
                        map.setTile(null, i, j);
                    }
                }
            }
            this.fillingAnchor = null;
        }
    }

    //TODO:
    //ANIMATIONS THAT RUN ONCE

}
