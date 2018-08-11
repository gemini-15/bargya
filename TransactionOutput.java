import java.security.PublicKey;

/**
 *
 * @author root
 *
 */
public class TransactionOutput {
	public String id;

	/** Le nouveau possédeur de la transaction */
	public PublicKey recu;

	/** Le montant qu'il possède */
	public float value;

	/** l'Id de la transaction l'output a été créé */
	public String parentTransactionId;

	/**
	 * Constructeur
	 */
	public TransactionOutput(PublicKey recu, float value, String parentTransactionId) {
		this.recu = recu;
		this.value = value;
		this.parentTransactionId = parentTransactionId;
		this.id = StringUtility
				.applySha256(StringUtility.getStringFromKey(recu) + Float.toString(value) + parentTransactionId);
	}

	/**
	 * Vérification si le bitcoin est bien à l'objet
	 * 
	 * @param clePublique
	 * @return boolean
	 */
	public boolean isMine(PublicKey clePublique) {
		return (clePublique == recu);
	}

}
