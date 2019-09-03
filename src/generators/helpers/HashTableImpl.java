package generators.helpers;

/**
 * 
 * @author Dirk Kröhan, Kamil Erhard
 *
 */
public class HashTableImpl {

	public interface CodeObserver {
		public void executed(Block block, int line);
	}

	public String[] hashTable;
	public int numElements = 0;
	public final double hashTableLoadFactor;

	private final CodeObserver observer;

	public static enum Block {
		HASHING, STORE, HASH, MUSTGROW, GROW;

		public static Block getByOrdinal(int ordinal) {
			for (Block b : values()) {
				if (b.ordinal() == ordinal) {
					return b;
				}
			}
			return null;
		}
	}

	public int homeposition, nextHashTableSize = -1;
	public int pos;
	public int sig;
	public int k;
	public String object;
	public int key;

	public HashTableImpl(CodeObserver observer) {
		this(7, 0.75, observer);
	}

	private void notifyObservers(Block block, int line) {
		if (this.observer != null) {
			this.observer.executed(block, line);
		}
	}

	public HashTableImpl(int hashtableSize, double hashTableLoadFactor, CodeObserver observer) {
		int x = getNextPrime(hashtableSize);
		this.hashTableLoadFactor = hashTableLoadFactor;
		this.hashTable = new String[x];
		this.observer = observer;
	}

	public void hashing(String[] dataElements, int[] dataKeys) {
		this.notifyObservers(Block.HASHING, 1);

		for (int i=0; i<dataElements.length; i++) {
			this.notifyObservers(Block.HASHING, 2);

			this.notifyObservers(Block.HASHING, 3);
			if (this.mustGrow()) {
				this.notifyObservers(Block.HASHING , 4);
				this.grow();
				i = -1;
				this.notifyObservers(Block.HASHING , 5);

				this.notifyObservers(Block.HASHING , 6);
				continue;
			} else {
				this.notifyObservers(Block.HASHING, 7);
			}

			this.key = dataKeys[i];
			this.object = dataElements[i];

			this.notifyObservers(Block.HASHING, 8);
			this.store(this.key, this.object);
		}
		this.notifyObservers(Block.HASHING, 9);
		this.notifyObservers(Block.HASHING, 10);
	}

	public int hash(int key) {
		this.notifyObservers(Block.HASH, 1);
		int hash = (int) (key - Math.floor((double) key / this.hashTable.length) * this.hashTable.length);
		this.notifyObservers(Block.HASH, 2);
		return hash;
	}

	public void grow() {
		this.notifyObservers(Block.GROW, 1);

		this.nextHashTableSize = getNextPrime(this.hashTable.length + 1);
		this.notifyObservers(Block.GROW , 2);

    // String[] copy = Arrays.copyOf(this.hashTable, this.hashTable.length);

		this.numElements = 0;
		this.notifyObservers(Block.GROW , 3);

		this.hashTable = new String[this.nextHashTableSize];
		this.notifyObservers(Block.GROW , 4);
	}

	public int getSize() {
		return this.hashTable.length;
	}

	public int getEntryCount() {
		return this.numElements;
	}

	public void store(int key, String value) {
		this.notifyObservers(Block.STORE , 1);

		this.notifyObservers(Block.STORE , 2);
		this.homeposition = this.hash(key);

		this.pos = this.homeposition;
		this.notifyObservers(Block.STORE , 3);

		boolean isPositionFree = this.hashTable[this.pos] == null;

		this.notifyObservers(Block.STORE , 4);
		if (!isPositionFree) {
			this.sig = -1; this.k = 1;
			this.notifyObservers(Block.STORE , 5);

			do {
				this.notifyObservers(Block.STORE , 6);

				this.sig *= -1;
				this.notifyObservers(Block.STORE , 7);

				this.k++;
				this.notifyObservers(Block.STORE , 8);

				this.notifyObservers(Block.STORE , 9);
				this.pos = this.hash(this.homeposition + this.sig * (int) Math.pow(this.k/2, 2));

				isPositionFree = this.hashTable[this.pos] == null;
				this.notifyObservers(Block.STORE , 10);
			} while (!isPositionFree);
		}
		this.notifyObservers(Block.STORE , 11);

		this.hashTable[this.pos] = value;
		this.notifyObservers(Block.STORE , 12);

		this.numElements++;
		this.notifyObservers(Block.STORE , 13);

		this.notifyObservers(Block.STORE , 14);
	}

	public boolean mustGrow() {
		this.notifyObservers(Block.MUSTGROW , 1);
		boolean mustGrow = this.numElements >= this.hashTable.length * this.hashTableLoadFactor;
		this.notifyObservers(Block.MUSTGROW , 2);
		return mustGrow;
	}

	public Object retrieve(int key) {
		int ha = this.hash(key);

		int sig = -1, k = 0;
		int pos = ha;

		while (true) {

			if (this.hashTable[pos] == null) {
				return null;
			} else if ((this.hashTable[pos] != null) && (this.hashTable[pos].hashCode() == key)) {
				return this.hashTable[pos];
			}

			sig *= -1;
			k++;

			pos = this.hash(ha + sig * 2 * k);
		}

	}
	
	private static int getNextPrime(int n) {

		int k = n;
		boolean isPrime = false;

		while (!isPrime) {
			isPrime = true;

			for (int i = 2; i <= Math.sqrt(k); i++) {
				if (k % i == 0) {
					isPrime = false;
					break;
				}
			}

			if (!isPrime) {
				k++;
			}
		}

		return k;
	}
}
