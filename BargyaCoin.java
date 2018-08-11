import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;

public class BargyaCoin {
	public static ArrayList<Block> blockchain = new ArrayList<>();

	public static HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	public int difficulty = 5;

	public static int minimumTransaction = 0;

	public static Wallet walletA;
	public static Wallet walletB;

	public static void main(String[] args) {
		// setup bouncey Castle as a Security provider
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

		// création des portes monnaies
		walletA = new Wallet();
		walletB = new Wallet();

		// test des clés publiques et privées
		System.out.println("Clés publiques et privées :");
		System.out.println(StringUtility.getStringFromKey(walletA.getPublicKey()));
		System.out.println(StringUtility.getStringFromKey(walletA.getPrivateKey()));

		// création d'un test de transaction entre A et B
		Transaction transaction = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(), 5, null);
		transaction.generateSignature(walletA.getPrivateKey());

		// vérification que la signature marche et qu'elle est bien vérifiée
		transaction.generateSignature(walletA.getPrivateKey());
		System.out.println("Vérification : ");
		System.out.println(transaction.verifySignature());
	}

}
