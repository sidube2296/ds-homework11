import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import edu.uwm.cs351.WordMultiset;

public class TestWordMultiset extends AbstractTestMap<String, Integer> {
	protected WordMultiset wm;
	
	@Override
	protected Map<String, Integer> create() {
		return new WordMultiset();
	}

	@Override
	protected void initMapElements() {
		k = new String[] {
				"apples", "bread", "celery", "dates", "eggs", 
				"fish", "grapes", "ham", "ice", "jello"
		};
		l = new String[k.length];
		for (int i=0; i < k.length; ++i) {
			l[i] = new String(k[i]);
		}
		v = new Integer[] {10, 1, 2, 3, 4, 5, 6, 7, 8, 9};
		w = new Integer[v.length];
		for (int i=0; i < v.length; ++i) {
			w[i] = i;
			if (i == 0) w[i] += 10; 
		}
		this.failFast = true;
		this.hasRemove = true;
		wm = new WordMultiset();
	}

	
	/// missing tests on things tested in AbstractTestMap
	
	public void test030() {
		assertException(NullPointerException.class, () -> m.put(null, 3));
	}
	
	public void test031() {
		assertException(IllegalArgumentException.class, () -> m.put("apples", null));
	}
	
	public void test032() {
		assertException(IllegalArgumentException.class, () -> m.put("bread", 0));
	}
	
	public void test033() {
		assertException(IllegalArgumentException.class, () -> m.put("celery", -42));
	}
	
	// only work on this while working on the test260 tests and later
	public void test270() {
		m.put("apples", 3);
		Map.Entry<String, Integer> e = m.entrySet().iterator().next();
		assertException(IllegalArgumentException.class, () -> e.setValue(0));
	}

	public void test271() {
		m.put("oranges", 5);
		Map.Entry<String, Integer> e = m.entrySet().iterator().next();
		assertException(IllegalArgumentException.class, () -> e.setValue(-8));
	}

	public void test272() {
		m.put("pear", 2);
		Map.Entry<String, Integer> e = m.entrySet().iterator().next();
		assertException(IllegalArgumentException.class, () -> e.setValue(null));
	}
	
	
	/// proper placeholder usage:
	
	public void test334() {
		m.put("Aa", 4);
		m.put("BB", 3);
		m.remove("Aa");
		assertEquals(Integer.valueOf(3), m.get("BB"));
	}
	
	public void test335() {
		m.put("Aa", 4);
		m.put("BB", 3);
		m.remove("Aa");
		((WordMultiset)m).add("Aa");
		assertEquals(Integer.valueOf(3), m.get("BB"));
		assertEquals(Integer.valueOf(1), m.get("Aa"));
	}

	public void test336() {
		m.put("Aa", 4);
		m.put("BB", 3);
		m.remove("Aa");
		m.put("BB", 6);
		assertEquals(Integer.valueOf(6), m.get("BB"));
	}
	
	
	public void test349() {
		m.put(k[1], v[1]);
		m.put(k[2], v[2]);
		m.put(k[3], v[3]);
		m.put(k[4], v[4]);
		m.put(k[5], v[5]);
		m.put(k[6], v[6]);
		m.put(k[7], v[7]);
		m.put(k[8], v[8]);
		m.put(k[9], v[9]);
		it = m.entrySet().iterator();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		it.next(); it.remove();
		assertTrue(m.entrySet().isEmpty());
		m.clear();
		assertFalse(it.hasNext());
	}
	
	
	/// test5xx: testing add
		
	public void test500() {
		assertEquals(0,wm.size());
	}

	public void test501() {
		assertTrue(wm.add("apple"));
		assertEquals(1,wm.size());
	}
	
	public void test502() {
		assertTrue(wm.add("apple"));
		assertEquals(false, wm.add("apple"));
		assertEquals(1, wm.size());
	}
	
	public void test503() {
		wm.add("apple");
		assertEquals(true, wm.add("barn"));
		assertEquals(2,wm.size());
	}
	
	public void test504() {
		wm.add("apple");
		wm.add("barn");
		assertFalse(wm.add("apple"));
		assertFalse(wm.add("barn"));
	}
	
	public void test505() {
		wm.add("barn");
		assertTrue(wm.add("apple"));
		assertEquals(2,wm.size());
	}
	
	public void test506() {
		wm.add("barn");
		wm.add("apple");
		assertFalse(wm.add("barn"));
		assertFalse(wm.add("apple"));
	}
	
	public void test507() {
		wm.add("apple");
		wm.add("barn");
		wm.add("crew");
		assertEquals(3,wm.size());
		assertFalse(wm.add("apple"));
		assertFalse(wm.add("barn"));
		assertFalse(wm.add("crew"));
	}
	
	public void test508() {
		String[] set = new String[] { "ant", "but", "he", "one", "other",
				"our", "no", "time", "up", "use"};
		
		assertTrue(wm.add(set[5]));
		assertTrue(wm.add(set[2]));
		assertTrue(wm.add(set[3]));
		assertTrue(wm.add(set[4]));
		assertTrue(wm.add(set[8]));
		assertTrue(wm.add(set[7]));
		assertTrue(wm.add(set[6]));
		assertTrue(wm.add(set[1]));
		assertTrue(wm.add(set[9]));
		assertTrue(wm.add(set[0]));
		
		for (String s: set)
			assertFalse("Should not allow duplicate: "+s, wm.add(s));
		assertEquals(10,wm.size());
	}
	
	public void test509() {
		assertException(NullPointerException.class, () -> wm.add(null));
	}

	public void test510() {
		assertEquals(null, wm.get("apple"));
	}
	
	public void test511() {
		wm.add("apple");
		assertEquals(Integer.valueOf(1), wm.get("apple"));
	}
	
	public void test512() {
		wm.add("apple");
		wm.add("bread");
		wm.add("apple");
		assertEquals(Integer.valueOf(2), wm.get("apple"));
		assertEquals(Integer.valueOf(1), wm.get("bread"));
	}
	
	public void test513() {
		wm.add("barn");
		wm.add("apple");
		wm.add("crew");
		wm.add("barn");
		wm.add("crew");
		wm.add("crew");
		assertEquals(Integer.valueOf(1), wm.get("apple"));
		assertEquals(Integer.valueOf(2), wm.get("barn"));
		assertEquals(Integer.valueOf(3), wm.get("crew"));		
	}

	public void test518() {
		String[] set = new String[] { "ant", "but", "he", "one", "other",
				"our", "no", "time", "up", "use"};
		
		assertTrue(wm.add(set[5]));
		assertTrue(wm.add(set[2]));
		assertTrue(wm.add(set[3]));
		assertTrue(wm.add(set[4]));
		assertTrue(wm.add(set[8]));
		assertTrue(wm.add(set[7]));
		assertTrue(wm.add(set[6]));
		assertTrue(wm.add(set[1]));
		assertTrue(wm.add(set[9]));
		assertTrue(wm.add(set[0]));
		
		for (int i=0; i < set.length; ++i) {
			for (int j=0; j < i; ++j) {
				assertFalse(wm.add(set[i]));
			}
		}
		
		assertEquals(set.length,wm.size());
		
		for (int i=0; i < set.length; ++i) {
			assertEquals(Integer.valueOf(i+1), wm.get(set[i]));
		}
	}
	
	
	/// test55x and following testing removeOne
	
	public void test550() {
		assertFalse(wm.removeOne("apple"));
	}
	
	public void test551() {
		wm.add("apple");
		assertTrue(wm.removeOne("apple"));
	}
	
	public void test552() {
		wm.add("apple");
		assertFalse(wm.removeOne("bread"));
	}
	
	public void test553() {
		wm.add("apple");
		wm.removeOne("apple");
		assertEquals(0, wm.size());
	}
	
	public void test554() {
		wm.add("bread");
		wm.removeOne("apple");
		assertEquals(1, wm.size());
	}
	
	public void test555() {
		wm.add("barn");
		wm.add("apple");
		assertTrue(wm.removeOne("apple"));
		assertEquals(1, wm.size());
	}
	
	public void test556() {
		wm.add("barn");
		wm.add("apple");
		assertTrue(wm.removeOne("barn"));
		assertFalse(wm.removeOne("barn"));
	}
	
	public void test557() {
		wm.add("bread");
		wm.add("apple");
		assertFalse(wm.removeOne("barn"));
		assertEquals(2, wm.size());
	}

	public void test558() {
		wm.add("barn");
		wm.add("apple");
		wm.add("crew");
		assertTrue(wm.removeOne("barn"));
		assertEquals(Integer.valueOf(1), wm.get("apple"));
		assertNull(wm.get("barn"));
		assertEquals(Integer.valueOf(1), wm.get("crew"));
	}
	
	public void test559() {
		wm.add("barn");
		wm.add("apple");
		wm.add("crew");
		wm.add("barn");
		wm.add("apple");
		assertTrue(wm.removeOne("barn"));
		assertEquals(Integer.valueOf(2), wm.get("apple"));
		assertEquals(Integer.valueOf(1), wm.get("barn"));
		assertEquals(Integer.valueOf(1), wm.get("crew"));
	}
}