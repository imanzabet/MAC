//=================================== KnapsackDemoApplet ============================================

import java.awt.*;
import java.applet.Applet;

import popcorn.*;
import java.util.BitSet;

/**
 * A demo of solving the Knapsack problem for real weights in parallel using Popcorn.<BR>
 * The problem: There is a bag that can hold a weight of W kg. There are N items weighting
 * w1,...,wn with values p1,...,pn. Find the subset of items that will maximize the value,
 * and fit into the bag.<BR>
 * Here in the demo we use bricks for the items, with width corresponding to their weight.
 */
public class KnapsackDemoApplet extends NonFlushingApplet implements Maximizer, Runnable {
    private static final int TOP_MARGIN = 30;      // margins to define the drawing area of the applet
    private static final int LEFT_MARGIN = 40;
    private static final int RIGHT_MARGIN = 40;
    private static final int BOTTOM_MARGIN = 50;
    private static final int ROW_SPACING = 20;
    private static final int ITEM_SPACING = 10;
    private static final int ITEM_HEIGHT = 30;

    private static final int NUMBER_OF_ITEMS = 40;
    private static final double BAG_CAPACITY = 700.0;
    private static final double MAXIMAL_WEIGHT = 120.0;
    private static final double MINIMAL_WEIGHT = 25.0;
    private static final int MAXIMAL_VALUE = 100;
    private static final int MINIMAL_VALUE = 5;

    private static final int ITERATIONS_PER_PACKET = 400;

    Item[] items;
    BitSet selection;
    int maxValue;
    double bagCapacity = BAG_CAPACITY;
    int packetSent,packetArrived;

    public void init() {
        setBackground(Color.white);
    }

    public void start() {
        items = buildRandomItems();
        selection = new BitSet(items.length);
        new Thread(this).start();
    }

    public void run() {
        int[] values = values(items);
        double[] weights = weights(items);
        new popcorn.benchmark.DistributerTracer.start();
        for (;;) {
            ComputationPacket packet =
                new KnapsackPacket(weights, values, bagCapacity,
                                   ITERATIONS_PER_PACKET,this);
            packet.go();
            packetSent++;
            synchronized(this) {
                if (packetSent>packetArrived+20) {
                    try {
                        wait();
                    } catch (InterruptedException e) {}
                }
            }
        }
        //Computation.collectAll();
    }

    public void paintNoFlushes(Graphics g) {
        drawUnselectedItems(g);
        drawBag(g);
        String status = "Packet sent: "+packetSent+" Packet arrived: "+packetArrived+
                      " Maximal value: "+maxValue;
        showStatus(status);
    }

    public synchronized void updateMaximum(Object arg, int value) {
        packetArrived++;
        notify();
        BitSet selection = (BitSet)arg;
        if (totalValue(selection)>maxValue) {
            this.selection=selection;
            maxValue=totalValue(selection);
        }
        repaint();
    }

    int totalValue(BitSet selection) {
        int value=0;
        for (int i=0; i<items.length; i++)
            if (selection.get(i))
                value+=items[i].value();
        return value;
    }

    int[] values(Item[] items) {
        int[] values = new int[items.length];
        for (int i=0; i<values.length; i++)
            values[i]=items[i].value();
        return values;
    }

    double[] weights(Item[] items) {
        double[] weights = new double[items.length];
        for (int i=0; i<weights.length; i++)
            weights[i]=items[i].weight();
        return weights;
    }

    void drawUnselectedItems(Graphics g) {
        int x=LEFT_MARGIN;
        int y=TOP_MARGIN;
        for (int i=0; i<items.length; i++) {
            if (selection.get(i))
                continue;
            if (x+RIGHT_MARGIN+items[i].width() >= size().width) {
                x=LEFT_MARGIN;
                y+=items[i].height()+ROW_SPACING;
            }
            items[i].drawAt(g,x,y);
            x+=items[i].width()+ITEM_SPACING;
        }
    }

    void drawBag(Graphics g) {
        int width = (int) BAG_CAPACITY;
        int left=(size().width-width)/2;
        int top=size().height-BOTTOM_MARGIN-ITEM_HEIGHT;

        // -- frame --
        g.setColor(Color.blue);
        g.drawLine(left-1,top-1,left-1,top+ITEM_HEIGHT+1);
        g.drawLine(left-1,top+ITEM_HEIGHT+1,left+width+2,top+ITEM_HEIGHT+1);
        g.drawLine(left+width+2,top+ITEM_HEIGHT+1,left+width+2,top-1);

        g.drawLine(left-2,top-1,left-2,top+ITEM_HEIGHT+2);
        g.drawLine(left-2,top+ITEM_HEIGHT+2,left+width+3,top+ITEM_HEIGHT+2);
        g.drawLine(left+width+3,top+ITEM_HEIGHT+2,left+width+3,top-1);

        // -- selected items --

        int x=left;
        int y=top;
        for (int i=0; i<items.length; i++) {
            if (!selection.get(i))
                continue;
            items[i].drawAt(g,x,y);
            x+=items[i].width();
        }
    }

    static Item[] buildRandomItems() {
        Item[] items = new Item[NUMBER_OF_ITEMS];
        for (int i=0; i<NUMBER_OF_ITEMS; i++) {
            double weight = (Math.random()*(MAXIMAL_WEIGHT-MINIMAL_WEIGHT)+MINIMAL_WEIGHT);
            int value = (int)(Math.random()*(MAXIMAL_VALUE-MINIMAL_VALUE)+MINIMAL_VALUE);
            int width = (int) weight;
            items[i]=new Item(weight,value,width,ITEM_HEIGHT);
        }
        return items;
    }
}

class Item {
    int width,height;
    int value;
    double weight;
    Color bgColor;
    Color frameColor=Color.black;
    Color textColor=Color.black;

    public Item(double weight, int value, int width, int height) {
        this.weight=weight;
        this.value=value;
        this.width=width;
        this.height=height;
        bgColor=chooseRandomColor();
    }

    public void drawAt(Graphics g, int x, int y) {
        g.setColor(bgColor);
        g.fillRect(x,y,width,height);
        g.setColor(frameColor);
        g.drawRect(x,y,width,height);
        g.drawRect(x+1,y+1,width-2,height-2);
        int left=x+width/2-10;
        int bottom=y+height/2+5;
        String text=value+"$";
        g.setColor(textColor);
        g.drawString(text,left,bottom);
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public int value() {
        return value;
    }

    public double weight() {
        return weight;
    }

    static Color chooseRandomColor() {
        int r=(int)(Math.random()*256);
        int g=(int)(Math.random()*256);
        int b=(int)(Math.random()*256);
        return new Color(r,g,b);
    }
}

interface Maximizer {
    public void updateMaximum(Object arg, int value);
}

class KnapsackPacket extends ComputationPacket {
    Maximizer observer;

    public KnapsackPacket(double[] weights, int[] values,
                          double totalWeight, int iterations,Maximizer observer) {
        super(new KnapsackComputelet(weights, values, totalWeight,iterations));
        this.observer=observer;
    }

    public void completed() {
        Pair p = (Pair) getResult();
        done();
        observer.updateMaximum(p.first, ((Integer)p.second).intValue());
    }

}

class KnapsackComputelet implements Computelet {
    private double[] weights;
    private int[] values;
    private double totalWeight;
    private int iterations;
    private int n;  // number of items

    public KnapsackComputelet(double[] weights, int[] values, double totalWeight, int iterations) {
        this.weights=weights;
        this.values=values;
        this.iterations=iterations;
        this.totalWeight=totalWeight;
        n=weights.length;
    }

    public Object compute() {
        int maxValue=0;
        BitSet bestSelection = new BitSet(n);

        for (int i=0; i<iterations; i++) {
            BitSet selection = new BitSet(n);
            int value=0;
            double weight=0.0;

            do {
                int bit = (int) (Math.random()*n);
                if (selection.get(bit))
                    continue;   // if item is allready choosen, choose another
                if (weight+weights[bit]>totalWeight)
                    break;
                selection.set(bit);
                weight+=weights[bit];
                value+=values[bit];
            } while(true);
            if (value>maxValue) {
                bestSelection = (BitSet) selection.clone();
                maxValue=value;
            }
        }
        return new Pair(bestSelection, new Integer(maxValue));
    }
}

class Pair {
    public Object first,second;

    public Pair(Object first, Object second) {
        this.first=first;
        this.second=second;
    }
}

class NonFlushingApplet extends Applet {

   private Graphics offGraphics;
   private Dimension offDimension;
   private Image offImage;

   public final void update(Graphics g) {
       Dimension d=size();
       if ( (offGraphics == null)
           || (d.width != offDimension.width)
           || (d.height != offDimension.height) ) {
          offDimension = d;
          offImage = createImage(d.width, d.height);
          offGraphics = offImage.getGraphics();
       }
       offGraphics.setColor(getBackground());
       offGraphics.fillRect(0, 0, d.width, d.height);
       paintNoFlushes(offGraphics);
       g.drawImage(offImage, 0, 0, this);
   }

   public final void paint(Graphics g) {
      update(g);
   }

   public void paintNoFlushes(Graphics g) {
   }
}

