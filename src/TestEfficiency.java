import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import edu.uwm.cs.junit.EfficiencyTestCase;
import edu.uwm.cs351.WordMultiset;
import edu.uwm.cs351.util.DefaultEntry;

public class TestEfficiency extends EfficiencyTestCase {

	private WordMultiset m;
	
	private Random random;
	
	private static final int POWER = 21; // million entries
	private static final int SIZE = (1 << (POWER-1)) - 1;
	private static final int TESTS = SIZE/POWER; 
	private static final int BASE = 10_000_000; // must be more than 1<<POWER
	
	protected String makeKey(int i) {
		return String.valueOf(BASE+i);
	}
	
	protected void setUp() {
		super.setUp();
		random = new Random();
		try {
			assert m.size() == TESTS : "cannot run test with assertions enabled";
		} catch (NullPointerException ex) {
			throw new IllegalStateException("Cannot run test with assertions enabled");
		}
		m = new WordMultiset();
		for (int power = POWER; power > 1; --power) {
			int incr = 1 << power;
			int start = 1 << (power-1);
			for (int j=0; j < 1<<(POWER-power); ++j) {
				m.put(makeKey((j*incr+start)/2), Integer.valueOf(j*incr+start));
			}
		}
	}
		
	
	@Override
	protected void tearDown() {
		m = null;
		super.tearDown();
	}


	public void testA() {
		for (int i=0; i < SIZE; ++i) {
			assertEquals(SIZE,m.size());
		}
	}
	
	public void testB() {
		for (int i=0; i < TESTS; ++i) {
			assertFalse(m.add(makeKey(i+1)));
		}
	}

	public void testC() {
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(SIZE-1)+1;
			assertTrue(m.containsKey((Object)makeKey(r)));
			assertEquals(r*2,m.get((Object)makeKey(r)).intValue());
			assertNull(m.get((Object)makeKey(SIZE+1+r)));
			assertFalse(m.containsKey((Object)makeKey(SIZE+1+r)));
		}
	}
	
	public void testD() {
		Set<Integer> touched = new HashSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(SIZE-1-i)+1;
			if (!touched.add(r)) continue;
			Integer val = m.remove(makeKey(r));
			assertFalse(val == null);
		}
		assertEquals(SIZE-touched.size(), m.size());
	}

	public void testE() {
		Set<Integer> touched = new HashSet<Integer>();
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(SIZE-1)+1;
			if (!touched.add(r)) continue; // don't check again
			assertEquals(r*2,m.put(makeKey(r), 3).intValue());
			assertEquals(Integer.valueOf(3), m.get((Object)makeKey(r)));
		}
	}

	public void testF() {
		for (int i=0; i < TESTS; ++i) {
			assertTrue(m.removeOne(makeKey(i+1)));
			assertEquals(SIZE, m.entrySet().size());
		}
	}
	
	public void testG() {
		for (int i=1; i < TESTS; ++i) {
			assertFalse("should not contain bad entry for " + i,
					m.entrySet().contains(new DefaultEntry<>(makeKey(i*4),Integer.valueOf(i*4))));
			assertTrue("should contain entry for " + i,
					m.entrySet().contains((Object)new DefaultEntry<>(makeKey(i*4),Integer.valueOf(i*8))));
			assertFalse("should not contain non-existent entry for " + i,
					m.entrySet().contains(new DefaultEntry<>(makeKey(SIZE+1+i*4), Integer.valueOf(i*8))));	
		}
	}
	
	public void testH() {
		Set<Entry<String,Integer>> es = m.entrySet();
		for (int i=0; i < TESTS; ++i) {
			assertFalse("should not remove bad entry for " + i,
					es.remove(new DefaultEntry<>(makeKey(i*+1),Integer.valueOf(i*4+1))));
			assertTrue("should be able to remove entry for " + i, 
					es.remove((Object)new DefaultEntry<>(makeKey(i*2+1),Integer.valueOf(i*4+2))));	
			assertFalse("should not remove twice entry for " + i,
					es.remove(new DefaultEntry<>(makeKey(i*2+1),Integer.valueOf(i*4+2))));
		}
		assertEquals(SIZE-TESTS, m.size());
	}

	public void testI() {
		Set<Entry<String,Integer>> es = m.entrySet();
		Map.Entry<String,Integer> e = es.iterator().next();
		e = new DefaultEntry<>(e.getKey(), e.getValue());
		for (int i=0; i < SIZE; ++i) {
			Iterator<Entry<String,Integer>> it= es.iterator();
			assertEquals(e, it.next());
		}
	}
	
	public void testJ() {
		Iterator<Entry<String,Integer>> it = m.entrySet().iterator();
		Set<Integer> seen = new HashSet<Integer>();
		for (int i=1; i <= SIZE; ++i) {
			assertTrue("After " + i + " next(), should still have next",it.hasNext());
			// unpack while trying to avoid creating new strings
			Map.Entry<String,Integer> e = it.next();
			assertTrue(e.getKey(), seen.add(e.getValue())); // message is the key that appeared againB
			int v = Integer.parseInt(e.getKey()) - BASE;
			assertEquals(v*2, e.getValue().intValue());
		}
	}
	
	public void testK() {
		int removed = 0;
		assertEquals(SIZE,m.size());
		Iterator<Entry<String,Integer>> it = m.entrySet().iterator();
		for (int i = 1; i < TESTS; ++i) {
			it.next();
			if (random.nextBoolean()) {
				it.remove();
				++removed;
			}
		}
		assertEquals(SIZE-removed,m.size());
	}

	public void testL() {
		String k = makeKey(SIZE/2);
		for (int i=0; i < SIZE-1; ++i) {
			assertTrue(m.removeOne(k));
		}
		assertFalse(m.removeOne(k));
		assertEquals(SIZE-1, m.size());
	}
	
	public void testM() {
		Iterator<Entry<String,Integer>> it = m.entrySet().iterator();
		for (int i=1; i < SIZE; ++i) {
			it.next();
			it.remove();
		}
		it = m.entrySet().iterator();
		for (int i=0; i < SIZE; ++i) {
			assertTrue(it.hasNext());
		}
	}
	
	public void testN() {
		Iterator<Entry<String,Integer>> it = m.entrySet().iterator();
		it.next();
		for (int i=1; i < SIZE; ++i) {
			it.next();
			it.remove();
		}
		it = m.entrySet().iterator();
		it.next();
		for (int i=0; i < SIZE; ++i) {
			assertFalse(it.hasNext());
		}
	}
	
	public void testO() {
		Iterator<Entry<String,Integer>> it = m.entrySet().iterator();
		String key1 = it.next().getKey();
		for (int i=1; i < SIZE; ++i) {
			it.next();
			it.remove();
		}
		assertEquals(1, m.size());
		Set<Integer> touched = new HashSet<Integer>();
		touched.add(Integer.parseInt(key1) - BASE);
		for (int i=0; i < TESTS; ++i) {
			int r = random.nextInt(SIZE-1)+1;
			if (!touched.add(r)) continue; // don't check again
			assertNull(m.put(makeKey(r), 3));
			assertEquals(Integer.valueOf(3), m.get((Object)makeKey(r)));
		}
	}
	
	public void testP() {
		for (int i=0; i < SIZE; ++i) {
			m.clear();
		}
	}
	
	public void testQ() {
		for (int i=0; i < SIZE; ++i) {
			m.entrySet().clear();
		}
	}

}
