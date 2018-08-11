import java.util.ArrayList;
import java.util.Date;

/**
 * Classe Block servant à créer la blockchain.
 * 
 * @author root
 *
 */
public class Block {
	public String hash;
	/** signature digitale de la block */
	public String previousHash;
	private String data;
	
	public String merkleRoot;
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	
	/** L'information qui sera un simple message (ou la transaction) */
	private long timeStamp;

	/**
	 * Minage
	 */
	private int nonce;

	/**
	 * Block constructeur
	 * 
	 * @param data
	 * @param PreviousHash
	 */
	public Block(String data, String PreviousHash) {
		this.data = data;
		
		this.previousHash = PreviousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
	}
	
	public Block(String previousHash) {
		this.previousHash = previousHash;
		this.timeStamp = new Date().getTime();
		this.hash = calculateHash();
		
	}

	/**
	 * calculateur de hash
	 * 
	 * @return
	 */
	public String calculateHash() {
		String calculatedHash = StringUtility
				.applySha256(previousHash + Long.toString(timeStamp) + merkleRoot + Integer.toString(nonce));
		return calculatedHash;
	}

	/**
	 * Proof of work
	 * 
	 * @param difficulty
	 */
	public void mineBlock(int difficulty) {
		String target = new String(new char[difficulty]).replace('\0', '0');
		while (!this.hash.substring(0, difficulty).equals(target)) {
			nonce++;
			this.hash = calculateHash();
		}
		System.out.println("Block miné" + this.hash);
	}
	
	
/**
 * Manque exception RuntimeError
 * @param transaction
 * @return
 */
	public Boolean addTransaction(Transaction transaction) {
		if(transaction == null) return false;
		if (this.previousHash != "0") {
			if (transaction.processTransaction() != true) {
					System.out.println("Transaction failed to process");
					return false;
			}
		}
		transactions.add(transaction);
		System.out.println("Transaction successfully add to block");
		return true;
	}
}
