import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Transaction {
	public String transactionId;
	public PublicKey sender;
	public PublicKey receiver;
	public float value;
	public byte[] signature;

	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs = new ArrayList<TransactionOutput>();

	private static int sequence = 0; // comptage des nombres de transactions effectuées

	/**
	 * constructeur Transaction
	 * 
	 * @param from
	 * @param to
	 * @param value
	 * @param inputs
	 */
	public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.receiver = to;
		this.inputs = inputs;
		this.value = value;
	}

	public String calculateHash() {
		sequence++;
		return StringUtility.applySha256(StringUtility.getStringFromKey(sender)
				+ StringUtility.getStringFromKey(receiver) + Float.toString(value) + sequence);
	}

	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtility.getStringFromKey(this.sender) + StringUtility.getStringFromKey(this.receiver)
				+ Float.toString(value);
		signature = StringUtility.applyECDSASig(privateKey, data);
	}

	public boolean verifySignature() {
		String data = StringUtility.getStringFromKey(this.sender) + StringUtility.getStringFromKey(this.receiver)
				+ Float.toString(value);
		return StringUtility.verifyECDSASig(sender, data, signature);
	}

	public boolean processTransaction() {

		if (verifySignature() == false) {
			System.out.println("Transaction Signature Failed to verify");
			return false;
		}

		// gathers transaction inputs
		for (TransactionInput i : inputs) {
			i.setUTXO(BargyaCoin.UTXOs.get(i.getTransactionOutputId()));
		}

		// check if transaction is valid
		if (getInputsValue() < BargyaCoin.minimumTransaction) {
			System.out.println("# Transaction Input too small : " + getInputsValue());
			return false;
		}

		// génération des transactions de sortie : (transaction outputs)
		float leftOver = getInputsValue() - value;
		transactionId = calculateHash();
		outputs.add(new TransactionOutput(this.receiver, value, transactionId));
		outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

		// add outputs to Unspent list
		for (TransactionOutput o : outputs) {
			BargyaCoin.UTXOs.put(o.id, o);
		}

		// remove transaction inputs from UTXO lists as spent :
		for (TransactionInput i : inputs) {
			if (i.getUTXO() == null)
				continue; // if transaction can't be found we skip it
			BargyaCoin.UTXOs.remove(i.getUTXO().id);
		}

		return true;
	}

	/**
	 * Méthode de retour de la somme des inputs(UTXOs) values.
	 * 
	 * @return Sum inputs(UTXOs)
	 */
	public float getInputsValue() {
		float total = 0;
		for (TransactionInput i : this.inputs) {
			if (i.getUTXO() == null)
				continue;
			total += i.getUTXO().value;
		}
		return total;
	}

	/**
	 * Retourne la somme des valeurs des outputs.
	 * 
	 * @return Sum outputs values
	 */
	public float getOutputsValue() {
		float total = 0;
		for (TransactionOutput o : this.outputs) {
			if (o == null)
				continue;
			total += o.value;
		}
		return total;
	}
}
