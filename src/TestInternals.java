import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import edu.uwm.cs351.WordMultiset;
import edu.uwm.cs351.util.DefaultEntry;
import junit.framework.TestCase;

public class TestInternals extends TestCase {
	protected static boolean VERBOSE = true;
	protected WordMultiset.Spy spy;
	protected WordMultiset self;
	
	@Override // implementation
	public void setUp() {
		spy = new WordMultiset.Spy();
	}
	
	
	protected int reports = 0;
	
	protected <T> void assertReporting(T expected, boolean expectReport, Supplier<T> test) {
		reports = 0;
		Consumer<String> savedReporter = spy.getReporter();
		try {
			spy.setReporter((String message) -> {
				++reports;
				if (message == null || message.trim().isEmpty()) {
					assertFalse("Uninformative report is not acceptable", true);
				}
				if (!expectReport) {
					assertFalse("Reported error incorrectly: " + message, true);
				}
			});
			assertEquals(expected, test.get());
			if (expectReport) {
				assertEquals("Expected exactly one invariant error to be reported", 1, reports);
			}
			spy.setReporter(null);
		} finally {
			spy.setReporter(savedReporter);
		}
	}
		
	protected void assertWellFormed(boolean expected, WordMultiset r) {
		assertReporting(expected, !expected, () -> spy.wellFormed(r));
	}
	
	protected class MyEntry extends DefaultEntry<String, Integer> {
		public MyEntry(String s, Integer i) { super(s,i); }
	}
	
	protected MyEntry PLACE_HOLDER = new MyEntry(null,null);
	
	protected MyEntry p() { return PLACE_HOLDER; }
	
	protected MyEntry e(String s, Integer i) { return new MyEntry(s,i); }
	
	protected Map.Entry<String,Integer>[] ea(MyEntry... pieces) {
		return pieces;
	}
	
	protected int hash(Map.Entry<String,Integer>[] a, String key, boolean phOK) {
		self = spy.newInstance(a, PLACE_HOLDER, -1, -1, -1);
		return spy.hash(self, key, phOK);
	}
	
	protected static final String[][] keys7 = new String[7][5];
	protected static final String[][] keys13 = new String[13][11];
	
	static {
		int rem7 = 7 * 5;
		int rem13 = 13 * 11;
		int i;
		// starting S10000 has negative hashes
		for (i=9950; rem7 > 0 || rem13 > 0; ++i) {
			String key = "S" + i;
			int h = key.hashCode();
			if (rem7 > 0) {
				int h7 = ((h % 7) + 7) % 7;
				int h5 = ((h % 5) + 5) % 5;
				if (keys7[h7][h5] == null) {
					keys7[h7][h5] = key;
					--rem7;
				}
			}
			if (rem13 > 0) {
				int h13 = ((h % 13) + 13) % 13;
				int h11 = ((h % 11) + 11) % 11;
				if (keys13[h13][h11] == null) {
					keys13[h13][h11] = key;
					--rem13;
				}
				
			}
		}
		if (VERBOSE) {
			System.out.println("Done at S" + i + ": hash = " + ("S"+i).hashCode());
			System.out.println("     0     1     2     3     4      5     6");
			for (int j=0; j< 5; ++j) {
				System.out.print(j+": ");
				for (i=0; i < 7; ++i) {
					System.out.print(keys7[i][j]);
					System.out.print(" ");
				}
				System.out.println();
			}
			System.out.println("     0     1      2      3     4      5     6      7      8      9     10     11     12");
			for (int j=0; j< 11; ++j) {
				System.out.print(j+": ");
				for (i=0; i < 13; ++i) {
					System.out.print(keys13[i][j]);
					System.out.print(" ");
				}
				System.out.println();
			}
		}
	}
	
	
	/// testHmn: testing hash
	
	public void testH00() {
		assertEquals(0, hash(ea(null, null, null, null, null, null, null), keys7[0][0], false));
	}
	
	public void testH01() {
		assertEquals(1, hash(ea(null, null, null, null, null, null, null), keys7[1][0], false));
	}
	
	public void testH02() {
		assertEquals(2, hash(ea(null, null, null, null, null, null, null), keys7[2][0], false));
	}
	
	public void testH03() {
		assertEquals(3, hash(ea(null, null, null, null, null, null, null), keys7[3][0], false));
	}
	
	public void testH04() {
		assertEquals(4, hash(ea(null, null, null, null, null, null, null), keys7[4][0], false));
	}
	
	public void testH05() {
		assertEquals(5, hash(ea(null, null, null, null, null, null, null), keys7[5][0], false));
	}
	
	public void testH06() {
		assertEquals(6, hash(ea(null, null, null, null, null, null, null), keys7[6][0], false));
	}
	
	public void testH07() {
		assertEquals(0, hash(ea(null, null, null, null, null, null, null), keys7[0][1], false));
	}
	
	public void testH08() {
		assertEquals(1, hash(ea(null, null, null, null, null, null, null), keys7[1][2], false));
	}
	
	public void testH09() {
		assertEquals(2, hash(ea(null, null, null, null, null, null, null), keys7[2][3], false));
	}
	
	public void testH10() {
		assertEquals(0, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[0][2], false));
	}
	
	public void testH11() {
		assertEquals(1, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[1][2], true));
	}
	
	public void testH12() {
		assertEquals(2, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[2][2], false));
	}
	
	public void testH13() {
		assertEquals(3, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[3][2], true));
	}
	
	public void testH14() {
		assertEquals(4, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[4][2], false));
	}
	
	public void testH15() {
		assertEquals(5, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[5][3], true));
	}
	
	public void testH16() {
		assertEquals(6, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[6][3], false));
	}
	
	public void testH17() {
		assertEquals(7, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[7][3], true));
	}
	
	public void testH18() {
		assertEquals(8, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[8][3], false));
	}
	
	public void testH19() {
		assertEquals(9, hash(ea(null, null, null, null, null, null, null,
							    null, null, null, null, null, null), keys13[9][3], true));
	}
	
	public void testH20() {
		assertEquals(0, hash(ea(null, null, e(keys7[0][2],3), null, p(), null, null), keys7[0][2], false));
	}
	
	public void testH21() {
		assertEquals(1, hash(ea(e(keys7[1][2],12), null, e(keys7[1][2],3), null, p(), null, null), keys7[1][2], false));
	}

	public void testH22() {
		assertEquals(2, hash(ea(p(), e(keys7[2][2],22), null, p(), p(), null, null), keys7[2][2], true));
	}
	
	public void testH23() {
		assertEquals(3, hash(ea(p(), p(), e(keys13[3][3],-10), null, p(), null, null,
			                    null, null, p(), e(keys13[3][3],10), p(), null), keys13[3][3], true));
	}

	public void testH24() {
		assertEquals(4, hash(ea(null, null, p(), e(keys7[4][3],43), e(keys7[4][3],7), null, null), keys7[4][3], false));
	}
	
	public void testH25() {
		assertEquals(5, hash(ea(null, p(), null, p(), p(), e(keys13[5][2],25), null,
			                    null, null, p(), e(keys13[5][2],52), p(), null), keys13[5][2], true));
	}

	public void testH26() {
		assertEquals(6, hash(ea(p(), e(keys7[6][0],22), null, p(), p(), e(keys7[6][0],60), e(keys7[6][0],6)), keys7[6][0], true));
	}
	
	
	public void testH30() {
		assertEquals(0, hash(ea(null, null, null, null, e(keys7[4][0],1), null, e(keys7[4][2],6)), keys7[4][2], false));
	}
	
	public void testH31() {
		assertEquals(1, hash(ea(e(keys7[0][3],1), null, null, null, null, null, null), keys7[0][0], true));
	}
	
	public void testH32() {
		assertEquals(1, hash(ea(null, null, null, e(keys7[4][4],4), null, null, null), keys7[3][4], false));
	}
	
	public void testH33() {
		assertEquals(3, hash(ea(null, p(), null, null, null, null, null), keys7[1][1], false));
	}
	
	public void testH34() {
		assertEquals(4, hash(ea(null, p(), null, e(keys7[1][2],3), null, null, null), keys7[1][2], false));
	}
	
	public void testH35() {
		assertEquals(5, hash(ea(e(keys13[0][1],5), null, null, null, null, null, null,
			                    null, null, null, null, null, null), keys13[0][4], false));
	}
	
	public void testH36() {
		assertEquals(6, hash(ea(null, null, null, null, null, null, null,
		                        null, e(keys13[8][6],8), null, null, null, null), keys13[8][10], true));
	}
	
	public void testH37() {
		assertEquals(7, hash(ea(null, p(), null, null, null, null, null,
			                    null, null, null, null, null, null), keys13[1][5], false));
	}
	
	public void testH38() {
		assertEquals(8, hash(ea(null, null, null, null, null, null, null,
			                    null, e(keys13[11][9],2), null, e(keys13[11][9],3), p(), null), keys13[11][9], true));
	}
	
	public void testH39() {
		assertEquals(9, hash(ea(null, null, e(keys13[6][2],1), null, null, null, p(),
			                    null, e(keys13[6][2],2), e(keys13[6][2],3), null, null, null), keys13[6][2], true));
	}
	
	public void testH40() {
		assertEquals(0, hash(ea(null, null, null, p(), null, e(keys7[5][0],2), null), keys7[3][1], false));
	}
	
	public void testH41() {
		assertEquals(1, hash(ea(null, null, e(keys7[2][4],1), null, null, e(keys7[5][1],2), null), keys7[2][2], true));
	}
	
	public void testH42() {
		assertEquals(2, hash(ea(null, null, null, null, p(), null, p()), keys7[6][4], false));
	}
	
	public void testH43() {
		assertEquals(3, hash(ea(p(), e(keys7[4][2],3), null, e(keys7[4][2],1), p(), e(keys7[4][2],2), null), keys7[4][2], true));
	}
	
	public void testH44() {
		assertEquals(4, hash(ea(e(keys7[2][0],3), null, e(keys7[2][3],1), p(), e(keys7[2][0],2), null, null), keys7[2][0], false));
	}
	
	public void testH45() {
		assertEquals(5, hash(ea(null, e(keys13[1][5],1), null, e(keys13[3][1],2), null, null, null,
			    null, null, null, null, null, null), keys13[1][1], true));
	}
	
	public void testH46() {
		assertEquals(6, hash(ea(e(keys13[0][8],3), null, null, null, null, null, null,
			                    p(), null, null, null, null, null), keys13[7][5], false));
	}
	
	public void testH47() {
		assertEquals(7, hash(ea(null, null, null, null, null, null, null,
			    null, null, p(), null, e(keys13[11][5],1), null), keys13[11][10], false));
	}
	
	public void testH48() {
		assertEquals(8, hash(ea(null, null, null, null, null, null, p(),
			                    p(), e(keys13[6][0],2), null, null, null, null), keys13[6][0], true));
	}
	
	public void testH49() {
		assertEquals(9, hash(ea(null, null, null, e(keys13[3][7],2), null, e(keys13[10][5],5), null,
			    null, p(), e(keys13[10][5],4), p(), null, null), keys13[10][5], true));
	}
	
	public void testH50() {
		assertEquals(0, hash(ea(null, p(), null, null, p(), p(), null), keys7[5][2], false));
	}
	
	public void testH51() {
		assertEquals(1, hash(ea(e(keys7[0][4],2), null, null, e(keys7[3][1],1), e(keys7[4][4],3), null, e(keys7[3][3],4)), keys7[3][3], true));
	}
	
	public void testH52() {
		assertEquals(2, hash(ea(e(keys7[0][4],2), null, e(keys7[3][1],1), p(), null, p(), null), keys7[3][1], true));
	}
	
	public void testH53() {
		assertEquals(3, hash(
				ea(null, null, p(), null, null, null, null,
			       null, p(), p(), null, null, null), keys13[8][6], false));
	}
	
	public void testH54() {
		assertEquals(4, hash(
				ea(null, null, null, null, e(keys13[10][10],5), null, p(),
			       null, e(keys13[8][3],4), null, p(), e(keys13[10][10],3), null), keys13[10][10], true));

	}
	
	public void testH55() {
		assertEquals(5, hash(ea(e(keys7[0][1],2), null, p(), null, e(keys7[4][3],1), null, p()), keys7[6][4], false));
	}
	
	public void testH56() {
		assertEquals(6, hash(
				ea(e(keys13[0][9],4), null, null, e(keys13[3][2],3), null, null, null,
			       e(keys13[7][1],1), null, null, e(keys13[10][1],2), null, null), keys13[7][2], true));
	}
	
	public void testH57() {
		assertEquals(7, hash(
				ea(null, p(), null, null, p(), null, null,
			       null, null, e(keys13[9][0],2), null, null, e(keys13[12][6],1)), keys13[1][7], false));
	}
	
	public void testH58() {
		assertEquals(8, hash(
				ea(null, e(keys13[4][5],4), null, null, p(), null, e(keys13[10][9],2),
			       e(keys13[7][2],5), e(keys13[10][9],3), null, p(), p(), null), keys13[10][9], true));
	}
	
	public void testH59() {
		assertEquals(9, hash(
				ea(p(), null, null, p(), p(), null, null,
			       p(), p(), null, e(keys13[7][8],1), null, p()), keys13[7][8], false));

	}
	
	public void testH60() {
		assertEquals(0, hash(ea(p(), null, null, null, null, null, null), keys7[0][0], true));
	}
	
	public void testH61() {
		assertEquals(1, hash(ea(null, p(), null, null, null, null, null), keys7[1][0], true));
	}
	
	public void testH62() {
		assertEquals(2, hash(ea(null, null, p(), e(keys7[2][4],1), null, null, null), keys7[2][0], true));
	}

	public void testH63() {
		assertEquals(3, hash(ea(null, null, null, p(), e(keys7[3][1],2), p(), null), keys7[3][1], true));
	}
	
	public void testH64() {
		assertEquals(4, hash(ea(null, null, e(keys7[2][3],1), null, p(), null, null), keys7[2][1], true));
	}
	
	public void testH65() {
		assertEquals(5, hash(ea(null, e(keys7[1][3],1), null, e(keys7[3][2],2), null, p(), e(keys7[5][1],3)), keys7[6][1], true));
	}

	public void testH66() {
		assertEquals(6, hash(ea(null, null, e(keys7[2][1],1), e(keys7[3][0],2), null, null, p()), keys7[2][3], true));
	}
	
	public void testH67() {
		assertEquals(7, hash(ea(null, e(keys13[2][7],1), null, null, null, null, null,
							    p(), null, null, p(), null, null), keys13[7][8], true));
	}
	
	public void testH68() {
		assertEquals(8, hash(ea(null, null, null, null, null, p(), null,
							    null, p(), null, null, e(keys13[11][2],2), null), keys13[11][9], true));
	}
	
	public void testH69() {
		assertEquals(9, hash(ea(null, null, e(keys13[2][8],1), e(keys13[3][1],2), null, null, null,
							    null, null, p(), p(), e(keys13[10][8],4), null), keys13[2][6], true));
	}

	
	/// testWmn: testing wellFormed
	
	public void testW00() {
		self = spy.newInstance(null, PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(false, self);
	}
	
	public void testW01() {
		self = spy.newInstance(ea(null, null, null, null, null), PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(false, self);
	}
	
	public void testW02() {
		self = spy.newInstance(ea(null, null, null, null, null, null, null), PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(true, self);
	}
	
	public void testW03() {
		self = spy.newInstance(ea(null, null, null, null, null, null, null, null, null), PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(false, self);
	}
	
	public void testW04() {
		MyEntry[] ea = new MyEntry[11];
		self = spy.newInstance(ea, PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(false, self);
	}
	
	public void testW05() {
		MyEntry[] ea = new MyEntry[13];
		self = spy.newInstance(ea, PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(true, self);
	}
	
	public void testW06() {
		MyEntry[] ea = new MyEntry[17];
		self = spy.newInstance(ea, PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(false, self);
	}
	
	public void testW07() {
		MyEntry[] ea = new MyEntry[19];
		self = spy.newInstance(ea, PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(true, self);
	}
	
	public void testW08() {
		MyEntry[] ea = new MyEntry[53231];
		self = spy.newInstance(ea, PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(false, self);
	}
	
	public void testW09() {
		MyEntry[] ea = new MyEntry[53233];
		self = spy.newInstance(ea, PLACE_HOLDER, 0, 0, 0);
		assertWellFormed(true, self);
	}
	
	public void testW10() {
		self = spy.newInstance(ea(null, p(), null, null, null, null, p()), PLACE_HOLDER, 0, 2, 10);
		assertWellFormed(true, self);
	}
	
	public void testW11() {
		self = spy.newInstance(ea(null, p(), null, null, null, null, p()), PLACE_HOLDER, 0, 1, 10);
		assertWellFormed(false, self);
	}
	
	public void testW12() {
		self = spy.newInstance(ea(null, p(), null, null, null, null, p()), PLACE_HOLDER, 0, 3, 10);
		assertWellFormed(false, self);
	}
	
	public void testW13() {
		self = spy.newInstance(ea(p(), null, null, null, null, null, e(keys7[6][3],13)), PLACE_HOLDER, 1, 2, 10);
		assertWellFormed(true, self);
	}
	
	public void testW14() {
		self = spy.newInstance(ea(null, p(), null, null, null, null, e(keys7[6][3],13)), PLACE_HOLDER, 1, 1, 10);
		assertWellFormed(false, self);
	}
	
	public void testW15() {
		self = spy.newInstance(ea(null, e(keys7[1][4],4), null, null, null, null, p()), PLACE_HOLDER, 1, 2, 10);
		assertWellFormed(true, self);
	}

	public void testW16() {
		self = spy.newInstance(ea(e(keys7[0][4],4), null, null, null, null, null, p()), PLACE_HOLDER, 1, 1, 10);
		assertWellFormed(false, self);
	}

	public void testW17() {
		self = spy.newInstance(ea(null, p(), null, p(), null, null, p()), PLACE_HOLDER, 0, 3, 10);
		assertWellFormed(true, self);
	}
	
	public void testW18() {
		self = spy.newInstance(
				ea(null, p(), null, e(keys13[3][8],8), null, null, p(),
				   e(keys13[7][5],12), null, null, null, p(), null), PLACE_HOLDER, 2, 5, 10);
		assertWellFormed(true, self);
	}
	
	public void testW19() {
		self = spy.newInstance(
				ea(null, p(), null, e(keys13[3][8],8), null, null, p(),
				   e(keys13[7][5],12), null, null, null, p(), null), PLACE_HOLDER, 2, 3, 10);
		assertWellFormed(false, self);
	}

	
	public void testW20() {
		self = spy.newInstance(ea(null, p(), null, null, null, null, p()), PLACE_HOLDER, 1, 2, 10);
		assertWellFormed(false, self);
	}
	
	public void testW21() {
		self = spy.newInstance(ea(null, p(), null, null, null, null, e(keys7[6][3],13)), PLACE_HOLDER, 0, 2, 10);
		assertWellFormed(false, self);
	}
	
	public void testW22() {
		self = spy.newInstance(ea(null, p(), null, null, null, null, e(keys7[6][3],13)), PLACE_HOLDER, 2, 2, 10);
		assertWellFormed(false, self);
	}
	
	public void testW23() {
		self = spy.newInstance(ea(null, e(keys7[1][4],4), null, null, null, null, e(keys7[6][0],3)), PLACE_HOLDER, 2, 2, 10);
		assertWellFormed(true, self);
	}
	
	public void testW24() {
		self = spy.newInstance(ea(e(keys7[0][2],4), null, p(), null, e(keys7[4][1], 3), null, null), PLACE_HOLDER, 2, 3, 10);
		assertWellFormed(true, self);
	}
	
	public void testW25() {
		self = spy.newInstance(
				ea(null, p(), null, e(keys13[3][8],8), null, null, p(),
				   e(keys13[7][5],12), null, null, null, p(), null), PLACE_HOLDER, 1, 5, 10);
		assertWellFormed(false, self);
	}

	public void testW26() {
		self = spy.newInstance(
				ea(null, p(), null, e(keys13[3][8],8), null, null, p(),
				   e(keys13[7][5],12), null, null, null, p(), null), PLACE_HOLDER, 3, 5, 10);
		assertWellFormed(false, self);
	}

	public void testW27() {
		self = spy.newInstance(
				ea(null, p(), null, e(keys13[3][8],8), null, null, p(),
				   e(keys13[7][5],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 3, 5, 10);
		assertWellFormed(true, self);
	}

	public void testW28() {
		self = spy.newInstance(
				ea(e(keys13[0][1],7), null, null, e(keys13[3][8],8), null, null, p(),
				   e(keys13[7][5],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 3, 5, 10);
		assertWellFormed(false, self);
	}

	public void testW29() {
		self = spy.newInstance(
				ea(e(keys13[0][1],7), null, null, e(keys13[3][8],8), null, null, p(),
				   e(keys13[7][5],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 4, 5, 10);
		assertWellFormed(true, self);
	}

	public void testW30() {
		self = spy.newInstance(ea(null, e(null,0), null, null, null, null, p()), PLACE_HOLDER, 1, 2, 10);
		assertWellFormed(false, self);
	}

	public void testW31() {
		self = spy.newInstance(ea(null, e(null,0), null, null, null, null, p()), PLACE_HOLDER, 0, 1, 10);
		assertWellFormed(false, self);
	}

	public void testW32() {
		self = spy.newInstance(ea(null, e(null,0), null, null, null, null, p()), PLACE_HOLDER, 0, 2, 10);
		assertWellFormed(false, self);
	}
	
	public void testW33() {
		self = spy.newInstance(ea(null, e(keys7[1][4],0), null, null, null, null, p()), PLACE_HOLDER, 1, 2, 10);
		assertWellFormed(false, self);
	}
	
	public void testW34() {
		self = spy.newInstance(ea(null, e(keys7[1][4],4), null, null, null, null, e(keys7[6][0],-9)), PLACE_HOLDER, 2, 2, 10);
		assertWellFormed(false, self);
	}


	public void testW38() {
		self = spy.newInstance(
				ea(e(keys13[0][1],7), null, null, e(null,0), null, null, p(),
				   e(keys13[7][5],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 4, 5, 10);
		assertWellFormed(false, self);
	}

	public void testW39() {
		self = spy.newInstance(
				ea(e(keys13[0][1],7), null, null, e(null,0), null, null, p(),
				   e(keys13[7][5],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 3, 5, 10);
		assertWellFormed(false, self);
	}
	
	public void testW40() {
		self = spy.newInstance(ea(e(keys7[0][2],4), null, p(), null, e(keys7[4][1], 3), null, p()), PLACE_HOLDER, 2, 4, 10);
		assertWellFormed(false, self);
	}
	
	public void testW41() {
		self = spy.newInstance(ea(e(keys7[0][2],4), null, p(), null, null, p(), p()), PLACE_HOLDER, 1, 4, 10);
		assertWellFormed(false, self);
	}
	
	public void testW42() {
		self = spy.newInstance(ea(p(), null, p(), null, null, p(), p()), PLACE_HOLDER, 0, 4, 10);
		assertWellFormed(false, self);
	}
	
	public void testW43() {
		self = spy.newInstance(
				ea(null, p(), null, p(), null, null, p(),
				   p(), null, null, null, p(), null), PLACE_HOLDER, 0, 5, 10);
		assertWellFormed(true, self);
	}
	
	public void testW44() {
		self = spy.newInstance(
				ea(e(keys13[0][2],2), null, e(keys13[2][7],9), e(keys13[3][5],8), null, null, e(keys13[6][3],9),
				   null, null, null, null, e(keys13[11][0],11), e(keys13[12][4],8)), PLACE_HOLDER, 6, 6, 10);
		assertWellFormed(true, self);
	}
	
	public void testW45() {
		self = spy.newInstance(
				ea(e(keys13[0][2],2), null, e(keys13[2][7],9), e(keys13[3][5],8), null, null, e(keys13[6][3],9),
				   null, p(), null, null, e(keys13[11][0],11), e(keys13[12][4],8)), PLACE_HOLDER, 6, 7, 10);
		assertWellFormed(false, self);
	}
	
	public void testW46() {
		self = spy.newInstance(
				ea(e(keys13[0][2],2), null, e(keys13[2][7],9), e(keys13[3][5],8), null, null, e(keys13[6][3],9),
				   null, p(), null, null, e(keys13[11][0],11), e(keys13[12][4],8)), PLACE_HOLDER, 6, 6, 10);
		assertWellFormed(false, self);
	}
	
	public void testW47() {
		self = spy.newInstance(
				ea(e(keys13[0][2],2), null, e(keys13[2][7],9), e(keys13[3][5],8), null, null, e(keys13[6][3],9),
				   null, p(), null, null, e(keys13[11][0],11), e(keys13[12][4],8)), PLACE_HOLDER, 5, 6, 10);
		assertWellFormed(false, self);
	}

	public void testW48() {
		MyEntry[] ea = new MyEntry[61];
		assertEquals(51,"hello".hashCode() % 61);
		for (int i=0; i < 30; ++i) {
			ea[i] = p();
		}
		self = spy.newInstance(ea, p(), 1, 31, 42);
		ea[51] = e("hello", 3);
		assertWellFormed(false, self);
	}

	public void testW49() {
		MyEntry[] ea = new MyEntry[61];
		assertEquals(51,"hello".hashCode() % 61);
		for (int i=0; i < 29; ++i) {
			ea[i] = p();
		}
		self = spy.newInstance(ea, p(), 1, 30, 42);
		ea[51] = e("hello", 3);
		assertWellFormed(false, self);
	}
	
	public void testW50() {
		self = spy.newInstance(ea(null, null, null, null, null, null, e(keys7[4][1],13)), PLACE_HOLDER, 1, 1, 50);
		assertWellFormed(false, self);
	}
	
	public void testW51() {
		self = spy.newInstance(ea(null, null, null, null, p(), null, e(keys7[4][1],13)), PLACE_HOLDER, 1, 2, 50);
		assertWellFormed(true, self);
	}
	
	public void testW52() {
		MyEntry e4 = e(keys7[4][1],13);
		self = spy.newInstance(ea(null, null, null, null, e4, null, e4), PLACE_HOLDER, 2, 2, 50);
		assertWellFormed(false, self);
	}
	
	public void testW53() {
		self = spy.newInstance(ea(null, e(keys7[4][1],13), null, null, e(keys7[4][3],1), null, e(keys7[6][0],2)), PLACE_HOLDER, 3, 3, 10);
		assertWellFormed(true, self);
	}

	public void testW54() {
		MyEntry e4 = e(keys7[4][1],1);
		self = spy.newInstance(ea(null, e4, null, null, e4, null, e(keys7[6][0],2)), PLACE_HOLDER, 3, 3, 10);
		assertWellFormed(false, self);
	}

	public void testW55() {
		self = spy.newInstance(
				ea(e(keys13[0][1],7), null, null, p(), null, null, p(),
				   e(keys13[0][6],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 3, 5, 10);
		assertWellFormed(true, self);
	}

	public void testW56() {
		self = spy.newInstance(
				ea(null, null, null, p(), null, null, p(),
				   e(keys13[0][6],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 2, 5, 10);
		assertWellFormed(false, self);
	}

	public void testW57() {
		self = spy.newInstance(
				ea(p(), null, null, p(), null, null, e(keys13[0][2],4),
				   e(keys13[0][6],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 3, 5, 10);
		assertWellFormed(true, self);
	}
	
	public void testW58() {
		self = spy.newInstance(
				ea(p(), null, null, null, null, null, e(keys13[0][2],4),
				   e(keys13[0][6],12), null, null, null, null, e(keys13[12][4],1)), PLACE_HOLDER, 3, 4, 10);
		assertWellFormed(false, self);
	}

	public void testW59() {
		self = spy.newInstance(
				ea(e(keys13[0][2],2), null, e(keys13[2][7],9), e(keys13[3][5],8), null, null, e(keys13[6][3],9),
				   null, null, null, null, e(keys13[11][3],11), e(keys13[11][0],8)), PLACE_HOLDER, 6, 6, 10);
		assertWellFormed(true, self);
	}
	
	
	/// testYmn: tests of rehash
	
	public void testY00() {
		MyEntry[] ea = new MyEntry[7];
		ea[0] = ea[1] = ea[3] = ea[4] = p();
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 0, 4);
		assertEquals(7, r.length);
		for (int i=0; i < r.length; ++i) {
			assertNull(r[i]);
		}
	}
	
	public void testY01() {
		MyEntry[] ea = new MyEntry[7];
		ea[0] = ea[1] = ea[3] = ea[4] = p();
		ea[3] = e(keys7[4][3], 15);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 1, 4);
		assertEquals(7, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 4:
				assertSame(ea[3], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}
	
	public void testY02() {
		MyEntry[] ea = new MyEntry[7];
		ea[0] = ea[1] = ea[3] = ea[4] = p();
		ea[3] = e(keys13[4][3], 15);
		ea[4] = e(keys13[4][1], 6);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 2, 4);
		assertEquals(13, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 4:
				assertSame(ea[3], r[i]);
				break;
			case 6:
				assertSame(ea[4], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}
	
	public void testY03() {
		MyEntry[] ea = new MyEntry[7];
		ea[0] = ea[1] = ea[3] = ea[4] = p();
		ea[0] = e(keys13[4][10], 3);
		ea[3] = e(keys13[4][3], 15);
		ea[4] = e(keys13[4][1], 6);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 3, 4);
		assertEquals(13, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 4:
				assertSame(ea[0], r[i]);
				break;
			case 6:
				assertSame(ea[4], r[i]);
				break;
			case 8:
				assertSame(ea[3], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}
	
	public void testY04() {
		MyEntry[] ea = new MyEntry[7];
		ea[0] = ea[1] = ea[3] = ea[4] = p();
		ea[0] = e(keys13[4][10], 3);
		ea[1] = e(keys13[4][9], 17);
		ea[3] = e(keys13[4][3], 15);
		ea[4] = e(keys13[4][1], 6);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 4, 4);
		assertEquals(19, r.length);
		assertEquals(-7, keys13[4][1].hashCode() % 19);
		assertEquals(-4, keys13[4][3].hashCode() % 19);
		assertEquals(2, keys13[4][9].hashCode() % 19);
		assertEquals(13, keys13[4][10].hashCode() % 19);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 2:
				assertSame(ea[1], r[i]);
				break;
			case 12:
				assertSame(ea[4], r[i]);
				break;
			case 15:
				assertSame(ea[3], r[i]);
				break;
			case 13:
				assertSame(ea[0], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}
	
	public void testY05() {
		MyEntry[] ea = new MyEntry[13];
		for (int i=0; i < ea.length; i += 2) {
			ea[i] = p();
		}
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 0, 7);
		assertEquals(7, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			default:
				assertNull(r[i]);
				break;
			}
		}
	}

	public void testY06() {
		MyEntry[] ea = new MyEntry[13];
		for (int i=0; i < ea.length; i += 2) {
			ea[i] = p();
		}
		ea[8] = e(keys7[3][2], 2);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 1, 7);
		assertEquals(7, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 3:
				assertSame(ea[8], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}

	public void testY07() {
		MyEntry[] ea = new MyEntry[13];
		for (int i=0; i < ea.length; i += 2) {
			ea[i] = p();
		}
		ea[2] = e(keys13[3][2], 2);
		ea[6] = e(keys13[3][7], 1);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 2, 7);
		assertEquals(13, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 3:
				assertSame(ea[2], r[i]);
				break;
			case 11:
				assertSame(ea[6], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}

	public void testY08() {
		MyEntry[] ea = new MyEntry[13];
		for (int i=0; i < ea.length; i += 2) {
			ea[i] = p();
		}
		ea[2] = e(keys13[3][2], 2);
		ea[6] = e(keys13[5][7], 1);
		ea[10] = e(keys13[5][10], 3);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 3, 7);
		assertEquals(13, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 3:
				assertSame(ea[2], r[i]);
				break;
			case 5:
				assertSame(ea[6], r[i]);
				break;
			case 1:
				assertSame(ea[10], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}

	public void testY09() {
		MyEntry[] ea = new MyEntry[13];
		for (int i=0; i < ea.length; i += 2) {
			ea[i] = p();
		}
		ea[0] = e("apple", 1);
		ea[2] = e("orange", 2);
		ea[4] = e("pear", 3);
		ea[8] = e("banana", 4);
		ea[12] = e("grape", 5);
		assertEquals(8, "apple".hashCode() % 31);
		assertEquals(-27, "orange".hashCode() % 31);
		assertEquals(21, "pear".hashCode() % 31);
		assertEquals(0, "banana".hashCode() % 31);
		assertEquals(8, "grape".hashCode() % 31);
		assertEquals(25, "grape".hashCode() % 29);
		Map.Entry<String,Integer>[] r = spy.rehash(ea, p(), 5, 7);
		assertEquals(31, r.length);
		for (int i=0; i < r.length; ++i) {
			switch (i) {
			case 8:
				assertSame(ea[0], r[i]);
				break;
			case 4:
				assertSame(ea[2], r[i]);
				break;
			case 21:
				assertSame(ea[4], r[i]);
				break;
			case 0:
				assertSame(ea[8], r[i]);
				break;
			case 3:
				assertSame(ea[12], r[i]);
				break;
			default:
				assertNull(r[i]);
				break;
			}
		}
	}
	
}