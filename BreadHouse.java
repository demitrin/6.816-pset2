import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicReferenceArray;

/*
 * The BreadBox interface defines methods that are present in all BreadBox
 * objects. The default state of a BreadBox is empty.
 */
interface BreadBox {
   /*
    * Returns whether or not there is a loaf of bread in the breadbox. The
    * default state is empty.
    *
    * @return
    *            True if there is a loaf, false if it is still empty.
    */
    public boolean isEmpty();
    
   /*
    * This method adds a loaf of bread to an empty breadbox.
    * 
    * If this method is called on a non-empty breadbox, an exception is thrown.
    */
    public void addLoaf();
}

/*
 * The BreadHouse class represents the house where Alex and Sam live.
 * 
 * When Alex arrives at home, method alex() is called. When Sam arrives at home,
 * method alex() is called.
 */
public class BreadHouse {
   /*
    * All fields accessed by both threads must be Atomic classes from
    * java.util.concurrent.atomic
    */
    private AtomicBoolean s1;
    private AtomicBoolean s2;
    private AtomicBoolean a1;
    private AtomicBoolean a2;

    public BreadHouse() {
        s1 = new AtomicBoolean(false);
        s2 = new AtomicBoolean(false);
        a1 = new AtomicBoolean(false);
        a2 = new AtomicBoolean(false);
    }
    
   /*
    * Executes Alex's strategy.
    *
    * This method is called at most once, when Alex arrives at home from work.
    * It may run concurrently with sam(), which will get the same,
    * initially-empty BreadBox, or alex() may run before sam(), after sam(),
    * or without sam() ever running.
    * When this method returns, box should not be empty.
    *
    * @param box
    *            A reference to the breadbox which must be refilled.
    */
    public void alex(BreadBox box) {
        a1.set(true);

        if (s2.get()) {
            a2.set(true);
        } else {
            a2.set(false);
        }

        while (s1.get() && ((a2.get() && s2.get()) || (!a2.get() && !s2.get()))) {}

        if (box.isEmpty()) {
            box.addLoaf();
        }

        a1.set(false);
	}
	
   /*
    * Executes Sam's strategy.
    *
    * This method is called at most once, when Sam arrives at home from work.
    * It may run concurrently with alex(), which will get the same,
    * initially-empty BreadBox, or sam() may run before alex(), after alex(),
    * or without alex() ever running.
    * When this method returns, box should not be empty.
    * 
    * @param box
    *            A reference to the breadbox which must be refilled.
    */
    public void sam(BreadBox box) {
        s1.set(true);

        if (!a2.get()) {
            s2.set(true);
        } else {
            s2.set(false);
        }

        while (a1.get() && ((a2.get() && !s2.get()) || (!a2.get() && s2.get()))) {}

        if (box.isEmpty()) {
            box.addLoaf();
        }

        s1.set(false);
	}
}
