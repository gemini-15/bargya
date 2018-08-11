import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * classe Wallet, objet porte-monnaie
 * 	
 * @author Bargach Yassine
 *
 */

public class Wallet {
	private PrivateKey privateKey;
	private PublicKey publicKey;

	public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	/**
	 * Constructeur porte-monnaie
	 */
	public Wallet() {
		generateKeyPair();
	}

	/**
	 * getPrivateKey
	 * 
	 * @return la valeur de privateKey
	 */
	public PrivateKey getPrivateKey() {
		return this.privateKey;
	}

	/**
	 * getPublicKey
	 * 
	 * @return la valeur de publicKey
	 */
	public PublicKey getPublicKey() {
		return this.publicKey;
	}

	/**
	 * modifie la valeur de privateKey
	 * 
	 * @param newPrivateKey
	 */
	public void setPrivateKey(PrivateKey newPrivateKey) {
		this.privateKey = newPrivateKey;
	}

	/**
	 * modifie la valeur de publicKey
	 * 
	 * @param newPublicKey
	 */
	public void setPublicKey(PublicKey newPublicKey) {
		this.publicKey = newPublicKey;
	}

	/**
	 * générateur de clé public et privé
	 */
	public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");

			// initialisation du générateur de clé, et générer une clé pair
			keyGen.initialize(ecSpec, random); // 256 bytes produit un level de sécurité acceptable

			KeyPair keyPair = keyGen.generateKeyPair();

			// modification de la clé publique et privée à partir de keyPair
			setPrivateKey(keyPair.getPrivate());
			setPublicKey(keyPair.getPublic());

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * retourne la balance et met UTXO qui appartiennent au porte monnaie à la
	 * liste this.UTXOs
	 * 
	 * @return
	 */
	public float getBalance() {
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item : BargyaCoin.UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if (UTXO.isMine(publicKey)) { // si l'output m'appartient i.e. si
											// le coin m'appartient
				this.UTXOs.put(UTXO.id, UTXO); // ajout à la liste des Unspent Transaction
				total += UTXO.value;
			}
		}
		return total;
	}
	
	/**
	 * g�n�re et retourne une nouvelle transaction � partir de ce porte feuille
	 * @param _recu
	 * @param value
	 * @return newTransaction
	 */
	public Transaction envoiTransaction(PublicKey _recu, float value) {
		if (getBalance() < value) {
			System.out.println("#Not enough funds to send transaction. Transaction Discarded");
			return null;
		}
		
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
		
		float total = 0;
		
		for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.id));
			if (total > value) break;
		}
		
		Transaction newTransaction = new Transaction(publicKey, _recu, value, inputs);
		newTransaction.generateSignature(privateKey);
		
		for (TransactionInput input : inputs) {
			UTXOs.remove(input.getTransactionOutputId());
		}
		return newTransaction;
		
		
	}
}
