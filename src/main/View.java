package main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class View extends JComponent
{
    Random rand = new Random();
    BufferedImage image, copy;
    public int resolution = 32;

    ColorWheel color;

    public View()
    {
        createNewImage();
    }

    public void createNewImage()
    {
        image = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        color = ColorWheel.values()[rand.nextInt(12)];
        int[] info = {-1, -1};
        boolean brokeStartLine = true, brokeEndLine = false;
        int maxStreak = 4;
        for(int i = 0; i < resolution; i++) {
            int streak = 0, nextStartingPoint = 0, nextEndingPoint = 0;
            if(info[0] > -1) {
                ArrayList<Integer> pos = new ArrayList<>();
                if (info[0] - 1 >= 0) pos.add(info[0] - 1);
                pos.add(info[0]);
                if(i > resolution/2) pos.add(info[0] + 1);
                nextStartingPoint = pos.get(rand.nextInt(pos.size()));

                pos.clear();
                if (info[1] - 1 > nextStartingPoint && i > resolution/2) pos.add(info[1] - 1);
                if (info[1] > nextStartingPoint) pos.add(info[1]);
                if (i < resolution/2) pos.add(info[1] + 1);
                if(pos.size() > 0) nextEndingPoint = pos.get(rand.nextInt(pos.size()));
            }
            if(info[0] < 0) nextStartingPoint = ThreadLocalRandom.current().nextInt(5*(resolution/16), resolution/2);
            if(info[1] < 0) nextEndingPoint = ThreadLocalRandom.current().nextInt(resolution/2 + 1, resolution - 5*(resolution/16));
            if (rand.nextInt(2) == 0 && !brokeStartLine || brokeStartLine && i < (resolution/2 - resolution/3)){
                brokeStartLine = true;
                continue;
            }
            if ((i > (resolution/2 + resolution/3) && rand.nextInt(3) == 0) && !brokeEndLine || brokeEndLine){
                brokeEndLine = true;
                continue;
            }
            //System.out.println(nextStartingPoint + " " + nextEndingPoint);
            for (int j = 0; j < resolution; j++) {
                g.setColor(change(new Color(color.red, color.green, color.blue, 255), 0, 0, 0));
                if (streak > 0)
                {
                    g.fillRect(j, i, 1, 1);
                    streak++;
                    if(j == nextEndingPoint || streak >= maxStreak){
                        info[1] = j;
                        maxStreak += Math.pow(8, resolution);
                        break;
                    }
                }
                else if(j == nextStartingPoint)
                {
                    info[0] = j;
                    g.fillRect(j, i, 1, 1);
                    streak++;
                }
            }
        }

        int sat = 0, bright = 100;
        int lightSourceXSign = (rand.nextBoolean()?(-1):1)*rand.nextInt(2);
        int lightSourceYSign = (rand.nextBoolean()?(-1):1)*(lightSourceXSign == 0 ? 1 : rand.nextInt(2));
        boolean shrink = rand.nextBoolean();
        for(int i=0; i <= rand.nextInt(21); i++) {
            addShade(2*i, sat, bright, 2*i*lightSourceXSign, 2*i*lightSourceYSign, shrink);
            sat = rand.nextInt(101);
            bright = rand.nextInt(101);
        }

    }

    public void addShade(int increment, int sat, int bright, int lightSourceX, int lightSourceY, boolean shrink)
    {
        copy = new BufferedImage(resolution, resolution, BufferedImage.TYPE_INT_ARGB);
        Color newCol = change(new Color(color.red, color.green, color.blue, 255), 0, sat, bright);
        for(int i = lightSourceX; i < (shrink ? resolution - lightSourceX : resolution + lightSourceX); i++)
            for(int j = lightSourceY; j < (shrink ? resolution - lightSourceY : resolution + lightSourceY); j++)
                if(i+lightSourceX > 0 && j+lightSourceY > 0 && i+increment < resolution && j+increment < resolution && image.getRGB(i+lightSourceX,j+lightSourceY) != 0)
                    copy.setRGB(i,j, newCol.getRGB());

        for(int i = 0; i < copy.getWidth(); i++)
            for(int j = 0; j < copy.getHeight(); j++)
                if(copy.getRGB(i, j) != 0 && image.getRGB(i, j) != 0) image.setRGB(i, j, newCol.getRGB());
    }

    int ind = 0;
    public void save() throws IOException {
        File outputfile = new File(Game.name.getText()+"_"+ind+"(x"+resolution+").png");
        ImageIO.write(image, "png", outputfile);
        ind++;
    }

    @Override
    public void paintComponent(Graphics g0)
    {
        Graphics2D g = (Graphics2D) g0;
        //g.setColor(Color.white);
        g.drawImage(image, null, getBounds().width/2 - resolution/2, getBounds().height/2 - resolution/2);
        //g.drawImage(copy, null, getBounds().width/2 - resolution/2, getBounds().height/2 - resolution/2);
        g.drawRect(getBounds().width/2 - resolution/2, getBounds().height/2 - resolution/2, image.getWidth(), image.getHeight());
        //g.drawString(color.name() + " " + color.ordinal(), 0, 150);
        //g.drawString(color.getComplementary(color).name() + " " + color.getComplementary(color).ordinal(), 0, 180);
    }

    public Color change(Color c, int hue, int sat, int bright)
    {
        float[] hsb = new float[3];
        Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsb);
        Color n = Color.getHSBColor(hsb[0] - ((float)hue/100)*hsb[0], hsb[1] - ((float)sat/100)*hsb[1], hsb[2] - ((float)bright/100)*hsb[2]);
        return n;
    }
}