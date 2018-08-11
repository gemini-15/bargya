
/**
 *
 * @author root
 *
 */
public class TransactionInput {
	/** Reference to transactionOutputs -> transaction Id */
	private String transactionOutputId;

	/** Contains the Unspent transaction Output */
	private TransactionOutput UTXO;

	public TransactionInput(String transactionOutputId) {
		this.transactionOutputId = transactionOutputId;
	}

	/**
	 * Méthode d'accès à la valeur de UTXO.
	 * 
	 * @return valeur de UTXO
	 */
	public TransactionOutput getUTXO() {
		return this.UTXO;
	}

	/**
	 * Méthode de changement de la valeur de UTXO.
	 * 
	 * @param newUTXO
	 */
	public void setUTXO(TransactionOutput newUTXO) {
		this.UTXO = newUTXO;
	}

	/**
	 * Méthode d'accès à la valeur de transactionOutputId
	 * 
	 * @return valeur de transactionOutputId
	 */
	public String getTransactionOutputId() {
		return this.transactionOutputId;
	}

	/**
	 * Méthode de changement de la valeur de transactionOutputId
	 * 
	 * @param newTransactionOutputId
	 */
	public void setTransactionOutputId(String newTransactionOutputId) {
		this.transactionOutputId = newTransactionOutputId;
	}

}
