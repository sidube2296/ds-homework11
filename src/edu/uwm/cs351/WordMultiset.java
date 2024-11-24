package edu.uwm.cs351;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.function.Consumer;

import edu.uwm.cs351.util.AbstractEntry;
import edu.uwm.cs351.util.Primes;

/**
 * Multiset of strings, placed in a hash table
 */
public class WordMultiset extends AbstractMap<String,Integer> 
{
	private static class MyEntry extends AbstractEntry<String, Integer>
	{
		String string;
		int count;

		MyEntry (String s) { this(s, 1); }
		MyEntry (String s, int c) { string = s; count = c; }
		
		@Override // required
		public String getKey() {
			return string;
		}
		
		@Override // required
		public Integer getValue() {
			return count;
		}
		
		@Override // implementation
		public Integer setValue(Integer v) {
			if (v == null || v <= 0) throw new IllegalArgumentException("must be positive: " + v);
			int old = count;
			count = v;
			return old;
		}
	}
	
	private static final int INITIAL_CAPACITY = 7;
	
	private MyEntry[] data;
	private int numUsed;
	private int numEntries;
	private int version;
	
	private static MyEntry PLACE_HOLDER = new MyEntry(null);
	
	/**
	 * Hash the key to a table index, following double hashing, 
	 * returning the first index that
	 * (1) includes an entry with the key, or
	 * (2) has null, or
	 * (3) has a placeholder (if phOK is true *and* the key cannot be found).
	 * This code assumes that double hashing
	 * will find a valid index.  It may run forever otherwise.
	 * @param key string to look for, must not be null
	 * @param phOK whether we return a slot with a placeholder in preference to an empty slot
	 * @return first index meeting the requirements using double hashing.
	 */
	private int hash(String key, boolean phOK) {
		// TODO
	    int L = data.length;
	    int h1 = Math.floorMod(key.hashCode(), L); 
	    int h2 = 1 + Math.floorMod(key.hashCode(), L - 2);
	    int index = h1;
	    int f_index = -1;

	    while (true) {
	        if (data[index] == null) {
	            return (phOK && f_index != -1) ? f_index : index;
	        } else if (data[index] == PLACE_HOLDER) {
	            if (phOK && f_index == -1) {
	            	f_index = index;
	            }
	        } else if (data[index].string != null && data[index].string.equals(key)) {
	            return index;
	        }
	        index = Math.floorMod(index + h2, L);
	    }
	}

	
	private static Consumer<String> reporter = (s) -> System.out.println("Invariant error: "+ s);
	
	/**
	 * Used to report an error found when checking the invariant.
	 * By providing a string, this will help debugging the class if the invariant should fail.
	 * @param error string to print to report the exact error found
	 * @return false always
	 */
	private static boolean report(String error) {
		reporter.accept(error);
		return false;
	}
	
	/**
	 * Check the invariant.  
	 * Returns false if any problem is found. 
	 * @return whether invariant is currently true.
	 * If false is returned then exactly one problem has been reported.
	 */
	private boolean wellFormed() {
		// TODO
	    if (data == null) return report("Data array is null");

	    if (data.length < INITIAL_CAPACITY) return report("Data array length is less than the initial capacity.");	    
	    if (!Primes.isPrime(data.length)) return report("Data array length is not a prime.");

	    if (!Primes.isPrime(data.length - 2)) return report("Data array length and length-2 are not twin primes.");
	    
	    int n = 0;
	    int m = 0;
	    
	    for (int i = 0; i < data.length; i++) {
	        MyEntry entry = data[i];        
	        if (entry != null) {
	            n++;	            
	            if (entry != PLACE_HOLDER) {
	                m++;
	                if (entry.getKey() == null) return report("Real entry at index " + i + " has a null key.");	                
	                if (entry.getValue() == null || entry.getValue() <= 0) return report("Real entry at index " + i + " has a non-positive count.");
	                int f = hash(entry.getKey(), false);
	                if (f != i) return report("Real entry for key \"" + entry.getKey() + "\" should be at index " + f + " but found at index " + i + ".");
	            }
	        }
	    }
	    if (numUsed != n) return report("numUsed (" + numUsed + ") does not match actual number of used slots (" + n + ").");

	    if (numEntries != m) return report("numEntries (" + numEntries + ") does not match actual number of real entries (" + m + ").");

	    if (numUsed > data.length / 2) return report("numUsed (" + numUsed + ") exceeds half of the array length (" + data.length / 2 + ").");

		return true;
	}

	private WordMultiset(boolean unused) { } // do not modify, used by Spy
	
	/**
	 * Creates an empty multiset
	 */
	public WordMultiset() {
		// TODO: Implement the constructor (BEFORE the assertion!)
		data = new MyEntry[INITIAL_CAPACITY];
	    numUsed = 0;
	    numEntries = 0;
	    version = 0;
		assert wellFormed() : "invariant false at end of constructor";
	}
	
	@Override // required
	public int size() {
		assert wellFormed() : "invariant false at start of size()";
		return numEntries;
	}
	
	/**
	 * Create a new data array that is at least four times the number of entries 
	 * (at least INITIAL_CAPACITY) and place all the entries in the order that they appear
	 * in the original array.  The new array will have no place holders.
	 */
	private void rehash() {
		// TODO
	}
	
	
	/**
	 * Add a new string to the multiset. If it already exists, 
	 * increase the count for the string and return false.
	 * Otherwise, set the count to one and return true.
	 * @param str the string to add (must not be null)
	 * @return true if str was added, false otherwise
	 * @throws NullPointerException if str is null
	 */
	public boolean add(String str) {
		assert wellFormed() : "invariant false at start of add";
		boolean result = false;
		// TODO: Implement this method
		assert wellFormed() : "invariant false at end of add";
		return result;
	}
	
	/**
	 * Remove one copy of a word from the multiset.
	 * If there are multiple copies, then we just adjust the count,
	 * and the map is unaffected (iterators don't go stale).
	 * @param str string to remove one of, may be null (but ignored if so)
	 * @return true if the word was in the multiset.
	 */
	public boolean removeOne(String str) {
		assert wellFormed() : "invariant false at start of removeOne";
		boolean result = false;
		// TODO: implement this method
		assert wellFormed() : "invariant false at end of removeOne";
		return result;
	}

	private final EntrySet entrySet = new EntrySet();
	
	@Override // required
	public Set<Map.Entry<String,Integer>> entrySet() {
		assert wellFormed() : "invariant broken in entrySet";
		return entrySet;
	}
	
	private class EntrySet extends AbstractSet<Map.Entry<String, Integer>>
	{
		@Override // required
		public int size() {
			assert wellFormed(): "invariant failed in size";
			return numEntries;
		}
		
		@Override // efficiency
		public boolean contains(Object x) {
			assert wellFormed() : "invariant broken in contains";
			if (!(x instanceof Map.Entry<?,?>)) return false;
			Map.Entry<?,?> e = (Map.Entry<?,?>)x;
			if (!(e.getKey() instanceof String)) return false;
			if (!(e.getValue() instanceof Integer)) return false;
			return e.getValue().equals(get(e.getKey()));
		}
		
		@Override // efficiency
		public boolean remove(Object x) {
			if (!contains(x)) return false;
			WordMultiset.this.remove(((Map.Entry<?,?>)x).getKey());
			return true;
		}
		
		@Override // required
		public Iterator<Map.Entry<String,Integer>> iterator() {
			assert wellFormed() : "invariant broken in iterator";
			return new EntrySetIterator();
		}
		
		// TODO: efficiency override. (Wait until doing efficiency testing)
	}
	
	private class EntrySetIterator implements Iterator<Map.Entry<String, Integer>> {

		private int index;
		int remaining;
		private boolean canRemove;
		private int colVersion;
		
		private boolean wellFormed() {
			if (!WordMultiset.this.wellFormed()) return false;
			if (version != colVersion) return true;
			int r = 0;
			if (index == data.length) {
				if (canRemove) return report("cannot remove when no element");
			} else {
				if (data[index] == null) return report("index is on null");
				if (data[index] == PLACE_HOLDER) return report("index is on place holder");
				if (!canRemove) ++r;
			}
			for (int i=index+1; i < data.length; ++i) {
				if (data[i] == null) continue;
				if (data[i] != PLACE_HOLDER) ++r;
			}
			if (r != remaining) return report("remaining claims " + remaining + ", but should be " + r);
			return true;
		}
		
		// TODO: a helper method
		
		EntrySetIterator() {
			assert wellFormed() : "invariant broken in iterator constructor";
		}
		
		private void checkVersion() {
			if (version != colVersion) throw new ConcurrentModificationException("stale");
		}
		
		@Override //required
		public boolean hasNext() {
			assert wellFormed() : "invariant broken in hasNext";
			checkVersion();
			return false; // TODO
		}

		@Override //required
		public Entry<String, Integer> next() {
			assert wellFormed() : "invariant broken in next";
			checkVersion();
			if (!hasNext()) throw new NoSuchElementException("no more");
			// TODO: complete
			assert wellFormed() : "invariant broken by next";
			return data[index];
		}
		
		@Override // implementation
		public void remove() {
			assert wellFormed() : "invariant broken in remove";
			checkVersion();
			// TODO
			colVersion = version;
			assert wellFormed() : "invariant broken by remove";
		}
	}
		
	/**
	 * Used for testing the invariant.  Do not change this code.
	 */
	public static class Spy {
		private static class SpyEntry extends MyEntry {
			final Map.Entry<String, Integer> source;
			SpyEntry(String k, Integer v, Map.Entry<String,Integer> src) {
				super(k, v);
				source = src;
			}
		}
		/**
		 * Return the sink for invariant error messages
		 * @return current reporter
		 */
		public Consumer<String> getReporter() {
			return reporter;
		}

		/**
		 * Change the sink for invariant error messages.
		 * @param r where to send invariant error messages.
		 */
		public void setReporter(Consumer<String> r) {
			reporter = r;
		}
		
		/**
		 * Create a debugging instance of the ADT
		 * with a particular data structure.
		 * @param a array of entries, used to create the data table
		 * @param p place holder used
		 * @param m num entries
		 * @param u num used
		 * @param v version
		 * @return a new instance of a BallSeq with the given data structure
		 */
		public WordMultiset newInstance(Map.Entry<String,Integer>[] a, Object p, int m, int u, int v) {
			WordMultiset result = new WordMultiset(false);
			result.data = null;
			if (a != null) {
				result.data = new MyEntry[a.length];
				for (int i=0; i < a.length; ++i) {
					if (a[i] == p) {
						result.data[i] = PLACE_HOLDER;
					} else if (a[i] != null) {
						result.data[i] = new SpyEntry(a[i].getKey(), a[i].getValue(), a[i]);
					}
				}
			}
			result.numEntries = m;
			result.numUsed = u;
			result.version = v;
			return result;
		}

		/**
		 * Run the hash method in the debugging instance
		 * @param wm debugging instance 
		 * @param s string to check, must not be null
		 * @param okPH whether a placeholder is acceptable
		 * @return index
		 */
		public int hash(WordMultiset wm, String s, boolean okPH) {
			return wm.hash(s, okPH);
		}
		
		/**
		 * Return whether debugging instance meets the 
		 * requirements on the invariant.
		 * @param wm instance of to use, must not be null
		 * @return whether it passes the check
		 */
		public boolean wellFormed(WordMultiset wm) {
			return wm.wellFormed();
		}
		
		public Map.Entry<String,Integer>[] rehash(Map.Entry<String,Integer>[] a, Map.Entry<String, Integer> p, int m, int u) {
			WordMultiset wm = this.newInstance(a, p, m, u, 42);
			wm.rehash();
			if (wm.data == null) return null;
			@SuppressWarnings("unchecked")
			Map.Entry<String,Integer>[] result = (Map.Entry<String, Integer>[]) new Map.Entry<?, ?>[wm.data.length];
			for (int i=0; i < result.length; ++i) {
				if (wm.data[i] == null) continue;
				if (wm.data[i] == PLACE_HOLDER) result[i] = p;
				else if (wm.data[i] instanceof SpyEntry) {
					SpyEntry se = (SpyEntry)wm.data[i];
					result[i] = se.source;
					assert wm.data[i].string == se.source.getKey() : "rehash should not change the keys of any node!";
					assert wm.data[i].count == se.source.getValue() : "rehash should not change the count of any node!";
				} else result[i] = wm.data[i];
			}
			return result;
		}
	}
}